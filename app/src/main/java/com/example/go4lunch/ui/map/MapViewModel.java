package com.example.go4lunch.ui.map;


import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

public class MapViewModel {
    public List<LatLng> getRestaurantPositions() {
        return Arrays.asList(
                new LatLng(43.5512114, 7.0181816),  // Coordinates for restaurant 1
                new LatLng(43.5531088, 7.0187543),  // Coordinates for restaurant 2
                new LatLng(43.5531171, 7.0209806),  // Coordinates for restaurant 3
                new LatLng(43.552602,  7.0197695),  // Coordinates for restaurant 4
                new LatLng(43.5508205, 7.0235123)   // Coordinates for restaurant 5
        );
    }

    public List<String> getRestaurantNames() {
        return Arrays.asList(
                "Fouquet's Cannes",
                "La Cantina Cannes",
                "Le grain de sel",
                "L'épicurieux",
                "Restaurant EricKa"
        );
    }
}
