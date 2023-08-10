package com.example.go4lunch.ui.list;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.models.PlacesResponse;
import com.example.go4lunch.network.PlacesAPI;
import com.example.go4lunch.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {

    // Utilisez MutableLiveData pour que vous puissiez observer les changements depuis le fragment
    private final MutableLiveData<List<String>> restaurantDetails = new MutableLiveData<>();

    public MutableLiveData<List<String>> getRestaurantDetails() {
        loadRestaurants();  // Charger les données depuis l'API lorsque cette méthode est appelée
        return restaurantDetails;
    }

    private void loadRestaurants() {
        PlacesAPI placesAPI = RetrofitClient.getPlacesAPI();
        Call<PlacesResponse> call = placesAPI.getNearbyRestaurants("43.5510,7.0216", 1000, "restaurant", "AIzaSyDpUG1OyllltIlVnKCodnRGViiokJt8m34");

        call.enqueue(new Callback<PlacesResponse>() {
            @Override
            public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PlacesResponse.Restaurant> restaurants = response.body().results;
                    List<String> detailsList = new ArrayList<>();

                    for (PlacesResponse.Restaurant restaurant : restaurants) {
                        String details = restaurant.name + " - " + restaurant.vicinity +
                                ", " + restaurant.rating + " étoiles, Ferme à ??"; // vous devrez ajuster l'heure de fermeture
                        detailsList.add(details);
                    }

                    restaurantDetails.setValue(detailsList);  // Mettre à jour le MutableLiveData avec les nouvelles données
                }
            }

            @Override
            public void onFailure(Call<PlacesResponse> call, Throwable t) {
                // Gérez les erreurs ici. Pour l'instant, nous allons simplement définir une liste vide en cas d'erreur.
                restaurantDetails.setValue(new ArrayList<>());
            }
        });
    }
}
