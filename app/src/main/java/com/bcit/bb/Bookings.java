package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.ArrayList;


public class Bookings extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the bookings collection
    CollectionReference idsRef = db.collection("bookings");

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    // Create a query against the collection.
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private ArrayList<BookingTemplate> listItems;



    String[] gymEquipment = new String[]{"Treadmill", "Rowing machine", "Dumbbells", "Leg press", "Pullup bar",
            "Chess press", "Bench press", "Pullup bar", "Lat pulldown"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        recyclerView = (RecyclerView)findViewById(R.id.item_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        setQuene();
    }

    public void onAddBookingClick(View view) {
        Intent intent = new Intent(this, AddNewBooking.class);
        startActivity(intent);
    }
    //not working yet need to edit currentingboooking adapter class
    public void onEditBookingClick(View view) {
        Intent intent = new Intent(this, EditBooking.class);
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