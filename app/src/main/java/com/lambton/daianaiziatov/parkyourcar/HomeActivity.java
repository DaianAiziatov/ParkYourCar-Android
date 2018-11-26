package com.lambton.daianaiziatov.parkyourcar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private TextView emailTextView;
    private TextView lastLoginTextView;
    private TextView ticketsTotalTextView;
    private SharedPreferences loginPreferences;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private int numberOfTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        emailTextView = (TextView) findViewById(R.id.email_text_view);
        lastLoginTextView = (TextView) findViewById(R.id.login_date_text_view);
        ticketsTotalTextView = (TextView) findViewById(R.id.tickets_text_view);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        emailTextView.setText("User email: " + user.getEmail());
        long dateInMS = loginPreferences.getLong("logDate", 0);
        Date loginDate = new Date(dateInMS);
        lastLoginTextView.setText("Last login: " + loginDate.toString());
        loadNumberOfParkingTickets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNumberOfParkingTickets() {
        DatabaseReference ticketsReference = database.getReference().child("users").child(user.getUid()).child("tickets");
        ticketsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberOfTickets = (int) dataSnapshot.getChildrenCount();
                ticketsTotalTextView.setText("Tickets total: " + numberOfTickets);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showAlertWithMessage("Failed to read data");
            }
        });
    }



    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
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
