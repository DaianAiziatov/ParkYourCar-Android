package com.lambton.daianaiziatov.parkyourcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (getIntent().getBooleanExtra("fromReport", false)) {
            fragmentTransaction.add(R.id.container, new ReportFragment(),"fragment_report");
        } else if (getIntent().getBooleanExtra("fromAddNewTicket", false)) {
            fragmentTransaction.add(R.id.container, new AddNewTicketFragment(),"fragment_add_new_ticket");
        } else {
            fragmentTransaction.add(R.id.container, new HomeFragment(),"fragment_home");
        }
        fragmentTransaction.commit();
        setTitle("Home");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (id == R.id.nav_home) {
            fragmentTransaction.replace(R.id.container, new HomeFragment(),"fragment_home");
            setTitle("Home");
        } else if (id == R.id.nav_report) {
            fragmentTransaction.replace(R.id.container, new ReportFragment(),"fragment_report");
            setTitle("Report");
        } else if (id == R.id.nav_add_ticket) {
            fragmentTransaction.replace(R.id.container, new AddNewTicketFragment(),"fragment_add_new_ticket");
            setTitle("New Ticket");
        } else if (id == R.id.nav_location) {
            fragmentTransaction.replace(R.id.container, new LocationFragment(),"fragment_location");
            setTitle("Location");
        } else if (id == R.id.nav_update_profile) {
            fragmentTransaction.replace(R.id.container, new UpdateProfileFragment(),"fragment_update_profile");
            setTitle("Update Profile");
        } else if (id == R.id.nav_instruction) {
            fragmentTransaction.replace(R.id.container, new InstructionFragment(),"fragment_instruction");
            setTitle("Instruction");
        } else if (id == R.id.nav_contact) {
            contactUs();
        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            goToActivity(LoginActivity.class);
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goToActivity(Class<?> className) {
        Intent intent = new Intent(this, className);
        this.startActivity(intent);
    }

    private void contactUs() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Contact us.");
        alertDialog.setMessage("Need any help? Contact us by:");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "CALL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendEmail();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "EMAIL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        makeCall();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "SMS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sendSMS();
                    }
                });
        alertDialog.show();
    }

    private void sendSMS() {
        String message = "My question is:";
        String phoneNo = "+12345678900";
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNo));
        smsIntent.putExtra("sms_body", message);
        if(smsIntent.resolveActivity(this.getPackageManager()) != null)
        {
            startActivity(smsIntent);
        }
        else
        {
            showAlertWithMessage("No application to handle SMS");
        }
    }

    private void sendEmail() {
        String email = "parking@lambton.com";
    }

    private void makeCall() {
        String phoneNo = "+12345678900";
        String dial = "tel:" + phoneNo;
        Intent phoneItent = new Intent(Intent.ACTION_DIAL, Uri.parse(dial));
        if(phoneItent.resolveActivity(this.getPackageManager()) != null)
        {
            startActivity(phoneItent);
        }
        else
        {
            showAlertWithMessage("No application to handle Phone call");
        }
    }

    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
