package com.example.messenger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;


public class LoginViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth;
    private static final String LOG_TAG = "LoginViewModel";

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> activityStartVerification = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Boolean> getActivityStartVerification() {
        return activityStartVerification;
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> activityStartVerification.setValue(true))
                .addOnFailureListener(e -> errorMessage.setValue(e.getMessage()));
    }

}
