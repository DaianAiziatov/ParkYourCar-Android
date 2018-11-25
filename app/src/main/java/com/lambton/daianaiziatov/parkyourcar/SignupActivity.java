package com.lambton.daianaiziatov.parkyourcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lambton.daianaiziatov.parkyourcar.Models.User;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEdittext;
    private EditText numberEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String contactNumber;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstNameEditText = findViewById(R.id.user_first_name_edit_view);
        lastNameEditText = findViewById(R.id.user_last_name_edit_view);
        emailEdittext = findViewById(R.id.user_email_edit_view);
        numberEditText = findViewById(R.id.user_number_edit_view);
        passwordEditText = findViewById(R.id.user_password_edit_view);
        passwordConfirmationEditText = findViewById(R.id.user_password_confirmation_edit_view);

        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view) {
        if (areAllFieldsFilled()) {
            if (isPasswordValid()) {
                email = emailEdittext.getText().toString();
                password = passwordEditText.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    writeDetailsToFirebase();
                                    goToActivity(LoginActivity.class);
                                } else {
                                    showAlertWithMessage("Error occurred while creating new user.\nPlease try again.");
                                }

                            }
                        });
            } else {
                showAlertWithMessage("Please check passwords fields");
            }
        } else {
            showAlertWithMessage("Please fill all fields.");
        }
    }

    private void writeDetailsToFirebase(){
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").child(user.getUid());

        firstName = firstNameEditText.getText().toString();
        lastName = lastNameEditText.getText().toString();
        contactNumber = numberEditText.getText().toString();
        email = emailEdittext.getText().toString();

        User userInstance = new User(firstName, lastName, contactNumber, email);
        myRef.setValue(userInstance.toMap());
    }

    private boolean areAllFieldsFilled() {
        boolean valid = true;

        String firstName = firstNameEditText.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("Required.");
            valid = false;
        } else {
            firstNameEditText.setError(null);
        }

        String lastName = lastNameEditText.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Required.");
            valid = false;
        } else {
            lastNameEditText.setError(null);
        }

        String email = emailEdittext.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEdittext.setError("Required.");
            valid = false;
        } else {
            emailEdittext.setError(null);
        }

        String number = numberEditText.getText().toString();
        if (TextUtils.isEmpty(number)) {
            numberEditText.setError("Required.");
            valid = false;
        } else {
            numberEditText.setError(null);
        }


        String password = passwordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        String passwordConfirmation = passwordConfirmationEditText.getText().toString();
        if (TextUtils.isEmpty(passwordConfirmation)) {
            passwordConfirmationEditText.setError("Required.");
            valid = false;
        } else {
            passwordConfirmationEditText.setError(null);
        }

        return valid;
    }

    private boolean isPasswordValid() {
        boolean valid = true;

        String password = passwordEditText.getText().toString();
        String passwordConfirmation = passwordConfirmationEditText.getText().toString();
        if (!password.equals(passwordConfirmation)) {
            passwordConfirmationEditText.setError("Not match.");
            valid = false;
        } else {
            passwordConfirmationEditText.setError(null);
        }
        return valid;
    }

    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
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

    private void goToActivity(Class<?> className) {
        Intent intent = new Intent(this, className);
        this.startActivity(intent);
    }
}
