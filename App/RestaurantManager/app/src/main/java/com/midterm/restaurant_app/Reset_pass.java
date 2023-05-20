package com.midterm.restaurant_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.midterm.restaurant_app.view.SignIn;

public class Reset_pass extends AppCompatActivity {

    private EditText edtForgotEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        Button btnSend = findViewById(R.id.btn_send);
        mAuth = FirebaseAuth.getInstance();

        edtForgotEmail = findViewById(R.id.edt_forgot);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtForgotEmail.getText().toString().equals("")){
                    sendPassThroughtEmail(edtForgotEmail.getText().toString());
                }
                else if(edtForgotEmail.getText().toString().equals("")){
                    Toast toast = Toast.makeText(Reset_pass.this,"Email does not exist !", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        TextView button_txt_snin = findViewById(R.id.textView_SignIn);
        button_txt_snin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Reset_pass.this, SignIn.class);
                startActivity(intent2);
            }
        });
    }

    public void sendPassThroughtEmail(String email){
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast toast = Toast.makeText(Reset_pass.this,"Check email to get new Password !", Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent1 = new Intent(Reset_pass.this, SignIn.class);
                            startActivity(intent1);
                        }
                        else{
                            Toast toast = Toast.makeText(Reset_pass.this,"Email does not exist !", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }
}