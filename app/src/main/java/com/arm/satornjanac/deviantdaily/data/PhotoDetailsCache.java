package com.arm.satornjanac.deviantdaily.data;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetailsCache {

    private static List<PhotoDetails> sCachedPhotoDetails;

    public static List<PhotoDetails> getCachedPhotoDetails() {
        return sCachedPhotoDetails;
    }

    public static void setCachedPhotoDetails(List<PhotoDetails> sCachedPhotoDetails) {
        PhotoDetailsCache.sCachedPhotoDetails = sCachedPhotoDetails;
    }

    public static PhotoDetails getPhotoDetails(int i) {
        if (sCachedPhotoDetails != null) {
            if (i < sCachedPhotoDetails.size()) {
                return sCachedPhotoDetails.get(i);
            }
        }
        return null;
    }

    public static int getPhotoCacheSize(){
        return sCachedPhotoDetails == null ? 0 : sCachedPhotoDetails.size();
    }

    public static void deleteItemFromPosition(int position) {
        if (sCachedPhotoDetails != null) {
            sCachedPhotoDetails.remove(position);
        }
    }

    public static void addItem(PhotoDetails photoDetails) {
        if (sCachedPhotoDetails != null) {
            sCachedPhotoDetails.add(photoDetails);
        } else {
            sCachedPhotoDetails = new ArrayList<>();
            sCachedPhotoDetails.add(photoDetails);
        }
    }
}
