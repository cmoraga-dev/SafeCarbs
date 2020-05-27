package com.android.safecarbs.main.util.datePicker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private int year, month, day, minAge, maxAge;
    private DatePickerDialog.OnDateSetListener listener;

    public void setDefaultDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setDefaultDateToday() {
        this.year = Calendar.getInstance().get(Calendar.YEAR);
        this.month = Calendar.getInstance().get(Calendar.MONTH);
        this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    }

    public void setMinMaxAge(int min, int max) {
        minAge = min;
        maxAge = max;
    }

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dp = new DatePickerDialog(getActivity(), listener, year, month, day);


        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - minAge);


        // Min and max date
        dp.getDatePicker().setMaxDate(c.getTimeInMillis());

        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - maxAge);
        dp.getDatePicker().setMinDate(c.getTimeInMillis());
        return dp;

    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
}
