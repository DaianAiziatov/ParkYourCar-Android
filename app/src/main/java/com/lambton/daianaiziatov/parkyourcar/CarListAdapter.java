package com.lambton.daianaiziatov.parkyourcar;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.lambton.daianaiziatov.parkyourcar.Models.Car;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {

    private ArrayList<Car> carArrayList;
    private Context context;
    private StorageReference carLogoRef;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference carsReference;

    public CarListAdapter(Context context) {
        this.carArrayList = new ArrayList<>();
        this.context = context;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.carLogoRef = storage.getReference().child("cars_logos");
        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();
        this.user = mAuth.getCurrentUser();
        this.carsReference = database.getReference().child("users").child(user.getUid()).child("cars");
        loadCarList();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car, viewGroup, false);
        CarViewHolder carViewHolder = new CarViewHolder(itemView);

        return carViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder carViewHolder, final int position) {
        Car car = carArrayList.get(position);

        carViewHolder.carInfoTextView.setText(car.getColor() + " " + car.getManufacturer() + " " + car.getModel());
        carViewHolder.carPlateTextView.setText(car.getPlateNumber());
        setImageFor(car, carViewHolder.carLogoImageView);

        carViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteCar(position);
                return false;
            }
        });

        carViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(true);
                v.setBackgroundColor(0xFF00FF00);
            }
        });
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

    private void deleteCar(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete country this car?");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               deleteCarFromFirebase(position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteCarFromFirebase(int position) {
        final Car car = carArrayList.get(position);
        carsReference.child(car.getCarId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    carArrayList.remove(car);
                    notifyDataSetChanged();
                } else {
                    showAlertWithMessage(databaseError.getMessage());
                }
            }
        });

    }

    private void loadCarList() {
        Log.d("INSIDELOADCAR", "true");
        carsReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                showAlertWithMessage(error.getMessage());
            }
        });
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
