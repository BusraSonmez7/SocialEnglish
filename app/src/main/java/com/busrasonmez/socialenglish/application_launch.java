package com.busrasonmez.socialenglish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class application_launch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_launch);

        Thread logoanimasyon = new Thread(){
            @Override
            public void run() {
                ImageView logo = findViewById(R.id.logo);
                TextView text = findViewById(R.id.text);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation);
                logo.startAnimation(animation);
                text.startAnimation(animation);
            }
        };
        logoanimasyon.start();

        Thread son = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), user_login.class);
                    startActivity(intent);
                    finish();


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        son.start();

    }
}