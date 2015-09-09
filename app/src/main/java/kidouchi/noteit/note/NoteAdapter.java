package kidouchi.noteit.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import kidouchi.noteit.R;
import kidouchi.noteit.activity.NoteEditorActivity;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.db.AppDatabase;

/**
 * Created by iuy407 on 8/9/15.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Note[] mNotes;
    private AppDatabase mDB;

    public NoteAdapter(Note[] notes, Context context, AppDatabase db) {
        mNotes = notes;
        mDB = db;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
        NoteViewHolder viewHolder = new NoteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int position) {
        noteViewHolder.bindNote(mNotes[position]);
    }

    @Override
    public int getItemCount() {
        return mNotes.length;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNoteTitleLabel;
        private ImageView mNoteBitmap;
        private ImageButton mDeleteButton;
        private Note note;
        private Context context;

        public NoteViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mNoteTitleLabel = (TextView) itemView.findViewById(R.id.note_item_title_label);
            mNoteBitmap = (ImageView) itemView.findViewById(R.id.note_item_bitmap);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(this);
        }

        public void bindNote(final Note note) {
            this.note = note;
            // Bind title
            mNoteTitleLabel.setText(note.getTitle());

            // Bind text bitmap
            byte[] bitArr = note.getTextBitmap();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitArr, 0, bitArr.length);
            mNoteBitmap.setImageBitmap(bitmap);

            // Setup delete button listener
            mDeleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mDB.deleteNote(note.getId());
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onClick(View v) {
            // Start the Note Editor Activity
            Intent intent = new Intent(context, NoteEditorActivity.class);
            intent.putExtra(NoteListFragment.NOTE, note);
            context.startActivity(intent);
        }
    }


}
