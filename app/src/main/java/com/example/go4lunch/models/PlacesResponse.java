package com.example.go4lunch.models;

import java.util.List;

public class PlacesResponse {
    public List<Restaurant> results;

    public static class Restaurant {
        public String name;
        public String vicinity;
        public float rating;
        // ... d'autres champs si nécessaire
    }
}
