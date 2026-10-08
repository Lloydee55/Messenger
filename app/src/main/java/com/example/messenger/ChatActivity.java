package com.example.messenger;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.activity.ProfileActivity;
import com.example.messenger.viewmodel.ChatViewModel;
import com.example.messenger.viewmodel.ChatViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_id";
    private static final String EXTRA_OTHER_USER_ID = "other_id";

    private TextView titleTextView;
    private View onlineStatus;
    private RecyclerView messageRecyclerView;
    private EditText messageEditText;
    private ImageView sendMessageImageView;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    private MessagesAdapter messagesAdapter;

    private User recipientsProfile;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
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
        sendMessageImageView.setOnClickListener(v -> {
            Message message = new Message(
                    messageEditText.getText().toString().trim(),
                    currentUserId,
                    recipientsProfile.getId());
            viewModel.sendMessage(message);
        });
        titleTextView.setOnClickListener(v -> {
            startActivity(ProfileActivity.newIntent(this, recipientsProfile));
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }

    public void initViews(){
        titleTextView = findViewById(R.id.titleTextView);
        onlineStatus = findViewById(R.id.onlineStatus);
        messageEditText = findViewById(R.id.messageEditText);
        sendMessageImageView = findViewById(R.id.sendMessageImageView);

        currentUserId = getIntent().getStringExtra(EXTRA_CURRENT_USER_ID);

        recipientsProfile = (User) getIntent().getSerializableExtra(EXTRA_OTHER_USER_ID);

        if (recipientsProfile != null) {
            viewModelFactory = new ChatViewModelFactory(currentUserId, recipientsProfile.getId());
        }
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);

        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messagesAdapter = new MessagesAdapter(currentUserId);
        messageRecyclerView.setAdapter(messagesAdapter);
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, messages -> {
            messagesAdapter.setMessages(messages);
            if (!messages.isEmpty()) {
                messageRecyclerView.scrollToPosition(messages.size() - 1);
            }
        });
        viewModel.getError().observe(this, error -> {
            if (error != null)
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
        viewModel.getMessageSend().observe(this, sent -> {
            if(sent){
                messageEditText.setText("");
            }
        });
        viewModel.getOtherUser().observe(this, user -> {
            String userInfo = String.format("%s %s", user.getName(), user.getLastname());
            titleTextView.setText(userInfo);
            int drResId;
            if(user.isOnline()) drResId = R.drawable.circle_green;
            else drResId = R.drawable.circle_red;
            Drawable background = ContextCompat.getDrawable(ChatActivity.this, drResId);
            onlineStatus.setBackground(background);
        });
    }

    public static Intent newIntent(Context context, User recipientsProfile, String currentUserId){
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_USER_ID, recipientsProfile);
        return intent;
    }
}