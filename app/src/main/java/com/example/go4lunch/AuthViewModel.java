package com.example.go4lunch;

import androidx.lifecycle.ViewModel;

// Add these import statements
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthViewModel extends ViewModel {
    private final FirebaseAuth auth;

    public AuthViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public void register(String name, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdates);
                    } else {
                        // Handle failure
                    }
                });
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login success
                    } else {
                        // Handle failure
                    }
                });
    }

    public void resetPassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Email sent
                    } else {
                        // Handle failure
                    }
                });
    }
}
