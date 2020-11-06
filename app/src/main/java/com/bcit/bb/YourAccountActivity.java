package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bcit.bb.uiFeatures.MultiSelectionSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class YourAccountActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference idsRef = db.collection("users");
    String TAG = "debug";
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_account);

        final TextView username = findViewById(R.id.username);
        final TextView email = findViewById(R.id.email);

        idsRef.whereEqualTo("id", currentuser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String temp_id = document.getId();
                                Log.d("debug", temp_id);
                                String username_db = (String) document.getData().get("username");
                                Log.d("debug", (String) document.getData().get("username"));
                                username.setText(username_db);

                                String email_db = (String) document.getData().get("email");
                                Log.d("debug", (String) document.getData().get("email"));
                                email.setText(email_db);

//                                String gym_choice_db = (String)  document.getData().get("gym_choice");
//                                Log.d("debug", (String) document.getData().get("gym_choice"));
//                                String choice = gym_choice.getSelectedItem().toString();
                            }
                        } else {
                            Log.d("debug", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void get_choice() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference subjectsRef = rootRef.collection("admins").document(currentuser)
                .collection("gymname");

        final List<String> choices = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, choices);


        // Multi spinner
        final Spinner spinner;
        spinner = findViewById(R.id.gym_choice_spinner);

        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("gymchoice");
                        choices.add(subject);
                    }
                    //spinner.setItem(choices);

                }
            }
        });

    }
}
