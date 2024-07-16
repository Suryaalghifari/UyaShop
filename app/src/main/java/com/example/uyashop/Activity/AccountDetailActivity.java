package com.example.uyashop.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.uyashop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountDetailActivity extends AppCompatActivity {

    private TextView emailTextView, nameTextView;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ImageView backBtnDashboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        emailTextView = findViewById(R.id.emailTextView);
        nameTextView = findViewById(R.id.nameTextView);
        backBtnDashboard = findViewById(R.id.backBtnDashboard);


        auth = FirebaseAuth.getInstance();

        // Mendapatkan userId dari user yang sedang login
        String userId = auth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Mengambil informasi user dari database berdasarkan userId
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String email = snapshot.child("email").getValue(String.class);
                    String name = snapshot.child("namalengkap").getValue(String.class); // Perubahan di sini

                    emailTextView.setText(email);
                    nameTextView.setText(name);
                } else {
                    Toast.makeText(AccountDetailActivity.this, "User tidak ditemukan!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountDetailActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        backBtnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
