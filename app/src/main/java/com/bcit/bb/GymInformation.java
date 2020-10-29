package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class GymInformation extends AppCompatActivity implements OnMapReadyCallback {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    private GoogleMap mMap;
    float zoomLevel = 13.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        get_Gym_Information();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void get_Gym_Information(){
        DocumentReference docRef = db.collection("admins").document("adminTest");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> info = document.getData();

                        List<String> group = (List<String>) document.get("gymhours");
                        TextView mon_hour = findViewById( R.id.info_hour_mon_textview );
                        TextView tues_hour = findViewById( R.id.info_hour_tues_textview );
                        TextView wed_hour = findViewById( R.id.info_hour_wed_textview );
                        TextView thurs_hour = findViewById( R.id.info_hour_thur_textview );
                        TextView fri_hour = findViewById( R.id.info_hour_fri_textview );
                        TextView sat_hour = findViewById( R.id.info_hour_sat_textview );
                        TextView sun_hour = findViewById( R.id.info_hour_sun_textview );

                        TextView max_cap = findViewById( R.id.info_gym_cap_textview );
                        TextView street = findViewById( R.id.info_gym_street_textview );
                        TextView city = findViewById( R.id.info_gym_city_textview );
                        TextView phone = findViewById( R.id.info_gym_phone_textview );

                        mon_hour.setText(group.get(0));
                        tues_hour.setText(group.get(1));
                        wed_hour.setText(group.get(2));
                        thurs_hour.setText(group.get(3));
                        fri_hour.setText(group.get(4));
                        sat_hour.setText(group.get(5));
                        sun_hour.setText(group.get(6));

                        street.setText(info.get("street").toString());
                        city.setText(info.get("city").toString());
                        max_cap.setText(info.get("maxcap").toString());
                        phone.setText(info.get("phone").toString());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in BCIT recreation center and move the camera
        LatLng bcit = new LatLng(49.248920, -123.000693);
        mMap.addMarker(new MarkerOptions().position(bcit).title("BCIT Recreation Center"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bcit, zoomLevel));
    }

}