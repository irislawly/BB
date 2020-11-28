package com.bcit.bb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditBookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Debug";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TextView booking_date;

    /**
     * OnCreate function to run app.
     * @param savedInstanceState state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);


        if (user != null) {
            DocumentReference docRef = db.collection("users").document(currentuser);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String gym_id = (String) document.getData().get("gymid");
                            get_Timeslots(gym_id);
                            get_Equipment(gym_id);

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            Intent intent = getIntent();
            String dateStr = intent.getStringExtra("date");
            booking_date = findViewById(R.id.booking_date_textview_edit);

            booking_date.setText(dateStr);

        } else {
            Log.d(TAG, "NOT LOGGED ON");
        }

        Button button = findViewById(R.id.booking_add_date_edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");

            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_time_edit);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            /**
             * Assigns spinner to value when clicked.
             * @param arg0 arg0
             * @param arg1 arg1
             * @param arg2 arg2
             * @param arg3 arg3
             */
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Spinner spinTime = (Spinner) findViewById(R.id.spinner_time_edit);
                if (!spinTime.getSelectedItem().toString().isEmpty()) {
                    capacity_Listener();
                }

            }

            /**
             * Assigns spinner to defualt value when nothing is clicked.
             * @param arg0 arg0
             */
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Spinner spinTime = (Spinner) findViewById(R.id.spinner_time_edit);
                spinTime.setSelection(0);
            }
        });

    }

    /**
     * Generate timeslots of a gym's certain day.
     * @param gym_id gym id
     */
    public void get_Timeslots(String gym_id) {

        DocumentReference docRef = db.collection("admins").document(gym_id);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_time_edit);
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
                        TextView name = findViewById(R.id.booking_gym_name_textview_edit);
                        name.setText(info.get("gymname").toString());

                        List<String> group = (List<String>) document.get("gymhours");

                        String[] timeslots = new String[0];
                        try {
                            timeslots = getTimeslotInterval(group);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

    /**
     * Helper function for get_Timeslot to generate 2 hour intervals.
     * @param group group of date
     * @return String array of timeslots
     * @throws ParseException exception
     */
    public String[] getTimeslotInterval(List<String> group) throws ParseException {
        TextView d =findViewById(R.id.booking_date_textview_edit);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = df.parse(d.getText().toString());
        int day = dt.getDay();


        String hours = group.get(day);
        String start = hours.substring(0, hours.indexOf(" "));
        String end = hours.substring(hours.indexOf("-") + 2);

        int am = Integer.parseInt(start.substring(0, start.indexOf("a")));
        int pm =  Integer.parseInt(end.substring(0, end.indexOf("p")));
        pm += 12;

        ArrayList<String> intervals = new ArrayList<String>();
        for(int i = am ; i <= pm-2 ; i+=2){
            String hr = "";
            if(i > 12){
                if(i == pm-3 && am+pm % 2 != 0 ){
                    hr = "" + (i - 12) + "pm" + " - " + (i - 9) + "pm";

                }else {
                    hr = "" + (i - 12) + "pm" + " - " + (i - 10) + "pm";
                }
            }else if(i>10){
                hr = "" + (i) + "am"  + " - " + (i-10) + "pm";

            } else{
                hr = "" + (i) + "am"  + " - " + (i+2) + "am";

            }
            intervals.add(hr);

        }
        String[] timeslots = new String[intervals.size()];
        timeslots = intervals.toArray(timeslots);
        return timeslots;

    }

    /**
     * Sets a new date.
     * @param view View
     * @param year year
     * @param month month
     * @param dayOfMonth day of month
     */
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString  = df.format(c.getTime());
        TextView textView = findViewById(R.id.booking_date_textview_edit);
        textView.setText(currentDateString);

        DocumentReference docRef = db.collection("users").document(currentuser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String gym_id = (String) document.getData().get("gymid");
                        get_Timeslots(gym_id);
                        get_Equipment(gym_id);

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
     * Function to generate gym equipment onto the spinner for user to pick.
     * @param gym_id gym id
     */
    public void get_Equipment(String gym_id) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("admins").document(gym_id)
                .collection("equipments");

        final List<String> subjects = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subjects);


        // Multi spinner
        final MultiSelectionSpinner spinner2;
        spinner2 = (MultiSelectionSpinner) findViewById(R.id.booking_spinner_equip_edit);

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

    /**
     * Function to generate current capacity / number of other users that have booked the timeslot
     * as well.
     */
    public void capacity_Listener() {
        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    TextView date = findViewById(R.id.booking_date_textview_edit);
                    String dateStr = date.getText().toString();
                    Spinner spinTime = (Spinner) findViewById(R.id.spinner_time_edit);
                    String timeslot = spinTime.getSelectedItem().toString();
                    TextView cap = findViewById(R.id.current_cap_textview_edit);
                    TextView gymname = findViewById(R.id.booking_gym_name_textview_edit);
                    String gymStr = gymname.getText().toString();

                    int capacity = 0;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (document.getData().get("date").equals(dateStr) &&
                                        document.getData().get("timeslot").equals(timeslot) &&
                                        document.getData().get("gymname").equals(gymStr)) {
                                    capacity++;
                                }
                            }
                            DocumentReference docRef = db.collection("users").document(currentuser);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                            String gym_id = (String) document.getData().get("gymid");
                                            DocumentReference adminRef = db.collection("admins").document(gym_id);
                                            adminRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            int maxcap = Integer.parseInt(document.getData().get("maxcap").toString());
                                                            if (maxcap <= capacity) {
                                                                Toast.makeText(getApplicationContext(), "CHANGE DATA ITS FULL! "
                                                                        + maxcap, Toast.LENGTH_SHORT).show();

                                                                cap.setText("Current Capacity: FULL");
                                                            } else {
                                                                cap.setText("Current Capacity: " + capacity);
                                                            }
                                                        }
                                                    } else {
                                                        Log.d(TAG, "get failed with ", task.getException());
                                                    }
                                                }
                                            });

                                        } else {
                                            Log.d(TAG, "No such document");
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

    /**
     * Cancel activity and redirect back to Booking page.
     * @param view view
     */
    public void onNewBookingCancelClick(View view) {
        Intent intent = new Intent(this, BookingActivity.class);
        startActivity(intent);
    }

    /**
     * Readd user reservation to databaes, takes in the data input and checks for validation.
     * @param view View
     */
    public void onNewBookingAddClick(View view) {

        db.collection("bookings")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    TextView date = findViewById(R.id.booking_date_textview_edit);
                    String dateStr = date.getText().toString();
                    TextView gymName = findViewById(R.id.booking_gym_name_textview_edit);
                    String gymStr = gymName.getText().toString();

                    Spinner spinTime = (Spinner) findViewById(R.id.spinner_time_edit);
                    String timeslot = spinTime.getSelectedItem().toString();

                    MultiSelectionSpinner spin =(MultiSelectionSpinner) findViewById(R.id.booking_spinner_equip_edit);
                    String s = spin.getSelectedItemsAsString();

                    boolean duplicate = false;
                    boolean hourOverlap = false;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(  document.getData().get("timeslot").equals(timeslot) &&
                                        document.getData().get("id").equals(user.getUid()) ){
                                    duplicate = true;
                                }

                                if(document.getData().get("date").equals(dateStr) &&
                                        document.getData().get("timeslot").equals(timeslot) &&
                                        document.getData().get("id").equals(user.getUid())){
                                    hourOverlap = true;
                                }

                            }
                            if(hourOverlap){
                                Toast.makeText(getApplicationContext(), "You have hour overlapping with others"
                                        , Toast.LENGTH_SHORT).show();
                            }
                            else if(spin.getSelections()==0){
                                Toast.makeText(getApplicationContext(), "You didn't choose any equipments!"
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
                                Intent intent = new Intent(getBaseContext(), BookingActivity.class);
                                startActivity(intent);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * Writes time slot information to database.
     * @param date Date
     * @param timeslot Interval of timeslot
     * @param equipId equipment id
     * @param equip equip names
     * @param id id of user
     * @param gym gym name
     */
    public void writeToDatabase(String date, String timeslot, String equipId, String equip, String id, String gym) {

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("equip", equip);
        reservation.put("equipid", equipId);
        reservation.put("timeslot", timeslot);
        reservation.put("id", id);
        reservation.put("gymname", gym);
        reservation.put("date", date);

        Intent intent = getIntent();
        String docId = intent.getStringExtra("id");

        db.collection("bookings").document(docId)
                .set(reservation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,  "Successfully read.");
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