package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.lambton.daianaiziatov.parkyourcar.Models.Car;
import com.lambton.daianaiziatov.parkyourcar.Models.ParkingTicket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private CarListAdapter carListAdapter;

    private SharedPreferences loginPreferences;

    private ParkingTicket parkingTicket;
    private Integer lastSelectedPosition;

    private Date currentDate;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public AddNewTicketFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addNewTicketView =  inflater.inflate(R.layout.fragment_add_new_ticket, container, false);
        parkingTicket = new ParkingTicket();

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

        //spinner setup
        timmmingSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ParkingTicket.Timing.values()));
        timmmingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parkingTicket.setTiming(ParkingTicket.Timing.values()[position]);
                totalTextView.setText("Total: $" + getTotal(ParkingTicket.Timing.values()[position]));
                parkingTicket.setPaymentAmount(getTotal(ParkingTicket.Timing.values()[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        paymentSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ParkingTicket.PaymentMethod.values()));
        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parkingTicket.setPaymentMethod(ParkingTicket.PaymentMethod.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    @Override
    public void recyclerViewListClicked(View v, int position) {
        Car selectedCar = carListAdapter.getCarArrayList().get(position);
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
            parkingTicket.setCarManufacturer(car.getManufacturer());
            parkingTicket.setCarModel(car.getModel());
            parkingTicket.setCarColor(car.getColor());
            parkingTicket.setCarPlate(car.getPlateNumber());
        }
    }
}
