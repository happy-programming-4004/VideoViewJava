package com.hjy.videoview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    int savePosition = 0;
    VideoView videoView;
    ProgressBar progressBar;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);

        /*视频源*/
        String vv = "https://ugcsjy.qq.com/uwMROfz2r5zIIaQXGdGnC2dfDmb_xYKxrIGz_bGUg2Lja6ru/l3014b4yyd9.mp4?sdtfrom=v1010&guid=f722f8242d58af35edb97b40ad46c093&vkey=E6B3AD41A10E48ED1A024A810D62E593EB288EADA0C7994D6E706F3031FD29C7F0E3BFD7AAF78CCAA4CA92AFA35615CE87B69829006FE973E26A43EE862EC8B0CF07611C9B6E7D1988ADB30997E7BEB88499ED8B93256D4728F854AE817AB5F903E62036320082480C7AD4993F9E425AA58122FEA8EC780A7BFC424E04E8F995";
//        String videoPath = "android.resource://"+getPackageName()+"/"+R.raw.video1;
        String videoPath = vv+"";
//        videoView.setVideoPath(videoPath);

        videoView.setVideoURI(Uri.parse(videoPath));

        /*视频控制器*/
//        videoView.setMediaController(new MediaController(this));

        /*视频监听*/
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                String TAG = "myLog";
                Log.e(TAG, "onPrepared: "+String.valueOf(mediaPlayer.toString()));
                progressBar2.setVisibility(View.INVISIBLE);
                progressBar.setMax(mediaPlayer.getDuration());

                mediaPlayer.setLooping(true);
                /*setSpeed 倍速 setPitch 音高*/
                mediaPlayer.setPlaybackParams(new PlaybackParams().setSpeed(1.0f).setPitch(1.0f));
                mediaPlayer.seekTo(savePosition);
                mediaPlayer.start();
            }
        });
        /*视频结束时 做一些处理*/
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
        /*视频错误 如给了一个错误的地址、网络视频不存在*/
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });
//        videoView.seekTo(3000);
//        videoView.start();

        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        // send a broadcast to the widget.
                        if (videoView.isPlaying()){
                            progressBar.setProgress(videoView.getCurrentPosition());
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 2000); // Executes the task every 5 seconds.

    }

    @Override
    protected void onPause() {
        super.onPause();
        savePosition = videoView.getCurrentPosition();
    }
}