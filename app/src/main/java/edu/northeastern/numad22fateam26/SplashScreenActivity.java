package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

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

    }
}