package com.example.android.conde.com.musicmachine;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String KEY_SONG = "song";
    //Checks whether the binding was successful or not
    private boolean mBound = false;
    private Button mPlayButton;
    private PlayerService mPlayerService;
    //Tells us when we've been successfully connected and when we've benn disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mBound = true;
            PlayerService.LocalBinder localBinder = (PlayerService.LocalBinder) binder;
            mPlayerService = localBinder.getService();
            if(mPlayerService.isPlaying()){
                mPlayButton.setText("pause");
            }
        }

        //Called when something unexpected happens that results in disconnection
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayButton = findViewById(R.id.playPauseButton);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBound){
                    if(mPlayerService.isPlaying()){
                        mPlayerService.pause();
                        mPlayButton.setText("play");
                    }else {
                        Intent intent = new Intent(MainActivity.this,
                                PlayerService.class);
                        startService(intent);
                        mPlayerService.play();
                        mPlayButton.setText("pause");
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PlayerService.class);
        //Service gets created automatically when bind to it
        bindService(intent,mServiceConnection , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBound){
        unbindService(mServiceConnection);
        mBound = false;
        }
    }
}
