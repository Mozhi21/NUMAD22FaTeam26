package edu.northeastern.numad22fateam26.finalProject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.fragments.Recommendation;

public class ViewStoryActivity extends AppCompatActivity {

    public static final String VIDEO_URL_KEY = "videoURL";
    public static final String FILE_TYPE = "file type";

    PlayerView exoPlayer;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        init();

        String url = getIntent().getStringExtra(VIDEO_URL_KEY);

        String type = getIntent().getStringExtra(FILE_TYPE);

        if (url == null || url.isEmpty()) {
            finish();
        }

        if(type.contains("image")){
            imageView.setVisibility(View.VISIBLE);
            exoPlayer.setVisibility(View.GONE);

            Glide.with(getApplicationContext()).load(url).into(imageView);

        }else{

            //video

            exoPlayer.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);


            MediaItem item = MediaItem.fromUri(url);

            SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
            player.setMediaItem(item);

            exoPlayer.setPlayer(player);

            player.play();
        }




    }

    void init() {

        exoPlayer = findViewById(R.id.videoView);
        imageView = findViewById(R.id.imageView);

    }

}