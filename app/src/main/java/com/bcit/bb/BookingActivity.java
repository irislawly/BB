package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;

import com.bcit.bb.adapters.BookingAdapter;
import com.bcit.bb.adapters.BookingTemplate;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Activity for Booking page, has Calendar, buttons, and adapters to display user's reserved info.
 */
public class BookingActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "Debug ";
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton addButt;
    private ArrayList<BookingTemplate> listItems;
    private CompactCalendarView compactCalendarView;
    private TextView calMonth;

    /**
     * Runs app.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.INVISIBLE);
        addButt = findViewById(R.id.buttonAdd);
        compactCalendarView = findViewById(R.id.compactcalendar_view);

        Date d = compactCalendarView.getFirstDayOfCurrentMonth();

        String monthString = new DateFormatSymbols().getMonths()[d.getMonth()];
        String yearString = " " + (d.getYear()+1900);
        calMonth = findViewById(R.id.month_title);
        calMonth.setText(monthString.substring(0,3) + yearString);
        highlightCalendarEvents();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(final Date dateClicked) {
                listItems = new ArrayList<>();
                List<Event> events = compactCalendarView.getEvents(dateClicked);

                if (events.size() != 0) {
                    addButt.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "Day booked, choose another date."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });
                    String dateId = (String) events.get(0).getData();
                    Log.d(TAG, "Day was clicked: " + dateClicked + " with Data: " + (String) events.get(0).getData());
                    DocumentReference docRef = db.collection("bookings").document(dateId);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                    String temp_id = document.getId();
                                    Log.d("debug", temp_id);
                                    String temp_equip = (String) document.getData().get("equip");

                                    String temp_date = (String) document.getData().get("date");

                                    String temp_timeslot = (String) document.getData().get("timeslot");

                                    String temp_gymname = (String) document.getData().get("gymname");
                                    String temp_gymid = (String) document.getData().get("gymid");
                                    BookingTemplate book123 = new BookingTemplate(temp_equip,
                                            temp_timeslot, temp_date, temp_id, temp_gymname, temp_gymid);
                                    listItems.add(book123);
                                    Log.d("debug", document.getId() + " => " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                                adapter = new BookingAdapter(listItems, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }else {
                    listItems.clear();
                    adapter = new BookingAdapter(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    addButt.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), NewBookingActivity.class);
                            intent.putExtra("date", dateClicked);
                            intent.putExtra("weekday", dateClicked.getDay());
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                int month = firstDayOfNewMonth.getMonth();
                String monthString = new DateFormatSymbols().getMonths()[month];
                String yearString = " " + 2020;
                Log.d(TAG, "Month was scrolled to: " + monthString);
                calMonth.setText(monthString.substring(0,3)  + yearString);

            }
        });
    }

    /**
     * Event handler for add booking click. Sends user to NewBookingActivity.
     * @param view View
     */
    public void onAddBookingClick(View view) {
        Toast.makeText(getApplicationContext(), "Choose a date on calendar."
                , Toast.LENGTH_SHORT).show();
    }

    /**
     * Highlights days that have a reservation on calendar with a DOT.
     */
    public void highlightCalendarEvents() {

        db.collection("bookings")
                .whereEqualTo("id", currentuser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String temp_id = document.getId();
                                String temp_date = (String) document.getData().get("date");
                                Log.d("TAG", document.getId() + " => " + document.getData());

                                long miliSecsDate = milliseconds(temp_date);
                                Event ev1 = new Event(Color.BLUE, miliSecsDate, temp_id);
                                compactCalendarView.addEvent(ev1);
                            }

                        } else {
                            Log.d("debug", "Error getting documents: ", task.getException());
                        }

                    }
                });
    }

    /**
     * Event handler to change calendar to the previous month.
     * @param view View
     */
    public void prevMonthClick(View view) {
        compactCalendarView.scrollLeft();
        Date d = compactCalendarView.getFirstDayOfCurrentMonth();
        String monthString = new DateFormatSymbols().getMonths()[d.getMonth()];
        String yearString = " " + (d.getYear()+1900);
        Log.d(TAG, "Month was scrolled to: " + monthString);
        calMonth.setText(monthString.substring(0,3) + yearString);
    }

    /**
     * Event handler to change calendar to the next month.
     * @param view View
     */
    public void nextMonthClick(View view) {
        compactCalendarView.scrollRight();
        Date d = compactCalendarView.getFirstDayOfCurrentMonth();
        String monthString = new DateFormatSymbols().getMonths()[d.getMonth()];
        String yearString = " " + (d.getYear()+1900);
        Log.d(TAG, "Month was scrolled to: " + monthString);
        calMonth.setText(monthString.substring(0,3) + yearString);
    }

    /**
     * Converts to milliseconds for calendar conversions.
     * @param date Date
     * @return long milliseconds
     */
    public long milliseconds(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Event handler to edit user's booking and sends to edit page.
     * @param view View
     */
    public void onEditBookingClick(View view) {
        Intent intent = new Intent(this, EditBookingActivity.class);
        TextView timeslotID = findViewById(R.id.timeslotID);
        TextView book_date = findViewById(R.id.book_date);
        String dateStr = book_date.getText().toString();
        String idStr = timeslotID.getText().toString();
        intent.putExtra("id", idStr);
        intent.putExtra("date", dateStr);

        startActivity(intent);
    }

    /**
     * Event handler to delete user's booking.
     * @param view View
     */
    public void onDeleteBookingClick(View view) {
        TextView timeslotID = findViewById(R.id.timeslotID);
        String idStr = timeslotID.getText().toString();

        db.collection("bookings").document(idStr)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
        alert();
    }

    /**
     * Alert message for deleting booking.
     */
    public void alert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Adds settings to device emulator's back button.
     */
    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, MenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}