package kidouchi.noteit;

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

import kidouchi.noteit.activity.NoteEditorActivity;
import kidouchi.noteit.activity.NoteListActivity;

/**
 * Created by iuy407 on 8/9/15.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Note[] mNotes;
    private NoteDatabase mDB;

    public NoteAdapter(Note[] notes, Context context, NoteDatabase db) {
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
        private ImageView mNoteScreenshot;
        private ImageButton mDeleteButton;
        private Note note;
        private Context context;

        public NoteViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mNoteTitleLabel = (TextView) itemView.findViewById(R.id.note_title_label);
            mNoteScreenshot = (ImageView) itemView.findViewById(R.id.note_screenshot);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(this);
        }

        public void bindNote(final Note note) {
            this.note = note;
            mNoteTitleLabel.setText(note.getTitle());

            byte[] bitmapArr = note.get_screenshot();
            if (bitmapArr != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapArr, 0, bitmapArr.length);
                // TODO: Get the body text to show, must finish
//                Matrix matrix = new Matrix();
//                matrix.postScale(0.5f, 0.5f);
//                Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, 30, 100, 100, matrix, true);
                mNoteScreenshot.setImageBitmap(bitmap);
            }

            // Setup delete button listener
            mDeleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mDB.deleteNote(note.getId());
                    Intent intent = new Intent(context, NoteListActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            // Start the Note Editor Activity
            Intent intent = new Intent(context, NoteEditorActivity.class);
            intent.putExtra(NoteListActivity.NOTE, note);
            context.startActivity(intent);
        }
    }


}
