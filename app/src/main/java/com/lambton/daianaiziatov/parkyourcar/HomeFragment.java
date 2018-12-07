package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class HomeFragment extends Fragment {


    private TextView emailTextView;
    private TextView lastLoginTextView;
    private TextView ticketsTotalTextView;
    private SharedPreferences loginPreferences;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private int numberOfTickets;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View homeView =  inflater.inflate(R.layout.fragment_home, container, false);

        emailTextView = (TextView) homeView.findViewById(R.id.email_text_view);
        lastLoginTextView = (TextView) homeView.findViewById(R.id.login_date_text_view);
        ticketsTotalTextView = (TextView) homeView.findViewById(R.id.tickets_text_view);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        emailTextView.setText("User email: " + user.getEmail());
        long dateInMS = loginPreferences.getLong("logDate", 0);
        Date loginDate = new Date(dateInMS);
        lastLoginTextView.setText("Last login: " + loginDate.toString());
        loadNumberOfParkingTickets();

        return homeView;
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
