package edu.northeastern.numad22fateam26;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupHyperlink();
    }

    public void back(View view) {
        AboutActivity.super.onBackPressed();
    }

    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.xinyigitlink);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView jingTextView = findViewById(R.id.jing_contact);
        jingTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}