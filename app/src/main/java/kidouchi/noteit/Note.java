package kidouchi.noteit;

/**
 * Created by iuy407 on 8/10/15.
 */
public class Note {
    private int _id;
    private String _title;
    private String _text;

    public Note() {}

    public Note(int id) {
        this._id = id;
        this._title = "Untitled";
        this._text = ""; // Every new note starts empty
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
}
