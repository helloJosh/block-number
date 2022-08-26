package com.example.blocknumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

public class MainLobby extends AppCompatActivity {

    private TextView user_name;
    private ImageView user_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lobby);

        user_name= findViewById(R.id.name);
        user_image = findViewById(R.id.imageView);


    }
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.activateBlock:
                intent = new Intent(this, BlockSetting.class);
                startActivity(intent);
                break;
            case R.id.blockUser:
                intent = new Intent(this, BlockUser.class);
                startActivity(intent);
                break;
            case R.id.addDatabase:
                intent = new Intent(this, DBinput.class);
                startActivity(intent);
                break;
            case R.id.logOut:
                intent = new Intent(this,LoginWithGoogle.class);
                startActivity(intent);
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
            case R.id.requestRemoving:
                intent = new Intent(this,NumberRelease.class);
                startActivity(intent);
                break;
            case R.id.admin:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}