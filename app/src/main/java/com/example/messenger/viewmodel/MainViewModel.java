package com.example.messenger.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.messenger.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private MutableLiveData<List<User>> users = new MutableLiveData<>();
    private MutableLiveData<User> loginUser = new MutableLiveData<>();

    public LiveData<User> getLoginUser() {
        return loginUser;
    }
    public LiveData<List<User>> getUsers() {
        return users;
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        displayingUsers();
    }

    public void displayingUsers(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser == null)
                    return;
                List<User> userFromDb = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user == null)
                        return;
                    if(!user.getId().equals(currentUser.getUid()))
                        userFromDb.add(user);
                    else loginUser.setValue(user);
                }
                users.setValue(userFromDb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MainViewModel", "Failed to read value.", error.toException());
            }
        });
    }

    public void setUserOnline(boolean isOnline){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null)
            return;
        databaseReference.child(firebaseUser.getUid()).child("online").setValue(isOnline);
    }

    public FirebaseUser currentUser(){
        return mAuth.getCurrentUser();
    }

    public void logout(){
        mAuth.signOut();
    }
}
