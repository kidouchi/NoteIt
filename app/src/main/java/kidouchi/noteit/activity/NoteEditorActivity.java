package kidouchi.noteit.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.Note;
import kidouchi.noteit.NoteDatabase;
import kidouchi.noteit.R;

public class NoteEditorActivity extends AppCompatActivity {

    private NoteDatabase mDB;

    @Bind(R.id.edit_text) EditText mEditText;
    @Bind(R.id.edit_title) EditText mEditTitle;
    @Bind(R.id.save_button) ImageButton mSaveButton;
    @Bind(R.id.camera_button) ImageButton mCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        ButterKnife.bind(this);

        mDB = new NoteDatabase(this);
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        final Note note = intent.getParcelableExtra(NoteListActivity.NOTE);
        mEditText.setText(note.getText().toString());
        mEditTitle.setText(note.getTitle().toString());

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
                View rootView = mEditText.getRootView();
                rootView.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
                rootView.setDrawingCacheEnabled(false);

                // Convert bitmap to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] bitmapArr = stream.toByteArray();
                // Update screenshot
                mDB.updateNoteScreenshot(note.getId(), bitmapArr);

                Intent intent = new Intent(NoteEditorActivity.this, NoteListActivity.class);
                startActivity(intent);
            }
        });

        // Action for when CAMERA button is pressed
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
