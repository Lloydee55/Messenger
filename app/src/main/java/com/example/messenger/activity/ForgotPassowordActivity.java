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

import com.example.messenger.viewmodel.ForgotPasswordViewModel;
import com.example.messenger.R;

public class ForgotPassowordActivity extends AppCompatActivity {

    EditText emailEditText;
    Button forgotPasswordButton;
    ForgotPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_passoword);
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
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        forgotPasswordButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            viewModel.resetPassword(email);
            viewModel.getActivityStartVerification().observe(this, aBoolean -> {
                if(aBoolean) startActivity(LoginActivity.newIntent(this));
            });
            viewModel.getErrorMessage().observe(this, error -> {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            });
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ForgotPassowordActivity.class);
    }

    private void initViews(){
        emailEditText = findViewById(R.id.forPasEmailEditText);
        forgotPasswordButton =  findViewById(R.id.forgotPasswordButton);
    }
}