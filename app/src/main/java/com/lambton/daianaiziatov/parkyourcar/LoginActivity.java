package com.lambton.daianaiziatov.parkyourcar;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText userEmailEditText;
    private EditText userPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailEditText = (EditText) findViewById(R.id.user_email_edit_view);
        userPasswordEditText = (EditText) findViewById(R.id.user_password_edit_view);

        mAuth = FirebaseAuth.getInstance();

    }

    public void loginPressed(View view) {
        if (areAllFieldsFilled()) {
            String userEmail = userEmailEditText.getText().toString();
            String password = userPasswordEditText.getText().toString();
            mAuth.signInWithEmailAndPassword(userEmail, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, "Wrong User/Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "Please fill email & password", Toast.LENGTH_SHORT).show();
        }

//        if (userEmail.equals("daian@mail.com") && password.equals("123456")) {
//            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
////            Intent intent = new Intent(this, HomeActivity.class);
////            intent.putExtra(USER_EMAIL, userEmail);
////            this.startActivity(intent);


    }

    public void signupPressed(View view) {

    }

    private boolean areAllFieldsFilled() {
        return !userPasswordEditText.getText().toString().isEmpty() && !userEmailEditText.getText().toString().isEmpty();
    }

}
