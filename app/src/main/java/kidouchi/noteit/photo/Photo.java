package kidouchi.noteit.photo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iuy407 on 8/22/15.
 */
public class Photo implements Parcelable {
    private int _id;
    private byte[] _photo;

    public Photo(int id, byte[] photo) {
        this._id = id;
        this._photo = photo;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
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
        dest.writeByteArray(_photo);
    }

    private Photo(Parcel in) {
        _id = in.readInt();
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
