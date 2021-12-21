package com.design.shadowingjapanese;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Animation topAnim, bottomAnim;
    private ImageView imgLogo;
    private TextView txtNameApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        AnhXa();
        imgLogo.setAnimation(topAnim);
        txtNameApp.setAnimation(bottomAnim);
        int FLASH_SCREEN = 2400;
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LevelActivity.class);
            startActivity(intent);
            finish();
        }, FLASH_SCREEN);

    }
    private void AnhXa(){
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        txtNameApp = findViewById(R.id.txtNameApp);
        imgLogo = findViewById(R.id.imageView);
    }

}