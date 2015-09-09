package kidouchi.noteit.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;

import kidouchi.noteit.note.Note;
import kidouchi.noteit.photo.Photo;

/**
 * Created by iuy407 on 8/13/15.
 */
public class AppDatabase extends SQLiteOpenHelper {

    /// Filepath to emulator sqlite3 file: data/data/kidouchi.noteit/databases/noteDB.db

    private static final String DB_NAME = "noteDB.db";
    private static final int DB_VER = 6;

    public static final String TABLE_NOTES = "table_notes";
    public static final String COLUMN_NOTE_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_TEXT_BITMAP = "bitmap";

    public static final String TABLE_PHOTOS = "table_photos";
    public static final String COLUMN_PHOTO_ID = "_id";
    public static final String COLUMN_PHOTO_TITLE = "title";
    public static final String COLUMN_PHOTO_FILEPATH = "filepath";
    public static final String COLUMN_PHOTO_BITMAP = "photo";

    private SQLiteDatabase noteDB = null;

    public AppDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create NOTE Table
        String createNotesTable = "CREATE TABLE " + TABLE_NOTES + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_TEXT + " TEXT, " +
                COLUMN_TEXT_BITMAP + " BLOB)";
        db.execSQL(createNotesTable);

        // Create PHOTO Table
        String createPhotosTable = "CREATE TABLE " + TABLE_PHOTOS + " (" +
                COLUMN_PHOTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PHOTO_TITLE + " TEXT, " +
                COLUMN_PHOTO_FILEPATH + " TEXT, " +
                COLUMN_PHOTO_BITMAP + "BLOB)";
        db.execSQL(createPhotosTable);
     }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropNoteTable = "DROP TABLE IF EXISTS " + TABLE_NOTES;
        db.execSQL(dropNoteTable);

        String dropPhotoTable = "DROP TABLE IF EXISTS " + TABLE_PHOTOS;
        db.execSQL(dropPhotoTable);
        // Create brand new tables
        onCreate(db);
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
        byte[] bitmap = null;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, newTitle);
            values.put(COLUMN_TEXT, newText);
            values.put(COLUMN_TEXT_BITMAP, bitmap);
            int rowId = (int) noteDB.insert(TABLE_NOTES, null, values);

            noteDB.setTransactionSuccessful();
            return new Note(rowId, newTitle, newText, bitmap);
        } finally {
            noteDB.endTransaction();
        }
    }

    public Photo createNewPhoto(byte[] photo) {
        noteDB.beginTransaction();

        String newTitle = "Untitled";
        String newFilepath = "";
        byte[] bitmap = null;

        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_PHOTO_BITMAP, photo);
            int rowId = (int) noteDB.insert(TABLE_PHOTOS, null, values);

            noteDB.setTransactionSuccessful();
            return new Photo(rowId, newTitle, newFilepath, photo);
        } finally {
            noteDB.endTransaction();
        }
    }

    public Note getNote(int id) {
        Cursor cursor = noteDB.query(
                TABLE_NOTES,
                new String[] { COLUMN_NOTE_ID },
                null,
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
            int colTextBitmapNum = cursor.getColumnIndex(COLUMN_TEXT_BITMAP);
            byte[] textBitmap = cursor.getBlob(colTextBitmapNum);

            return new Note(id, title, text, textBitmap);
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
            int colIdNum = cursor.getColumnIndex(COLUMN_NOTE_ID);
            int rowId = cursor.getInt(colIdNum);

            int colTitleNum = cursor.getColumnIndex(COLUMN_TITLE);
            String noteTitle = cursor.getString(colTitleNum);

            int colTextNum = cursor.getColumnIndex(COLUMN_TEXT);
            String noteText = cursor.getString(colTextNum);

            int colTextBitmap = cursor.getColumnIndex(COLUMN_TEXT_BITMAP);
            byte[] noteBitmap = cursor.getBlob(colTextBitmap);

            notes.add(new Note(rowId, noteTitle, noteText, noteBitmap));
            cursor.moveToNext();
        }

        return notes;
    }

    public ArrayList<Photo> getAllPhotos() {
        ArrayList<Photo> photos = new ArrayList<Photo>();

        // Get all photos in database
        Cursor cursor = noteDB.query(
                TABLE_PHOTOS, null, null, null, null, null, null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int colId = cursor.getColumnIndex(COLUMN_PHOTO_ID);
            int rowId = cursor.getInt(colId);

            int colTitle = cursor.getColumnIndex(COLUMN_PHOTO_TITLE);
            String photoTitle = cursor.getString(colTitle);

            int colFilepath = cursor.getColumnIndex(COLUMN_PHOTO_FILEPATH);
            String photoFilepath = cursor.getString(colFilepath);

            int colPhotoBitmap = cursor.getColumnIndex(COLUMN_PHOTO_BITMAP);
            byte[] photoBitmap = cursor.getBlob(colPhotoBitmap);

            photos.add(new Photo(rowId, photoTitle, photoFilepath, photoBitmap));
            cursor.moveToNext();
        }

        return photos;
    }

    public void updateNoteTitle(int id, String newTitle) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, newTitle);
        noteDB.update(
                TABLE_NOTES,
                values,
                COLUMN_NOTE_ID + " = ?",
                new String[]{ id + "" }
        );
    }

    public void updateNoteText(int id, String newText) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, newText);
        noteDB.update(
                TABLE_NOTES,
                values,
                COLUMN_NOTE_ID + " = ?",
                new String[]{ id + "" }
        );
    }

    public void updateNoteBitmap(int id, byte[] bitmap) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT_BITMAP, bitmap);
        noteDB.update(
                TABLE_NOTES,
                values,
                COLUMN_NOTE_ID + "= ?",
                new String[]{ id + "" }
        );
    }

    public void deleteNote(int id) {
        noteDB.delete(
                TABLE_NOTES,
                COLUMN_NOTE_ID + " = ?",
                new String[]{id + ""}
        );
    }

    public void deletePhoto(int id) {
        noteDB.delete(
                TABLE_PHOTOS,
                COLUMN_PHOTO_ID + " = ?",
                new String[] { id+"" }

        );
    }

}
