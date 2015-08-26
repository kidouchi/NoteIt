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
import kidouchi.noteit.R;
import kidouchi.noteit.db.NoteItDatabase;
import kidouchi.noteit.note.Note;

public class NoteEditorActivity extends AppCompatActivity {

    public static final String PHOTO_PATH = "";
    private NoteItDatabase mDB;

    @Bind(R.id.edit_text) EditText mEditText;
    @Bind(R.id.edit_title) EditText mEditTitle;
    @Bind(R.id.save_button) ImageButton mSaveButton;
//    @Bind(R.id.camera_button) ImageButton mCameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        ButterKnife.bind(this);

        mDB = new NoteItDatabase(this);
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        final Note note;
        if (intent.getParcelableExtra(NoteListActivity.NOTE) != null) {
            note = intent.getParcelableExtra(NoteListActivity.NOTE);
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

                Intent intent = new Intent(NoteEditorActivity.this, NoteListActivity.class);
                startActivity(intent);
            }
        });

        // Action for when CAMERA button is pressed
//        mCameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CameraUtilities camera = new CameraUtilities(NoteEditorActivity.this);
//                camera.dispatchTakePictureIntent();
//                camera.galleryAddPic();
//            }
//        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check the request was successful
//        if (requestCode == CameraUtilities.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            String photoPathName = CameraUtilities.mCurrentPhotoPath;
//            Bitmap imageBitmap = BitmapFactory.decodeFile(photoPathName);
//            int cursor = mEditText.getSelectionStart();
//            SpannableStringBuilder builder = new SpannableStringBuilder(mEditText.getText());
//            ImageSpan imageSpan = new ImageSpan(NoteEditorActivity.this, imageBitmap,
//                    ImageSpan.ALIGN_BASELINE);
//            builder.setSpan(imageSpan, cursor-10, cursor, SpannableStringBuilder.SPAN_INCLUSIVE_EXCLUSIVE);
//            mEditText.setText(builder);
//            mEditText.setSelection(cursor);
//            camera.galleryAddPic();
//            Toast.makeText(
//                    NoteEditorActivity.this,
//                    "Your picture was saved in your gallery",
//                    Toast.LENGTH_LONG).show();
//        }
//    }

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
