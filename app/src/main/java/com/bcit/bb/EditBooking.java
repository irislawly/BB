package com.bcit.bb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class EditBooking extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

    }
    public void onEditCancelClick(View view) {
        Intent intent = new Intent(this, Bookings.class);
        startActivity(intent);
    }
    public void onEditUpdateClick(View view) {
        Intent intent = new Intent(this, Bookings.class);
        startActivity(intent);
    }

}

