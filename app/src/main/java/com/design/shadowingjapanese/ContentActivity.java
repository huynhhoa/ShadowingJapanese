package com.design.shadowingjapanese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class ContentActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private TextView txtGiayFull, txtGiayChay;
    private SeekBar sbTime;
    private ImageButton btnPlay, btnPause;
    private PhotoView imgContent, imgContent2, imgContent3, imgContent4;
    private ProgressBar pbLoading;
    private String pathFile = "";
    private float speed = 1.0f;
    private InterstitialAd mInterstitialAd;
    private boolean isMusicPlaying = false;
    private Intent serviceIntent;
    private boolean mBound = false;
    private BackgroundSoundService backgroundSoundService;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundSoundService.MyServiceBinder myServiceBinder = (BackgroundSoundService.MyServiceBinder) service;
            backgroundSoundService = myServiceBinder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

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
                        //         mInterstitialAd.show(ContentActivity.this);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
        AdView mAdView = findViewById(R.id.adView_Content);
        adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        int levelPos = intent.getIntExtra("levelPos", 0);
        int listPos = intent.getIntExtra("listPos", 0);
        speed = intent.getFloatExtra("speed", 1.0f);
        String namePos = intent.getStringExtra("name");

        AnhXa();
        if (isConnect(ContentActivity.this)) {
            showInternetDialog();
        } else {
            String nameFromFirebase, mp3Firebase, imgFirebase;
            Query myQuery;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            getSupportActionBar().setTitle(namePos);
            if (levelPos == 0) {
                mp3Firebase = "level1/";
                imgFirebase = "level1_img/";
            } else if (levelPos == 1) {
                mp3Firebase = "level2/";
                imgFirebase = "level2_img/";
            } else {
                mp3Firebase = "collection_media/";
                imgFirebase = "collection_img/";
            }

            nameFromFirebase = String.valueOf(listPos + 1);
            myQuery = databaseReference.child(imgFirebase).child(nameFromFirebase + "-1");
            myQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pathFile = snapshot.getValue(String.class);
                    new DownloadImageTask(imgContent).execute(pathFile);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            pathFile = "";
            myQuery = databaseReference.child(imgFirebase).child(nameFromFirebase + "-2");
            myQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pathFile = snapshot.getValue(String.class);
                    new DownloadImageTask(imgContent2).execute(pathFile);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            if ((levelPos == 0 && listPos >= 35) ||
                    (levelPos == 1 && listPos == 53)
            ) {
                myQuery = databaseReference.child(imgFirebase).child(nameFromFirebase + "-3");
                myQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pathFile = snapshot.getValue(String.class);
                        new DownloadImageTask(imgContent3).execute(pathFile);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                pathFile = "";
                myQuery = databaseReference.child(imgFirebase).child(nameFromFirebase + "-4");
                myQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        pathFile = snapshot.getValue(String.class);
                        new DownloadImageTask(imgContent4).execute(pathFile);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            pathFile = "";
            myQuery = databaseReference.child(mp3Firebase).child(nameFromFirebase);
            myQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    pathFile = snapshot.getValue(String.class);
                    mediaOnline(pathFile);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void mediaOnline(String path) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        btnPause.setVisibility(View.INVISIBLE);
        btnPlay.setVisibility(View.VISIBLE);

        if (!isMusicPlaying) {
            pbLoading.setVisibility(View.GONE);
            serviceIntent.putExtra("path", path);
            serviceIntent.putExtra("speed", speed);
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            isMusicPlaying = true;

            btnPause.setVisibility(View.VISIBLE); //   ||
            btnPlay.setVisibility(View.INVISIBLE); //   >
            buttonPause();
            UpdateTime();
            sekBarAction();
        } else {
            stopService(serviceIntent);
            isMusicPlaying = false;
        }
        buttonPlay();
    }

    private void buttonPause() {
        btnPause.setOnClickListener(v -> {
            if (isMusicPlaying && mBound) {
                if (backgroundSoundService.isPlay()) {
                    backgroundSoundService.pause();
                    btnPause.setVisibility(View.INVISIBLE);
                    btnPlay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void buttonPlay() {
        btnPlay.setOnClickListener(v -> {
            if (mBound) {
                if (!backgroundSoundService.isPlay()) {
                    backgroundSoundService.play();
                    btnPause.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void UpdateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMusicPlaying && mBound) {
                    if (backgroundSoundService.isPlay()) {
                        txtGiayFull.setText(getTimeString(backgroundSoundService.duration()));
                        sbTime.setMax((int) backgroundSoundService.duration());
                        txtGiayChay.setText(getTimeString(backgroundSoundService.getCurrentPos()));
                        sbTime.setProgress(backgroundSoundService.getCurrentPos());
                    }
                }
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    private void sekBarAction() {
        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBound)
                    backgroundSoundService.getSeekTo(sbTime.getProgress());
            }
        });
    }

    private void AnhXa() {
        serviceIntent = new Intent(this, BackgroundSoundService.class);
        mediaPlayer = new MediaPlayer();
        imgContent = findViewById(R.id.img_Content);
        imgContent2 = findViewById(R.id.img_Content2);
        imgContent3 = findViewById(R.id.img_Content3);
        imgContent4 = findViewById(R.id.img_Content4);
        txtGiayChay = findViewById(R.id.txtGiayChay);
        txtGiayFull = findViewById(R.id.txtGiayFull);
        sbTime = findViewById(R.id.sbTime);
        btnPause = findViewById(R.id.btnPause);
        btnPause.setVisibility(View.GONE);
        btnPlay = findViewById(R.id.btnPlay);
        pbLoading = findViewById(R.id.pbLoading);
        pbLoading.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.tb_Content);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
    }

    @SuppressLint("DefaultLocale")
    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
        finish();
        return true;
    }

    private boolean isConnect(ContentActivity contentActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) contentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private void showInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please connect to the Internet")
                .setCancelable(true)
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}