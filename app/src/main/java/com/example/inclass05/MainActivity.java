package com.example.inclass05;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.buttongo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()){
                    Toast.makeText(MainActivity.this, "Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Is Not Connected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null || !networkInfo.isConnected()) {
            if ((networkInfo.getType() != ConnectivityManager.TYPE_WIFI) && (networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                return false;
            }
        }
        return true;
    }
}
