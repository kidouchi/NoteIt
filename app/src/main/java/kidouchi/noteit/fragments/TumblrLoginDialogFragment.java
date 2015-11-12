package kidouchi.noteit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import kidouchi.noteit.R;
import kidouchi.noteit.api.TumblrApi;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

/**
 * Created by iuy407 on 9/21/15.
 */
public class TumblrLoginDialogFragment extends android.support.v4.app.DialogFragment {

    private Button mTumblrLoginBtn;
    private TumblrApi mTumblrApiInstance = TumblrApi.getInstance();

    public static TumblrLoginDialogFragment newInstance() {
        TumblrLoginDialogFragment dialog = new TumblrLoginDialogFragment();
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate the Twitter dialog
        final View dialogSignInView = inflater.inflate(R.layout.tumblr_signin_dialog, null);

        mTumblrLoginBtn = (Button) dialogSignInView.findViewById(R.id.tumblr_login_button);

        builder.setView(dialogSignInView)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        final AlertDialog dialog = builder.create();

        mTumblrLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TumblrApiConnection().execute();
            }
        });

        return dialog;
    }

    private class TumblrApiConnection extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String authUrl;
            try {
                CommonsHttpOAuthConsumer consumer = mTumblrApiInstance.getConsumer();
                CommonsHttpOAuthProvider provider = mTumblrApiInstance.getProvider();
                authUrl = provider.retrieveRequestToken(consumer, TumblrApi.CALLBACK_URL);
                mTumblrApiInstance.setConsumer(consumer);
                mTumblrApiInstance.setProvider(provider);
                return authUrl;
            } catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            } catch (OAuthNotAuthorizedException e) {
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String authUrl) {
            Intent authIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));

            // Verify the intent will resolve to at least one activity
            if (authIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(authIntent);
            }
        }
    }
}
