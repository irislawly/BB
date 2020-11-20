package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class YourAccountActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference idsRef = db.collection("users");
    String TAG = "debug";
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String[] slogans;
    TextView slogan_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_account);

        slogans = getResources().getStringArray(R.array.slogans);
        slogan_tv = findViewById(R.id.slogan_textview);

        get_user_info();
        get_choice();
        updateSlogan();
    }

    public void get_user_info(){
        DocumentReference docRef = db.collection("users").document(currentuser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TextView username = findViewById(R.id.username);
                TextView email = findViewById(R.id.email);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String username_db = (String) document.getData().get("name");
                        username.setText(username_db);

                           String email_db = (String) document.getData().get("email");
                            Log.d("debug", (String) document.getData().get("email"));
                             email.setText(email_db);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void get_choice() {
        db.collection("admins")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> choices = new ArrayList<>();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, choices);
                        Spinner spinner;

                        spinner = findViewById(R.id.gym_choice_spinner);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String choice = (String) document.getData().get("gymname");
                                choices.add(choice);
                                Log.d(TAG, document.getId() + " => " + document.getData().get("gymname"));
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void onEditAccountClick(View view) {
        Intent intent = new Intent(this, EditAccount.class);
        startActivity(intent);
    }

    private void updateSlogan() {
        Random random = new Random();

        int maxIndex = slogans.length;
        int generatedIndex = random.nextInt(maxIndex);

        slogan_tv.setText(slogans[generatedIndex]);
    }

    public void onWriteToDatabase(View v) {
        Spinner spin = findViewById(R.id.gym_choice_spinner);
        String gymchoice = spin.getSelectedItem().toString();
        Toast toast=Toast. makeText(getApplicationContext(),"Butt clicked " + gymchoice, Toast. LENGTH_SHORT);
        toast.show();
        db.collection("admins")
                .whereEqualTo("gymname", gymchoice)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TextView name = findViewById(R.id.username);
                                TextView email = findViewById(R.id.email);
                                Map<String, Object> user = new HashMap<>();

                                user.put("gymchoice", document.get("gymname").toString());
                                user.put("gymid", document.getId());
                                //These variables are needed to update the document, otherwise it'll disappear
                                user.put("name" ,name.getText().toString());
                                user.put("email", email.getText().toString());

                                db.collection("users").document(currentuser)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully rewritten!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

}
