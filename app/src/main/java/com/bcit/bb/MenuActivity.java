package com.bcit.bb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;


import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onBookingClick(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }

    public void onPolicyClick(View view) {
        Intent intent = new Intent(this, PolicyActivity.class);
        startActivity(intent);
    }

    public void onGymInfoClick(View view) {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }

    public void onAccClick(View view) {
        Intent intent = new Intent(this, YourAccountActivity.class);
        startActivity(intent);
    }

    public void onGymSignoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
