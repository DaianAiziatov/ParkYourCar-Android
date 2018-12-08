package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lambton.daianaiziatov.parkyourcar.Models.Car;

import java.util.ArrayList;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private ArrayList<Car> carArrayList;
    private Context context;

    public CarListAdapter(ArrayList<Car> carArrayList, Context context) {
        this.carArrayList = carArrayList;
        this.context = context;
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
        // TODO: Set image
    }

    @Override
    public int getItemCount() {
        return carArrayList.size();
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
