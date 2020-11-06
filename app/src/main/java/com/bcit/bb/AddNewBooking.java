package com.bcit.bb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bcit.bb.uiFeatures.DatePickerFragment;
import com.bcit.bb.uiFeatures.MultiSelectionSpinner;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewBooking extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
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

        Button button = (Button) findViewById(R.id.booking_add_date);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });




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

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString  = df.format(c.getTime());
        TextView textView = (TextView) findViewById(R.id.booking_date_textview);
        textView.setText(currentDateString);
    }

    public void get_Equipment() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("admins").document("adminTest")
                .collection("equipments");

        final List<String> subjects = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);


        // Multi spinner
        final MultiSelectionSpinner spinner2;
        spinner2 = (MultiSelectionSpinner) findViewById(R.id.booking_spinner_equip);

        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("equipname");
                        subjects.add(subject);
                    }
                    spinner2.setItems(subjects);

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

                    DocumentReference adminRef = db.collection("admins").document("adminTest");
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
                            adminRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            int maxcap = Integer.parseInt(document.getData().get("maxcap").toString());
                                            if(maxcap <=capacity){
                                                Toast.makeText(getApplicationContext(),"CHANGE DATA ITS FULL! "
                                                        + maxcap,Toast. LENGTH_SHORT).show();

                                                cap.setText("Current Capacity: FULL");
                                            } else   {
                                                cap.setText("Current Capacity: " + capacity);
                                            }
                                        } else {

                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
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

    public void onNewBookingAddClick(View view) {

        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    TextView date = findViewById(R.id.booking_date_textview);
                    String dateStr = date.getText().toString();
                    TextView gymName = findViewById(R.id.booking_gym_name_textview);
                    String gymStr = gymName.getText().toString();

                    Spinner spinTime = (Spinner) findViewById(R.id.spinner_time);
                    String timeslot = spinTime.getSelectedItem().toString();

                    MultiSelectionSpinner spin =(MultiSelectionSpinner) findViewById(R.id.booking_spinner_equip);
                    String s = spin.getSelectedItemsAsString();

                    boolean duplicate = false;
                    boolean hourOverlap = false;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("date").equals(dateStr) &&
                                        document.getData().get("id").equals(user.getUid()) ){
                                    duplicate = true;
                                }

                                if(document.getData().get("date").equals(dateStr) &&
                                        document.getData().get("timeslot").equals(user.getUid()) ){
                                    hourOverlap = true;
                                }



                            }

                            if(duplicate) {
                                Toast.makeText(getApplicationContext(), "Timeslot already exists!"
                                        , Toast.LENGTH_SHORT).show();

                            }
                            else if(spin.getSelections()==0){
                                Toast.makeText(getApplicationContext(), "You didn't choose any equipments!"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            else if(hourOverlap){
                                Toast.makeText(getApplicationContext(), "You have hour overlapping with others"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            else if(spin.getSelections() > 4){
                                Toast.makeText(getApplicationContext(), "Too many equipment selected!"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            else if(dateStr.equals("")){
                                Toast.makeText(getApplicationContext(), "Date is empty!"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            else {
                                writeToDatabase(dateStr, timeslot, "0", s, user.getUid(), gymStr);
                                Intent intent = new Intent(getBaseContext(), Bookings.class);
                                startActivity(intent);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void writeToDatabase(String date, String timeslot, String equipId, String equip, String id, String gym) {

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("equip", equip);
        reservation.put("equipid", equipId);
        reservation.put("timeslot", timeslot);
        reservation.put("id", id);
        reservation.put("name", gym);
        reservation.put("date", date);




        db.collection("bookings").document()
                .set(reservation)
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