package com.android.safecarbs.main.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.safecarbs.R;
import com.android.safecarbs.main.database.DataBaseHelper;
import com.android.safecarbs.main.login.LoginActivity;
import com.android.safecarbs.main.util.datePicker.DatePickerFragment;
import com.android.safecarbs.main.util.textWatcher.TextWatcherEditTex;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText etName;
    TextInputEditText etSurname;
    TextInputEditText etMail;
    TextInputEditText etPass;
    TextInputEditText etPass2;
    TextInputEditText etDateBorn;
    TextInputEditText etHeight;
    TextInputEditText etWeight;
    TextView genderText;
    Button insert_btn;
    Spinner genderSpinner;
    int spinnerCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinnerCounter = 0;

        //genderText
        genderText = findViewById(R.id.textInputGender);

        //genderSpinner - gender
        genderSpinner = findViewById(R.id.GenderSpinner);

        // Create an ArrayAdapter using the string array and a default genderSpinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genders_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the genderSpinner
        genderSpinner.setAdapter(adapter);


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinnerCounter >0)  isSpinnerEmpty();
                spinnerCounter++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //get texts from boxes

        etName = findViewById(R.id.nameET);
        etSurname = findViewById(R.id.SurnameET);
        etMail = findViewById(R.id.MailET);
        etPass = findViewById(R.id.PassET);
        etDateBorn = findViewById(R.id.DateET);
        etPass2 = findViewById(R.id.PassConfirm);
        etHeight = findViewById(R.id.heightET);
        etWeight = findViewById(R.id.weightET);
        insert_btn = findViewById(R.id.BtnRegister);


        //datePicker
        etDateBorn.setOnClickListener(this);


        // set password property
        etPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        //set validations on TextChange Listener


        TextWatcherEditTex twName = new TextWatcherEditTex(etName);
        etName.addTextChangedListener(twName);

        TextWatcherEditTex twSurname = new TextWatcherEditTex(etSurname);
        etSurname.addTextChangedListener(twSurname);

        TextWatcherEditTex twWeight = new TextWatcherEditTex(etWeight);
        etWeight.addTextChangedListener(twWeight);

        TextWatcherEditTex twHeight = new TextWatcherEditTex(etHeight);
        etHeight.addTextChangedListener(twHeight);


        TextWatcherEditTex twPass = new TextWatcherEditTex(etPass);
        twPass.setConfirmET(etPass2);
        etPass.addTextChangedListener(twPass);

        TextWatcherEditTex twPassConfirm = new TextWatcherEditTex(etPass2);
        twPassConfirm.setOriginalET(etPass);
        etPass2.addTextChangedListener(twPassConfirm);

        TextWatcherEditTex twMail = new TextWatcherEditTex(etMail);
        etMail.addTextChangedListener(twMail);


    }


    public void insert(View view) {
        String TBL_NAME = "tbl_user";

        DataBaseHelper dBaseHelper = new DataBaseHelper(this, "db_usuarios");

        String query = "CREATE TABLE IF NOT EXISTS " + TBL_NAME + " (nombre TEXT, apellido TEXT, genero TEXT, peso TEXT, altura TEXT, correo TEXT, clave TEXT, fechNacimiento TEXT, PRIMARY KEY (correo))";

        SQLiteDatabase db = dBaseHelper.getWritableDatabase();

        empty(etName);
        empty(etSurname);
        checkEmptyDateField();
        empty(etHeight);
        empty(etMail);
        empty(etWeight);
        empty(etPass);
        empty(etPass2);
        isSpinnerEmpty();
        isEmail(etMail.getText().toString());
        checkUsedMail(etMail.getText().toString(), db);

        if (etName.getError() !=null || etSurname.getError() !=null
                || etDateBorn.getError() !=null || etHeight.getError() !=null
                || etMail.getError() !=null || etPass.getError() !=null
                || etPass2.getError() !=null || isSpinnerEmpty()) return;


        if (db != null) {
            db.execSQL(query);
            ContentValues register = new ContentValues();

            register.put("nombre", etName.getText().toString());
            register.put("apellido", etSurname.getText().toString());
            register.put("genero", genderSpinner.getSelectedItem().toString());
            register.put("peso", etWeight.getText().toString());
            register.put("altura", etHeight.getText().toString());
            register.put("correo", etMail.getText().toString());
            register.put("clave", etPass.getText().toString());
            register.put("fechNacimiento", etDateBorn.getText().toString());


            long i = db.insert("tbl_user", null, register);

            if (i > 0) {
              Toast.makeText(this, "Registro exitoso", Toast.LENGTH_LONG).show();
              Intent intent = new Intent(this, LoginActivity.class);
              startActivity(intent);
            }

        }

    }


    private void checkUsedMail(String mail, SQLiteDatabase db) {
        try {

            Cursor result = db.rawQuery("SELECT * FROM tbl_user  where correo = ?", new String[]{mail});
            boolean found = result.getCount() > 0;
            result.close();

            if (found) etMail.setError("Correo en uso");

        }catch (SQLiteException e){
            //if db or table doesnt even exists
            Log.e("SQLite", e.toString());

        }
    }


    private boolean isSpinnerEmpty(){
        genderText.setTextSize(12);
        TextView errorText = (TextView)genderSpinner.getSelectedView();
        errorText.setError(null);

        Log.i("genderspinner","es null? " + (genderSpinner.getSelectedItem() == null) + " es empry?" + (genderSpinner.getSelectedItem().toString().isEmpty()));
        if (genderSpinner.getSelectedItem().toString().equals("-") ) {

            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("     Selecciona género");//changes the selected item text to this
            return true;
        }
        return false;

    }

    private boolean isEmail (String s) {
        if (Patterns.EMAIL_ADDRESS.matcher(s).matches()){
            etMail.setError(null);
            return true;
    }
        else{
            etMail.setError("Formato de email inválido");
        }
        return false;
    }

    public void empty(EditText campo) {
        String dato = campo.getText().toString().trim();

        if (TextUtils.isEmpty(dato)) {
            campo.setError("Campo requerido");
            campo.requestFocus();

        }

    }

    private boolean checkEmptyDateField () {
        if (etDateBorn.getText() == null || etDateBorn.getText().toString().isEmpty()) {
            etDateBorn.setError("Campo requerido");
            return true;
        }
        etDateBorn.setError(null);
        return false;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.DateET) {
            showDatePickerDialog();
        }
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            // +1 because January is zero
            final String selectedDate = day + " / " + (month + 1) + " / " + year;
            etDateBorn.setText(selectedDate);
        });
        newFragment.setDefaultDate(1990, 0, 1);
        newFragment.setMinMaxAge(2, 99);
        newFragment.show(getSupportFragmentManager(), "datePicker");
        etDateBorn.setError(null);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}