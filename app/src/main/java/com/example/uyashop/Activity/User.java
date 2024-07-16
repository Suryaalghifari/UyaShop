package com.example.uyashop.Activity;

public class User extends Register {
    public String nama_lengkap;
    public String email;

    public User() {
        // Default constructor diperlukan untuk panggilan ke DataSnapshot.getValue(User.class)
    }

    public User(String nama_lengkap, String email) {
        this.nama_lengkap = nama_lengkap;
        this.email = email;
    }
}

