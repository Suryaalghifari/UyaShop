package com.example.uyashop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uyashop.R;
import com.example.uyashop.databinding.ActivityOrderDetailBinding;

public class OrderDetailActivity extends AppCompatActivity {
    private ActivityOrderDetailBinding binding;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        backBtn = findViewById(R.id.backBtn);

        // Ambil data dari SharedPreferences dan tampilkan di UI
        displayOrderDetails();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.sudahBayarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hapus data dari SharedPreferences
                clearSharedPreferences();

                // Reset tampilan OrderDetailActivity
                resetUI();

                // Tampilkan pesan "Terimakasih, pesananmu segera dikirim"
                showThankYouMessage();
            }
        });
    }

    private void displayOrderDetails() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String orderTimestamp = sharedPreferences.getString("orderTimestamp", "No Timestamp");
        float itemTotal = sharedPreferences.getFloat("itemTotal", 0.0f);
        float tax = sharedPreferences.getFloat("tax", 0.0f);
        float delivery = sharedPreferences.getFloat("delivery", 0.0f);
        float total = sharedPreferences.getFloat("total", 0.0f);

        // Tampilkan data
        binding.OrderTime.setText(orderTimestamp);
        binding.itemTotal.setText("Rp" + itemTotal);
        binding.pajak.setText("Rp" + tax);
        binding.Delivery.setText("Rp" + delivery);
        binding.totalTxt.setText("Rp" + total);
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Hapus semua data dari SharedPreferences
        editor.apply();
    }

    private void resetUI() {
        // Kosongkan atau reset tampilan sesuai kebutuhan
        binding.OrderTime.setText("");
        binding.itemTotal.setText("");
        binding.pajak.setText("");
        binding.Delivery.setText("");
        binding.totalTxt.setText("");
    }

    private void showThankYouMessage() {
        Toast.makeText(OrderDetailActivity.this, "Terimakasih, pesananmu segera dikirim", Toast.LENGTH_SHORT).show();
    }
}
