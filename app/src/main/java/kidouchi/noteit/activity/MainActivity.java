package kidouchi.noteit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import kidouchi.noteit.R;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.fragments.PhotoListFragment;

public class MainActivity extends FragmentActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3N6v5WACB2ve8oUYi50ZpFzKt";
    private static final String TWITTER_SECRET = "xDV0VX1SpnglmD1i7MTsB4r9LVOrwOiiXGH8dBEGgdBrLBlW52";


    private NoteListFragment mNoteListFragment;
    private PhotoListFragment mPhotoListFragment;

    private static final int NOTE_TAB_INT = 0;
    private static final int PHOTO_TAB_INT = 1;

    public static final int PHOTO_VIEWER_INTENT_TAG = 42;

    private ListSectionsPagerAdapter mListSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Bind(R.id.pager_tab_strip) PagerTabStrip mPagerTabStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

        Intent intent = getIntent();

        if (intent != null) {
            int tag = intent.getIntExtra(PhotoViewerActivity.PHOTO_VIEWER_TAG, 0);
            // Check that PhotoViewerActivity sent intent
            if (tag == PHOTO_VIEWER_INTENT_TAG) {
                mViewPager.setCurrentItem(PHOTO_TAB_INT);
            }
        }
    }

    public class ListSectionsPagerAdapter extends FragmentStatePagerAdapter {

        public ListSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case NOTE_TAB_INT:
                    return mNoteListFragment;
                case PHOTO_TAB_INT:
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
            switch(position) {
                case NOTE_TAB_INT:
                    return "NOTES";
                case PHOTO_TAB_INT:
                    return "PHOTOS";
            }
            return null;
        }
    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        mNoteListFragment.closeDatabase();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
}
