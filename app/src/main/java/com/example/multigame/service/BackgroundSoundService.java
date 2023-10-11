package com.example.multigame.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.multigame.R;

public class BackgroundSoundService extends Service {

    private String TAG = "BackgroundSoundService";
    MediaPlayer mediaPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(BackgroundSoundService.this, R.raw.sound_trackss);
        mediaPlayer.setLooping(true);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        Log.d(TAG, "Media Palyer started");
        if (mediaPlayer.isLooping() != true) {
            Log.d(TAG, "Problem in Playing Audio");
        }
        return START_STICKY;
    }

    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
