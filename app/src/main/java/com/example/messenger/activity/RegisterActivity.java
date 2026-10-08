package com.example.messenger.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.R;
import com.example.messenger.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {


    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nameEditText;
    private EditText lastNameEditText;
    private Button registerButton;

    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());

            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    imeInsets.bottom
            );
            return insets;
        });

        initViews();
        observeViewModel();
        setupListeners();
    }

    private void initViews() {
        emailEditText = findViewById(R.id.registerEmailEditText);
        passwordEditText = findViewById(R.id.registerPasswordEditText);
        nameEditText = findViewById(R.id.registerNameEditText);
        lastNameEditText = findViewById(R.id.registerLastNameEditText);
        registerButton = findViewById(R.id.registerButton);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    private void observeViewModel() {

        viewModel.getRegisterState().observe(this, state -> {
            if (state == RegisterViewModel.RegisterState.SUCCESS) {
                startActivity(MainActivity.newIntent(this));
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> {

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();

            boolean isValid = true;

            if (email.isEmpty()) {
                emailEditText.setError("Enter email");
                isValid = false;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Enter password");
                isValid = false;
            }

            if (name.isEmpty()) {
                nameEditText.setError("Enter name");
                isValid = false;
            }

            if (lastName.isEmpty()) {
                lastNameEditText.setError("Enter last name");
                isValid = false;
            }

            if (!isValid) {
                Toast.makeText(this, "Empty field", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.register(
                    email,
                    password,
                    name,
                    lastName
            );
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
}