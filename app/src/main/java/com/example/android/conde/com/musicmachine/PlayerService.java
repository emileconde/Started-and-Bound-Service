package com.example.android.conde.com.musicmachine;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class PlayerService extends Service {
    private MediaPlayer mPlayer;
    private static final String TAG = "PlayerService";
    private IBinder mIBinder = new LocalBinder();


    @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
        //Stops music when done playing
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopSelf();
            }
        });
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        mPlayer = MediaPlayer.create(this, R.raw.jingle);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        mPlayer.release();
    }

    //Helps facilitate access to playerService in MainActivity
    public class LocalBinder extends Binder {
        public PlayerService getService(){
            return PlayerService.this;
        }
    }


    //CLIENT METHODS

    public boolean isPlaying(){
        return mPlayer.isPlaying();
    }

    public void play(){
        mPlayer.start();
    }

    public void pause(){
        mPlayer.pause();
    }


}
