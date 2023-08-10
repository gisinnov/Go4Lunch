package com.example.go4lunch.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.go4lunch.R;
import com.example.go4lunch.viewmodel.ListViewModel;

public class ListFragment extends Fragment {

    private ListViewModel viewModel;
    private RecyclerView recyclerView;  // Assurez-vous d'avoir un RecyclerView dans votre layout

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);  // L'ID doit être défini dans votre layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // TODO: Initialize your RecyclerView adapter here

        // Fetch and observe data
        String userLocation = "43.5528,7.0174";  // Coordinates for Cannes
        viewModel.fetchNearbyRestaurantsDetails(userLocation, 500);  // 5km radius
        viewModel.getNearbyRestaurantsDetails().observe(getViewLifecycleOwner(), placesResponse -> {
            if (placesResponse != null) {
                // TODO: Update your RecyclerView with the list of restaurants
            }
        });
    }
}
