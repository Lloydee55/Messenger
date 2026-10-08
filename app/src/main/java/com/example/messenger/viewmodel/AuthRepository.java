package com.example.messenger.viewmodel;

public interface AuthRepository {

    void register(
            String email,
            String password,
            String name,
            String lastName,
            AuthCallback callback
    );

}
