package com.example.go4lunch.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.FragmentMapBinding;
import com.example.go4lunch.ui.map.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private FragmentMapBinding binding;
    private GoogleMap mMap;

    private List<Marker> restaurantMarkers = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialization of the Map
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Cannes and move the camera
        LatLng cannes = new LatLng(43.5523, 7.0174);
        mMap.addMarker(new MarkerOptions().position(cannes).title("Marker in Cannes"));

        // Adjust this value to control the level of zoom and show a larger area
        float zoomLevel = 18.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cannes, zoomLevel));

        // Sample restaurant positions
        List<LatLng> restaurantPositions = getSampleRestaurantPositions();

        // Loop through the restaurant positions and add markers for them
        for (LatLng position : restaurantPositions) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(position).title("Restaurant Marker"));
            restaurantMarkers.add(marker);
        }
    }

    // A method to return sample restaurant positions (Replace this with your actual data)
    private List<LatLng> getSampleRestaurantPositions() {
        List<LatLng> restaurantPositions = new ArrayList<>();

        // Add sample restaurant positions (you can replace these with your actual data)
        restaurantPositions.add(new LatLng(43.5560, 7.0150)); // Restaurant 1
        restaurantPositions.add(new LatLng(43.5500, 7.0200)); // Restaurant 2
        restaurantPositions.add(new LatLng(43.5530, 7.0180)); // Restaurant 3

        return restaurantPositions;
    }

    // Method to remove all restaurant markers from the map
    private void removeRestaurantMarkers() {
        for (Marker marker : restaurantMarkers) {
            marker.remove();
        }
        restaurantMarkers.clear();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }
}
