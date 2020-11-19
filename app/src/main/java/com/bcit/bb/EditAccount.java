package com.bcit.bb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditAccount extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference idsRef = db.collection("users");
    String TAG = "debug";
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    EditText username_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        username_field = findViewById(R.id.username_field);
    }

    public void onUpdateUsername(View view) {
        final DocumentReference docRef = db.collection("users").document(currentuser);
        String username = username_field.getText().toString();
        Log.d(TAG,"Upcoming change: " + username);
        docRef.update("name", username).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditAccount.this, "Updated succesfully", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(this, YourAccountActivity.class);
        startActivity(intent);
    }
}