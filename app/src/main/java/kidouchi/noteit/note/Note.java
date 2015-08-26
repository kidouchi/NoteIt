package kidouchi.noteit.note;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iuy407 on 8/10/15.
 */
public class Note implements Parcelable {
    private int _id;
    private String _title;
    private String _text;
    private byte[] _textBitmap;

    public Note(int id, String title, String text, byte[] textBitmap) {
        this._id = id;
        this._title = title;
        this._text = text;
        this._textBitmap = textBitmap;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return this._title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getText() {
        return this._text;
    }

    public void setText(String text) {
        this._text = text;
    }

    public byte[] getTextBitmap() {
        return _textBitmap;
    }

    public void setTextBitmap(byte[] _textBitmap) {
        this._textBitmap = _textBitmap;
    }

    // Make note parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_title);
        dest.writeString(_text);
        dest.writeInt(_textBitmap.length);
        dest.writeByteArray(_textBitmap);
    }

    private Note(Parcel in) {
        _id = in.readInt();
        _title = in.readString();
        _text = in.readString();
        _textBitmap = new byte[in.readInt()];
        in.readByteArray(_textBitmap);
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
