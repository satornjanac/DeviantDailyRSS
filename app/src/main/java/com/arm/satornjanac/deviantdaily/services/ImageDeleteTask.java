package com.arm.satornjanac.deviantdaily.services;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

public class ImageDeleteTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        String urlStr = params[0];
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        int lastIndexOfSlash = urlStr.lastIndexOf('/');
        String fileName;
        if (lastIndexOfSlash == -1) {
            fileName = urlStr;
        } else {
            fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
        }
        File f = new File(root, fileName);
        f.delete();
        return null;
    }
}
