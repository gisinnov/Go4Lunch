package com.example.go4lunch.ui.list;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class ListViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<Place> mCurrentPlace;

    public ListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is list fragment");
    }

    public void initiateFetchPlace() {
        // Assurez-vous que Google Places est déjà initialisé.

        PlacesClient placesClient = Places.createClient(getApplicationContext());

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        FetchPlaceRequest request = FetchPlaceRequest.builder("ChIJn1DQ55t9L48RWnfLcX-DaEw", placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            mCurrentPlace.setValue(response.getPlace());
        }).addOnFailureListener(exception -> {
            // Gérez l'erreur ici
        });
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Place> getCurrentPlace() {
        return mCurrentPlace;
    }
}
