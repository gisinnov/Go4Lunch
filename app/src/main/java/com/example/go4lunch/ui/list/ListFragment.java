package com.example.go4lunch.ui.list;

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

public class ListFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ListView listView = root.findViewById(R.id.list_view);

        ListViewModel listViewModel = new ViewModelProvider(this).get(ListViewModel.class);

        listViewModel.getUserList().observe(getViewLifecycleOwner(), userList -> {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userList);
            listView.setAdapter(adapter);
        });

        return root;
    }
}
