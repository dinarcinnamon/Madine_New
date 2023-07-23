package com.example.madine;

import static com.example.madine.MainActivity.email;
import static com.example.madine.MainActivity.name;
import static com.example.madine.MainActivity.noUnit;
import static com.example.madine.MainActivity.password;
import static com.example.madine.MainActivity.plat;
import static com.example.madine.MainActivity.telpNo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.madine.helper.Simplify;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EditText noUnitET = findViewById(R.id.editTextNoUnit);
        EditText nameET = findViewById(R.id.editTextName);
        EditText telpET = findViewById(R.id.editTextTelpNo);
        EditText emailET = findViewById(R.id.editTextEmail);
        EditText passET = findViewById(R.id.editTextPassword);
        EditText confET = findViewById(R.id.editTextConfirmPassword);
        EditText platET = findViewById(R.id.editTextLicenseNumber);
        ImageView imageView = findViewById(R.id.buttonRegister);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noUnit = noUnitET.getText().toString();
                name = nameET.getText().toString();
                telpNo = telpET.getText().toString();
                plat = platET.getText().toString();
                email = emailET.getText().toString();
                password = passET.getText().toString();

                // Validations for input email and password
                if (TextUtils.isEmpty(email)) {
                    Simplify.showToastMessage(getApplicationContext(),"Silahkan isi email !!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Simplify.showToastMessage(getApplicationContext(),"Silahkan isi password !!");
                    return;
                }

                if(password.length()< 6){
                    Simplify.showToastMessage(getApplicationContext(),"Password terlalu pendek");
                    return;
                }
                if(!password.equals(confET.getText().toString())){
                    Simplify.showToastMessage(getApplicationContext(),"Password tidak cocok");
                    return;
                }

                Intent i = new Intent(RegisterActivity.this, VerifikasiActivity.class);

                startActivity(i);
                // tutup activity ini
                finish();
            }
        });

        platET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Kosongkan
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kosongkan
            }

            @Override
            public void afterTextChanged(Editable s) {
                String capitalizedText = s.toString().toUpperCase();
                if (!s.toString().equals(capitalizedText)) {
                    platET.setText(capitalizedText);
                    platET.setSelection(capitalizedText.length());
                }
            }
        });

    }
}