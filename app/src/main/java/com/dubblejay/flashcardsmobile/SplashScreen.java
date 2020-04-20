package com.dubblejay.flashcardsmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        ImageView appIcon = findViewById(R.id.appIcon);

        Animation splashScreenFromTop = AnimationUtils.loadAnimation(this, R.anim.splash_screen_from_top);

        appIcon.setAnimation(splashScreenFromTop);

        TextView flashCardsTextView = findViewById(R.id.flashcardsTextView);

        Animation splashScreenFromBottom = AnimationUtils.loadAnimation(this, R.anim.splash_screen_from_bottom);

        flashCardsTextView.setAnimation(splashScreenFromBottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }

        }, 2500);
    }
}
