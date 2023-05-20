package com.midterm.restaurant_app;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.midterm.restaurant_app.view.SignIn;
import com.midterm.restaurant_app.view.SignUp;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Button button_Signin = findViewById(R.id.btn_login);
        button_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, SignIn.class);
                startActivity(intent);
            }
        });

       Button button_Signup = findViewById(R.id.btn_snUp);
        button_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

    }
}