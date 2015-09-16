package kidouchi.noteit.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.photo.Photo;
import kidouchi.noteit.photo.PhotoAdapter;

public class PhotoListFragment extends Fragment {

    public static final String PHOTO = PhotoListFragment.class.getSimpleName();
    public static final int REQUEST_TAKE_PHOTO = 100;
    public static final String PHOTO_FILEPATH = "PHOTO_FILEPATH";

    private File mPhotoFile;
    private PhotoAdapter mAdapter;

    private Photo[] mPhotos;
    private AppDatabase mDB;

    @Bind(R.id.photo_recycler_view) RecyclerView mPhotoRecyclerView;
    @Bind(R.id.camera_fab_button) FloatingActionButton mCameraButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.photo_list_fragment, container, false);
        ButterKnife.bind(this, rootView);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getActivity().getPackageManager();
                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    dispatchTakePictureIntent();
                } else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Camera Error")
                            .setMessage("You do not have a camera")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDB = new AppDatabase(getActivity());
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Photo> photos = mDB.getAllPhotos();
        mPhotos = photos.toArray(new Photo[photos.size()]);
        mAdapter = new PhotoAdapter(mPhotos, getActivity(), mDB);
        mPhotoRecyclerView.setAdapter(mAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mPhotoRecyclerView.setLayoutManager(layoutManager);
        mPhotoRecyclerView.setHasFixedSize(false);
    }

    // Returns the photo filepath
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create the File where the photo should go
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (mPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check the request was successful
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
//            Photo newPhoto = mDB.createNewPhoto("file:" + mPhotoFile.getAbsolutePath());
            Photo newPhoto = mDB.createNewPhoto(mPhotoFile.getAbsolutePath());
            mAdapter.addPhoto(newPhoto);
            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
            mAdapter.notifyItemRangeInserted(0, mAdapter.getItemCount());
            mAdapter.notifyDataSetChanged();
            Toast.makeText(
                    getActivity(),
                    "Your picture was also saved in your gallery",
                    Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, // prefix
                ".jpg", // suffix
                storageDir // directory
        );

        return image;
    }

//    public void galleryAddPic(String filepath) {
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File file = new File(filepath);
//        Uri contentUri = Uri.fromFile(file);
//        intent.setData(contentUri);
//        getActivity().sendBroadcast(intent);
//    }

}
