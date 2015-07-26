package com.arm.satornjanac.deviantdaily.services;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.arm.satornjanac.deviantdaily.DataSetObservableActivity;
import com.arm.satornjanac.deviantdaily.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageSaveTask extends AsyncTask<String, Void, String> {

    private DataSetObservableActivity mActivity;

    public ImageSaveTask(DataSetObservableActivity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlStr = params[0];
        Bitmap img = null;
        String fileName = "";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(urlStr);
        HttpResponse response;
        try {
            File root =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            response = (HttpResponse)client.execute(request);
            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
            InputStream inputStream = bufferedEntity.getContent();
            img = BitmapFactory.decodeStream(inputStream);
            int lastIndexOfSlash = urlStr.lastIndexOf('/');
            if (lastIndexOfSlash == -1) {
                fileName = urlStr;
            } else {
                fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
            }
            FileOutputStream f = new FileOutputStream(new File(root, fileName));
            img.compress(Bitmap.CompressFormat.JPEG, 90, f);
            f.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Log.e("***ARM***", e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("***ARM***", e.getMessage());
        } catch (Exception e) {
            Log.e("***ARM***", e.getMessage());
        }
        return fileName;
    }

    @Override
    protected void onPostExecute(String fileName) {
        super.onPostExecute(fileName);
        if (!TextUtils.isEmpty(fileName)) {
            Utils.showSnackbar(mActivity, fileName);
        }
    }
}
