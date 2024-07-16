package com.example.uyashop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uyashop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText create_email, create_pw, nama_lengkap;
    private Button btn_SignUp;
    private TextView signin_text;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        create_email = findViewById(R.id.create_email);
        create_pw = findViewById(R.id.create_pw);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        btn_SignUp = findViewById(R.id.btn_SignUp);
        signin_text = findViewById(R.id.signin_text);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Uncomment jika diperlukan
        // databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://uyashop-6c03a-default-rtdb.firebaseio.com/");
        // databaseReference = FirebaseDatabase.getInstance().getReference("User");

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = create_email.getText().toString();
                String password = create_pw.getText().toString();
                String namalengkap = nama_lengkap.getText().toString();

                if (email.isEmpty() || password.isEmpty() || namalengkap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() <= 6) {
                    Toast.makeText(getApplicationContext(), "Paswword Harus Lebih Dari 6 Karakter!", Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    auth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        String userId = auth.getCurrentUser().getUid();
                                                        User user = new User(email, namalengkap);
                                                        databaseReference.child(userId).setValue(user)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getApplicationContext(), "Selamat, pendaftaran berhasil. Silahkan cek email Anda", Toast.LENGTH_SHORT).show();
                                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                        } else {
                                                                            Toast.makeText(getApplicationContext(), "Maaf, penyimpanan data gagal", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Verifikasi gagal dikirim", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Maaf, pendaftaran gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signin_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Register.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
    static class User {
        public String email;
        public String namalengkap;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String namalengkap) {
            this.email = email;
            this.namalengkap = namalengkap;
        }
    }
}

