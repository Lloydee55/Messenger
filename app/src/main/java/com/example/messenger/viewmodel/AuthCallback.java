package com.example.messenger.viewmodel;

public interface AuthCallback {
    void onSuccess();
    void onError(String message);
}
