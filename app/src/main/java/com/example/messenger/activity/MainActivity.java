package com.example.messenger.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.ChatActivity;
import com.example.messenger.User;
import com.example.messenger.viewmodel.MainViewModel;
import com.example.messenger.R;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    MainViewModel mViewModel;
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private User profileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mViewModel.currentUser() == null){
            mViewModel.logout();
            startActivity(LoginActivity.newIntent(this));
            finish();
        }

        mViewModel.getUsers().observe(this, users -> {
            if(users != null)
                userListAdapter.setUsers(users);
            else Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        });

        mViewModel.getLoginUser().observe(this, user ->
            profileUser = user
        );

        userListAdapter.setOnUserClickListener(user -> {
            Intent intent = ChatActivity.newIntent(MainActivity.this, user, profileUser.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.setUserOnline(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logoutItem) {
            mViewModel.setUserOnline(false);
            mViewModel.logout();
            startActivity(LoginActivity.newIntent(this));
            finish();
            return true;
        }
        if(item.getItemId() == R.id.profileItem){
            if (profileUser != null) {
                startActivity(ProfileActivity.newIntent(this, profileUser));
            } else {
                Toast.makeText(this, "Данные ещё загружаются", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void initViews(){
        recyclerView = findViewById(R.id.usersRecyclerView);
        userListAdapter = new UserListAdapter();
        recyclerView.setAdapter(userListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileUser = new User();

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, MainActivity.class);
    }
}