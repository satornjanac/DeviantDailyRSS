package com.arm.satornjanac.deviantdaily.data;

public class PhotoDetails {

    private String mThumbnailUrl;
    private String mPhotoUrl;

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

    public boolean isLocalFile() {
        return isLocalFile;
    }

    public void setIsLocalFile(boolean isLocalFile) {
        this.isLocalFile = isLocalFile;
    }
}
