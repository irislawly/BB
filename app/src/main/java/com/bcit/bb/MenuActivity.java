package com.bcit.bb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;


import androidx.appcompat.app.AppCompatActivity;

/**
 * Menu page with buttons to redirect user around the app.
 */
public class MenuActivity extends AppCompatActivity {

    /**
     * Runs app.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Event handler to redirect user to Booking page.
     * @param view View
     */
    public void onBookingClick(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }

    /**
     * Event handler to redirect user to Policy page.
     * @param view View
     */
    public void onPolicyClick(View view) {
        Intent intent = new Intent(this, PolicyActivity.class);
        startActivity(intent);
    }

    /**
     * Event handler to redirect user to GymInformation page.
     * @param view View
     */
    public void onGymInfoClick(View view) {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }

    /**
     * Event handler to redirect user to UserAccount page.
     * @param view View
     */
    public void onAccClick(View view) {
        YourAccountActivity youracc = new YourAccountActivity();
        youracc.show(getSupportFragmentManager(), "your account dialog");
    }

    /**
     * Event handler to sign out page.
     * @param view View
     */
    public void onGymSignoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
