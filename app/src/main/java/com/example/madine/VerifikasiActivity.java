package com.example.madine;

import static com.example.madine.MainActivity.email;
import static com.example.madine.MainActivity.name;
import static com.example.madine.MainActivity.noUnit;
import static com.example.madine.MainActivity.password;
import static com.example.madine.MainActivity.plat;
import static com.example.madine.MainActivity.telpNo;
import static com.example.madine.MainActivity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.madine.helper.Simplify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VerifikasiActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Button registerButton;
    private FirebaseAuth mAuth;

    private  DatabaseReference imgRef;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private ImageView imageView;
    private Uri imageUri;

    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasi);

        // Inisialisasi komponen UI
        imageView = findViewById(R.id.imageView);
        Button captureButton = findViewById(R.id.captureButton);
        Button pickButton = findViewById(R.id.pickButton);
        progressBar = findViewById(R.id.progressBar2);
        registerButton = findViewById(R.id.btn_push_register);

        //firebase init
        mAuth = FirebaseAuth.getInstance();
        imgRef = FirebaseDatabase.getInstance().getReference("img").child(noUnit).child("imageUrl");
        /// Atur onClickListener untuk tombol Capture
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });

        // Atur onClickListener untuk tombol Pick
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPickPictureIntent();
            }
        });

        // Atur onClickListener untuk tombol Register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validasi input pengguna sebelum mengunggah gambar dan data ke Firebase
                if (validateInput()) {
                    // show the visibility of progress bar to show loading
                    progressBar.setVisibility(View.VISIBLE);
                    uploadImageToStorage();
                }
            }
        });
    }

    //region "Methods"
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Jika izin kamera belum diberikan, munculkan dialog permintaan izin.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            // Jika izin kamera sudah diberikan, langsung buka aktivitas kamera.
            dispatchTakePictureIntent();
        }
    }

    // Override metode onRequestPermissionsResult untuk menangani hasil permintaan izin.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // Periksa apakah izin kamera telah diberikan oleh pengguna.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin kamera diberikan, buka aktivitas kamera.
                dispatchPickPictureIntent();
            } else {
                // Izin kamera ditolak, tangani sesuai kebutuhan aplikasi.
                Toast.makeText(this, "Izin kamera ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Metode untuk memanggil intent kamera untuk mengambil gambar
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    // Metode untuk memanggil intent galeri untuk memilih gambar
    private void dispatchPickPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPictureIntent.setType("image/*");
        startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Gambar diambil dari kamera
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                imageUri = getImageUriFromBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                // Gambar dipilih dari galeri
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }

    // Metode untuk mengubah Bitmap menjadi Uri
    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }

    private boolean validateInput() {
        // Lakukan validasi input pengguna di sini
        // Misalnya, validasi apakah semua kolom telah diisi dengan benar

        return true; // Mengembalikan true jika validasi berhasil
    }

    private void uploadImageToStorage() {
        if (imageUri == null) {
            // Jika tidak ada gambar yang dipilih, langsung simpan data pengguna ke database
            saveUserDataToDatabase(null);
            return;
        }

        // Membuat referensi Firebase Storage dengan path yang unik
        String imageFileName = noUnit + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + imageFileName);

        // Mengunggah gambar ke Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Gambar berhasil diunggah, mendapatkan URL gambar
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();

                                // Simpan data pengguna ke Realtime Database beserta URL gambar
                                saveUserDataToDatabase(imageUrl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Gagal mendapatkan URL gambar
                                Simplify.showToastMessageWHITE(getApplicationContext(),"Failed to get image URL");
                                // hide the progress bar
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gagal mengunggah gambar
                        Simplify.showToastMessageWHITE(getApplicationContext(),"Failed to upload image");
                        // hide the progress bar
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void saveUserDataToDatabase(String imageUrl) {
        // Ambil data dari input pengguna dan simpan ke Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("user");
        String userId = databaseRef.push().getKey();

        String unitNo = MainActivity.noUnit;
        String phoneNumber = MainActivity.telpNo;

        User user = new User(unitNo,name,phoneNumber,plat,email,imageUrl);

        databaseRef.child(unitNo).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data pengguna berhasil disimpan, tampilkan pesan sukses
                        registerNewUser();
                        // Lakukan tindakan lain setelah berhasil mendaftar
                        Intent i = new Intent(VerifikasiActivity.this, MainAdmin.class);
                        imgRef.setValue(imageUrl);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Gagal menyimpan data pengguna, tampilkan pesan kesalahan
                        Simplify.showToastMessageWHITE(getApplicationContext(),"Failed to register");
                    }
                });
    }



    private void registerNewUser()
    {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()) {
                            Simplify.showToastMessageWHITE(getApplicationContext(),"Registrasi Berhasil");
                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);

                        }
                        else {

                            // Registration failed
                            Simplify.showToastMessageWHITE(getApplicationContext(),"Registrasi Gagal!!" + " Silahkan Coba lagi");
                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    //endregion
}