package com.android.safecarbs.main.accounts;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.safecarbs.R;
import com.android.safecarbs.main.database.DataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class accountViewer extends AppCompatActivity {

    TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_viewer);
        resultText = findViewById(R.id.accounts_result);
        searchAccounts();
    }

    public void searchAccounts () {

        DataBaseHelper dBaseHelper = new DataBaseHelper(this, "db_usuarios");
        SQLiteDatabase db = dBaseHelper.getWritableDatabase();

        try {
            Cursor queryResult = db.rawQuery("SELECT * FROM tbl_user", null);

            if (queryResult.getCount() == 0) {
                resultText.setText("Sin resultados");
                queryResult.close();
                return;
            }


            ArrayList<String> columnNames = new ArrayList<>();
            columnNames.addAll(Arrays.asList(queryResult.getColumnNames()));

            StringBuffer buffer = new StringBuffer();
            while (queryResult.moveToNext()) {
                for (int i = 0; i < columnNames.size(); i++) {
                    String name = columnNames.get(i);
                    buffer.append(name).append(": ").append(queryResult.getString(queryResult.getColumnIndex(name))).append("\n");
                }
                buffer.append("\n\n");
            }
            resultText.setText(buffer);
            queryResult.close();

        } catch (SQLException e) {
            Log.e("SQLExcep", e.toString());
            resultText.setText("Sin resultados");

        }

    }
}
