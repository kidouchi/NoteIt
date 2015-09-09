package kidouchi.noteit.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kidouchi.noteit.R;
import kidouchi.noteit.fragments.NoteListFragment;
import kidouchi.noteit.fragments.PhotoListFragment;

public class MainActivity extends FragmentActivity {

    private NoteListFragment mNoteListFragment;
    private PhotoListFragment mPhotoListFragment;

    private ListSectionsPagerAdapter mListSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public class ListSectionsPagerAdapter extends FragmentPagerAdapter {

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

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
