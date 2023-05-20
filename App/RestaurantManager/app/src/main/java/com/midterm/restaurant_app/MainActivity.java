package com.midterm.restaurant_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.restaurant_app.databinding.ActivityMainBinding;
import com.midterm.restaurant_app.model.Account;
import com.midterm.restaurant_app.view.ChatMain;

public class MainActivity extends AppCompatActivity {
    public static String GMAIL;
    public static Account accountSignIn;
    private DrawerLayout drawerLayout;
    private FloatingActionButton flbtnLogout;
    private LinearLayout linearMenu;
    private LinearLayout linearAction;
    private ImageView ivSideMenu;
    private LinearLayout navChat, navMenu;
    private LinearLayout navSer;
    private LinearLayout navHome;
    private LinearLayout navHis;
    private LinearLayout navAccount;
    private LinearLayout linearRequest;
    private ActivityMainBinding mainBinding;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainBinding.getRoot();
        setContentView(view);

        GMAIL = getIntent().getStringExtra("GMAIL");
        String pass = getIntent().getStringExtra("Pass");

        linearMenu = findViewById(R.id.linear_menu);
        linearRequest = findViewById(R.id.linearRequest);
        linearAction = findViewById(R.id.linear_action);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Account");
        MainActivity mainActivity = new MainActivity();
        String gmail = mainActivity.GMAIL;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    accountSignIn = dataSnapshot.getValue(Account.class);
                    if(accountSignIn.getEmail().equals(gmail)){
                        FirebaseDatabase.getInstance().getReference("Account").child(accountSignIn.getIdAcc()).child("password").setValue(pass);
                        if(accountSignIn.getIdRole() == 0){
                            linearAction.removeView(linearMenu);
                            linearAction.removeView(linearRequest);
                        }
                        if(accountSignIn.getUrlAvatar()!= null){
                            ImageView imageView = mainBinding.myImageView;
                            ImageView imageView1 = mainBinding.myAvatar;
                            mainBinding.tvName.setText(accountSignIn.getNameUser());
                            mainBinding.tvGmail.setText(accountSignIn.getEmail());
                            setImageAvatar(imageView);
                            setImageAvatar(imageView1);
                        }
                        break;
                    }
                }
            }
            private void setImageAvatar(ImageView image){
                Glide.with(getApplicationContext())
                        .load(accountSignIn.getUrlAvatar())
                        .centerCrop()
                        .placeholder(R.drawable.initialimage)
                        .into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        linearRequest = mainBinding.linearRequest;
        linearRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
                navController.navigate(R.id.alertRequest);
            }
        });

        navChat = findViewById(R.id.linear_chat);
        navChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, ChatMain.class);
                startActivity(intent);
            }
        });

        navMenu = findViewById(R.id.linear_menu);
        navMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
                navController.navigate(R.id.menuFoodFragment);
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        ivSideMenu = findViewById(R.id.iv_side_menu);
        ivSideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        flbtnLogout = findViewById(R.id.flbtn_logout);
        flbtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        navHome = mainBinding.navHome;
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
                navController.navigate(R.id.homenav);
                setBgTv(0, R.drawable.ic_baseline_home_24,getResources().getColor(R.color.green), R.drawable.border_bottom_green);
                setBgTv(1, R.drawable.ic_baseline_local_grocery_store_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(2, R.drawable.ic_baseline_restore_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(3, R.drawable.ic_baseline_account_circle_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
            }
        });

        navSer = mainBinding.navServe;
        navSer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
                navController.navigate(R.id.serveFragment);
                setBgTv(0, R.drawable.ic_baseline_home_ser,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(1, R.drawable.ic_baseline_local_grocery_store_ser,getResources().getColor(R.color.green), R.drawable.border_bottom_green);
                setBgTv(2, R.drawable.ic_baseline_restore_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(3, R.drawable.ic_baseline_account_circle_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
            }
        });

        navHis = mainBinding.navHis;
        navHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
                navController.navigate(R.id.hisOrderFragment);
                setBgTv(0, R.drawable.ic_baseline_home_ser,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(1, R.drawable.ic_baseline_local_grocery_store_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(2, R.drawable.ic_baseline_restore_his,getResources().getColor(R.color.green), R.drawable.border_bottom_green);
                setBgTv(3, R.drawable.ic_baseline_account_circle_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
            }
        });
        navAccount = mainBinding.navAccount;
        navAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController = Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView);
                navController.navigate(R.id.accountFragment);
                setBgTv(0, R.drawable.ic_baseline_home_ser,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(1, R.drawable.ic_baseline_local_grocery_store_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(2, R.drawable.ic_baseline_restore_24,getResources().getColor(R.color.white),R.drawable.border_bottom_blue);
                setBgTv(3, R.drawable.baseline_account_circle_green,getResources().getColor(R.color.green), R.drawable.border_bottom_green);
            }
        });
    }

    private void setBgTv(int position, int icon, int color, int background) {
        ImageView imageView;
        TextView textView;

        switch (position) {
            case 0:
                imageView = mainBinding.imvHome;
                textView = mainBinding.tvHome;
                break;
            case 1:
                imageView = mainBinding.imvServe;
                textView = mainBinding.tvServe;
                break;
            case 2:
                imageView = mainBinding.imvOrdered;
                textView = mainBinding.tvOrdered;
                break;
            case 3:
                imageView = mainBinding.imvAcc;
                textView = mainBinding.tvAcc;
                break;
            default:
                return;
        }

        imageView.setImageResource(icon);
        textView.setTextColor(color);
        textView.setBackgroundResource(background);
    }

}