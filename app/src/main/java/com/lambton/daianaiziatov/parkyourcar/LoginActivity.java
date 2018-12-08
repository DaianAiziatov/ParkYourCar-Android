package com.lambton.daianaiziatov.parkyourcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    private String userEmail, password;
    private FirebaseAuth mAuth;
    private EditText userEmailEditText;
    private EditText userPasswordEditText;
    private Switch rememberMeSwitch;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailEditText = (EditText) findViewById(R.id.user_email_edit_view);
        userPasswordEditText = (EditText) findViewById(R.id.user_password_edit_view);
        rememberMeSwitch = (Switch) findViewById(R.id.rememberme_switch);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        mAuth = FirebaseAuth.getInstance();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            userEmailEditText.setText(loginPreferences.getString("email", ""));
            userPasswordEditText.setText(loginPreferences.getString("password", ""));
            rememberMeSwitch.setChecked(true);
        }

    }

    public void loginPressed(View view) {
        if (areAllFieldsFilled()) {
            userEmail = userEmailEditText.getText().toString();
            password = userPasswordEditText.getText().toString();
            mAuth.signInWithEmailAndPassword(userEmail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                if (rememberMeSwitch.isChecked()) {
                                    loginPrefsEditor.putBoolean("saveLogin", true);
                                    loginPrefsEditor.putString("email", userEmail);
                                    loginPrefsEditor.putString("password", password);
                                } else {
                                    loginPrefsEditor.clear();
                                }
                                loginPrefsEditor.putLong("logDate", System.currentTimeMillis());
                                loginPrefsEditor.commit();
                                goToActivity(MainActivity.class);
                            } else {
                                // If sign in fails, display a message to the user.
                                showAlertWithMessage("Wrong Email/Password");
                            }
                        }
                    });
        } else {
            showAlertWithMessage("Please fill email & password");
        }
    }

    public void signupPressed(View view) {
        goToActivity(SignupActivity.class);
    }

    private boolean areAllFieldsFilled() {
        boolean valid = true;

        String email = userEmailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userEmailEditText.setError("Required.");
            valid = false;
        } else {
            userEmailEditText.setError(null);
        }

        String password = userPasswordEditText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            userPasswordEditText.setError("Required.");
            valid = false;
        } else {
            userPasswordEditText.setError(null);
        }
        return valid;
    }

    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
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
