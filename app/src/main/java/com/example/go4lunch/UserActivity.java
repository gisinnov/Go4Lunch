package com.example.go4lunch;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.go4lunch.databinding.ActivityUserBinding;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        updateUI();
    }

    private void updateUI() {
        FirebaseUser user = viewModel.getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();

            // Afficher le nom de l'utilisateur
            TextView nameTextView = findViewById(R.id.user_name);
            nameTextView.setText(name);

            // Afficher la photo de l'utilisateur
            ImageView profileImageView = findViewById(R.id.user_profile_image);
            Glide.with(this).load(photoUrl).into(profileImageView);
        }
    }
}
