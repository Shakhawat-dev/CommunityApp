package com.metacoders.communityapp.activities.details;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.auther.AuthorPageActivity;
import com.metacoders.communityapp.activities.comments.CommentsActivity;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.models.newModels.SinglePostResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.ConvertTime;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView mMediaTitle, mMediaDate, mMediaViews, mMediaComments, mMediaDetails, authorTv;
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
        authorTv = findViewById(R.id.author);

        reportBtn = findViewById(R.id.reportImage);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);

        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Post Details");
        } catch (Exception e) {

        }

        setUpIcon();

        findViewById(R.id.media_see_all_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailsActivity.this, CommentsActivity.class);
                intent.putExtra("POST_ID", post.getId());
                intent.putExtra("slug", post.getSlug());
                startActivity(intent);
            }
        });

        post = (Post.PostModel) o.getSerializableExtra("POST");

        authorTv.setOnClickListener(v -> {

            Intent p = new Intent(getApplicationContext(), AuthorPageActivity.class);
            p.putExtra("author_id", post.getUser_id());
            startActivity(p);
        });


        findViewById(R.id.shareIcon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, "Check This From NewsRme");
                share.putExtra(Intent.EXTRA_TEXT, "" + AppPreferences.postLinkBUilder(post.getSlug(), post.getLang()));
                startActivity(Intent.createChooser(share, "Share link!"));

            }
        });


        setDetails();
    }

    private void setUpIcon() {
        SparkButton button = findViewById(R.id.spark_button);


        button.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

//                if (buttonState) {
//                    // Button is active
//                } else {
//                    // Button is inactive
//                }
                createLike();
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
    }


    private void createLike() {

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<LoginResponse.forgetPassResponse> callwd = api.likePost(
                post.getId(),
                post.getUser_id(),
                post.getId()
        );

        callwd.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Message : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Error : " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void setDetails() {
        mMediaTitle.setText(post.getTitle() + "");
        SimpleDateFormat df = new SimpleDateFormat(Constants.CREATED_AT_FORMAT);
        try {
            Date date = df.parse(post.getCreated_at());
            mMediaDate.setText(ConvertTime.getTimeAgo(date));
        } catch (ParseException e) {
            e.printStackTrace();
            mMediaDate.setText(post.getCreated_at() + "");
        }

        mMediaViews.setText(post.getHit() + "");
        mMediaComments.setText("0");
        if (post.getDescription() == null || post.getDescription().isEmpty()) {
            mMediaDetails.setText("No Description");
        } else {
            mMediaDetails.setText(post.getDescription() + "");
        }
        authorTv.setText(post.getName() + "");

        String imageLink = "";
        if (post.getImage() == null || post.getImage().isEmpty()) {
            imageLink = post.getThumb() + "";
        } else imageLink = post.getImage() + "";
        Glide.with(getApplicationContext())
                .load(imageLink + "")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(playerView);
    }

    private void loadPostDetails(String slug) {

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

        Call<SinglePostResponse> NetworkCall = api.getSinglePost(slug);

        NetworkCall.enqueue(new Callback<SinglePostResponse>() {
            @Override
            public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {

                if (response.isSuccessful()) {
                    SinglePostResponse res = response.body();

                    Toast.makeText(getApplicationContext() , "R -> " + res.getPostLikesCheck() , Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onResponse: " + SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

                } else {

                }

            }

            @Override
            public void onFailure(Call<SinglePostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPostDetails(post.getSlug());
    }
}