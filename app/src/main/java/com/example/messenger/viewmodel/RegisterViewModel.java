package com.example.messenger.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class RegisterViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;

    public enum RegisterState {
        SUCCESS,
        ERROR
    }

    private final MutableLiveData<RegisterState> registerState = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<RegisterState> getRegisterState() {
        return registerState;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        authRepository = new FirebaseAuthRepository();
    }

    public void register(
            String email,
            String password,
            String name,
            String lastName
    ) {
        authRepository.register(
                email,
                password,
                name,
                lastName,
                new AuthCallback() {
                    @Override
                    public void onSuccess() {
                        registerState.postValue(RegisterState.SUCCESS);
                    }

                    @Override
                    public void onError(String message) {
                        errorMessage.postValue(message);
                    }
                }
        );
    }

}

