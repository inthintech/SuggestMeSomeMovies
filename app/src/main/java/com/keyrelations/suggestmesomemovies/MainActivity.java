package com.keyrelations.suggestmesomemovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialise the facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        //sets the layout file
        setContentView(R.layout.activity_main);
    }
}
