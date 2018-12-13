package com.lambton.daianaiziatov.parkyourcar;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lambton.daianaiziatov.parkyourcar.Models.ParkingTicket;


public class ReportFragment extends Fragment implements RecyclerViewClickListener, SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private SearchView searchView;

    private TicketListAdapter ticketListAdapter;
    private ParkingTicket selectedTicket;


    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View reportView =  inflater.inflate(R.layout.fragment_report, container, false);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = reportView.findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ticketListAdapter.getFilter().filter(s);
        return true;
    }
}
