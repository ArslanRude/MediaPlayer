package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button btnPlay, btnPause, btnStop;
    private View outerCircle, middleCircle, innerCircle;
    private Animation rotateAnimation, blinkAnimation;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        outerCircle = findViewById(R.id.outerCircle);
        middleCircle = findViewById(R.id.middleCircle);
        innerCircle = findViewById(R.id.innerCircle);

        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);

         mediaPlayer = MediaPlayer.create(this, R.raw.sample_music);

        btnPlay.setOnClickListener(v -> playMusic());
        btnPause.setOnClickListener(v -> pauseMusic());
        btnStop.setOnClickListener(v -> stopMusic());
    }
    private void playMusic() {
        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }

            // Change Play button to green
            btnPlay.setBackgroundResource(R.drawable.button_red);

            // Reset other buttons to purple
            btnPause.setBackgroundResource(R.drawable.button_red);
            btnStop.setBackgroundResource(R.drawable.button_red);

            // Change circles to green
            outerCircle.setBackgroundResource(R.drawable.circle_outer_green);
            middleCircle.setBackgroundResource(R.drawable.circle_middle_green);
            innerCircle.setBackgroundResource(R.drawable.circle_inner_green);

            // Clear any existing animations
            innerCircle.clearAnimation();

            // Start rotation animation
            innerCircle.startAnimation(rotateAnimation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void pauseMusic() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }

            // Change Pause button to orange
            btnPause.setBackgroundResource(R.drawable.button_red);

            // Reset other buttons to purple
            btnPlay.setBackgroundResource(R.drawable.button_red);
            btnStop.setBackgroundResource(R.drawable.button_red);

            // Change circles to orange
            outerCircle.setBackgroundResource(R.drawable.circle_outer_orange);
            middleCircle.setBackgroundResource(R.drawable.circle_middle_orange);
            innerCircle.setBackgroundResource(R.drawable.circle_inner_orange);

            // Clear rotation animation
            innerCircle.clearAnimation();

            // Start blink animation
            innerCircle.startAnimation(blinkAnimation);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.prepare(); // Prepare for next play
            }

            // Change Stop button to red
            btnStop.setBackgroundResource(R.drawable.button_red);

            // Reset other buttons to purple
            btnPlay.setBackgroundResource(R.drawable.button_red);
            btnPause.setBackgroundResource(R.drawable.button_red);

            // Change circles to red
            outerCircle.setBackgroundResource(R.drawable.circle_outer_red);
            middleCircle.setBackgroundResource(R.drawable.circle_middle_red);
            innerCircle.setBackgroundResource(R.drawable.circle_inner_red);

            // Clear all animations
            innerCircle.clearAnimation();

            // Reset to purple after 5 seconds
            handler.postDelayed(() -> resetToDefault(), 5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void resetToDefault() {
        // Reset all buttons to purple
        btnPlay.setBackgroundResource(R.drawable.button_red);
        btnPause.setBackgroundResource(R.drawable.button_red);
        btnStop.setBackgroundResource(R.drawable.button_red);

        // Reset circles to purple
        outerCircle.setBackgroundResource(R.drawable.circle_outer);
        middleCircle.setBackgroundResource(R.drawable.circle_middle);
        innerCircle.setBackgroundResource(R.drawable.circle_inner);

        // Clear any animations
        innerCircle.clearAnimation();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}