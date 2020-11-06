package com.bcit.bb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onBookingClick(View view) {
        Intent intent = new Intent(this, Bookings.class);
        startActivity(intent);
    }

    public void onPolicyClick(View view) {
        Intent intent = new Intent(this, PolicyActivity.class);
        startActivity(intent);
    }

    public void onGymInfoClick(View view) {
        Intent intent = new Intent(this, GymInformation.class);
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
