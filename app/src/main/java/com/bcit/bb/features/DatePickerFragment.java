package com.bcit.bb.features;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Date picker fragment
 * Source: https://stackoverflow.com/questions/14024921/retrieve-and-set-data-from-dialogfragment-datepicker
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    /**
     * Creates calendar dialog.
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),  (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);
    }

    /**
     * Sets date.
     * @param view v
     * @param year year
     * @param month month
     * @param day day
     */
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = month + " " + day + ", " + year;

    }
}