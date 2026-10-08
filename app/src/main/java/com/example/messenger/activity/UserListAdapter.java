package com.example.messenger.activity;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.R;
import com.example.messenger.User;

import java.util.ArrayList;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {

    private List<User> users = new ArrayList<>();
    private OnUserClickListener onUserClickListener;

    public void setOnUserClickListener(OnUserClickListener onUserClickListener) {
        this.onUserClickListener = onUserClickListener;
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_user,
                parent,
                false
        );
        return new UserListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        User user = users.get(position);
        String userInfo = String.format("%s %s", user.getName(), user.getLastname());
        holder.textView.setText(userInfo);

        int drResId;
        if(user.isOnline()) drResId = R.drawable.circle_green;
        else drResId = R.drawable.circle_red;
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), drResId);
        holder.onlineStatus.setBackground(background);
        holder.itemView.setOnClickListener(v -> {
            if(onUserClickListener != null) onUserClickListener.onUserClick(user);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    interface OnUserClickListener{
        void onUserClick(User user);
    }

    protected static class UserListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        View onlineStatus;
        public UserListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.nameUserTextView);
            onlineStatus = itemView.findViewById(R.id.onlineStatus);
        }
    }
}
