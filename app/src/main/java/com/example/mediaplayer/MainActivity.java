package com.example.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Button btnPlay, btnPause, btnStop;
    private View outerCircle, middleCircle, innerCircle;
    private LinearLayout soundVisualizer;
    private View bar1, bar2, bar3, bar4, bar5;
    private Animation rotateAnimation, blinkAnimation;
    private Handler handler = new Handler();
    private Runnable visualizerRunnable;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        outerCircle = findViewById(R.id.outerCircle);
        middleCircle = findViewById(R.id.middleCircle);
        innerCircle = findViewById(R.id.innerCircle);
        soundVisualizer = findViewById(R.id.soundVisualizer);

        bar1 = findViewById(R.id.bar1);
        bar2 = findViewById(R.id.bar2);
        bar3 = findViewById(R.id.bar3);
        bar4 = findViewById(R.id.bar4);
        bar5 = findViewById(R.id.bar5);

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

            // Show and start visualizer
            soundVisualizer.setVisibility(View.VISIBLE);
            startVisualizer();

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

            // Stop visualizer
            stopVisualizer();
            soundVisualizer.setVisibility(View.INVISIBLE);

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

            // Stop visualizer
            stopVisualizer();
            soundVisualizer.setVisibility(View.INVISIBLE);

            // Reset to purple after 5 seconds
            handler.postDelayed(() -> resetToDefault(), 5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startVisualizer() {
        stopVisualizer(); // Stop any existing animation

        visualizerRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    animateBar(bar1, 100);
                    animateBar(bar2, 150);
                    animateBar(bar3, 120);
                    animateBar(bar4, 180);
                    animateBar(bar5, 140);

                    handler.postDelayed(this, 200);
                }
            }
        };
        handler.post(visualizerRunnable);
    }

    private void animateBar(View bar, int delay) {
        handler.postDelayed(() -> {
            float scale = 0.3f + random.nextFloat() * 0.7f; // Random scale between 0.3 and 1.0

            ScaleAnimation scaleAnimation = new ScaleAnimation(
                    1f, 1f,                    // X scale (no change)
                    scale, 1f,                 // Y scale (from scale to 1)
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            scaleAnimation.setDuration(300);
            scaleAnimation.setFillAfter(true);

            bar.startAnimation(scaleAnimation);
        }, delay);
    }

    private void stopVisualizer() {
        if (visualizerRunnable != null) {
            handler.removeCallbacks(visualizerRunnable);
            visualizerRunnable = null;
        }

        // Clear animations from all bars
        bar1.clearAnimation();
        bar2.clearAnimation();
        bar3.clearAnimation();
        bar4.clearAnimation();
        bar5.clearAnimation();
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
        stopVisualizer();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}