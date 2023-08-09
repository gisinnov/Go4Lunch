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
import com.example.go4lunch.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new MapViewModel();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Permissions.hasLocationPermission(getActivity())) {
            setupMarkers();
            enableUserLocation();
        } else {
            Permissions.requestLocationPermission(getActivity());
        }
    }

    private void setupMarkers() {
        if (mMap == null) return;

        mMap.clear();

        List<LatLng> positions = viewModel.getRestaurantPositions();
        List<String> names = viewModel.getRestaurantNames();

        for (int i = 0; i < positions.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title(names.get(i)));
        }

        if (!positions.isEmpty()) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positions.get(0), 16.0f));
        }
    }

    private void enableUserLocation() {
        if (mMap == null) return;

        // Afficher la position actuelle de l'utilisateur sur la carte
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permissions.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMarkers();
                enableUserLocation();
            } else {
                // Handle permission denied scenario
                // Vous pouvez ici éventuellement mettre la vue par défaut sur le monde
                if (mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.zoomTo(1.0f)); // Vue globale
                }
            }
        }
    }
}
