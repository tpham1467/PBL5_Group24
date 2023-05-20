package com.midterm.restaurant_app.viewmodel.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.model.Conversation;
import com.midterm.restaurant_app.view.Chat;

import java.util.ArrayList;

public class ChatMainAdapter extends RecyclerView.Adapter<ChatMainAdapter.ChatViewHolder> {
    private ArrayList<Conversation> chatItemsList;

    public ChatMainAdapter(ArrayList<Conversation> chatItemsList) {
        this.chatItemsList = chatItemsList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Conversation chatItem = chatItemsList.get(position);
//        holder.userAvatar.setImageDrawable(chatItem.getAvatar());
//        holder.userName.setText(chatItem.getUserName());
//        holder.latestMessage.setText(chatItem.getLatestMessage());


    }

    @Override
    public int getItemCount() {
        return chatItemsList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public ImageView userAvatar;
        public TextView userName;
        public TextView latestMessage;
        private LinearLayout linearChatItem;
        public ChatViewHolder(View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            latestMessage = itemView.findViewById(R.id.latest_message);
            linearChatItem = itemView.findViewById(R.id.linear_chatItem);
            linearChatItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Chat.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}
