package com.design.shadowingjapanese;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.io.IOException;

public class BackgroundSoundService extends Service {
    //  private static final String TAG = null;
    private MediaPlayer mediaPlayer;
    private float speed = 1.0f;
    private final Binder binder = new MyServiceBinder();
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        String path = intent.getStringExtra("path");
        speed = intent.getFloatExtra("speed", 1.0f);
        mediaPlayer.reset();
        try {
            if(mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepareAsync(); //chuan bi du lieu
                //show progessbar
                mediaPlayer.setOnPreparedListener(mp -> {
                    mp.start();
                    mp.setPlaybackParams(new PlaybackParams().setSpeed(speed));
                    mp.setLooping(true);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer.reset();
        }

    }
    public class MyServiceBinder extends Binder{
        public BackgroundSoundService getService(){

            return BackgroundSoundService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public boolean onUnbind (Intent intent){
        return super.onUnbind(intent);
    }
    //public client method
    public boolean isPlay(){
       return mediaPlayer.isPlaying();
    }
    public void play(){
        mediaPlayer.start();
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public long duration(){
        return mediaPlayer.getDuration();
    }
    public int getCurrentPos(){
        return mediaPlayer.getCurrentPosition();
    }
    public void getSeekTo(int see){
         mediaPlayer.seekTo(see);
    }
}