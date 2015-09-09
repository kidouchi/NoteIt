package kidouchi.noteit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.activity.NoteEditorActivity;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.note.Note;
import kidouchi.noteit.note.NoteAdapter;

public class NoteListFragment extends ListFragment {

    public static final String NOTE = "NOTE";
    private Note[] mNotes;
    private AppDatabase mDB;

    @Bind(R.id.noteList) RecyclerView mNoteCardList;
    @Bind(R.id.edit_fab) FloatingActionButton mEditFabButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_note_fragment, container, false);
        ButterKnife.bind(rootView);

        mDB = new AppDatabase(getActivity());
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Setup the Note List Layout
        ArrayList<Note> dbNotes = mDB.getAllNotes();
        mNotes = dbNotes.toArray(new Note[dbNotes.size()]);
        NoteAdapter adapter = new NoteAdapter(mNotes, getActivity(), mDB);
        mNoteCardList.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mNoteCardList.setLayoutManager(layoutManager);

        mNoteCardList.setHasFixedSize(false);

        // Setup the Note Edit Button
        mEditFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteEditorActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void closeDatabase() {
        mDB.close();
    }
}
