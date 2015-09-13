package kidouchi.noteit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.fragments.PhotoListFragment;

public class MainActivity extends FragmentActivity {

    private NoteListFragment mNoteListFragment;
    private PhotoListFragment mPhotoListFragment;

    private ListSectionsPagerAdapter mListSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Bind(R.id.pager_tab_strip) PagerTabStrip mPagerTabStrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mListSectionsPagerAdapter = new ListSectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mListSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
               @Override
               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

               @Override
               public void onPageSelected(int position) {
                   switch (position) {
                       case 0:
                           mPagerTabStrip.setBackgroundColor(getResources()
                                   .getColor(R.color.notes_tab_color));
                           break;
                       case 1:
                           mPagerTabStrip.setBackgroundColor(getResources()
                                   .getColor(R.color.photos_tab_color));
                           break;
                   }
               }

               @Override
               public void onPageScrollStateChanged(int state) {}
           }
        );

        mNoteListFragment = new NoteListFragment();
        mPhotoListFragment = new PhotoListFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check the request was successful
        Log.d("M123", "IN HERE: " + requestCode + " | " + resultCode + " | " + data);

        if (requestCode == PhotoListFragment.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
//            Intent intent = new Intent(getActivity(), PhotoViewerActivity.class);
//            Bundle extras = new Bundle();
//            extras.put
//            extras.putExtras(data);
//            extras.putExtra(CameraUtilities.PHOTO_FILEPATH,
//                    data.getStringExtra(CameraUtilities.PHOTO_FILEPATH));
//            intent.putExtras(extras);
//            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
        return;

    }

    public class ListSectionsPagerAdapter extends FragmentStatePagerAdapter {

        public ListSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return mNoteListFragment;
                case 1:
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
                case 0:
                    return "NOTES";
                case 1:
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
