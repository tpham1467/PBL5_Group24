package com.midterm.restaurant_app.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.midterm.restaurant_app.MainActivity;
import com.midterm.restaurant_app.R;
import com.midterm.restaurant_app.Reset_pass;
import com.midterm.restaurant_app.databinding.ActivitySignInBinding;

public class SignIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edtUser;
    private EditText edtPass;
    private ActivitySignInBinding binding;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);

        edtUser = findViewById(R.id.edt_user);
        edtPass = findViewById(R.id.edt_pass);

        LinearLayout linearLoginByGG = binding.linearSigningg;

        linearLoginByGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInWithGG();
            }
        });

        Button button_Signin = findViewById(R.id.btn_snin);
        button_Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtUser.getText().toString().equals("") && !edtPass.getText().toString().equals("")){
                    login(edtUser.getText().toString(),edtPass.getText().toString());
                }
                else if(edtUser.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SignIn.this,"Enter your user !", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(edtPass.getText().toString().equals("")){
                    Toast toast = Toast.makeText(SignIn.this,"Enter your password !", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        TextView button_txt_snup = findViewById(R.id.tv_SignUp);
        button_txt_snup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToSignUp = new Intent(SignIn.this, SignUp.class);
                startActivity(intentToSignUp);
            }
        });


        TextView button_txt_Forgotpass_snin = findViewById(R.id.textView_Forgotpass_snin);
        button_txt_Forgotpass_snin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToResetPass = new Intent(SignIn.this, Reset_pass.class);
                startActivity(intentToResetPass);
            }
        });

    }

    private void SignInWithGG(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                HomeFragment();
            } catch (ApiException e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void HomeFragment(){
        finish();
        Intent intentToHome = new Intent(SignIn.this, MainActivity.class);
        intentToHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentToHome);
    }

    private void login(String email, String pass){
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intentToHome = new Intent(SignIn.this, MainActivity.class);
                            intentToHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intentToHome.putExtra("GMAIL", email);
                            intentToHome.putExtra("Pass", binding.edtPass.getText().toString());
                            startActivity(intentToHome);
                        }
                        else{
                            Toast toast = Toast.makeText(SignIn.this,"User or Password error", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }
}