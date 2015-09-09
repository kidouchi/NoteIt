package kidouchi.noteit.photo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iuy407 on 8/22/15.
 */
public class Photo implements Parcelable {
    private int _id;
    private String _title;
    private String _filepath;
    private byte[] _photo;

    public Photo(int id, String title, String filepath, byte[] photo) {
        this._id = id;
        this._title = title;
        this._filepath = filepath;
        this._photo = photo;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getFilepath() {
        return _filepath;
    }

    public void setFilepath(String _filepath) {
        this._filepath = _filepath;
    }

    public byte[] getPhoto() {
        return _photo;
    }

    public void setPhoto(byte[] photo) {
        this._photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(_photo.length);
        dest.writeByteArray(_photo);
    }

    private Photo(Parcel in) {
        _id = in.readInt();
        _photo = new byte[in.readInt()];
        in.readByteArray(_photo);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
