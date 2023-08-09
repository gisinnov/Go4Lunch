package com.example.go4lunch.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.FragmentListBinding;
import com.google.android.libraries.places.api.model.Place;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListViewModel listViewModel = new ViewModelProvider(this).get(ListViewModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textList;
        listViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        listViewModel.getCurrentPlace().observe(getViewLifecycleOwner(), place -> {
            // Mettez à jour votre interface utilisateur avec les détails du lieu.
            // Exemple:
            textView.setText(place.getName());
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
