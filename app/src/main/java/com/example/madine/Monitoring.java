package com.example.madine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.madine.helper.Simplify;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Monitoring extends AppCompatActivity {
    private static boolean stateA1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        stateA1 = true;

        ImageButton btnBack = findViewById(R.id.btnBack);

        Button boxA1 = findViewById(R.id.parking_a1);
        Button boxA2 = findViewById(R.id.parking_a2);
        Button boxA3 = findViewById(R.id.parking_a3);
        Button boxA4 = findViewById(R.id.parking_a4);
        Button boxA5 = findViewById(R.id.parking_a5);
        Button boxA6 = findViewById(R.id.parking_a6);

        Button boxB1 = findViewById(R.id.parking_b1);
        Button boxB2 = findViewById(R.id.parking_b2);
        Button boxB3 = findViewById(R.id.parking_b3);
        Button boxB4 = findViewById(R.id.parking_b4);

        ColorStateList RED = ColorStateList.valueOf(Color.RED); // Ganti dengan warna yang diinginkan
        ColorStateList GREEN = ColorStateList.valueOf(Color.GREEN); // Ganti dengan warna yang diinginkan

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("transaction").child("position");

        // Mendapatkan data dari Realtime Database
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Mendapatkan nilai email dari setiap child
                String[] states = new String[10];
                View[] boxes = {boxA1, boxA2, boxA3,boxA4, boxA5, boxA6, boxB1, boxB2, boxB3,boxB4};
                states[0] = dataSnapshot.child("A1").child("status").getValue(String.class);
                states[1] = dataSnapshot.child("A2").child("status").getValue(String.class);
                states[2] = dataSnapshot.child("A3").child("status").getValue(String.class);
                states[3] = dataSnapshot.child("A4").child("status").getValue(String.class);
                states[4] = dataSnapshot.child("A5").child("status").getValue(String.class);
                states[5] = dataSnapshot.child("A6").child("status").getValue(String.class);

                states[6] = dataSnapshot.child("B1").child("status").getValue(String.class);
                states[7] = dataSnapshot.child("B2").child("status").getValue(String.class);
                states[8] = dataSnapshot.child("B3").child("status").getValue(String.class);
                states[9] = dataSnapshot.child("B4").child("status").getValue(String.class);


                for (int i = 0; i < states.length; i++) {
                    if (states[i].equalsIgnoreCase("Empty")) {
                        boxes[i].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    } else {
                        boxes[i].setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Penanganan kesalahan baca dari Realtime Database
                Simplify.showToastMessageWHITE(getApplicationContext(),"Error: " + databaseError.getMessage());
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(Monitoring.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}