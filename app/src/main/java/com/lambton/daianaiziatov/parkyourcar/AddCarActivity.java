package com.lambton.daianaiziatov.parkyourcar;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddCarActivity extends AppCompatActivity {

    private Spinner manufacturerSpinner;
    private Spinner modelSpinner;
    private Spinner colorSpinner;
    private EditText plateEditText;
    private Button addCarButton;
    private ImageView carLogoImageView;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private DatabaseReference manufacturerReference;
    private DatabaseReference colorsReference;
    private DatabaseReference carReference;
    private StorageReference carLogoRef;

    private HashMap<String, ArrayList<String>> modelsByManufacturer;
    private ArrayList<String> manufacturerList;
    private ArrayList<String> models;
    private ArrayList<String> colors;

    private Car car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        car = new Car();

        modelsByManufacturer = new HashMap<>();
        manufacturerList = new ArrayList<>();
        manufacturerList.add("Manufacturers");
        models = new ArrayList<>();
        models.add("Model");
        colors = new ArrayList<>();
        colors.add("Color");

        // firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        carLogoRef = storage.getReference().child("cars_logos");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        manufacturerReference = database.getReference().child("cars_models");
        colorsReference = database.getReference().child("colors");
        carReference = database.getReference().child("users").child(user.getUid()).child("cars");

        manufacturerSpinner = findViewById(R.id.manufacturer_spinner);
        modelSpinner = findViewById(R.id.model_spinner);
        colorSpinner = findViewById(R.id.color_spinner);
        plateEditText = findViewById(R.id.plate_edit_text);
        addCarButton = findViewById(R.id.add_car_button);
        carLogoImageView = findViewById(R.id.car_logo_image_view);

        loadCarsAndColors();

        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CAR", car.toString());
            }
        });

        modelSpinner.setAdapter(new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_item, models));

    }

    private void setImageFor(String brand) {
        carLogoRef.child(brand + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(carLogoImageView);
            }
        });
    }

    private void loadCarsAndColors() {
        manufacturerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    final Map<String, String> value = (Map<String, String>) data.getValue();
                    final String name = value.get("name");
                    manufacturerList.add(name);
                    ArrayList<String> models = new ArrayList<>(Arrays.asList(value.get("models").split(", ")));
                    models.set(0, "Models");
                    modelsByManufacturer.put(name, models);
                }
                setupModelsAndBrandsSpinners();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showAlertWithMessage(databaseError.getMessage());
            }
        });

        colorsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final HashMap<String, String> value = (HashMap<String, String>) dataSnapshot.getValue();
                colors.addAll(Arrays.asList(value.get("colors").split(", ")));
                setupColorSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showAlertWithMessage(databaseError.getMessage());
            }
        });
    }

    private void showAlertWithMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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

    private void setupModelsAndBrandsSpinners() {
        manufacturerSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, manufacturerList));
        manufacturerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    String manufacturer = manufacturerList.get(position);
                    models = modelsByManufacturer.get(manufacturer);
                    car.setManufacturer(manufacturer);
                    setImageFor(manufacturer);
                    modelSpinner.setAdapter(new ArrayAdapter<>(AddCarActivity.this, android.R.layout.simple_spinner_item, models));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    car.setModel(models.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupColorSpinner() {
        colorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, colors));
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    car.setColor(colors.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
