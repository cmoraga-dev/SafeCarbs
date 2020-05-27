package com.android.safecarbs.main.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.safecarbs.R;
import com.android.safecarbs.main.accounts.ExplanationAccounts;
import com.android.safecarbs.main.choView.ChoViewActivity;
import com.android.safecarbs.main.database.DataBaseHelper;
import com.android.safecarbs.main.register.RegisterActivity;
import com.android.safecarbs.main.search.SearchActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usernameET, passET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.ETMail);
        passET = findViewById(R.id.etPass);

    }

    public void goRegister (View view){
       Intent intent = new Intent(this, RegisterActivity.class);
       startActivity(intent);
   }

    public void login(View view) {
        if (usernameET == null || passET == null) return;

        String username, pass;
        username = usernameET.getText().toString();
        pass = passET.getText().toString();


        if (!check_login(username, pass)) {
            Toast.makeText(this,"Cuenta no encontrada o clave inválida", Toast.LENGTH_SHORT).show();
            return;
        };

        Toast.makeText(this,"Bienvenid@", Toast.LENGTH_SHORT).show();

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        Log.i("Activity executed?", String.valueOf(pref.getBoolean("show", true)));

        if(!pref.getBoolean("show", true)){
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ChoViewActivity.class);
            startActivity(intent);
        }

    }

    private boolean check_login(String mail, String pass) {
        try {


            DataBaseHelper dBaseHelper = new DataBaseHelper(this, "db_usuarios");
            SQLiteDatabase db = dBaseHelper.getWritableDatabase();

            Cursor result = db.rawQuery("SELECT * FROM tbl_user  where correo = ? and clave = ?", new String[]{mail, pass});
            boolean found = result.getCount() > 0;
            result.close();

            return found;
        }catch (SQLiteException e){
            Log.e("SQLite", e.toString());
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    public void goAccounts(View view){
        Intent intent = new Intent(this, ExplanationAccounts.class);
        startActivity(intent);

    }
}
