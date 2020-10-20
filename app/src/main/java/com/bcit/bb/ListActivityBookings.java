package com.bcit.bb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bcit.bb.Bookings;
import com.bcit.bb.R;
import com.bcit.bb.currentBookingAdapter;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/*
List activity class containing parsing the JSON using Volley
 */
public class ListActivityBookings extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    String searchedWords;
    private ArrayList<Bookings> listItems;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Create a reference to the bookings collection
    CollectionReference idsRef = db.collection("bookings");

    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
