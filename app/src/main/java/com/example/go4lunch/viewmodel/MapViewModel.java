package com.example.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.go4lunch.models.PlacesResponse;
import com.example.go4lunch.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends ViewModel {

    private MutableLiveData<PlacesResponse> nearbyRestaurants = new MutableLiveData<>();

    public LiveData<PlacesResponse> getNearbyRestaurants() {
        return nearbyRestaurants;
    }

    public void fetchNearbyRestaurants(String location, int radius) {
        RetrofitClient.getPlacesAPI().getNearbyRestaurants(location, radius, "restaurant", "AIzaSyDpUG1OyllltIlVnKCodnRGViiokJt8m34")
                .enqueue(new Callback<PlacesResponse>() {
                    @Override
                    public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                        if (response.isSuccessful()) {
                            nearbyRestaurants.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<PlacesResponse> call, Throwable t) {
                        // Handle the error
                    }
                });
    }
}
