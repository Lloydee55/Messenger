package com.example.messenger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> activityStartVerification = new MutableLiveData<>();
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Boolean> getActivityStartVerification() {
        return activityStartVerification;
    }

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public void resetPassword(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(success -> {
                    activityStartVerification.setValue(true);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue(e.getMessage());
                });
    }
}
