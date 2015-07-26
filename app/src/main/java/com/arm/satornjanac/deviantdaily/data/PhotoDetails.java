package com.arm.satornjanac.deviantdaily.data;


import android.graphics.Bitmap;

public class PhotoDetails {

    private String mThumbnailUrl;
    private String mPhotoUrl;
    private Bitmap mPhoto;

    private boolean isLocalFile;

    public PhotoDetails(String mThumbnailUrl, String mPhotoUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
        this.mPhotoUrl = mPhotoUrl;
        this.isLocalFile = false;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public Bitmap getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Bitmap mPhoto) {
        this.mPhoto = mPhoto;
    }

    public boolean isLocalFile() {
        return isLocalFile;
    }

    public void setIsLocalFile(boolean isLocalFile) {
        this.isLocalFile = isLocalFile;
    }
}
