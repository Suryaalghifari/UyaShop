package com.example.uyashop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.uyashop.Adapter.CategoryAdapter;
import com.example.uyashop.Adapter.PopularAdapter;
import com.example.uyashop.Adapter.SliderAdapter;
import com.example.uyashop.Domain.CategoryDomain;
import com.example.uyashop.Domain.ItemsDomain;
import com.example.uyashop.Domain.SliderItems;
import com.example.uyashop.databinding.ActivityDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends BaseActivity {
    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        buttonNavigation();
        buttonNavigation1();
        buttonNavigation2();


    }

    private void buttonNavigation2() {
        binding.accountactivity.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, AccountDetailActivity.class)));
    }
    private void buttonNavigation1() {
        binding.orderDetail.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, OrderDetailActivity.class)));
    }

    private void buttonNavigation() {
        binding.cartActivity.setOnClickListener(v -> startActivity(new Intent(Dashboard.this, CartActivity.class)));
    }

    private void initPopular() {
        DatabaseReference myref=database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items=new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                     if(!items.isEmpty())
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(Dashboard.this,2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                        binding.recyclerViewPopular.setNestedScrollingEnabled(true);
                }
                    binding.progressBarPopular.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarOfficial.setVisibility(View.VISIBLE);

        ArrayList<CategoryDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(CategoryDomain.class));
                    }
                    if (!items.isEmpty()) {
                        LinearLayoutManager layoutManager = new LinearLayoutManager(Dashboard.this,
                                LinearLayoutManager.HORIZONTAL, false);
                        binding.recyclerViewOfficial.setLayoutManager(layoutManager);
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));
                        binding.recyclerViewOfficial.setNestedScrollingEnabled(true);
                    }
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef=database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items=new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items){
        binding.viewPagerSlider.setAdapter(new SliderAdapter(items,binding.viewPagerSlider));
        binding.viewPagerSlider.setClipToPadding(false);
        binding.viewPagerSlider.setClipChildren(false);
        binding.viewPagerSlider.setOffscreenPageLimit(3);
        binding.viewPagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewPagerSlider.setPageTransformer(compositePageTransformer);


    }
}