package kidouchi.noteit.note;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import kidouchi.noteit.R;
import kidouchi.noteit.activity.NoteEditorActivity;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.utilities.BitmapUtilities;

/**
 * Created by iuy407 on 8/9/15.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private ArrayList<Note> mNotes;
    private AppDatabase mDB;

    public NoteAdapter(Note[] notes, Context context, AppDatabase db) {
        mNotes = new ArrayList<Note>(Arrays.asList(notes));
        mDB = db;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
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

//        @Bind(R.id.note_item_title_label) TextView mNoteTitleLabel;
//        @Bind(R.id.note_item_bitmap) ImageView mNoteBitmap;
//        @Bind(R.id.note_delete_button) ImageButton mDeleteButton;
//        @Bind(R.id.share_button) ImageButton mShareButton;
//        @Bind(R.id.twitter_button) ImageButton mTwitterButton;
//        @Bind(R.id.tumblr_button) ImageButton mTumblrButton;
//        @Bind(R.id.twitch_button) ImageButton mTwitchButton;

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
//            ButterKnife.bind(context, itemView);

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
                }
            });

            // Create levels of roll out animation
            final Animation rollOutFirstBtn =
                    AnimationUtils.loadAnimation(context, R.anim.roll_out_first);
            final Animation rollOutSecondBtn =
                    AnimationUtils.loadAnimation(context, R.anim.roll_out_second);
            final Animation rollOutThirdBtn =
                    AnimationUtils.loadAnimation(context, R.anim.roll_out_third);

            // Create levels of roll in animation
            final Animation rollInFirstBtn =
                    AnimationUtils.loadAnimation(context, R.anim.roll_in_first);
            final Animation rollInSecondBtn =
                    AnimationUtils.loadAnimation(context, R.anim.roll_in_second);
            final Animation rollInThirdBtn =
                    AnimationUtils.loadAnimation(context, R.anim.roll_in_third);

            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pressedOut) {
                        mGooglePlusButton.startAnimation(rollInThirdBtn);
                        mTumblrButton.startAnimation(rollInSecondBtn);
                        mTwitterButton.startAnimation(rollInFirstBtn);
                        pressedOut = false;
                    } else {
                        mTwitterButton.startAnimation(rollOutFirstBtn);
                        mTumblrButton.startAnimation(rollOutSecondBtn);
                        mGooglePlusButton.startAnimation(rollOutThirdBtn);
                        pressedOut = true;
                    }
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
