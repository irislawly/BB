package com.bcit.bb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditAccount extends AppCompatDialogFragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference idsRef = db.collection("users");
    String TAG = "debug";
    String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    EditText username_field;
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_edit_account, null);
        builder.setView(view)
                .setTitle("Login")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final DocumentReference docRef = db.collection("users").document(currentuser);
                        String username = username_field.getText().toString();
                        Log.d(TAG,"Upcoming change: " + username);
                        docRef.update("name", username).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(getActivity(), "Updated succesfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(getContext(), MenuActivity.class);
                        startActivity(intent);
                    }
                });
        username_field = view.findViewById(R.id.username_field);
        return builder.create();
    }
}