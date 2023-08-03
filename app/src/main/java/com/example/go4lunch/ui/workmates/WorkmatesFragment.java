package com.example.go4lunch.ui.workmates;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;

public class WorkmatesFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        ListView listView = root.findViewById(R.id.list_view);

        WorkmatesViewModel workmatesViewModel = new ViewModelProvider(this).get(WorkmatesViewModel.class);

        workmatesViewModel.getUserList().observe(getViewLifecycleOwner(), userList -> {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userList);
            listView.setAdapter(adapter);
        });

        return root;
    }
}
