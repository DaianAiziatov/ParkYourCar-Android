package com.lambton.daianaiziatov.parkyourcar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lambton.daianaiziatov.parkyourcar.Models.Car;
import com.lambton.daianaiziatov.parkyourcar.Models.ParkingTicket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class AddNewTicketFragment extends Fragment implements RecyclerViewClickListener {

    private TextView totalTextView;
    private TextView dateTextView;
    private EditText emailEditText;
    private Button addCarButton;
    private RecyclerView recyclerView;
    private Spinner timmmingSpinner;
    private EditText parkingSlotEditText;
    private EditText parkingSpotEditText;
    private Spinner paymentSpinner;
    private Button getReceiptButton;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference ticketsReference;

    private CarListAdapter carListAdapter;

    private SharedPreferences loginPreferences;

    private ParkingTicket parkingTicket;
    private Car selectedCar;
    private Integer lastSelectedPosition;

    private Date currentDate;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String alertMessage = "";


    public AddNewTicketFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addNewTicketView =  inflater.inflate(R.layout.fragment_add_new_ticket, container, false);
        parkingTicket = new ParkingTicket();

        // firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        ticketsReference = database.getReference().child("users").child(user.getUid()).child("tickets");

        // findByID
        totalTextView = addNewTicketView.findViewById(R.id.total_text_view);
        dateTextView = addNewTicketView.findViewById(R.id.ticket_date_text_view);
        emailEditText = addNewTicketView.findViewById(R.id.user_email_edit_text);
        addCarButton = addNewTicketView.findViewById(R.id.add_car_button);
        recyclerView = addNewTicketView.findViewById(R.id.recycler_view);
        timmmingSpinner = addNewTicketView.findViewById(R.id.timming_spinner);
        parkingSlotEditText = addNewTicketView.findViewById(R.id.parking_slot_edit_text);
        parkingSpotEditText = addNewTicketView.findViewById(R.id.parking_spot_edit_text);
        paymentSpinner = addNewTicketView.findViewById(R.id.payment_spinner);
        getReceiptButton = addNewTicketView.findViewById(R.id.get_receipt_button);
        // add car button
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Go to Add Car Activity", Toast.LENGTH_SHORT).show();
            }
        });

        //recycler view setup
        recyclerView = addNewTicketView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(addNewTicketView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        carListAdapter = new CarListAdapter(addNewTicketView.getContext(), this);
        recyclerView.setAdapter(carListAdapter);

        // email textview
        loginPreferences = getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        emailEditText.setText(loginPreferences.getString("email", ""));

        // current date textview
        currentDate = new Date(System.currentTimeMillis());
        dateTextView.setText(dateFormat.format(currentDate));
        parkingTicket.setDate(dateFormat.format(currentDate));

        //spinner setup
        timmmingSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ParkingTicket.Timing.values()));
        timmmingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parkingTicket.setTiming(ParkingTicket.Timing.values()[position]);
                totalTextView.setText("Total: $" + getTotal(ParkingTicket.Timing.values()[position]));
                parkingTicket.setTotal(getTotal(ParkingTicket.Timing.values()[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        paymentSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ParkingTicket.PaymentMethod.values()));
        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parkingTicket.setPayment(ParkingTicket.PaymentMethod.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // get receipt button setup
        getReceiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReceipt();
            }
        });

        return addNewTicketView;
    }

    private double getTotal(ParkingTicket.Timing timing) {
        switch (timing) {
            case HALF_A_HOUR: return 3.0;
            case ONE_HOUR: return 7.0;
            case TWO_HOURS: return 15.0;
            case THREE_HOURS: return 25.0;
            case DAY_ENDS: return  10.0;
        }
        return 0.0;
    }

    private void getReceipt() {
        if (isValid()) {
            parkingTicket.setUserEmail(emailEditText.getText().toString());
            parkingTicket.setSlotNumber(parkingSlotEditText.getText().toString());
            parkingTicket.setSpotNumber(parkingSpotEditText.getText().toString());
            ticketsReference.push().setValue(parkingTicket, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Toast.makeText(getContext(), "Successfully saved!", Toast.LENGTH_SHORT).show();
                    } else {
                        showAlertWithMessage(databaseError.getMessage());
                    }
                }
            });
        } else {
            showAlertWithMessage(alertMessage);
            alertMessage = "";
        }
    }

    private boolean isValid() {
        boolean valid = true;
        Pattern regexp;
        Matcher matcher;

        String email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Required.");
            alertMessage += "\n-Email is required.";
            valid = false;
        } else {
            emailEditText.setError(null);
            regexp = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$");
            matcher = regexp.matcher(email);
            boolean result = matcher.matches();
            if (!result) {
                valid = false;
                emailEditText.setError("Invalid email format");
                alertMessage += "\n-Invalid email format.";
            }
        }

        if (selectedCar == null || !selectedCar.isSelected()) {
            alertMessage += "\n-Please select any car or add new.";
            valid = false;
        }

        String spot = parkingSpotEditText.getText().toString();
        if (TextUtils.isEmpty(spot)) {
            parkingSpotEditText.setError("Required.");
            alertMessage += "\n-Spot is required.";
            valid = false;
        } else {
            parkingSpotEditText.setError(null);
        }

        String slot = parkingSlotEditText.getText().toString();
        if (TextUtils.isEmpty(slot)) {
            parkingSlotEditText.setError("Required.");
            alertMessage += "\n-Slot is required.";
            valid = false;
        } else {
            parkingSlotEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        selectedCar = carListAdapter.getCarArrayList().get(position);
        if (lastSelectedPosition != null) {
            if (lastSelectedPosition == position) {
                selectedCar.setSelected(false);
                setCarToTicket(null);
                lastSelectedPosition = null;
            } else {
                Car previousPickedCar = carListAdapter.getCarArrayList().get(lastSelectedPosition);
                previousPickedCar.setSelected(false);
                lastSelectedPosition = position;
                selectedCar.setSelected(true);
                setCarToTicket(selectedCar);
            }
        } else {
            lastSelectedPosition = position;
            selectedCar.setSelected(true);
            setCarToTicket(selectedCar);
        }
    }

    private void setCarToTicket(Car car) {
        if (car != null) {
            Log.d("PICKED_CAR", car.toString());
            parkingTicket.setManufacturer(car.getManufacturer());
            parkingTicket.setModel(car.getModel());
            parkingTicket.setColor(car.getColor());
            parkingTicket.setPlate(car.getPlateNumber());
        }
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
