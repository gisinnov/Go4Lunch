package com.example.go4lunch.ui.user;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.go4lunch.AuthViewModel;
import com.example.go4lunch.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.databinding.ActivityUserBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

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

        // Enable the back button in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Ajouter un écouteur de clic pour le bouton "Enregistrer"
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(view -> saveUserData());
    }


    private void updateUI() {
        FirebaseUser user = viewModel.getCurrentUser();

        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Afficher le nom de l'utilisateur
            TextView nameTextView = findViewById(R.id.user_name);
            nameTextView.setText(name);

            // Afficher l'e-mail de l'utilisateur
            TextView emailTextView = findViewById(R.id.user_email);
            emailTextView.setText(email);

            // Afficher la photo de l'utilisateur
            ImageView profileImageView = findViewById(R.id.user_profile_image);

            // Récupérer l'URL de l'image de Firebase
            String userId = user.getUid();
            DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(userId);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String imageUrl = documentSnapshot.getString("imageUrl");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(UserActivity.this)
                                .load(imageUrl)
                                .placeholder(R.drawable.user_null)
                                .into(profileImageView);
                    } else {
                        Glide.with(UserActivity.this)
                                .load(R.drawable.user_null)
                                .into(profileImageView);
                    }
                }
            });

            // Remplir les champs d'édition avec les valeurs actuelles du nom et du prénom
            EditText editFirstName = findViewById(R.id.edit_first_name);
            EditText editLastName = findViewById(R.id.edit_last_name);
            editFirstName.setText(getFirstName(name));
            editLastName.setText(getLastName(name));
        }
    }


    private String getFirstName(String fullName) {
        String[] nameParts = fullName.split(" ");
        return nameParts.length > 0 ? nameParts[0] : "";
    }

    private String getLastName(String fullName) {
        String[] nameParts = fullName.split(" ");
        return nameParts.length > 1 ? nameParts[1] : "";
    }

    private void saveUserData() {
        FirebaseUser user = viewModel.getCurrentUser();
        if (user != null) {
            // Récupérer les nouvelles valeurs du nom et du prénom à partir des champs d'édition
            EditText editFirstName = findViewById(R.id.edit_first_name);
            EditText editLastName = findViewById(R.id.edit_last_name);
            String firstName = editFirstName.getText().toString();
            String lastName = editLastName.getText().toString();

            // Après avoir mis à jour le profil de l'utilisateur dans la méthode saveUserData
// Envoyez une diffusion pour notifier MainActivity de la mise à jour du profil
            Intent intent = new Intent("com.example.go4lunch.PROFILE_UPDATED");
            sendBroadcast(intent);


            // Mettre à jour le nom d'affichage de l'utilisateur
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(firstName + " " + lastName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Mettre à jour l'interface utilisateur avec les nouvelles valeurs
                            updateUI();
                            showSuccessMessage("Profile updated successfully!");
                        } else {
                            showErrorMessage("Failed to update profile. Please try again.");
                        }
                    });

            // Récupérer le nouveau mot de passe à partir du champ d'édition
            EditText editNewPassword = findViewById(R.id.edit_new_password);
            String newPassword = editNewPassword.getText().toString();

            if (!newPassword.isEmpty()) {
                // Mettre à jour le mot de passe de l'utilisateur
                user.updatePassword(newPassword)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Mot de passe mis à jour avec succès
                                // Vous pouvez afficher un message de succès ici si vous le souhaitez
                                showSuccessMessage("Password updated successfully!");
                            } else {
                                // Erreur lors de la mise à jour du mot de passe
                                // Vous pouvez afficher un message d'erreur ici si vous le souhaitez
                                showErrorMessage("Failed to update password. Please try again.");
                            }
                        });
            }

            // Récupérer la nouvelle adresse e-mail à partir du champ d'édition
            EditText editNewEmail = findViewById(R.id.edit_new_email);
            String newEmail = editNewEmail.getText().toString();

            if (!newEmail.isEmpty()) {
                // Mettre à jour l'adresse e-mail de l'utilisateur
                user.updateEmail(newEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Adresse e-mail mise à jour avec succès
                                // Vous pouvez afficher un message de succès ici si vous le souhaitez
                                showSuccessMessage("Email address updated successfully!");
                            } else {
                                // Erreur lors de la mise à jour de l'adresse e-mail
                                // Vous pouvez afficher un message d'erreur ici si vous le souhaitez
                                showErrorMessage("Failed to update email address. Please try again.");
                            }
                        });
            }
        }
    }


    private void showSuccessMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle back button click event
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Simulate pressing the device's back button
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
