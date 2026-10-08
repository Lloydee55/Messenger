package com.example.messenger.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.messenger.ExampleBottomSheet;
import com.example.messenger.R;
import com.example.messenger.User;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private boolean buttonState = true;
    private TextView ageTextView;
    private TextView birthdayTextView;
    private TextView cityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
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

        ExampleBottomSheet sheet = new ExampleBottomSheet();

        imageButton = findViewById(R.id.imageButton);
        ageTextView = findViewById(R.id.ageTextView);
        birthdayTextView = findViewById(R.id.birthdayTextView);
        cityTextView = findViewById(R.id.cityTextView);
        User user = (User) getIntent().getSerializableExtra("user");

        TextView textView = findViewById(R.id.profileNameTextView);
        if (user != null)
            textView.setText(String.format("%s %s", user.getName(), user.getLastname()));
        else
            textView.setText("Данных нет");
        imageButton.setOnClickListener(v -> {
            Drawable drawable;
            if(buttonState) {
                drawable = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_save);
                buttonState = false;
                sheet.setListener((name, status, age, birthday, city) -> {
                    textView.setText(name);
                    ageTextView.setText(age);
                    birthdayTextView.setText(birthday);
                    cityTextView.setText(city);
                });
                sheet.show(getSupportFragmentManager(), "edit_profile");
            }
            else {
                drawable = ContextCompat.getDrawable(this, android.R.drawable.ic_menu_edit);
                buttonState = true;

            }
            imageButton.setImageDrawable(drawable);

        });
    }


   /* @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(true);
    }*/

    public static Intent newIntent(Context context, User user){
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("user", user);
        return intent;
    }
}