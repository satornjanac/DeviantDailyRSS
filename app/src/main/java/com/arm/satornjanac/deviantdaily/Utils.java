package com.arm.satornjanac.deviantdaily;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.arm.satornjanac.deviantdaily.data.PhotoDetails;
import com.arm.satornjanac.deviantdaily.data.PhotoDetailsCache;
import com.arm.satornjanac.deviantdaily.services.ImageSaveTask;

import java.io.File;

public class Utils {

    public static void displayPopupWindow(final DataSetObservableActivity activity,
                                          final View anchorView, boolean isBigPhoto,
                                          final int position) {
        final PopupWindow popup = new PopupWindow(activity);
        View layout;
        if (isBigPhoto) {
            layout = activity.getLayoutInflater().inflate(R.layout.popup_content_big, null);
        } else {
            layout = activity.getLayoutInflater().inflate(R.layout.popup_content_small, null);
        }
        popup.setContentView(layout);

        PhotoDetails photoDetails = PhotoDetailsCache.getPhotoDetails(position);
        final String bigPhotoUrl = photoDetails.getPhotoUrl();
        final boolean isLocalFile = photoDetails.isLocalFile();

        final ImageButton buttonShare = (ImageButton) layout.findViewById(R.id.share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("image/jpeg");
                Drawable mDrawable;
                if (anchorView instanceof LinearLayout) {
                    ImageView imageView =
                            (ImageView) anchorView.findViewById(R.id.networkImageView);
                    mDrawable = imageView.getDrawable();
                } else {
                    mDrawable = ((ImageView) anchorView).getDrawable();
                }
                Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
                String path = MediaStore.Images.Media
                        .insertImage(activity.getContentResolver(), mBitmap, "Image Description",
                                null);
                Uri uri = Uri.parse(path);
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                activity.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                popup.dismiss();
            }
        });
        ImageButton buttonSave = (ImageButton) layout.findViewById(R.id.save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocalFile) {
                    Snackbar.make(activity.findViewById(android.R.id.content),
                            R.string.image_is_already_saved, Snackbar.LENGTH_LONG).show();
                    return;
                }
                new ImageSaveTask(activity).execute(bigPhotoUrl);
                Snackbar.make(activity.findViewById(android.R.id.content),
                        R.string.download_will_start_shortly_message, Snackbar.LENGTH_LONG).show();
                popup.dismiss();

            }
        });
        ImageButton buttonDelete = (ImageButton) layout.findViewById(R.id.delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocalFile) {
                    Utils.deleteFile(bigPhotoUrl);
                    PhotoDetailsCache.deleteItemFromPosition(position);
                    activity.onDataSetChanged();
                    return;
                }
                Snackbar.make(activity.findViewById(android.R.id.content),
                        R.string.cannot_delete_file_on_server, Snackbar.LENGTH_LONG).show();
            }
        });
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        int viewY = anchorView.getTop() + anchorView.getHeight();
        if (viewY + 200 > height) {
            popup.showAsDropDown(anchorView, 0, -2 * anchorView.getHeight(),
                    Gravity.VERTICAL_GRAVITY_MASK);
        } else {
            popup.showAsDropDown(anchorView, 0, 0, Gravity.VERTICAL_GRAVITY_MASK);
        }
    }

    public static void deleteFile(String photoUrl) {
        File root = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        int lastIndexOfSlash = photoUrl.lastIndexOf('/');
        String fileName;
        if (lastIndexOfSlash == -1) {
            fileName = photoUrl;
        } else {
            fileName = photoUrl.substring(photoUrl.lastIndexOf("/") + 1);
        }
        File f = new File(root, fileName);
        f.delete();
    }

    public static void showSnackbar(final DataSetObservableActivity activity, final String fileName) {
        Snackbar.make(activity.findViewById(android.R.id.content), R.string.image_is_downloaded,
                Snackbar.LENGTH_LONG).setAction(R.string.snackbar_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(fileName);
            }
        }).show();
        PhotoDetails photoDetails = new PhotoDetails(fileName, fileName);
        photoDetails.setIsLocalFile(true);
        PhotoDetailsCache.addItem(photoDetails);
        activity.onDataSetChanged();
    }
}
