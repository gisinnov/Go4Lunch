package com.example.go4lunch.models;

import java.util.List;

public class PlacesResponse {
    public List<Restaurant> results;

    public static class Restaurant {
        public String name;
        public String vicinity; // Adresse
        public float rating;
        public String icon; // URL de l'image du restaurant
        public List<String> types; // Types de restaurants (par exemple : "restaurant", "café", etc.)
        public OpeningHours opening_hours;
        public Geometry geometry;  // Ajout de la sous-classe geometry

        public static class OpeningHours {
            public boolean open_now;
            // Si vous voulez les horaires détaillés, vous pouvez ajouter une liste ici.
        }

        // Nouvelle sous-classe pour gérer la géométrie des restaurants
        public static class Geometry {
            public Location location;

            public static class Location {
                public double lat;
                public double lng;
            }
        }
    }
}
