package kidouchi.noteit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by iuy407 on 8/13/15.
 */
public class NoteDatabase extends SQLiteOpenHelper {

    /// Filepath to emulator sqlite3 file: data/data/kidouchi.noteit/databases/noteDB.db

    private static final String DB_NAME = "noteDB.db";
    private static final int DB_VER = 2;

    public static final String TABLE_NOTES = "table_notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_SCREENSHOT = "screenshot";

    private SQLiteDatabase noteDB = null;

    public NoteDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createNotesTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_SCREENSHOT + " BLOB)";
        db.execSQL(createNotesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DBAlter = "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN " + COLUMN_SCREENSHOT +
                " BLOB";
        db.execSQL(DBAlter);
    }

    public void open() throws SQLException {
        noteDB = this.getWritableDatabase();
    }

    public void close() {
        if (noteDB != null) {
            noteDB.close();
        }
    }

    public Note createNewNote() {
        noteDB.beginTransaction();
        String newTitle = "Untitled";
        String newText = "";
        byte[] newScreenshot = null;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, newTitle);
            values.put(COLUMN_TEXT, newText);
            values.put(COLUMN_SCREENSHOT, newScreenshot);
            int rowId = (int) noteDB.insert(TABLE_NOTES, null, values);

            noteDB.setTransactionSuccessful();
            return new Note(rowId, newTitle, newText, newScreenshot);
        } finally {
            noteDB.endTransaction();
        }
    }

    public Note selectNote(int id) {
        //String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_ID + " = " + id;

        Cursor cursor = noteDB.query(
                TABLE_NOTES,
                new String[] { COLUMN_ID },
                COLUMN_ID + " = ?",
                new String [] { id+"" },
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Find note title
            int colTitleNum = cursor.getColumnIndex(COLUMN_TITLE);
            String title = cursor.getString(colTitleNum);
            // Find note text
            int colTextNum = cursor.getColumnIndex(COLUMN_TEXT);
            String text = cursor.getString(colTextNum);
            // Find note screenshot
            int colScreenshotNum = cursor.getColumnIndex(COLUMN_SCREENSHOT);
            byte[] screenshot = cursor.getBlob(colScreenshotNum);

            Note note = new Note(id, title, text, screenshot);
            return note;
        }

        return null;
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();

        // Get all items in notes table
        Cursor cursor = noteDB.query(
            TABLE_NOTES, null, null, null, null, null, null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int colIdNum = cursor.getColumnIndex(COLUMN_ID);
            int rowId = cursor.getInt(colIdNum);

            int colTitleNum = cursor.getColumnIndex(COLUMN_TITLE);
            String noteTitle = cursor.getString(colTitleNum);

            int colTextNum = cursor.getColumnIndex(COLUMN_TEXT);
            String noteText = cursor.getString(colTextNum);

            int colScreenshotNum = cursor.getColumnIndex(COLUMN_SCREENSHOT);
            byte[] noteScreenshot = cursor.getBlob(colScreenshotNum);

            notes.add(new Note(rowId, noteTitle, noteText, noteScreenshot));
            cursor.moveToNext();
        }

        return notes;
    }

    public void updateNoteTitle(int id, String newTitle) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        noteDB.update(
                TABLE_NOTES,
                values,
                COLUMN_ID + " = ?",
                new String[] { id+"" }
        );
    }

    public void updateNoteText(int id, String newText) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, newText);
        noteDB.update(
                TABLE_NOTES,
                values,
                COLUMN_ID + " = ?",
                new String[] { id+"" }
        );
    }

    public void updateNoteScreenshot(int id, byte[] newScreenshot) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCREENSHOT, newScreenshot);
        noteDB.update(
                TABLE_NOTES,
                values,
                COLUMN_ID + " =?",
                new String[] { id+"" }
        );
    }

    public void deleteNote(int id) {
        noteDB.delete(
                TABLE_NOTES,
                COLUMN_ID + " = ?",
                new String[] { id+"" }
        );
    }

}
