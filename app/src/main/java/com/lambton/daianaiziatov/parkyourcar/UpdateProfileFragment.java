package com.lambton.daianaiziatov.parkyourcar;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lambton.daianaiziatov.parkyourcar.Models.User;

import java.util.HashMap;
import java.util.Map;


public class UpdateProfileFragment extends Fragment {

    private TextView emailTextView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText contactEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button updateButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View updateView = inflater.inflate(R.layout.fragment_update_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users").child(user.getUid());

        emailTextView = updateView.findViewById(R.id.email_text_view);
        firstNameEditText = updateView.findViewById(R.id.user_first_name_edit_view);
        lastNameEditText = updateView.findViewById(R.id.user_last_name_edit_view);
        contactEditText = updateView.findViewById(R.id.contact_number_edit_view);
        oldPasswordEditText = updateView.findViewById(R.id.old_password_edit_view);
        newPasswordEditText = updateView.findViewById(R.id.new_password_edit_view);
        confirmPasswordEditText = updateView.findViewById(R.id.confirm_password_edit_view);
        updateButton = updateView.findViewById(R.id.update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        emailTextView.setText("Email: " + user.getEmail());
        loadUser();

        return updateView;
    }

    private void update() {
        final String firstName = firstNameEditText.getText().toString();
        final String lastName = lastNameEditText.getText().toString();
        final String contactNumber = contactEditText.getText().toString();
        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put("firstName", firstName);
        userUpdate.put("lastName", lastName);
        userUpdate.put("contactNumber", contactNumber);
        myRef.updateChildren(userUpdate, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    showAlertWithMessage("User profile updated successfully!");
                } else {
                    showAlertWithMessage(databaseError.getMessage());
                }
            }
        });
    }

    private void loadUser() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                firstNameEditText.setText(value.get("firstName"));
                lastNameEditText.setText(value.get("lastName"));
                contactEditText.setText(value.get("contactNumber"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showAlertWithMessage(databaseError.getMessage());
            }
        });
    }

    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
