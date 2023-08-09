package com.example.go4lunch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.ActivitySignUpBinding;
import com.squareup.picasso.Picasso;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private AuthViewModel viewModel;
    private Uri imageUri; // Ajouté pour stocker l'URI de l'image

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cacher l'ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        binding.btnSignUp.setOnClickListener(v -> {
            String firstName = binding.etFirstName.getText().toString().trim();
            String lastName = binding.etLastName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            viewModel.register(firstName, lastName, email, password);
            viewModel.getIsRegistrationSuccessful().observe(this, isSuccessful -> {
                if (isSuccessful && imageUri != null) {
                    String userId = viewModel.getCurrentUserId();
                    viewModel.uploadProfilePicture(imageUri, userId);
                }
            });

            viewModel.getIsImageUploadSuccessful().observe(this, isSuccessful -> {
                if (isSuccessful) {
                    Toast.makeText(SignUpActivity.this, "Image et utilisateur enregistrés avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Échec du téléchargement de l'image", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnPickImage.setOnClickListener(v -> {
            if (Permissions.hasCameraPermission(this)) {
                openFileChooser();
            } else {
                Permissions.requestCameraPermission(this);
            }
        });

        // Set click listener for the "Already have an account?" text view
        binding.tvSignIn.setOnClickListener(v -> {
            // Start LoginActivity
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Enregistrez l'URI de l'image pour une utilisation ultérieure
            Picasso.get().load(imageUri).into(binding.profileImage); // Affichez l'image dans votre ImageView
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Permissions.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }
}