package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class Bookings extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the bookings collection
    CollectionReference idsRef = db.collection("bookings");

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    // Create a query against the collection.
//    Query query = idsRef.whereEqualTo("id", currentuser);


    String[] gymEquipment = new String[]{"Treadmill", "Rowing machine", "Dumbbells", "Leg press", "Pullup bar",
            "Chess press", "Bench press", "Pullup bar", "Lat pulldown"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        setQuene();
    }

    public void onAddBookingClick(View view) {
        Intent intent = new Intent(this, AddNewBooking.class);
        startActivity(intent);
    }
    public void onEditBookingClick(View view) {
        Intent intent = new Intent(this, EditBooking.class);
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
    public void setQuene(){
        Log.d("debug","hi");

                db.collection("bookings")
                .whereEqualTo("id", currentuser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView equip1 = findViewById(R.id.equip1);
                                String temp_equip = (String) document.getData().get("equip");
                                equip1.setText("Equip: "+temp_equip);

                                TextView date1 = findViewById(R.id.date1);
                                String temp_date = (String) document.getData().get("date");
                                date1.setText("Date: "+temp_date);

                                TextView timeslot1 = findViewById(R.id.timeslot1);
                                String temp_timeslot = (String) document.getData().get("timeslot");
                                timeslot1.setText("Timeslot: "+temp_timeslot);

                                Log.d("debug", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("debug", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}