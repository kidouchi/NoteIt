package kidouchi.noteit.note;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.PlusShare;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.ArrayList;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;
import kidouchi.noteit.R;
import kidouchi.noteit.activity.NoteEditorActivity;
import kidouchi.noteit.animation.RollInOutAnimation;
import kidouchi.noteit.api.TwitterApi;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.fragments.TumblrPostDialogFragment;
import kidouchi.noteit.utilities.BitmapUtilities;

/**
 * Created by iuy407 on 8/9/15.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> mNotes;
    private AppDatabase mDB;
    private Context mAdapterContext;

    public NoteAdapter(Note[] notes, Context context, AppDatabase db) {
        mNotes = new ArrayList<Note>(Arrays.asList(notes));
        mDB = db;
        mAdapterContext = context;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(TwitterApi.TWITTER_KEY, TwitterApi.TWITTER_SECRET);
        Fabric.with(mAdapterContext, new Twitter(authConfig), new TweetComposer());
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int position) {
        noteViewHolder.bindNote(mNotes.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNoteTitleLabel;
        private ImageView mNoteBitmap;
        private ImageButton mDeleteButton;
        private ImageButton mShareButton;
        private ImageButton mTwitterButton;
        private ImageButton mTumblrButton;
        private ImageButton mGooglePlusButton;

        private Note note;
        private Context context;
        private boolean pressedOut = false;

        public NoteViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            mNoteTitleLabel = (TextView) itemView.findViewById(R.id.note_item_title_label);
            mNoteBitmap = (ImageView) itemView.findViewById(R.id.note_item_bitmap);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.note_delete_button);
            mShareButton = (ImageButton) itemView.findViewById(R.id.note_share_button);
            mTwitterButton = (ImageButton) itemView.findViewById(R.id.note_twitter_button);
            mTumblrButton = (ImageButton) itemView.findViewById(R.id.note_tumblr_button);
            mGooglePlusButton = (ImageButton) itemView.findViewById(R.id.note_google_plus_button);

            itemView.setOnClickListener(this);
        }

        public void bindNote(final Note note, final int position) {
            this.note = note;
            // Bind title
            mNoteTitleLabel.setText(note.getTitle());

            // Bind text bitmap
            byte[] bitArr = note.getTextBitmap();
            if (bitArr != null) {
                mNoteBitmap.setImageBitmap(
                        BitmapUtilities.decodeSampledBitmapFromByte(bitArr, 300, 200));
            }
            // Setup delete button listener
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDB.deleteNote(note.getId());
                    mNotes.remove(position);
                    NoteAdapter.this.notifyItemRemoved(position);
                    NoteAdapter.this.notifyItemRangeChanged(0, mNotes.size());
                    NoteAdapter.this.notifyDataSetChanged();
                }
            });

            mShareButton.setOnClickListener(new View.OnClickListener() {
                RollInOutAnimation rollInOut = new RollInOutAnimation(context);
                @Override
                public void onClick(View v) {
                    if (pressedOut) {
                        rollInOut.rollInThird(mGooglePlusButton).start();
                        rollInOut.rollInSecond(mTumblrButton).start();
                        rollInOut.rollInFirst(mTwitterButton).start();
                        pressedOut = false;
                    } else {
                        rollInOut.rollOutFirst(mTwitterButton).start();
                        rollInOut.rollOutSecond(mTumblrButton).start();
                        rollInOut.rollOutThird(mGooglePlusButton).start();
                        pressedOut = true;
                    }
                }
            });

            mTwitterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TweetComposer.Builder builder = new TweetComposer.Builder(mAdapterContext)
                            .text(note.getText());
                    builder.show();
                }
            });

            mTumblrButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TumblrPostDialogFragment tumblrPostFrag =
                            TumblrPostDialogFragment.newInstance(note);
                    tumblrPostFrag.show(((FragmentActivity) context).getSupportFragmentManager(),
                            "TumblrPostDialogFragment");
                }
            });

            mGooglePlusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mAdapterContext, "g+ pressed", Toast.LENGTH_LONG).show();
                    Intent shareIntent = new PlusShare.Builder(mAdapterContext)
                            .setType("text/plain")
                            .setText(note.getText())
                            .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                            .getIntent();
                    mAdapterContext.startActivity(shareIntent);
                }
            });

        }

        @Override
        public void onClick(View v) {
            // Start the Note Editor Activity
            Intent intent = new Intent(context, NoteEditorActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(NoteListFragment.NOTE, note);
            context.startActivity(intent);
        }
    }


}
