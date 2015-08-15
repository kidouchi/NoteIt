package kidouchi.noteit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kidouchi.noteit.activity.NoteListActivity;


/**
 * Created by iuy407 on 8/9/15.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Note[] mNotes;

    public NoteAdapter(Note[] notes, Context context) {
        mNotes = notes;
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

        public TextView mNoteTitleLabel;

        public NoteViewHolder(View itemView) {
            super(itemView);

            mNoteTitleLabel = (TextView) itemView.findViewById(R.id.noteTitleLabel);
            itemView.setOnClickListener(this);
        }

        public void bindNote(Note note) {
            mNoteTitleLabel.setText(note.getTitle());
        }

        @Override
        public void onClick(View v) {
            // Start the Note Editor Activity
            NoteListActivity nla = new NoteListActivity();
            nla.openNoteEditor(mNotes);
        }
    }


}
