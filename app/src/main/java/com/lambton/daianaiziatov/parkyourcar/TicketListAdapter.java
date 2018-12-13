package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lambton.daianaiziatov.parkyourcar.Models.ParkingTicket;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketViewHolder> implements Filterable {

    private ArrayList<ParkingTicket> ticketArrayList;
    private ArrayList<ParkingTicket> filterTicketArrayList;
    private Context context;
    private TicketFilter ticketFilter;
    private StorageReference carLogoRef;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference ticketsReference;

    private ParkingTicket parkingTicket;

    private static RecyclerViewClickListener itemListener;


    public TicketListAdapter(Context context, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
        this.ticketArrayList = new ArrayList<>();
        this.filterTicketArrayList = new ArrayList<>();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.carLogoRef = storage.getReference().child("cars_logos");
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.user = mAuth.getCurrentUser();
        this.ticketsReference = database.getReference().child("users").child(user.getUid()).child("tickets");
        loadTicketList();
    }

    public ArrayList<ParkingTicket> getTicketArrayList() {
        return ticketArrayList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ticket, viewGroup, false);
        TicketViewHolder ticketViewHolder = new TicketViewHolder(itemView);

        return ticketViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder ticketViewHolder, int position) {
        parkingTicket = ticketArrayList.get(position);

        ticketViewHolder.carInfoTextView.setText(parkingTicket.getColor() + " " + parkingTicket.getManufacturer() + " " + parkingTicket.getModel());
        ticketViewHolder.carPlateTextView.setText(parkingTicket.getPlate());
        setImageForCar(parkingTicket.getManufacturer(), ticketViewHolder.carLogoImageView);
        ticketViewHolder.timingTextView.setText(parkingTicket.getTiming());
        ticketViewHolder.spotTextView.setText("Spot: " + parkingTicket.getSpotNumber());
        ticketViewHolder.slotTextView.setText("Slot: " + parkingTicket.getSlotNumber());
        setImageForPayment(parkingTicket.getPayment(), ticketViewHolder.paymentImageView);
        ticketViewHolder.totalTextView.setText("$" + parkingTicket.getTotal());
        ticketViewHolder.dateTextView.setText(parkingTicket.getDate());
    }

    @Override
    public int getItemCount() {
        return ticketArrayList.size();
    }

    private void loadTicketList() {
        ticketsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    final ParkingTicket ticket = data.getValue(ParkingTicket.class);
                    Log.d("TICKET_ADDED", ticket.toString());
                    ticketArrayList.add(ticket);
                    filterTicketArrayList.add(ticket);
                }
                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                showAlertWithMessage(error.getMessage());
            }
        });
    }

    private void setImageForCar(String brand, final ImageView imageView) {
        carLogoRef.child(brand + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // TODO: Handle error
            }
        });
    }

    private void setImageForPayment(String payment, final ImageView imageView) {
        switch (payment) {
            case "Ali Pay": imageView.setImageResource(R.drawable.ali_pay);
                break;
            case "Mastercard": imageView.setImageResource(R.drawable.mastercard);
                break;
            case "PayPal": imageView.setImageResource(R.drawable.paypal);
                break;
            case "Visa Credit": imageView.setImageResource(R.drawable.visa_credit);
                break;
            case "Visa Debit": imageView.setImageResource(R.drawable.visa_debit);
                break;
            case "WeChat Pay": imageView.setImageResource(R.drawable.wechat_pay);
                break;
        }


    }

    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
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

    @Override
    public Filter getFilter() {
        if (ticketFilter == null) {
            ticketFilter = new TicketFilter();
        }
        return ticketFilter;
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView carLogoImageView;
        TextView carInfoTextView;
        TextView carPlateTextView;
        TextView timingTextView;
        TextView spotTextView;
        TextView slotTextView;
        ImageView paymentImageView;
        TextView totalTextView;
        TextView dateTextView;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);

            carLogoImageView = itemView.findViewById(R.id.car_logo_image_view);
            carInfoTextView = itemView.findViewById(R.id.car_info_text_view);
            carPlateTextView = itemView.findViewById(R.id.car_plate_text_view);
            timingTextView = itemView.findViewById(R.id.timing_text_view);
            spotTextView = itemView.findViewById(R.id.spot_text_view);
            slotTextView = itemView.findViewById(R.id.slot_text_view);
            paymentImageView = itemView.findViewById(R.id.payment_image_view);
            totalTextView = itemView.findViewById(R.id.total_text_view);
            dateTextView = itemView.findViewById(R.id.date_text_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
            notifyDataSetChanged();
        }
    }

    private class TicketFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<ParkingTicket> tempList = new ArrayList<>();

                for (ParkingTicket ticket: filterTicketArrayList) {
                    if (ticket.getPlate().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(ticket);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else  {
                filterResults.count = filterTicketArrayList.size();
                filterResults.values = filterTicketArrayList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ticketArrayList = (ArrayList<ParkingTicket>) results.values;
            notifyDataSetChanged();
        }
    }
}
