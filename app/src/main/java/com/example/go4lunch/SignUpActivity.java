package com.example.go4lunch;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private AuthViewModel viewModel;
    private Uri imageUri;

    private static final int RC_SIGN_UP = 9002;
    private GoogleSignInClient mGoogleSignInClient;

    private void openImageSourceChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = Intent.createChooser(galleryIntent, "Choisissez une source");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooser, PICK_IMAGE_REQUEST);
    }

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                if (isSuccessful) {
                    if (imageUri != null) {
                        String userId = viewModel.getCurrentUserId();
                        viewModel.uploadProfilePicture(imageUri, userId);
                    }
                    Toast.makeText(SignUpActivity.this, "Inscription réussie!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "Échec de l'inscription", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.btnPickImage.setOnClickListener(v -> {
            if (Permissions.hasStoragePermission(this) && Permissions.hasCameraPermission(this)) {
                openImageSourceChooser();
            } else {
                if (!Permissions.hasCameraPermission(this)) {
                    Permissions.requestCameraPermission(this);
                }
                if (!Permissions.hasStoragePermission(this)) {
                    Permissions.requestStoragePermission(this);
                }
            }
        });

        binding.tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Configure Google Sign-Up
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Button listener for Google Sign-Up
        binding.btnGoogleSignUp.setOnClickListener(v -> {
            Intent signUpIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signUpIntent, RC_SIGN_UP);
        });

        viewModel.getIsGoogleSignInSuccessful().observe(this, isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(SignUpActivity.this, "Google Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Google Sign-Up Failed!", Toast.LENGTH_SHORT).show();
            }
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

        // Handle Google Sign-Up Result
        if (requestCode == RC_SIGN_UP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    viewModel.firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Toast.makeText(SignUpActivity.this, "Google Sign-Up Failed!", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                if (imageUri != null) {
                    Picasso.get().load(imageUri).into(binding.profileImage);
                } else if (data.getExtras() != null) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    binding.profileImage.setImageBitmap(imageBitmap);
                }
            }
        }
    }
}
