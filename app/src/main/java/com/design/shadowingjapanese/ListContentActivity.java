package com.design.shadowingjapanese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListContentActivity extends AppCompatActivity {
    private ArrayList<String> arrayList;
    private ListView lstDanhSach;
    private ArrayAdapter<String> arrayAdapter;
    private int levelPos;
    private float speed = 1.0f;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);

        AdRequest adRequest = new AdRequest.Builder().build();
        //test ca-app-pub-3940256099942544/1033173712
        //live ca-app-pub-2847353116904733/4916526048
        InterstitialAd.load(this, "ca-app-pub-2847353116904733/4916526048", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
        //bieu ngu
        AdView mAdView = findViewById(R.id.ad_listContent);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AnhXa();

        Intent intent = getIntent();
        levelPos = intent.getIntExtra("levelPos", 0);

        arrayList = new ArrayList<>();

        if (levelPos == 0) {
            getSupportActionBar().setTitle("初中級");
            for (int i = 1; i <= 10; i++)
                arrayList.add("Unit 1 - Section " + i);
            for (int i = 1; i <= 9; i++)
                arrayList.add("Unit 2 - Section " + i);
            for (int i = 1; i <= 8; i++)
                arrayList.add("Unit 3 - Section " + i);
            for (int i = 1; i <= 8; i++)
                arrayList.add("Unit 4 - Section " + i);
            for (int i = 1; i <= 7; i++)
                arrayList.add("Unit 5 - Section " + i);
        } else if (levelPos == 1) {
            getSupportActionBar().setTitle("中上級");
            int j = 1;
            for (int i = 1; i <= 62; i++) {
                if (i <= 3)
                    arrayList.add("Unit 1 - Section 1");
                else if (i <= 8)
                    arrayList.add("Unit 1 - Section 2");
                else if (i <= 12)
                    arrayList.add("Unit 2 - Section 1");
                else if (i <= 17)
                    arrayList.add("Unit 2 - Section 2");
                else if (i <= 20)
                    arrayList.add("Unit 3 - Section 1");
                else if (i <= 26)
                    arrayList.add("Unit 3 - Section 2");
                else if (i <= 28)
                    arrayList.add("Unit 4 - Section 1");
                else if (i <= 32)
                    arrayList.add("Unit 4 - Section 2");
                else if (i <= 36)
                    arrayList.add("Unit 5 - Section 1");
                else if (i <= 42)
                    arrayList.add("Unit 5 - Section 2");
                else if (i <= 45)
                    arrayList.add("Unit 6 - Section 1");
                else if (i <= 49)
                    arrayList.add("Unit 6 - Section 2");
                else if (i <= 51)
                    arrayList.add("Unit 7 - Section 1");
                else if (i <= 54)
                    arrayList.add("Unit 7 - Section 2");
                else {
                    arrayList.add("Unit 8 - Section " + j);
                    j++;
                }
            }

    }else if(levelPos ==2)

    {
        getSupportActionBar().setTitle("コレクション");

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("collection_name");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String nameTittle = childDataSnapshot.getValue(String.class);
                    if (nameTittle != null) {
                        arrayList.add(nameTittle);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    arrayAdapter =new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList);
        lstDanhSach.setAdapter(arrayAdapter);
        lstDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener()

    {
        @Override
        public void onItemClick (AdapterView < ? > parent, View view,int position, long id){
        Intent intent = new Intent(ListContentActivity.this, ContentActivity.class);
        intent.putExtra("levelPos", levelPos);
        intent.putExtra("listPos", position);
        intent.putExtra("name", String.valueOf(lstDanhSach.getItemAtPosition(position)));
        intent.putExtra("speed", speed);
        startActivity(intent);

    }
    });
}

    private void AnhXa() {
        lstDanhSach = findViewById(R.id.lstDanhSach);
        Toolbar toolbar = findViewById(R.id.tb_listContent);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_speed, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_speed05:
                speed = 0.5f;
                break;
            case R.id.menu_speed075:
                speed = 0.75f;
                break;
            case R.id.menu_speed1:
                speed = 1.0f;
                break;
            case R.id.menu_speed125:
                speed = 1.25f;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
        super.onBackPressed();
    }
}