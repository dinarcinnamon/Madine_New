package com.example.madine;

import static com.example.madine.MainActivity.email;
import static com.example.madine.MainActivity.password;
import static com.example.madine.MainActivity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madine.helper.Simplify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {

    private TextView registxt;
    private EditText usernameTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        usernameTextView = findViewById(R.id.username_inputText);
        passwordTextView = findViewById(R.id.password_inputText);
        Btn = findViewById(R.id.btn_login_admin);
        progressBar = findViewById(R.id.progressBar);
        ImageView imageViewTogglePassword = findViewById(R.id.imageViewTogglePassword);
//        usernameTextView.setText("");
//        passwordTextView.setText("");

        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Write a message to the database
                loginUserAccount();
            }
        });

        imageViewTogglePassword.setOnClickListener(new View.OnClickListener() {
            boolean passwordVisible = false;

            @Override
            public void onClick(View v) {
                if (passwordVisible) {
                    // Mengubah ke mode password tersembunyi
                    passwordTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewTogglePassword.setImageResource(R.drawable.ic_password_hidden);
                } else {
                    // Mengubah ke mode password terlihat
                    passwordTextView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewTogglePassword.setImageResource(R.drawable.ic_password_visible);
                }
                passwordVisible = !passwordVisible;

                // Setel kursor ke akhir teks
                passwordTextView.setSelection(passwordTextView.getText().length());
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof EditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void loginUserAccount()
    {

        // Take the value of two edit texts in Strings
        user = usernameTextView.getText().toString();
        email = user + "@gmail.com";
        password = passwordTextView.getText().toString();
        password = "123123";

        // validations for input email and password
        if (TextUtils.isEmpty(user)) {
            Simplify.showToastMessageWHITE(getApplicationContext(),"Please enter username!!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Simplify.showToastMessageWHITE(getApplicationContext(),"Please enter password!!");
            return;
        }

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Simplify.showToastMessageWHITE(getApplicationContext(),"Login successful!!");
                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent;
                                    if(user.equals("admin")){
                                        intent = new Intent(AdminLogin.this,
                                                MainAdmin.class);
                                        startActivity(intent);

                                    }else{
                                        Simplify.showToastMessageWHITE(getApplicationContext(),"User Bukan Admin !");

                                        // hide the progress bar
                                        progressBar.setVisibility(View.GONE);
                                    }


                                }

                                else {

                                    // sign-in failed
                                    Simplify.showToastMessageWHITE(getApplicationContext(),"Login failed!!");

                                    // hide the progress bar
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
    }


}