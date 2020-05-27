package com.android.safecarbs.main.util.textWatcher;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;



public class TextWatcherEditTex implements TextWatcher {


    private EditText editable;
    private EditText originalET, confirmET;
    private boolean hasOriginalET, hasConfirmET;

    public TextWatcherEditTex(EditText editable) {
        this.editable = editable;

    }

    public void setOriginalET(EditText originalET){
        hasOriginalET = true;
        this.originalET = originalET;
    }

    public void setConfirmET (EditText confirmET){
        hasConfirmET = true;
        this.confirmET = confirmET;

    }

    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {


        Log.e("JFT", "EDITTEXT => = " + s);
        if (editable.getText().toString().isEmpty()) {
            editable.setError("No has ingresado " + editable.getTag());
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    public void afterTextChanged(Editable s) {
        if (hasOriginalET && (!originalET.getText().toString().equals(editable.getText().toString()))){
            editable.setError("No coincide con " + editable.getTag() + " original");
        }

        if (hasConfirmET && (!editable.getText().toString().equals(confirmET.getText().toString()))){
            confirmET.setError("No coincide con " + editable.getTag() + " original");
        }

        int inputType = editable.getInputType()-1;

        if (inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD && editable.getHint().toString().equals("Crea una contraseña")){
            if (editable.getText().toString().length() < 4) editable.setError("Contraseña debe tener al menos 4 caracteres");
        }

        if (inputType == InputType.TYPE_CLASS_DATETIME){
            if (editable.getText().toString().length() > 0) editable.setError(null);
        }

    }




}

