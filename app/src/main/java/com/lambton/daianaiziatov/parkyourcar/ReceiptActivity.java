package com.lambton.daianaiziatov.parkyourcar;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lambton.daianaiziatov.parkyourcar.Models.Car;
import com.lambton.daianaiziatov.parkyourcar.Models.ParkingTicket;
import com.squareup.picasso.Picasso;

public class ReceiptActivity extends AppCompatActivity {

    private TextView dateTextView;
    private TextView manufacturerTextView;
    private TextView modelTextView;
    private TextView colorTextView;
    private TextView plateTextView;
    private TextView paymentTextView;
    private TextView totalTextView;
    private ImageView carLogoImageView;

    private ParkingTicket parkingTicket;

    private StorageReference carLogoRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        parkingTicket = getIntent().getParcelableExtra("ticket");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        carLogoRef = storage.getReference().child("cars_logos");

        dateTextView = findViewById(R.id.date_text_view);
        dateTextView.setText(parkingTicket.getDate());
        manufacturerTextView = findViewById(R.id.manufacturer_text_view);
        manufacturerTextView.setText(parkingTicket.getManufacturer());
        modelTextView = findViewById(R.id.model_text_view);
        modelTextView.setText(parkingTicket.getModel());
        colorTextView = findViewById(R.id.color_text_view);
        colorTextView.setText(parkingTicket.getColor());
        plateTextView = findViewById(R.id.plate_text_view);
        plateTextView.setText(parkingTicket.getPlate());
        paymentTextView = findViewById(R.id.payment_text_view);
        paymentTextView.setText("Payment method: " + parkingTicket.getPayment());
        totalTextView = findViewById(R.id.total_text_view);
        totalTextView.setText("Total: $" + parkingTicket.getTotal());
        carLogoImageView = findViewById(R.id.car_logo_image_view);
        setImageFor(parkingTicket.getManufacturer(), carLogoImageView);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(this, MainActivity.class);
        boolean fromReport = getIntent().getBooleanExtra("fromReport", false);
        intent.putExtra("fromReport", fromReport);
        this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("CDA", "onBackPressed Called");
                Intent intent = new Intent(this, MainActivity.class);
                boolean fromReport = getIntent().getBooleanExtra("fromReport", false);
                intent.putExtra("fromReport", fromReport);
                this.startActivity(intent);
                break;
        }
        return true;
    }

    private void setImageFor(String brand, final ImageView imageView) {
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
}
