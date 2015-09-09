package kidouchi.noteit.photo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by iuy407 on 8/17/15.
 *
 * Provided by Google's Android Developers website
 * Link: http://developer.android.com/training/camera/photobasics.html
 */
public class CameraUtilities {

    public static final int REQUEST_TAKE_PHOTO = 1;
    public static String mCurrentPhotoPath;
    private Activity mActivity;

    public CameraUtilities(Activity activity) {
        mActivity = activity;
    }

    // Returns the photo filepath
    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                mActivity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
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

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public void galleryAddPic() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(file);
        intent.setData(contentUri);
        mActivity.sendBroadcast(intent);
    }

}
