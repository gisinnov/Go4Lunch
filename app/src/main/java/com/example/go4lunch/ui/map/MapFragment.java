package com.example.go4lunch.ui.map;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.Permissions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mMap;
    private List<Marker> restaurantMarkers = new ArrayList<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (Permissions.hasLocationPermission(getActivity())) {
            setupMap();
        } else {
            Permissions.requestLocationPermission(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMap();
            }
        }
    }

    private void setupMap() {
        MapViewModel viewModel = new MapViewModel();
        List<LatLng> restaurantPositions = viewModel.getRestaurantPositions();
        List<String> restaurantNames = viewModel.getRestaurantNames();

        // Add markers for each restaurant
        BitmapDescriptor orangeIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
        for (int i = 0; i < restaurantPositions.size(); i++) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(restaurantPositions.get(i))
                    .icon(orangeIcon)
                    .title(restaurantNames.get(i)));
            restaurantMarkers.add(marker);
        }

        // Move the camera to focus on the restaurants
        LatLng cannes = new LatLng(43.5523, 7.0174);
        float zoomLevel = 13.0f; // This zoom level should be adjusted based on the distance between the restaurants
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cannes, zoomLevel));
    }
}

