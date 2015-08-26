package kidouchi.noteit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.db.NoteItDatabase;
import kidouchi.noteit.note.Note;
import kidouchi.noteit.note.NoteAdapter;

public class NoteListActivity extends Activity {

    public static final String NOTE = "NOTE";
    private Note[] mNotes;
    private NoteItDatabase mDB;

    @Bind(R.id.noteList) RecyclerView mNoteCardList;
    @Bind(R.id.edit_fab) FloatingActionButton mEditFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);

        mDB = new NoteItDatabase(this);
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Setup the Note List Layout
        ArrayList<Note> dbNotes = mDB.getAllNotes();
        mNotes = dbNotes.toArray(new Note[dbNotes.size()]);
        NoteAdapter adapter = new NoteAdapter(mNotes, this, mDB);
        mNoteCardList.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mNoteCardList.setLayoutManager(layoutManager);

        mNoteCardList.setHasFixedSize(false);

        // Setup the Note Edit Button
        mEditFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, NoteEditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mDB.close();
    }
}
