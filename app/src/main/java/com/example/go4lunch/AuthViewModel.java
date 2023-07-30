package com.example.go4lunch;

import androidx.lifecycle.ViewModel;

// Add these import statements
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthViewModel extends ViewModel {

    private FirebaseFirestore firestore;

    public AuthViewModel() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    private final FirebaseAuth auth;


    public void register(String firstName, String lastName, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(firstName + " " + lastName)
                                .build();

                        user.updateProfile(profileUpdates);

                        // Écrivez les données dans Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("firstName", firstName);
                        userData.put("lastName", lastName);
                        userData.put("email", email);

                        firestore.collection("users").document(user.getUid())
                                .set(userData)
                                .addOnSuccessListener(documentReference -> {
                                    // Écriture réussie
                                })
                                .addOnFailureListener(e -> {
                                    // Gestion de l'échec
                                });
                    } else {
                        // Gestion de l'échec
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
