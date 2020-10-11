package com.bcit.bb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddNewBooking extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinnerequipment);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Cardiovascular machine #1");
        categories.add("Cardiovascular machine #2");
        categories.add("Rowing machine #1");
        categories.add("Rowing machine #2");
        categories.add("Upright bike #1");
        categories.add("Upright bike #2");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

//        // Spinner element
//        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerdate);
//
//        // Spinner click listener
//        spinner2.setOnItemSelectedListener(this);
//
//        // Spinner Drop down elements
//        List<String> dates = new ArrayList<String>();
//        categories.add("2020-10-10");
//        categories.add("2020-10-11");
//        categories.add("2020-10-12");
//        categories.add("2020-10-13");
//        categories.add("2020-10-14");
//        categories.add("2020-10-15");
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter2);

    }
    public void onNewBookingCancelClick(View view) {
        Intent intent = new Intent(this, bookings.class);
        startActivity(intent);
    }
    public void onNewBookingUpdateClick(View view) {
        Intent intent = new Intent(this, bookings.class);
        startActivity(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
