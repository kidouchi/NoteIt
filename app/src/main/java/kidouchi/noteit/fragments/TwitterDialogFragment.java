package kidouchi.noteit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;

/**
 * Created by iuy407 on 9/18/15.
 */
public class TwitterDialogFragment extends android.support.v4.app.DialogFragment {

    @Bind(R.id.twitter_login_button) TwitterLoginButton mTwitterLoginBtn;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate the Twitter dialog
        builder.setView(inflater.inflate(R.layout.twitter_signin_dialog, null))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();

        // Bind twitter login button to dialog
        ButterKnife.bind(dialog);

        mTwitterLoginBtn.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

            }

            @Override
            public void failure(TwitterException e) {
                Toast.makeText(
                        getActivity(),
                        e.getMessage() + "Please try again",
                        Toast.LENGTH_LONG).show();
            }
        });

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mTwitterLoginBtn.onActivityResult(requestCode, resultCode, data);
    }
}
