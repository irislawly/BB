package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormatSymbols;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Bookings extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference idsRef = db.collection("bookings");
    String TAG = "Debug Iris";
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<BookingTemplate> listItems;
    private CompactCalendarView  compactCalendarView;
    private TextView calMonth;

    String[] gymEquipment = new String[]{"Treadmill", "Rowing machine", "Dumbbells", "Leg press", "Pullup bar",
            "Chess press", "Bench press", "Pullup bar", "Lat pulldown"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        recyclerView = (RecyclerView)findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        compactCalendarView = findViewById(R.id.compactcalendar_view);

              Date d = compactCalendarView.getFirstDayOfCurrentMonth();

           String monthString = new DateFormatSymbols().getMonths()[d.getMonth()];
           String yearString = " " + 2020;
             calMonth = findViewById(R.id.month_title);
             calMonth.setText(monthString + yearString);
        //   setQueue();
        highlightCalendarEvents();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                listItems = new ArrayList<>();
                List<Event> events = compactCalendarView.getEvents(dateClicked);


                if(events.size() != 0){
                    String dateId =  (String) events.get(0).getData();
                    Log.d(TAG, "Day was clicked: " + dateClicked + " with Data: " + (String) events.get(0).getData());
                    DocumentReference docRef = db.collection("bookings").document(dateId);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                    String temp_id =  document.getId();
                                    Log.d("debug", temp_id);
                                    String temp_equip = (String) document.getData().get("equip");
                                    Log.d("debug", (String) document.getData().get("equip"));

                                    String temp_date = (String) document.getData().get("date");
                                    Log.d("debug", (String) document.getData().get("date"));

                                    String temp_timeslot = (String) document.getData().get("timeslot");
                                    Log.d("debug", (String) document.getData().get("timeslot"));

                                    BookingTemplate book123 = new BookingTemplate(temp_equip, temp_timeslot, temp_date, temp_id);
                                    listItems.add(book123);
                                    Log.d("debug", document.getId() + " => " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                                adapter = new currentBookingAdapter(listItems, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
                Log.d(TAG, "Day was clicked: " + dateClicked + " with Data: " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                int month = firstDayOfNewMonth.getMonth();
                String monthString = new DateFormatSymbols().getMonths()[month];
                String yearString = " " + 2020;
                Log.d(TAG, "Month was scrolled to: " + monthString);
                calMonth.setText(monthString + yearString);

            }
        });
    }
    public void highlightCalendarEvents(){
/*
        long miliSecsDate = milliseconds ("2020-11-14");
        Log.d(TAG, "Date in milli :: FOR API >= 26 >>> " + miliSecsDate);
        long miliSecsDate2 = milliseconds ("2020-11-24");
        Event ev1 = new Event(Color.BLUE, miliSecsDate, "docid1");
        compactCalendarView.addEvent(ev1);
        Event ev2 = new Event(Color.BLUE, miliSecsDate2, "docid2");
        compactCalendarView.addEvent(ev2);
*/
        db.collection("bookings")
                .whereEqualTo("id", currentuser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String temp_id =  document.getId();
                                String temp_date = (String) document.getData().get("date");
                                Log.d(TAG, (String) document.getData().get("date"));

                                Log.d("TAG", document.getId() + " => " + document.getData());

                                long miliSecsDate = milliseconds (temp_date);
                                Event ev1 = new Event(Color.BLUE, miliSecsDate, temp_id);
                                compactCalendarView.addEvent(ev1);
                            }

                        } else {
                            Log.d("debug", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }
    public void prevMonthClick(View view) {
        compactCalendarView.scrollLeft();
        Date d = compactCalendarView.getFirstDayOfCurrentMonth();
        String monthString = new DateFormatSymbols().getMonths()[d.getMonth()];
        String yearString = " " + 2020;
        Log.d(TAG, "Month was scrolled to: " + monthString);
        calMonth.setText(monthString + yearString);
    }

    public void nextMonthClick(View view) {
        compactCalendarView.scrollRight();
        Date d = compactCalendarView.getFirstDayOfCurrentMonth();
        String monthString = new DateFormatSymbols().getMonths()[d.getMonth()];
        String yearString = " " + 2020;
        Log.d(TAG, "Month was scrolled to: " + monthString);
        calMonth.setText(monthString + yearString);
    }

    public long milliseconds(String date)
    {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    public void onAddBookingClick(View view) {
        Intent intent = new Intent(this, AddNewBooking.class);
        startActivity(intent);
    }
    //not working yet need to edit currentingboooking adapter class
    public void onEditBookingClick(View view) {
        Intent intent = new Intent(this, EditBooking.class);
        Log.d("debug"," Edit pressed");
        TextView timeslotID = findViewById(R.id.timeslotID);
        String idStr = timeslotID.getText().toString();
        Log.d("debugDelete" ,idStr);
        intent.putExtra("id", idStr);
        startActivity(intent);
    }

    public void onReturnMenuClick(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void onDeleteBookingClick(View view) {
        Log.d("debug","Delete pressed");
        TextView timeslotID = findViewById(R.id.timeslotID);
        String idStr = timeslotID.getText().toString();
        Log.d("debugDelete",idStr);

        db.collection("bookings").document(idStr)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("debug", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("debug", "Error deleting document", e);
                    }
                });


        Intent intent = new Intent(this, Bookings.class);
        startActivity(intent);
    }
    public void setQueue(){
        listItems = new ArrayList<>();
        Log.d("debug","hi");

        db.collection("bookings")
                .whereEqualTo("id", currentuser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String temp_id =  document.getId();
                                Log.d("debug", temp_id);
                                String temp_equip = (String) document.getData().get("equip");
                                Log.d("debug", (String) document.getData().get("equip"));

                                String temp_date = (String) document.getData().get("date");
                                Log.d("debug", (String) document.getData().get("date"));

                                String temp_timeslot = (String) document.getData().get("timeslot");
                                Log.d("debug", (String) document.getData().get("timeslot"));

                                BookingTemplate book123 = new BookingTemplate(temp_equip, temp_timeslot, temp_date, temp_id);
                                listItems.add(book123);
                                Log.d("debug", document.getId() + " => " + document.getData());
                            }
                            adapter = new currentBookingAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d("debug", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

}