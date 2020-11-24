package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bcit.bb.adapters.PolicyAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PolicyActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String TAG = "PolicyActivity";
    private ArrayList<String> policyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        Log.d(TAG, "onCreate: started");

        get_Gym_Choice();
        initPolicies();
    }

    private void initPolicies() {
        Log.d(TAG, "initPolicies: perparing policies");

        //policyListHeaders.add("COVID-19 Safety");
        policyList.add("All fitness centre workouts & classes require advanced bookings on the Bookings&Bulking service app, per first-come-first-serve policy.");
        policyList.add("Access to fitness facilities is limited to registered persons.");
        policyList.add("Fitness centre and class capacity is limited per time slot/class to allow for distancing.");
        policyList.add("Spotting, working out in partners/groups, or working in with other members are not allowed at this time.");
        policyList.add("Signs and directions are posted throughout the facility to guide traffic flow and queueing spots.");
        policyList.add("All members will receive a cleaning spray bottle to use during their workout.");
        policyList.add("Members are required to clean their equipment before and after each use.");
        policyList.add("Areas (pods) within the fitness centre free weight & stretching areas will be designated for individual workouts.");
        policyList.add("For fitness classes, individual work out spaces (pods) will be assigned upon entry into the activity space");

        //policyListHeaders.add("Arriving & Signing-in");
        policyList.add("Please arrive no sooner than 10 minutes before your booked workout session, as late arrivals will not be permitted inside.");
        policyList.add("Please have your booking information & valid member ID with you for confirmation's purposes.");

        //policyListHeaders.add("Equipment");
        policyList.add("Equipment is booked based on first-come-first-serve policy.");
        policyList.add("In case of equipment conflicts, both parties will be asked to show their booking information. If neither parties can prove for their reservation, both will be asked to dismiss from the designated area.");
        policyList.add("Participants are welcome to bring their own towels and yoga mats, but are not allowed to share equipment with others.");
        policyList.add("Members are encouraged to bring their own towel and personal water bottle.");
        policyList.add("All water fountains are closed. If needed, please refill your water bottle at a tap in a washroom.");
        policyList.add("All members should continue to arrive changed and ready to workout whenever possible.");

        //policyListHeaders.add("Amenities");
        policyList.add("Gym's washrooms, change rooms, showers, and workout lockers are available with restrictions and limited capacities.");
        policyList.add("Members are to adhere to all directional signage, time limits and capacity limits in the change areas and showers.");
        policyList.add("Showers have re-opened with limited capacities; guests are encouraged to shower at home when possible.");
        policyList.add("Members must bring their own shower products to use in the shower spaces, no soap nor shampoo will be available.");
        policyList.add("Members must bring their own personal lock for locker use during their workout (locks will not be provided), and are responsible for removing the contents and lock immediately after their activity has concluded.");

        policyList.add("Please stay home if you are not feeling well.");
        policyList.add("No bookings. No bulking.");

        initRecyclerView();
    }
    
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.policy_list);
        PolicyAdapter adapter = new PolicyAdapter(policyList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void get_Gym_Choice(){
        DocumentReference docRef = db.collection("users").document(currentuser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TextView policy_header = findViewById(R.id.policy_header);
                String str = "Please note there are currently additional safety protocols in place as we reopen during the COVID-19 pandemic. Where these rules differ from what’s listed below, the ";
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String gym_choice = (String) document.getData().get("gymchoice");
                        str += gym_choice;
                        str += "\'s Healthy protocols will take priority.";
                        policy_header.setText(str);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}