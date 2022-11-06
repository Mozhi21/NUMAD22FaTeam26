package edu.northeastern.numad22fateam26;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SplashScreenActivity extends AppCompatActivity {
    LottieAnimationView lottie;
    TextView groupInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // pumpkin man animation
        lottie = findViewById(R.id.lottieAnimationView);
        lottie.animate().translationX(3000).setDuration(8000).setStartDelay(1000);

        // team members' name fade in animation
        groupInfo = findViewById(R.id.groupInfo);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setStartOffset(2000);
        fadeIn.setDuration(1000);
        fadeIn.setFillAfter(true);
        groupInfo.startAnimation(fadeIn);

        // override the fadein animation END action.
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // wait 2 seconds after animation end before jumping into main activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}