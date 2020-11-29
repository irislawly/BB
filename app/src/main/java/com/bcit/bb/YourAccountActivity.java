package com.bcit.bb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

/**
 * Account Activity of user to change gym and name.
 */
public class YourAccountActivity extends AppCompatDialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = "debug";
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String[] slogans;
    TextView slogan_tv;
    ImageButton editbtn;
    Button changebtn;
    View view;

    /**
     * Creates dialog box
     * @param savedInstanceState state
     * @return dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_your_account, null);
        builder.setView(view)
                .setPositiveButton("done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        slogans = getResources().getStringArray(R.array.slogans);
        slogan_tv = view.findViewById(R.id.slogan_textview);
        editbtn = view.findViewById(R.id.edit_btn);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAccountClick(view);
            }
        });
        changebtn = view.findViewById(R.id.changebtn);
        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWriteToDatabase(v);
            }
        });
        getUserInfo();
        getChoice();
        updateSlogan();

        return builder.create();
    }

    /**
     * Get user information.
     */
    public void getUserInfo(){
        DocumentReference docRef = db.collection("users").document(currentuser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                TextView username = view.findViewById(R.id.username);
                TextView email = view.findViewById(R.id.email);
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

    /**
     * Get gym choice of user.
     */
    public void getChoice() {
        db.collection("admins")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> choices = new ArrayList<>();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, choices);
                        Spinner spinner;

                        spinner = view.findViewById(R.id.gym_choice_spinner);
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

    /**
     * Event handler when edit button is clicked.
     * @param view View
     */
    public void onEditAccountClick(View view) {
        EditAccount editacc = new EditAccount();
        editacc.show(getParentFragmentManager(), "edit your account");
    }

    /**
     * Generates random slogans of Booking&Bulking.
     */
    private void updateSlogan() {
        Random random = new Random();

        int maxIndex = slogans.length;
        int generatedIndex = random.nextInt(maxIndex);

        slogan_tv.setText(slogans[generatedIndex]);
    }

    /**
     * Writes changes to the database.
     * @param v View
     */
    public void onWriteToDatabase(View v) {
        Spinner spin = view.findViewById(R.id.gym_choice_spinner);
        String gymchoice = spin.getSelectedItem().toString();
        Toast toast =Toast. makeText(getContext(),"Your gym has been changed to " + gymchoice, Toast. LENGTH_SHORT);
        toast.show();
        db.collection("admins")
                .whereEqualTo("gymname", gymchoice)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TextView name = view.findViewById(R.id.username);
                                TextView email = view.findViewById(R.id.email);
                                Map<String, Object> user = new HashMap<>();

                                user.put("gymchoice", document.get("gymname").toString());
                                user.put("gymid", document.getId());
                                // These variables are needed to update the document, otherwise it'll disappear
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
