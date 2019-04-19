package com.lukas.srkandroid.activities.addCatch;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lukas.srkandroid.R;
import com.lukas.srkandroid.activities.addCatch.fragments.FormFragment;
import com.lukas.srkandroid.activities.addCatch.fragments.MapFragment;

public class AddCatch extends AppCompatActivity {

    FormFragment formFragment;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catch);

        formFragment = new FormFragment();
        mapFragment = new MapFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contentFragment, formFragment);
        transaction.commit();
    }

    public void showMap() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.contentFragment, mapFragment);
        transaction.commit();
    }
}
