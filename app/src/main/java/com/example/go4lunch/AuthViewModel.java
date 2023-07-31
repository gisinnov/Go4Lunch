package com.example.go4lunch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthViewModel extends ViewModel {

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;
    private final FirebaseStorage storage;
    private final MutableLiveData<Boolean> isRegistrationSuccessful = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isImageUploadSuccessful = new MutableLiveData<>();

    public AuthViewModel() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public LiveData<Boolean> getIsRegistrationSuccessful() {
        return isRegistrationSuccessful;
    }

    public LiveData<Boolean> getIsImageUploadSuccessful() {
        return isImageUploadSuccessful;
    }

    public String getCurrentUserId() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    public void register(String firstName, String lastName, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(firstName + " " + lastName)
                                .build();

                        assert user != null;
                        user.updateProfile(profileUpdates);

                        // Write the data to Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("firstName", firstName);
                        userData.put("lastName", lastName);
                        userData.put("email", email);

                        firestore.collection("users").document(user.getUid())
                                .set(userData)
                                .addOnSuccessListener(documentReference -> {
                                    isRegistrationSuccessful.postValue(true);
                                })
                                .addOnFailureListener(e -> {
                                    isRegistrationSuccessful.postValue(false);
                                });
                    } else {
                        isRegistrationSuccessful.postValue(false);
                    }
                });
    }

    public void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Login successful
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

    public void uploadProfilePicture(Uri imageUri, String userId) {
        StorageReference imageRef = storage.getReference().child("images/" + userId);

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            // Continue with the task to get the download URL
            return imageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                // Put download URL to Firestore
                firestore.collection("users").document(userId)
                        .update("imageUrl", downloadUri.toString())
                        .addOnSuccessListener(aVoid -> isImageUploadSuccessful.postValue(true))
                        .addOnFailureListener(e -> isImageUploadSuccessful.postValue(false));
            } else {
                isImageUploadSuccessful.postValue(false);
            }
        });
    }
}
