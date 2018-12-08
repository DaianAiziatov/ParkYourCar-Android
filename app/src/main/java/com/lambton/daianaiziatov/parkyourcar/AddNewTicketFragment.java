package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewTicketFragment extends Fragment {

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

    private Date currentDate;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public AddNewTicketFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View addNewTicketView =  inflater.inflate(R.layout.fragment_add_new_ticket, container, false);

        currentDate = new Date(System.currentTimeMillis());
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
        carListAdapter = new CarListAdapter(addNewTicketView.getContext());
        recyclerView.setAdapter(carListAdapter);

        // current date textview
        dateTextView.setText(dateFormat.format(currentDate));

        return addNewTicketView;
    }
}
