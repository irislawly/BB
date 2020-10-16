package com.bcit.bb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewBooking extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "BookingSnippets";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        if (user != null) {
            get_Timeslots();
            get_Equipment();

        } else {
            System.out.println("NOT LOGGED ON");
        }

    }


    public void get_Timeslots() {

        DocumentReference docRef = db.collection("admins").document("adminTest");

        Spinner spinner = (Spinner) findViewById(R.id.spinner_time);
        final List<String> subjects = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> info = document.getData();
                        TextView name = findViewById(R.id.booking_gym_name_textview);
                        name.setText(info.get("name").toString());

                        List<String> group = (List<String>) document.get("gymhours");
                        //Need to find which day it is to get the timeslots (2 hours)
                        String[] timeslots = {"9am - 11am", "11am - 1pm", "1am - 3pm", "3pm - 5pm",
                                "5pm - 7pm", "7pm - 9pm"};
                        for (int i = 0; i < timeslots.length; i++) {
                            subjects.add(timeslots[i]);
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void get_Equipment() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("admins").document("adminTest")
                .collection("equipments");
        Spinner spinner = (Spinner) findViewById(R.id.spinner_equipment);
        final List<String> subjects = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("equipname");
                        subjects.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void checkCapacityClick(View view) {

        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    TextView date = findViewById(R.id.booking_date_textview);
                    String dateStr = date.getText().toString();

                    Spinner spinTime = (Spinner) findViewById(R.id.spinner_time);
                    String timeslot = spinTime.getSelectedItem().toString();
                    TextView cap = findViewById(R.id.current_cap_textview);
                    int capacity = 0;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.getData().get("date").equals(dateStr) &&
                                        document.getData().get("timeslot").equals(timeslot) ){
                                        capacity++;
                                }
                            }
                            cap.setText("Current Capacity: " + capacity);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void onNewBookingCancelClick(View view) {
        Intent intent = new Intent(this, Bookings.class);
        startActivity(intent);
    }

    public void onNewBookingUpdateClick(View view) {
        TextView date = findViewById(R.id.booking_date_textview);
        String dateStr = date.getText().toString();

        Spinner spinTime = (Spinner) findViewById(R.id.spinner_time);
        String timeslot = spinTime.getSelectedItem().toString();

        Spinner spinEquip = (Spinner) findViewById(R.id.spinner_equipment);
        String equip = spinEquip.getSelectedItem().toString();
        write(dateStr, timeslot, "2", equip, user.getUid());

          Intent intent = new Intent(this, Bookings.class);
          startActivity(intent);
    }

    public void write(String date, String timeslot, String equipId, String equip, String id) {

        Map<String, Object> user = new HashMap<>();
        user.put("equip", equip);
        user.put("equipid", equipId);
        user.put("timeslot", timeslot);
        user.put("id", id);
        user.put("date", date);

        db.collection("bookings").document()
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}
