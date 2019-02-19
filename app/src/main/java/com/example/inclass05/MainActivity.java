package com.example.inclass05;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PhotoLink.PhotoData {

    TextView keywordHolder;
    ArrayList<String> photoList;
    ProgressBar progressBar;
    ImageView leftArrow;
    ImageView rightArrow;
    ImageView imageView;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keywordHolder = findViewById(R.id.keywordHolder);
        progressBar = findViewById(R.id.progressBar);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        imageView = findViewById(R.id.imageView);
        leftArrow.setEnabled(false);
        rightArrow.setEnabled(false);

        findViewById(R.id.buttongo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    imageView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    new GetDataAsync().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                } else {
                    Toast.makeText(MainActivity.this, "Internet is Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);


                if (currentPosition == 0) {
                    showImage(photoList.get(photoList.size() - 1));
                } else showImage(photoList.get(currentPosition - 1));
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if (currentPosition == photoList.size() - 1) {
                    showImage(photoList.get(0));
                } else showImage(photoList.get(currentPosition + 1));
            }
        });

    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null || !networkInfo.isConnected()) {
            if ((networkInfo.getType() != ConnectivityManager.TYPE_WIFI) && (networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                return false;
            }
        }
        return true;
    }

    private class GetDataAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String result = null;
            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();
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

            }
            return result;
        }

        @Override
        protected void onPostExecute(final String s) {
            final String[] stringArray = s.split(";");

            final ArrayList<String> str = new ArrayList<>();
            str.addAll(Arrays.asList(stringArray));
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Pick one")
                    .setItems(stringArray, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int i) {
                            TextView k = findViewById(R.id.keywordHolder);
                            k.setText(str.get(i));
                            new PhotoLink(MainActivity.this).execute("http://dev.theappsdr.com/apis/photos/index.php?keyword=" + str.get(i));
                        }
                    });

            builder.create().show();

        }
    }

    @Override
    public void handlePhotoData(ArrayList<String> arrayList) {
        this.photoList = arrayList;

        if (arrayList.size() == 1 || arrayList.isEmpty()) {
            leftArrow.setEnabled(false);
            rightArrow.setEnabled(false);
            imageView.setImageResource(android.R.color.transparent);
            Toast toast = Toast.makeText(getApplicationContext(), "No Image Found", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            leftArrow.setEnabled(true);
            rightArrow.setEnabled(true);
        }
        showImage(photoList.get(0));
    }

    public void showImage(String a) {
        new GetImage((ImageView) findViewById(R.id.imageView), progressBar).execute(a);
        currentPosition = photoList.indexOf(a);
    }
}
