package com.example.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PhotoLink extends AsyncTask<String, Void, ArrayList> {
    ImageView imageView;

    PhotoData photoData;
    public PhotoLink(PhotoData photoData) {
        this.photoData = photoData;
    }

    @Override
    protected ArrayList doInBackground(String... params) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String [] stringArray = null;
        ArrayList<String> result = new ArrayList<>();

        String a = "";
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                a = IOUtils.toString(connection.getInputStream(), "UTF8");
               stringArray = a.split("\n");
                for (String b : stringArray) {
                    result.add(b);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }   return result;
    }

    @Override
    protected void onPostExecute(ArrayList arrayList) {
        photoData.handlePhotoData(arrayList);
    }

    public static interface PhotoData{
        public void handlePhotoData(ArrayList<String> arrayList);
    }
}
