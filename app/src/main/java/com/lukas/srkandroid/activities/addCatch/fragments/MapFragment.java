package com.lukas.srkandroid.activities.addCatch.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lukas.srkandroid.R;
import com.lukas.srkandroid.activities.addCatch.AddCatch;
import com.lukas.srkandroid.activities.addCatch.AddCatchController;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private AddCatchController controller;

    private MapView mapView;
    private GoogleMap map;
    private LatLng location;

    private Button confirmButton;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        controller = new AddCatchController(this);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        view.requestFocus();

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        confirmButton = view.findViewById(R.id.confirmMapBtn);
        confirmButton.setOnClickListener(e -> {
            if (location == null) {
                return;
            }
            AddCatch activity = (AddCatch) getActivity();
            activity.setLocation(location);
            activity.showForm();
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            controller.getLocationInfo();
            return;
        }
        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location myLocation = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        if (myLocation == null){
            controller.getLocationInfo();
            return;
        }
        LatLng position = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        setMarkerOnPosition(position);
    }

    public void setMarkerOnPosition(LatLng position) {
        location = position;
        map.addMarker(new MarkerOptions().position(location).draggable(true));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(12).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) { }

            @Override
            public void onMarkerDrag(Marker marker) { }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                location = marker.getPosition();
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
