package com.example.inclass05;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PhotoLinks extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Bitmap bitmap = null;

    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection connection = null;
       Bitmap image = null;
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                image = BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return image;
    }
    protected void onPostExecute(Bitmap bitmap){
        if (bitmap != null && imageView != null){
            imageView.setImageBitmap(bitmap);
        }
    }
}
