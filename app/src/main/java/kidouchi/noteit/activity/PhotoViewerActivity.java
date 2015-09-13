package kidouchi.noteit.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.fragments.PhotoListFragment;
import kidouchi.noteit.photo.Photo;
import kidouchi.noteit.utilities.BitmapUtilities;

public class PhotoViewerActivity extends Activity {

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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        mPhotoImageView.setImageBitmap(
                BitmapUtilities.decodeSampledBitmapFromFile(photoPathName, width, 400));

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoViewerActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
    }


}
