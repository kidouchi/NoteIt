package kidouchi.noteit.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.fragments.PhotoListFragment;
import kidouchi.noteit.photo.Photo;

public class PhotoViewerActivity extends Activity {

    public static final String PHOTO_VIEWER_TAG = PhotoViewerActivity.class.getSimpleName();

    @Bind(R.id.photo_image_view) ImageView mPhotoImageView;
    @Bind(R.id.back_to_photo_list_button) FloatingActionButton mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Photo photo = intent.getParcelableExtra(PhotoListFragment.PHOTO);
        String photoPathName = photo.getFilepath();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPathName, bmOptions);

        // Set bitmap dimensions
        bmOptions.outWidth = mPhotoImageView.getMaxWidth();
        bmOptions.outHeight = mPhotoImageView.getMaxHeight()/2;

        bmOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPathName, bmOptions);

        mPhotoImageView.setImageBitmap(bitmap);
        Log.d("tEST1", "in here");
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(PHOTO_VIEWER_TAG, MainActivity.PHOTO_VIEWER_INTENT_TAG);
                startActivity(intent);
                finish();
            }
        });
    }



}
