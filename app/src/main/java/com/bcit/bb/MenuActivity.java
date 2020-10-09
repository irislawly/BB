package com.bcit.bb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onBookingClick(View view) {
        Intent intent = new Intent(this, bookings.class);
        startActivity(intent);
    }

    /*public void onPolicyClick(View view) {
        Intent intent = new Intent(this, PolicyActivity.class);
        startActivity(intent);
    }*/

    public void onInfoClick(View view) {
        Intent intent = new Intent(this, information.class);
        startActivity(intent);
    }

    /*public void onAccClick(View view) {
        Intent intent = new Intent(this, YourAccountActivity.class);
        startActivity(intent);
    }*/
}
