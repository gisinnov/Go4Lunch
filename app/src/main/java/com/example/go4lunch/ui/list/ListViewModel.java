package com.example.go4lunch.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ListViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<String>> userList;

    public ListViewModel() {
        userList = new MutableLiveData<>();
        getUsers();
    }

    public LiveData<ArrayList<String>> getUserList() {
        return userList;
    }

    private void getUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<String> tempList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> userData = document.getData();
                            String name = userData.get("firstName") + " " + userData.get("lastName");
                            tempList.add(name);
                        }
                        userList.setValue(tempList);
                    } else {
                        // Handle failure
                    }
                });
    }
}
