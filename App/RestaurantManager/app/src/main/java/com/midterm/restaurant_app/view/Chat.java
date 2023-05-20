package com.midterm.restaurant_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.viewmodel.adapter.ChatAdapter;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    ArrayList<String> messageList;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private FloatingActionButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageList = new ArrayList<>();
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(adapter);

        messageList.add("Xuan Hung");
        messageList.add("Ngu ngoc");
        adapter.notifyDataSetChanged();

        backButton = findViewById(R.id.btnBackDetailMessenger);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Chat.this, ChatMain.class);
                startActivity(intent);
            }
        });
        }
}
