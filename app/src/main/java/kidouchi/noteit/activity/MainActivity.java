package kidouchi.noteit.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.tumblr.jumblr.JumblrClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import kidouchi.noteit.R;
import kidouchi.noteit.api.TumblrApi;
import kidouchi.noteit.api.TwitterApi;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.fragments.PhotoListFragment;
import kidouchi.noteit.note.Note;
import kidouchi.noteit.photo.Photo;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class MainActivity extends FragmentActivity {

    public static final int PHOTO_VIEWER_INTENT_TAG = 42;
    private static final int NOTE_TAB_INT = 0;
    private static final int PHOTO_TAB_INT = 1;

    private TumblrApi mTumblrApiInstance = TumblrApi.getInstance();
    private AppDatabase mDB = AppDatabase.getInstance(this);

    @Bind(R.id.pager_tab_strip)
    PagerTabStrip mPagerTabStrip;
    private NoteListFragment mNoteListFragment;
    private PhotoListFragment mPhotoListFragment;
    private ListSectionsPagerAdapter mListSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(TwitterApi.TWITTER_KEY, TwitterApi.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /** Instantiate Database! **/
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mListSectionsPagerAdapter = new ListSectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mListSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        switch (position) {
                            case NOTE_TAB_INT:
                                mPagerTabStrip.setBackgroundColor(getResources()
                                        .getColor(R.color.notes_tab_color));
                                break;
                            case PHOTO_TAB_INT:
                                mPagerTabStrip.setBackgroundColor(getResources()
                                        .getColor(R.color.photos_tab_color));
                                break;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                }
        );

        mNoteListFragment = new NoteListFragment();
        mPhotoListFragment = new PhotoListFragment();

        /** RECEIVING INTENT **/
        Intent intent = getIntent();

        if (intent != null) {
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                // Pass uri and sign in to Tumblr
                new TumblrSigninAsyncTask(intent.getData(), this).execute();
                Toast.makeText(this, "Your post was made on Tumblr", Toast.LENGTH_LONG).show();
            } else {
                int tag = intent.getIntExtra(PhotoViewerActivity.PHOTO_VIEWER_TAG, 0);
                // Check that PhotoViewerActivity sent intent
                if (tag == PHOTO_VIEWER_INTENT_TAG) {
                    mViewPager.setCurrentItem(PHOTO_TAB_INT);
                }
            }
        }
    }

    private class TumblrSigninAsyncTask extends AsyncTask<Void, Void, Void> {

        private Uri mUri;
        private Context mContext;

        private TumblrSigninAsyncTask(Uri uri, Context context) {
            this.mUri = uri;
            this.mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            String verifier = mUri.getQueryParameter("oauth_verifier");
            try {
                CommonsHttpOAuthConsumer consumer = mTumblrApiInstance.getConsumer();
                CommonsHttpOAuthProvider provider = mTumblrApiInstance.getProvider();
                provider.retrieveAccessToken(consumer, verifier);

                mTumblrApiInstance.setConsumer(consumer);
                mTumblrApiInstance.setProvider(provider);

                JumblrClient client = mTumblrApiInstance.getClient();
                client.setToken(consumer.getToken(), consumer.getTokenSecret());
                mTumblrApiInstance.setClient(client);

                mTumblrApiInstance.setUser(client.user());
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
    }

    public class ListSectionsPagerAdapter extends FragmentStatePagerAdapter {

        public ListSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case NOTE_TAB_INT:
                    ArrayList<Note> notes = mDB.getAllNotes();
                    Note[] noteArr = new Note[notes.size()];
                    bundle.putParcelableArray("NoteArray", notes.toArray(noteArr));
                    mNoteListFragment.setArguments(bundle);
                    return mNoteListFragment;
                case PHOTO_TAB_INT:
                    ArrayList<Photo> photos = mDB.getAllPhotos();
                    Photo[] photoArr = new Photo[photos.size()];
                    bundle.putParcelableArray("PhotoArray", photos.toArray(photoArr));
                    mPhotoListFragment.setArguments(bundle);
                    return mPhotoListFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case NOTE_TAB_INT:
                    return "NOTES";
                case PHOTO_TAB_INT:
                    return "PHOTOS";
            }
            return null;
        }
    }

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
    public void onPause() {
        super.onPause();
        mDB.close();
    }
}
