package kidouchi.noteit.photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import kidouchi.noteit.R;
import kidouchi.noteit.activity.PhotoViewerActivity;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.fragments.PhotoListFragment;

/**
 * Created by iuy407 on 8/26/15.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Photo[] mPhotos;
    private AppDatabase mDB;

    public PhotoAdapter(Photo[] photos, Context context, AppDatabase db) {
        this.mPhotos = photos;
        this.mDB = db;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.photo_list_item, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bindPhoto(mPhotos[position]);
    }

    @Override
    public int getItemCount() {
        return mPhotos.length;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        private TextView mPhotoTitleLabel;
        private ImageView mPhotoBitmap;
        private ImageButton mDeleteButton;
        private Photo photo;
        private Context context;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
//            mPhotoTitleLabel = (TextView) itemView.findViewById(R.id.list_item_title_label);
            mPhotoBitmap = (ImageView) itemView.findViewById(R.id.photo_item_bitmap);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(this);
        }

        public void bindPhoto(final Photo photo) {
            this.photo = photo;

            // Bind title
//            mPhotoTitleLabel.setText(photo.getTitle());

            // Bind photo bitmap
            byte[] bitArr = photo.getPhoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitArr, 0, bitArr.length);
            mPhotoBitmap.setImageBitmap(bitmap);

            // Bind delete button and listener
            mDeleteButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mDB.deletePhoto(photo.getId());
                    Intent intent = new Intent(context, PhotoListFragment.class);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            // Open Photo Viewer
            Intent intent = new Intent(context, PhotoViewerActivity.class);
            intent.putExtra(PhotoListFragment.PHOTO, photo);
            context.startActivity(intent);
        }
    }
}