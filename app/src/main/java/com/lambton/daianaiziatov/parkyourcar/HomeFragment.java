package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lambton.daianaiziatov.parkyourcar.Models.Car;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class HomeFragment extends Fragment {


    private TextView emailTextView;
    private TextView lastLoginTextView;
    private TextView ticketsTotalTextView;
    private RecyclerView recyclerView;
    private Button addCarButton;

    private SharedPreferences loginPreferences;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private int numberOfTickets;

    private ArrayList<Car> carArrayList;
    private CarListAdapter carListAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View homeView =  inflater.inflate(R.layout.fragment_home, container, false);

        emailTextView = (TextView) homeView.findViewById(R.id.email_text_view);
        lastLoginTextView = (TextView) homeView.findViewById(R.id.login_date_text_view);
        ticketsTotalTextView = (TextView) homeView.findViewById(R.id.tickets_text_view);
        addCarButton = homeView.findViewById(R.id.add_car_button);

        loginPreferences = getActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        carArrayList = new ArrayList<>();

        emailTextView.setText("User email: " + user.getEmail());
        long dateInMS = loginPreferences.getLong("logDate", 0);
        Date loginDate = new Date(dateInMS);
        lastLoginTextView.setText("Last login: " + loginDate.toString());
        loadNumberOfParkingTickets();
        loadCarList();

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Go to Add Car Activity", Toast.LENGTH_SHORT).show();
            }
        });


        recyclerView = homeView.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(homeView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        carListAdapter = new CarListAdapter(carArrayList, homeView.getContext());
        recyclerView.setAdapter(carListAdapter);

        return homeView;
    }

    private void loadCarList() {
        Log.d("INSIDELOADCAR", "true");
        DatabaseReference ticketsReference = database.getReference().child("users").child(user.getUid()).child("cars");
        ticketsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATASNAPSHOT", dataSnapshot.toString());
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    final Map<String, String> value = (Map<String, String>) data.getValue();
                    final String id = data.getKey();
                    final String model = value.get("model");
                    final String color = value.get("color");
                    final String manufacturer = value.get("manufacturer");
                    final String plate = value.get("plate");
                    final Car car = new Car(id, manufacturer, model, plate, color);
                    Log.d("CARS_ADDED", car.toString());
                    carArrayList.add(car);
                }
                Log.d("CARS_COUNT", String.valueOf(carArrayList.size()));
                carListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                showAlertWithMessage(error.getMessage());
            }
        });
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
