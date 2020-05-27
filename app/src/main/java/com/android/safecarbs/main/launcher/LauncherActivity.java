package com.android.safecarbs.main.launcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.android.safecarbs.R;
import com.android.safecarbs.main.login.LoginActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        Handler myHandler = new Handler();

        myHandler.postDelayed(() -> {

            Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
            startActivity(i);
        }, 1500);}
}
