package com.kelvindu.learning.scenema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/*
 * this is basically a splash activity.
 * turns out this one is the first activity launched by the app instead of that MainActivity
 * to make the app launch the splash firt first you have to set it up on android manifest replacing the LAUNCH on MainActivity
 * with this one
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
