package com.example.uyashop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.uyashop.Adapter.CartAdapter;
import com.example.uyashop.Domain.ItemsDomain;
import com.example.uyashop.Helper.ManagmentCart;
import com.example.uyashop.Helper.Order;
import com.example.uyashop.R;
import com.example.uyashop.databinding.ActivityCartBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private double tax;
    private ManagmentCart managmentCart;
    private FirebaseDatabase database;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);

        database = FirebaseDatabase.getInstance();
        ordersRef = database.getReference("orders");

        calculatorCart();
        setVariable();
        initCartList();

        binding.checkoutBtn.setOnClickListener(v -> checkout());
    }

    private void checkout() {
        ArrayList<ItemsDomain> cartList = managmentCart.getListCart();
        double total = managmentCart.getTotalFee();

        Order order = new Order(cartList, total, Order.getCurrentTimestamp());

        ordersRef.push().setValue(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CartActivity.this, "Pesanan Berhasil Di Pesan!", Toast.LENGTH_SHORT).show();

                    // Simpan data di SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("orderTimestamp", Order.getCurrentTimestamp());
                    myEdit.putFloat("itemTotal", managmentCart.getTotalFee().floatValue());
                    myEdit.putFloat("tax", (float) tax);
                    myEdit.putFloat("delivery", 10.0f);
                    myEdit.putFloat("total", (float) (managmentCart.getTotalFee() + tax + 10.0));
                    myEdit.apply();

                    // Kirim data ke OrderDetailActivity
                    Intent intent = new Intent(CartActivity.this, Dashboard.class);
                    startActivity(intent);

                    managmentCart.clearCart();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CartActivity.this, "Gagal Memesan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void initCartList() {
        if (managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        } else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        binding.cartView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.cartView.setAdapter(new CartAdapter(managmentCart.getListCart(), this, () -> calculatorCart()));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void calculatorCart() {
        double percentTax = 0.02;
        double delivery = 10.0;
        tax = Math.round((managmentCart.getTotalFee() * percentTax * 100.0)) / 100.0;

        double total = Math.round((managmentCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((managmentCart.getTotalFee() * 100.0)) / 100.0;

        binding.totalFeeTxt.setText("Rp" + itemTotal);
        binding.taxTxt.setText("Rp" + tax);
        binding.deliveryTxt.setText("Rp" + delivery);
        binding.totalTxt.setText("Rp" + total);

        // Simpan data di SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putFloat("itemTotal", (float) itemTotal);
        myEdit.putFloat("tax", (float) tax);
        myEdit.putFloat("delivery", (float) delivery);
        myEdit.putFloat("total", (float) total);
        myEdit.apply();
    }
}
