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
        String[] policies = getResources().getStringArray(R.array.policies);

        for (String str : policies) {
            policyList.add(str);
        }

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