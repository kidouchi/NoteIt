package kidouchi.noteit.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.photo.CameraUtilities;
import kidouchi.noteit.photo.Photo;
import kidouchi.noteit.photo.PhotoAdapter;

public class PhotoListFragment extends ListFragment {

    public static final String PHOTO = PhotoListFragment.class.getSimpleName();
    private Photo[] mPhotos;
    private AppDatabase mDB;

    @Bind(R.id.photo_recycler_view) RecyclerView mPhotoRecyclerView;
    @Bind(R.id.camera_fab_button) FloatingActionButton mCameraButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_photo_fragment, container, false);
        ButterKnife.bind(rootView);

        mDB = new AppDatabase(getActivity());
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Photo> photos = mDB.getAllPhotos();
        mPhotos = photos.toArray(new Photo[photos.size()]);
        PhotoAdapter adapter = new PhotoAdapter(mPhotos, getActivity(), mDB);
        mPhotoRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mPhotoRecyclerView.setLayoutManager(layoutManager);

        mPhotoRecyclerView.setHasFixedSize(false);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    CameraUtilities cameraUtil = new CameraUtilities(getActivity());
                    cameraUtil.dispatchTakePictureIntent();
                }
            }
        });

        return rootView;
    }
}
