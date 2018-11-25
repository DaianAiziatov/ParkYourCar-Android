package com.lambton.daianaiziatov.parkyourcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.Dictionary;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEdittext;
    private EditText numberEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmationEditText;

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
                final String email = emailEdittext.getText().toString();
                String password = passwordEditText.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(SignupActivity.this, "User created successfully.", Toast.LENGTH_SHORT).show();
                                    // Write user details in database
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference().child("users").child(user.getUid());
                                    String firstName = firstNameEditText.getText().toString();
                                    String lastName = lastNameEditText.getText().toString();
                                    String contactNumber = numberEditText.getText().toString();
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("firstName", firstName);
                                    data.put("lastName", lastName);
                                    data.put("email", email);
                                    data.put("contactNumber",contactNumber);
                                    myRef.setValue(data);
                                    goToActivity(LoginActivity.class);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    private boolean areAllFieldsFilled() {
        boolean result = !firstNameEditText.getText().toString().isEmpty() &&
                !lastNameEditText.getText().toString().isEmpty() &&
                !emailEdittext.getText().toString().isEmpty() &&
                !numberEditText.getText().toString().isEmpty() &&
                !passwordEditText.getText().toString().isEmpty() &&
                !passwordConfirmationEditText.getText().toString().isEmpty();
        return result;
    }

    private boolean isPasswordValid() {
        if (!passwordEditText.getText().toString().isEmpty() && !passwordConfirmationEditText.getText().toString().isEmpty()) {
            String password = passwordEditText.getText().toString();
            String passwordConfirmation = passwordConfirmationEditText.getText().toString();
            return password.equals(passwordConfirmation);
        } else {
            return false;
        }
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
