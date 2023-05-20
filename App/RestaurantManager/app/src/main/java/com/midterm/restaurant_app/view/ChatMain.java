package com.midterm.restaurant_app.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.model.Conversation;
import com.midterm.restaurant_app.viewmodel.adapter.ChatMainAdapter;

import java.util.ArrayList;

public class ChatMain extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton btnBack;

    ArrayList<Conversation> chatItemList;

    private ChatMainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        recyclerView = findViewById(R.id.chat_main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatItemList = new ArrayList<Conversation>();
        adapter = new ChatMainAdapter(chatItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnBack = findViewById(R.id.btnBackMessager);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMain.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}