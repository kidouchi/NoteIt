package kidouchi.noteit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.Arrays;

import butterknife.Bind;
import kidouchi.noteit.Note;
import kidouchi.noteit.R;

public class NoteEditorActivity extends AppCompatActivity {

    @Bind(R.id.edit_text) EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(NoteListActivity.NOTE);
        Note note = Arrays.copyOf(parcelables, parcelables.length, Note[].class)[0];
        mEditText.setText(note.getText());
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_note_editor, menu);
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
