package com.design.shadowingjapanese;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class LevelActivity extends AppCompatActivity {
    private Button btnLevel1, btnLevel2, btnCollection, btnShare, btnMoreApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        AdView mAdView = findViewById(R.id.ad_level);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AnhXa();

        btnLevel1.setOnClickListener(v -> {
            if (isConnect(LevelActivity.this)) {
                showInternetDialog();
            }else{

            Intent intent = new Intent(LevelActivity.this, ListContentActivity.class);
            intent.putExtra("levelPos", 0);
            startActivity(intent);
            }
        });
        btnLevel2.setOnClickListener(v -> {
            if (isConnect(LevelActivity.this)) {
                showInternetDialog();
            }else{
            Intent intent = new Intent(LevelActivity.this, ListContentActivity.class);
            intent.putExtra("levelPos", 1);
            startActivity(intent);}
        });
        btnCollection.setOnClickListener(v -> {
            if (isConnect(LevelActivity.this)) {
                showInternetDialog();
            }else{
            Intent intent = new Intent(LevelActivity.this, ListContentActivity.class);
            intent.putExtra("levelPos", 2);
            startActivity(intent);}
        });
        btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            final String appPackName = getApplicationContext().getPackageName();
            String strAppLink = "";
            try {
                strAppLink = "https://play.google.com/store/apps/details?id=" + appPackName;
            } catch (android.content.ActivityNotFoundException e) {
                strAppLink = "https://play.google.com/store/apps/details?id=" + appPackName;
            }
            shareIntent.setType("text/link");
            String shareBody = "I'm learning Japanese with this app and I would like to introduce you to use:" + "\n" + "" + strAppLink;
            String shareSub = "Shadowing Japanese - Hanasou";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share Using"));
        });
        btnMoreApp.setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Fortuna+Studios")));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });
    }

    private void AnhXa() {
        btnLevel1 = findViewById(R.id.btn_level1);
        btnLevel2 = findViewById(R.id.btn_level2);
        btnCollection = findViewById(R.id.btn_collection);
        btnShare = findViewById(R.id.btn_share);
        btnMoreApp = findViewById(R.id.btn_moreapp);
    }

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please connect to the Internet")
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private boolean isConnect(LevelActivity levelActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) levelActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiCo = connectivityManager.getActiveNetworkInfo();
        if (wifiCo != null) {
            // connected to the internet
            if (wifiCo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
            } else if (wifiCo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
            }
            return false;
        } else {
            // not connected to the internet
            return true;
        }

    }

}