package com.metacoders.communityapp.activities.details;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.auther.AuthorPageActivity;
import com.metacoders.communityapp.activities.comments.CommentsActivity;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.SinglePostDetails;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.models.newModels.SinglePostResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.CallBacks;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.ConvertTime;
import com.metacoders.communityapp.utils.PlayerManager;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostDetailsPage extends AppCompatActivity implements CallBacks.playerCallBack {
    Boolean isFollowed = false;
    PlayerView playerView;
    SimpleExoPlayer player;
    boolean isPLaying = false;
    ImageView reportBtn;
    String LINK, ID, TITILE, category;
    boolean fullscreen = false;
    ImageView fullscreenButton;
    Dialog mFullScreenDialog;
    SparkButton sparkButton ;
    Post.PostModel post;
    private boolean mExoPlayerFullscreen = false;
    private TextView mMediaTitle, mMediaDate, mMediaViews, mMediaComments, mMediaDetails, authorTv;
    private Button mMediaAllComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_media_page);
        findViewById(R.id.loadingPanel).findViewById(View.GONE) ;
        Intent o = getIntent();
        sparkButton = findViewById(R.id.spark_button);
        TextView textView = findViewById(R.id.titleTV);
        mMediaTitle = (TextView) findViewById(R.id.media_title);
        authorTv = findViewById(R.id.author);
        mMediaDate = (TextView) findViewById(R.id.media_date);
        mMediaViews = (TextView) findViewById(R.id.media_views);
        mMediaComments = (TextView) findViewById(R.id.media_comments);
        mMediaDetails = (TextView) findViewById(R.id.media_details);
        mMediaAllComments = (Button) findViewById(R.id.media_see_all_comments);
        reportBtn = findViewById(R.id.reportImage);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);
        playerView.setUseArtwork(true);
        post = (Post.PostModel) o.getSerializableExtra("POST");
        //Toast.makeText(getApplicationContext() , "p" + post.getId()  , Toast.LENGTH_LONG).show(); ;

        setUpIcon();
        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Post Details");
        } catch (Exception e) {

        }

        findViewById(R.id.msgIcon).setOnClickListener(
                v -> {
                    Intent intent = new Intent(PostDetailsPage.this, CommentsActivity.class);
                    intent.putExtra("POST_ID", post.getId()+"");
                    intent.putExtra("slug", post.getSlug()+"");
                    startActivity(intent);
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


        mMediaAllComments.setOnClickListener(view -> {

            Intent intent = new Intent(PostDetailsPage.this, CommentsActivity.class);
            intent.putExtra("POST_ID", post.getId()+"");
            intent.putExtra("slug", post.getSlug()+"");

            startActivity(intent);
        });


        playerView.setPlayer(PlayerManager.getSharedInstance(PostDetailsPage.this).getPlayerView().getPlayer());

        PlayerManager.getSharedInstance(this).setPlayerListener(this);

        setDetails();

        if (post.getType().equals("audio")) {

            //Toast.makeText(getApplicationContext() , post.getPostType() + "" , Toast.LENGTH_LONG).show();
            // loadAudioDetails(post.getId());
            playMedia(post.getPath());
        } else if (post.getType().equals("video")) {
            playMedia(post.getPath());

        }
        // play the media

        fullscreenButton.setOnClickListener(v -> {

            if (!mExoPlayerFullscreen) {
                // not in fullscreen

                openFullScreenDialog();


            } else {

                closeFullScreenDialog();

            }


        });

        authorTv.setOnClickListener(v -> {

            Intent p = new Intent(getApplicationContext(), AuthorPageActivity.class);
            p.putExtra("author_id", post.getUser_id());
            p.putExtra("is_followed" , isFollowed);
            startActivity(p);


        });

        loadPostDetails(post.getSlug());

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

        mMediaViews.setText(post.getHit() + " Views");
        if (post.getDescription() == null || post.getDescription().isEmpty()) {
            mMediaDetails.setText("No Description");
        } else {
            mMediaDetails.setText(post.getDescription() + "");
        }

        authorTv.setText(post.getName() + "");

    }


    private void playMedia(String Path) {

        if (PlayerManager.getSharedInstance(PostDetailsPage.this).isPlayerPlaying()) {
            PlayerManager.getSharedInstance(PostDetailsPage.this).stopPlayer();
        }

        try {

            PlayerManager.getSharedInstance(PostDetailsPage.this).playStream(Path);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something Wen Wrong !!!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClickOnItem(Integer albumId) {

    }

    @Override
    public void onPlayingEnd() {

    }

    public void initFullsceen() {

        mFullScreenDialog = new Dialog(PostDetailsPage.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {

            public void onBackPressed() {
                if (mExoPlayerFullscreen) {
                    closeFullScreenDialog();
                }

            }


        };


    }

    private void closeFullScreenDialog() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        ((ViewGroup) playerView.getParent()).removeView(playerView); // removes the player screen


        ((FrameLayout) findViewById(R.id.parent_relative)).addView(playerView);

        mExoPlayerFullscreen = false;

        mFullScreenDialog.dismiss();


        // change the full screen image
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PostDetailsPage.this, R.drawable.full));


    }

    private void openFullScreenDialog() {
        // opening the dialgoue
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) playerView.getParent()).removeView(playerView); // removes the player screen

        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // change the full screen image
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PostDetailsPage.this, R.drawable.full));

        mExoPlayerFullscreen = true;

        mFullScreenDialog.show();


    }

    @Override
    public void onBackPressed() {


        if (PlayerManager.getSharedInstance(PostDetailsPage.this).isPlayerPlaying()) {
            PlayerManager.getSharedInstance(PostDetailsPage.this).stopPlayer();
            PlayerManager.getSharedInstance(PostDetailsPage.this).releasePlayer();

        }
        //   finish();
        super.onBackPressed();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        PlayerManager.getSharedInstance(PostDetailsPage.this).stopPlayer();
        PlayerManager.getSharedInstance(PostDetailsPage.this).releasePlayer();


    }

    @Override
    protected void onPause() {
        super.onPause();

        PlayerManager.getSharedInstance(PostDetailsPage.this).pausePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initFullsceen();
    }

    private void loadAudioDetails(String id) {

//        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext()) ;
//        String   accessTokens = sharedPrefManager.getUserToken();
        //  Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<SinglePostDetails> NetworkCall = api.getPostDetails(
                id
        );


        NetworkCall.enqueue(new Callback<SinglePostDetails>() {
            @Override
            public void onResponse(Call<SinglePostDetails> call, Response<SinglePostDetails> response) {

                if (response.code() == 201) {

                    SinglePostDetails postDetails = response.body();

                    List<SinglePostDetails.GetNewsAudioDetail> data = postDetails.getGetNewsAudioDetails();

                    if (data.size() > 0) {
                        playMedia(data.get(0).getAudioPath());

                    }


                }
            }

            @Override
            public void onFailure(Call<SinglePostDetails> call, Throwable t) {

            }
        });


    }

    private void setUpIcon() {


        sparkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {

                if (buttonState) {
                    // Button is active
                } else {
                    // Button is inactive
                }
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

    private void loadPostDetails(String slug) {
        findViewById(R.id.loadingPanel).findViewById(View.VISIBLE) ;
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

        Call<SinglePostResponse> NetworkCall = api.getSinglePost(slug);

        NetworkCall.enqueue(new Callback<SinglePostResponse>() {
            @Override
            public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {
                findViewById(R.id.loadingPanel).findViewById(View.GONE) ;
                if (response.isSuccessful()) {
                    SinglePostResponse res = response.body();

                  //  Toast.makeText(getApplicationContext(), "R -> " + res.getPostLikesCheck(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onResponse: " + SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

                    isFollowed = res.followerCheck != null;

                    if(res.postLikesCheck != null){
                        sparkButton.setChecked(true);
                    }else {
                        sparkButton.setChecked(false);
                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<SinglePostResponse> call, Throwable t) {
                findViewById(R.id.loadingPanel).findViewById(View.GONE) ;
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}