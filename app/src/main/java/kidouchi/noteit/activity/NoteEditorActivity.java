package kidouchi.noteit.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.note.Note;

public class NoteEditorActivity extends Activity {

    public static final String PHOTO_PATH = "";
    private AppDatabase mDB = AppDatabase.getInstance(this);

    @Bind(R.id.edit_text) EditText mEditText;
    @Bind(R.id.edit_title) EditText mEditTitle;
    @Bind(R.id.save_button) ImageButton mSaveButton;
//    @Bind(R.id.camera_button) ImageButton mCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        ButterKnife.bind(this);

        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        final Note note;
        if (intent.getParcelableExtra(NoteListFragment.NOTE) != null) {
            note = intent.getParcelableExtra(NoteListFragment.NOTE);
            mEditText.setText(note.getText().toString());
            mEditTitle.setText(note.getTitle().toString());
        } else {
            note = mDB.createNewNote();
        }

        // Action for when SAVE button is pressed
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update text
                String saveText = mEditText.getText().toString();
                mDB.updateNoteText(note.getId(), saveText);

                // Update title
                String saveTitle = mEditTitle.getText().toString();
                mDB.updateNoteTitle(note.getId(), saveTitle);

                // Get bitmap of text from EditText View
                mEditText.buildDrawingCache();
                Bitmap bitmap = mEditText.getDrawingCache();

                // Convert bitmap to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] bitmapArr = stream.toByteArray();

                // Update bitmap
                mDB.updateNoteBitmap(note.getId(), bitmapArr);

                Intent intent = new Intent(NoteEditorActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
