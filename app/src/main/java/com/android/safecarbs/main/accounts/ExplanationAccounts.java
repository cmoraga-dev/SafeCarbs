package com.android.safecarbs.main.accounts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.safecarbs.R;
import com.android.safecarbs.main.choView.ChoViewActivity;

public class ExplanationAccounts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explanation_accounts);
    }

    public void goAccounts(View view){
        Intent intent = new Intent(this, accountViewer.class);
        startActivity(intent);

    }
}
