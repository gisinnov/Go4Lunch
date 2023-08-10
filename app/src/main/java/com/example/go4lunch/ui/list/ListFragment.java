package com.example.go4lunch.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;

import java.util.List;

public class ListFragment extends Fragment {

    private ListView listView;
    private ListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = view.findViewById(R.id.restaurant_list);

        // Initialisez le viewModel en utilisant ViewModelProvider
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);

        // Observez les modifications de restaurantDetails
        viewModel.getRestaurantDetails().observe(getViewLifecycleOwner(), this::updateListView);
    }

    private void updateListView(List<String> restaurantDetails) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, restaurantDetails);
        listView.setAdapter(adapter);
    }
}
