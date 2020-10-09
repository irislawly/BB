package com.bcit.bb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class bookings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
    }

    public void onAddBookingClick(View view) {
        Intent intent = new Intent(this, AddNewBooking.class);
        startActivity(intent);
    }

    /*public void onEditBookingClick(View view) {
        Intent intent = new Intent(this, AddNewBooking.class);
        startActivity(intent);
    }*/

    public void onDeleteBookingClick(View view) {
        Intent intent = new Intent(this, Delete.class);
        startActivity(intent);
    }
}