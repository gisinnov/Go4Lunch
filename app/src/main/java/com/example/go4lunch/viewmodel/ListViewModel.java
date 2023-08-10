package com.example.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.go4lunch.models.PlacesResponse;
import com.example.go4lunch.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewModel extends ViewModel {

    private MutableLiveData<PlacesResponse> nearbyRestaurantsDetails = new MutableLiveData<>();

    public LiveData<PlacesResponse> getNearbyRestaurantsDetails() {
        return nearbyRestaurantsDetails;
    }

    public void fetchNearbyRestaurantsDetails(String location, int radius) {
        RetrofitClient.getPlacesAPI().getNearbyRestaurants(location, radius, "restaurant", "AIzaSyDpUG1OyllltIlVnKCodnRGViiokJt8m34")
                .enqueue(new Callback<PlacesResponse>() {
                    @Override
                    public void onResponse(Call<PlacesResponse> call, Response<PlacesResponse> response) {
                        if (response.isSuccessful()) {
                            nearbyRestaurantsDetails.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<PlacesResponse> call, Throwable t) {
                        // Handle the error
                    }
                });
    }
}
