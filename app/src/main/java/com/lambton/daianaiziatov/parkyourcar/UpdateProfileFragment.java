package com.lambton.daianaiziatov.parkyourcar;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    private String alertMessage = "";

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
                if (isUserInfoValid()) {
                    updateUserInfo();
                } else {
                    showAlertWithMessage(alertMessage);
                    alertMessage = "";
                }
            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newPassword = newPasswordEditText.getText().toString();
                String confirmPassword = s.toString();
                if (!newPassword.equals(confirmPassword)) {
                    newPasswordEditText.setError("Passwords are different");
                    confirmPasswordEditText.setError("Passwords are different");
                } else {
                    newPasswordEditText.setError(null);
                    confirmPasswordEditText.setError(null);
                }
            }
        });

        emailTextView.setText("Email: " + user.getEmail());
        loadUser();

        return updateView;
    }

    private void updateUserInfo() {
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
                    if (isPasswordValid()) {
                        updatePassword();
                    } else {
                        showAlertWithMessage("User profile updated successfully!");
                    }
                } else {
                    showAlertWithMessage(databaseError.getMessage());
                }
            }
        });
    }

    private void updatePassword() {
        String email = user.getEmail();
        String oldPassword = oldPasswordEditText.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String newPassword = newPasswordEditText.getText().toString();
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("PASSWORD_CHANGED", "TRUE");
                                showAlertWithMessage("User info and password updated successfully!");
                            } else {
                                Log.d("PASSWORD_CHANGED", "FALSE");
                                showAlertWithMessage("User info updated, but there is error while updating password: " + task.getException().toString());
                            }
                        }
                    });
                } else {
                    showAlertWithMessage("Password couldn't be changed: " + task.getException().toString());
                }
            }
        });
    }

    private boolean isUserInfoValid() {
        boolean valid = true;

        Pattern regexp;
        Matcher matcher;

        String firstName = firstNameEditText.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("Required.");
            alertMessage += "\n-Name is required.";
            valid = false;
        } else {
            firstNameEditText.setError(null);
            regexp = Pattern.compile("^(?![\\s.]+$)[a-zA-Z\\s.]*$");
            matcher = regexp.matcher(firstName);
            boolean result = matcher.matches();
            if (!result) {
                valid = false;
                firstNameEditText.setError("Only alphabets and spaces.");
                alertMessage += "\n-First name must include only alphabets and spaces.";
            }
        }

        String lastName = lastNameEditText.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Required.");
            alertMessage += "\n-Name is required.";
            valid = false;
        } else {
            lastNameEditText.setError(null);
            regexp = Pattern.compile("^(?![\\s.]+$)[a-zA-Z\\s.]*$");
            matcher = regexp.matcher(lastName);
            boolean result = matcher.matches();
            if (!result) {
                valid = false;
                lastNameEditText.setError("Only alphabets and spaces.");
                alertMessage += "\n-Last name must include only alphabets and spaces.";
            }
        }

        String number = contactEditText.getText().toString();
        if (TextUtils.isEmpty(number)) {
            contactEditText.setError("Required.");
            valid = false;
        } else {
            contactEditText.setError(null);
        }

        return valid;
    }

    private boolean isPasswordValid() {
        boolean valid = true;
        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (!TextUtils.isEmpty(oldPassword) || !TextUtils.isEmpty(newPassword) || !TextUtils.isEmpty(confirmPassword)) {
            if (!TextUtils.isEmpty(oldPassword) && !TextUtils.isEmpty(newPassword) && !TextUtils.isEmpty(confirmPassword)) {
                if (newPassword.equals(confirmPassword)) {
                    valid = true;
                } else {
                    valid = false;
                    alertMessage += "-New and Confirm passwords should be the same.";
                }
            } else {
                valid = false;
                alertMessage += "-Please fill all fields for changing password or delete all input from there if you don't want to change password";
            }
        }
        return valid;
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
