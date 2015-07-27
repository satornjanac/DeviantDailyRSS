package com.arm.satornjanac.deviantdaily.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.arm.satornjanac.deviantdaily.MainActivity;
import com.arm.satornjanac.deviantdaily.data.PhotoDetails;
import com.arm.satornjanac.deviantdaily.data.PhotoDetailsCache;
import com.arm.satornjanac.deviantdaily.parser.DeviantXMLParser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DeviantRSSDownloadAndFileLoadTask extends AsyncTask<Void, Void, Void>{

    private MainActivity mActivity;

    public DeviantRSSDownloadAndFileLoadTask(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        List photoDetails;
        photoDetails = getImageDetailsFromFeed();
        photoDetails.addAll(getImageDetailsFromFile());
        PhotoDetailsCache.setCachedPhotoDetails(photoDetails);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mActivity.setUpPhotoGrid(PhotoDetailsCache.getCachedPhotoDetails());
    }

    private boolean isPhoto(String name) {
        name = name.toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
    }

    private List getImageDetailsFromFeed() {
        List list = new ArrayList();
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse httpResponse;
        String responseString;

        ConnectivityManager cm =
                (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        try {
            if (isConnected) {
                HttpGet getRequest = new HttpGet(
                        "http://backend.deviantart.com/rss.xml?type=deviation&q=boost%3Apopular");
                getRequest.addHeader("Accept", "application/xml");

                httpResponse = httpclient.execute(getRequest);
                StatusLine statusLine = httpResponse.getStatusLine();

                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    // negative for unknown
                    if (httpResponse.getEntity().getContentLength() != 0) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        httpResponse.getEntity().writeTo(out);
                        out.close();
                        responseString = out.toString();
                        InputStream is = new ByteArrayInputStream(
                                Charset.forName("UTF-8").encode(responseString).array());
                        list = new DeviantXMLParser().parse(is);
                    }
                } else {
                    // Closes the connection.
                    httpResponse.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }
        } catch (ClientProtocolException e) {
            Log.e("Deviant", e.getMessage());
        } catch (IOException e) {
            Log.e("Deviant", e.getMessage());
        } catch (Exception e) {
            Log.e("Deviant", e.getMessage());
        }
        return list;
    }

    private List getImageDetailsFromFile() {
        List list = new ArrayList();
        try {
            File root =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File[] files = root.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (isPhoto(files[i].getName())) {
                    String fileName = files[i].toURI().toURL().toString();
                    fileName = fileName.replace(":", "://");
                    PhotoDetails photoDetail = new PhotoDetails(fileName, fileName);
                    photoDetail.setIsLocalFile(true);
                    list.add(photoDetail);
                }
            }
        } catch (MalformedURLException e) {
            Log.e("Deviant", e.getMessage());
        }
        return list;
    }
}
