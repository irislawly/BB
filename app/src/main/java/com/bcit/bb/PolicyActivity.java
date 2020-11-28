package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bcit.bb.adapters.PolicyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Policity activity to view covid-19 regulations.
 */
public class PolicyActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String TAG = "PolicyActivity";
    private ArrayList<String> policyList = new ArrayList<>();

    /**
     * Runs app.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        getGymChoice();
        initPolicies();
    }

    /**
     * Intialize policicies onto the activity.
     */
    private void initPolicies() {
        String[] policies = getResources().getStringArray(R.array.policies);

        for (String str : policies) {
            policyList.add(str);
        }

        initRecyclerView();
    }

    /**
     * User recycler view to hold policies.
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.policy_list);
        PolicyAdapter adapter = new PolicyAdapter(policyList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Get gym name and set on TextView.
     */
    public void getGymChoice(){
        DocumentReference docRef = db.collection("users").document(currentuser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TextView policy_header = findViewById(R.id.policy_header);
                String str = getResources().getString(R.string.policity_title);
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