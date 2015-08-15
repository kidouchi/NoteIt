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
import kidouchi.noteit.Note;
import kidouchi.noteit.NoteAdapter;
import kidouchi.noteit.NoteDatabase;
import kidouchi.noteit.R;

public class NoteListActivity extends Activity {

    public static final String NOTE = "NOTE";

    private Note[] mNotes;
    private NoteDatabase mDB;
    @Bind(R.id.noteList) RecyclerView mNoteCardList;
    @Bind(R.id.edit_fab) FloatingActionButton mEditFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        ButterKnife.bind(this);

        mDB = new NoteDatabase(this);
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Setup the Note List Layout
        ArrayList<Note> dbNotes = mDB.getAllNotes();
        mNotes = dbNotes.toArray(new Note[dbNotes.size()]);
        NoteAdapter adapter = new NoteAdapter(mNotes, this);
        mNoteCardList.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mNoteCardList.setLayoutManager(layoutManager);

        mNoteCardList.setHasFixedSize(false);

        // Setup the Note Edit Button
        mEditFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDB.createNewNote();
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

    public void openNoteEditor(Note[] notes) {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra(NOTE, notes);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
