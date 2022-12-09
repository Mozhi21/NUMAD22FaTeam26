package edu.northeastern.numad22fateam26.finalProject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.northeastern.numad22fateam26.R;

public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView logo;
    TextView slogan;
    SharedPreferences onBoardingScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        logo = findViewById(R.id.logo);
        slogan = findViewById(R.id.slogan);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //set animations
        logo.setAnimation(topAnim);
        slogan.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);


//                if (user == null || isFirstTime) {
//                    SharedPreferences.Editor editor = onBoardingScreen.edit();
//                    editor.putBoolean("firstTime", false);
//                    editor.commit();
//
//                    Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
//                    startActivity(intent);
//                    finish();
                    if (user == null) {
                        startActivity(new Intent(SplashActivity.this, ReplacerActivity.class));

                    } else {
                        startActivity(new Intent(SplashActivity.this, ExploreActivity.class));

                    }
//                }else {
//                    startActivity(new Intent(SplashActivity.this, ExploreActivity.class));
//                }
                finish();
            }
        }, 2500);


    }
}