package com.example.messenger.viewmodel;

import com.example.messenger.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAuthRepository implements AuthRepository{

    private final FirebaseAuth auth;
    private final DatabaseReference usersRef;

    public FirebaseAuthRepository() {
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public void register(
            String email,
            String password,
            String name,
            String lastName,
            AuthCallback callback
    ) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    FirebaseUser firebaseUser = result.getUser();
                    if (firebaseUser == null) {
                        callback.onError("Ошибка авторизации");
                        return;
                    }

                    User user = new User(
                            firebaseUser.getUid(),
                            name,
                            lastName,
                            false
                    );

                    usersRef.child(user.getId())
                            .setValue(user)
                            .addOnSuccessListener(aVoid ->
                                    callback.onSuccess()
                            )
                            .addOnFailureListener(e ->
                                    callback.onError(e.getMessage())
                            );
                })
                .addOnFailureListener(e ->
                        callback.onError(e.getMessage())
                );

    }
}
