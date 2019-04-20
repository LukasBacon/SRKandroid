package com.lukas.srkandroid.activities.addCatch;

import android.Manifest;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.lukas.srkandroid.R;
import com.lukas.srkandroid.activities.addCatch.fragments.FormFragment;
import com.lukas.srkandroid.activities.addCatch.fragments.MapFragment;
import com.lukas.srkandroid.entities.Catch;

public class AddCatch extends AppCompatActivity {

    private Catch catch0;

    private FormFragment formFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_catch);

        askForLocationPermission();

        formFragment = new FormFragment();
        mapFragment = new MapFragment();

        showFragment(R.id.contentFragment, formFragment, "formFragment", null, false);
    }

    public void showMap() {
        showFragment(R.id.contentFragment, mapFragment, "mapFragment", "formFragment", true);
    }

    public void showForm() {
        showFragment(R.id.contentFragment, formFragment, "formFragment", "mapFragment", false);
    }

    public Catch getCatch() {
        return catch0;
    }

    public void setLocation(LatLng location) {
        formFragment.setLocation(location);
    }

    private void showFragment(int resId, Fragment fragment, String tag, String lastTag, boolean addToBackStack ) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (lastTag != null) {
            Fragment lastFragment = fragmentManager.findFragmentByTag(lastTag);
            if ( lastFragment != null ) {
                transaction.hide(lastFragment);
            }
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        }
        else {
            transaction.add(resId, fragment, tag).setBreadCrumbShortTitle(tag);
        }
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    private void askForLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
}
