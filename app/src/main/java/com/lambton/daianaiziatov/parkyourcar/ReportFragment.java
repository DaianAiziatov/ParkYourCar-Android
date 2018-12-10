package com.lambton.daianaiziatov.parkyourcar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lambton.daianaiziatov.parkyourcar.Models.ParkingTicket;


public class ReportFragment extends Fragment implements RecyclerViewClickListener {

    private RecyclerView recyclerView;
    private TicketListAdapter ticketListAdapter;
    private ParkingTicket selectedTicket;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View reportView =  inflater.inflate(R.layout.fragment_report, container, false);

        //recycler view setup
        recyclerView = reportView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(reportView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        ticketListAdapter = new TicketListAdapter(reportView.getContext(), this);
        recyclerView.setAdapter(ticketListAdapter);

        return reportView;
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
        selectedTicket = ticketListAdapter.getTicketArrayList().get(position);
        Intent intent = new Intent(getContext(), ReceiptActivity.class);
        intent.putExtra("ticket", selectedTicket);
        intent.putExtra("fromReport", true);
        getActivity().startActivity(intent);
    }
}
