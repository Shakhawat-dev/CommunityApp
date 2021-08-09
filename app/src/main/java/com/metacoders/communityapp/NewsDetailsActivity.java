package com.metacoders.communityapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.metacoders.communityapp.models.newModels.Post;

public class NewsDetailsActivity extends AppCompatActivity {
    ImageView playerView;
    SimpleExoPlayer player;
    boolean isPLaying = false;
    ImageView reportBtn;

    String LINK, ID, TITILE, category;
    boolean fullscreen = false;
    ImageView fullscreenButton;
    Dialog mFullScreenDialog;
    Post.PostModel post;
    private boolean mExoPlayerFullscreen = false;
    private TextView mMediaTitle, mMediaDate, mMediaViews, mMediaComments, mMediaDetails;
    private Button mMediaAllComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Intent o = getIntent();

        TextView textView = findViewById(R.id.titleTV);
        mMediaTitle = (TextView) findViewById(R.id.media_title);
        mMediaDate = (TextView) findViewById(R.id.media_date);
        mMediaViews = (TextView) findViewById(R.id.media_views);
        mMediaComments = (TextView) findViewById(R.id.media_comments);
        mMediaDetails = (TextView) findViewById(R.id.media_details);

        reportBtn = findViewById(R.id.reportImage);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);

        findViewById(R.id.media_see_all_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailsActivity.this, CommentsActivity.class);
                intent.putExtra("POST_ID", post.getId());
                startActivity(intent);
            }
        });

        post = (Post.PostModel) o.getSerializableExtra("POST");


        setDetails();
    }

    private void setDetails() {
        mMediaTitle.setText(post.getTitle() + "");
        mMediaDate.setText(post.getCreated_at() + "");
        mMediaViews.setText(post.getHit() + "");
        mMediaComments.setText("0");
        mMediaDetails.setText(post.getDescription() + "");
        String imageLink = "";
        if (post.getImage() == null || post.getImage().isEmpty()) {
            imageLink = post.getThumb() + "";
        } else imageLink = post.getImage() + "";
        Glide.with(getApplicationContext())
                .load(imageLink + "")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(playerView);
    }
}