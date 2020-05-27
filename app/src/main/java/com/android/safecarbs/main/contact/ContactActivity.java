package com.android.safecarbs.main.contact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.safecarbs.R;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    public void onClick(View v) {
        handleMail(v);

    }

    private void handleMail(View v){

        String btnName = v.getResources().getResourceEntryName(v.getId());
        btnName = btnName.replace("btn", "");
        btnName = btnName.toLowerCase();

        Log.i("btn name", btnName);


        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:?subject=" + "Consulta SafeCarbs app"+ "&body=" + "Escriba aquí su consulta" + "&to=" + btnName+"@sfcarbsejemplo.com"));
        startActivity(Intent.createChooser(intent, "Choose an Email client :"));




    }
}
