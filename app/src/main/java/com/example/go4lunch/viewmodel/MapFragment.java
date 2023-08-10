package com.example.go4lunch.viewmodel;

import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.go4lunch.Permissions;
import com.example.go4lunch.R;
import com.example.go4lunch.models.PlacesResponse;
import android.Manifest;
import com.example.go4lunch.viewmodel.MapViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private MapViewModel viewModel;
    private GoogleMap mMap;

    public MapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MapViewModel.class);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;
                String userLocation = "43.5528,7.0174";
                LatLng cannesLatLng = new LatLng(43.5528, 7.0174);
                viewModel.fetchNearbyRestaurants(userLocation, 500);
                viewModel.getNearbyRestaurants().observe(getViewLifecycleOwner(), placesResponse -> {
                    if (placesResponse != null) {
                        for (PlacesResponse.Restaurant restaurant : placesResponse.results) {
                            LatLng latLng = new LatLng(restaurant.geometry.location.lat, restaurant.geometry.location.lng);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(restaurant.name));
                        }
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cannesLatLng, 18));
                });

                if (Permissions.hasLocationPermission(getActivity())) {
                    enableMyLocation();
                } else {
                    Permissions.requestLocationPermission(getActivity());
                }
            });
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Permissions.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            }
        }
    }
}
