package com.example.madine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);


        Button btnRegister = findViewById(R.id.btn_register);
        Button btnInfo = findViewById(R.id.btn_info_penghuni);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainAdmin.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainAdmin.this, InfoPenghuni.class);
                startActivity(i);
            }
        });
    }
}