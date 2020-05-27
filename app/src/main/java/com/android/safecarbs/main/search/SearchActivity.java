package com.android.safecarbs.main.search;

import androidx.appcompat.app.AppCompatActivity;

import com.android.safecarbs.main.contact.ContactActivity;
import com.android.safecarbs.main.database.AssetsDataBaseHelper;
import com.android.safecarbs.main.database.DataBaseHelper;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.android.safecarbs.R;
import com.android.safecarbs.main.login.LoginActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    String DB_NAME = "db_protected.db";
    String TABLE_NAME = "alimentos";
    AutoCompleteTextView autoCompleteTextView;
    TextView result;
    AssetsDataBaseHelper assetDatabaseOpenHelper;
    DataBaseHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //check DB and create if doesn't exists

        assetDatabaseOpenHelper = new AssetsDataBaseHelper(this, DB_NAME);
        assetDatabaseOpenHelper.saveDatabase();
        myDBHelper = new DataBaseHelper(this, DB_NAME);

        result = findViewById(R.id.resultView);
        autoCompleteTextView = findViewById(R.id.search);

        if (! autoCompleteTextView.getText().toString().isEmpty()) autoCompleteTextView.setOnClickListener(view ->  autocompleteOnClickListener ());

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (! autoCompleteTextView.getText().toString().isEmpty()) autocompleteOnClickListener ();
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });


    }



    private void autocompleteOnClickListener () {

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getAutoCompleteList());
        SpecialAdapter adapter = new SpecialAdapter(this, android.R.layout.simple_dropdown_item_1line, getAutoCompleteList());
        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.search);
        autoCompleteTextView.setAdapter(adapter);
        getResultSearch (autoCompleteTextView.getText().toString());
    }

    private void getResultSearch (String name){
        Cursor res = myDBHelper.getAllDataWhere(TABLE_NAME, "vch_nombre",name);

        if (res.getCount() == 0 && !autoCompleteTextView.getText().toString().isEmpty()) {

            result.setText("Sin resultados");

            return;
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("Medida: ").append(res.getString(res.getColumnIndex("vch_medida"))).append("\n");
                buffer.append("Equivalencia: ").append(res.getString(res.getColumnIndex("vch_equivalencia"))).append("\n");
                buffer.append("Calorías: ").append(res.getString(res.getColumnIndex("calorias"))).append("  Kcal\n");
                buffer.append("Lípidos: ").append(res.getString(res.getColumnIndex("lipidos"))).append(" Gr\n");
                buffer.append("Proteínas: ").append(res.getString(res.getColumnIndex("proteinas"))).append(" Gr\n");
                buffer.append("Hidratos de Carbono: ").append(res.getString(res.getColumnIndex("carbohidratos"))).append(" Gr\n\n");

            }
            result.setText(buffer);

            res.close();

        }


    }

    private ArrayList<String> getAutoCompleteList (){

        Cursor cursorNames = myDBHelper.getReadableDatabase().rawQuery("select distinct vch_nombre from alimentos", null);

        ArrayList <String> arr = new ArrayList<>();
        while (cursorNames.moveToNext()) {
            arr.add(cursorNames.getString(0));
        }
        cursorNames.close();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            arr.sort(String::compareToIgnoreCase);
        }
        return arr;

    }

    @Override
    public void onBackPressed() {
        cleanBtn();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
//        super.onBackPressed();
    }



    public void setCleanBtn(View view){
        cleanBtn();

    }

    private void cleanBtn(){
        autoCompleteTextView.setText("");
        autoCompleteTextView.clearListSelection();
        autoCompleteTextView.dismissDropDown();
        result.setText("");

    }


    public void goContact (View view){
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

}
