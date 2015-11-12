package kidouchi.noteit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tumblr.jumblr.types.TextPost;

import kidouchi.noteit.R;
import kidouchi.noteit.api.TumblrApi;
import kidouchi.noteit.note.Note;

/**
 * Created by iuy407 on 9/24/15.
 */
public class TumblrPostDialogFragment extends android.support.v4.app.DialogFragment {

    private EditText mPostTitle;
    private EditText mPostBody;
    private Button mPostButton;

    private TumblrApi mTumblrApiInstance = TumblrApi.getInstance();

    private Note note;

    public static TumblrPostDialogFragment newInstance(Note note) {
        TumblrPostDialogFragment dialog = new TumblrPostDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable("note", note);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        note = getArguments().getParcelable("note");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate the Twitter dialog
        View dialogView = inflater.inflate(R.layout.tumblr_post_note_dialog, null);

        mPostTitle = (EditText) dialogView.findViewById(R.id.tumblr_post_title);
        mPostBody = (EditText) dialogView.findViewById(R.id.tumblr_post_text_body);
        mPostButton = (Button) dialogView.findViewById(R.id.tumblr_post_button);

        mPostTitle.setText(note.getTitle());
        mPostBody.setText(note.getText());

        mPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TUMBLR USER", mTumblrApiInstance.getUser() + "");
                if (mTumblrApiInstance.getUser() == null) {
                    TumblrLoginDialogFragment tumblrLoginFrag =
                            TumblrLoginDialogFragment.newInstance();
                    tumblrLoginFrag.show(((FragmentActivity) getActivity())
                                    .getSupportFragmentManager(), "TumblrLoginDialogFragment");
                } else {
                    try {
                        TextPost post = mTumblrApiInstance.getClient()
                                .newPost(mPostTitle.getText().toString(), TextPost.class);
                        post.setBody(mPostBody.getText().toString());
                        post.save();
                        Toast.makeText(getActivity(), "Post was made to Tumblr",
                                Toast.LENGTH_LONG).show();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.InstantiationException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        builder.setView(dialogView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();

        return dialog;
    }

}
