package com.example.messenger.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.messenger.viewmodel.LoginViewModel;
import com.example.messenger.R;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView forgotPassTextView;
    private TextView registerTextView;
    private Button loginButton;
    LoginViewModel lViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
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

        loginButton.setOnClickListener(v -> {
                    if (emailEditText.getText().toString().trim().isEmpty() ||
                            passwordEditText.getText().toString().trim().isEmpty()) {
                        Toast.makeText(this, "Пустые поля", Toast.LENGTH_SHORT).show();
                    } else {
                        lViewModel.login(
                                emailEditText.getText().toString(),
                                passwordEditText.getText().toString()
                        );
                        lViewModel.getActivityStartVerification().observe(this,
                                aBoolean -> {
                                    if (aBoolean)
                                        startActivity(MainActivity.newIntent(LoginActivity.this));
                                    finish();
                                });
                        lViewModel.getErrorMessage().observe(this, message -> {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
        );
        forgotPassTextView.setOnClickListener(v ->
                startActivity(ForgotPassowordActivity.newIntent(LoginActivity.this))
        );
        registerTextView.setOnClickListener(v -> {

                    startActivity(RegisterActivity.newIntent(LoginActivity.this));
                }
        );
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        forgotPassTextView = findViewById(R.id.forgotPassTextView);
        registerTextView = findViewById(R.id.registerTextView);
        loginButton = findViewById(R.id.loginButton);

        lViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }
}