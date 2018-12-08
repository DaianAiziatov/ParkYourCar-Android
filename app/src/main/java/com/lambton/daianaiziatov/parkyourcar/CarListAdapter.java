package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lambton.daianaiziatov.parkyourcar.Models.Car;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private ArrayList<Car> carArrayList;
    private Context context;
    private StorageReference carLogoRef;

    public CarListAdapter(ArrayList<Car> carArrayList, Context context) {
        this.carArrayList = carArrayList;
        this.context = context;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.carLogoRef = storage.getReference().child("cars_logos");
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car, viewGroup, false);
        CarViewHolder carViewHolder = new CarViewHolder(itemView);

        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder carViewHolder, int position) {
        Car car = carArrayList.get(position);

        carViewHolder.carInfoTextView.setText(car.getColor() + " " + car.getManufacturer() + " " + car.getModel());
        carViewHolder.carPlateTextView.setText(car.getPlateNumber());
        setImageFor(car, carViewHolder.carLogoImageView);
    }

    @Override
    public int getItemCount() {
        return carArrayList.size();
    }

    private void setImageFor(Car car, final ImageView imageView) {
        carLogoRef.child(car.getManufacturer() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

    public class CarViewHolder extends RecyclerView.ViewHolder {

        ImageView carLogoImageView;
        TextView carInfoTextView;
        TextView carPlateTextView;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            carLogoImageView = itemView.findViewById(R.id.car_logo_image_view);
            carInfoTextView = itemView.findViewById(R.id.car_info_text_view);
            carPlateTextView = itemView.findViewById(R.id.car_plate_text_view);
        }
    }
}
