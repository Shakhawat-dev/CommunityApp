package com.metacoders.communityapp.activities.details;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
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
import com.metacoders.communityapp.utils.PlayerManager;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostDetailsPage extends AppCompatActivity implements CallBacks.playerCallBack {
    public static String LIVETIVELINK = "https://newsrme.s3.ap-southeast-1.amazonaws.com/frontend/video/hls/7xtvXeoDsBi42AH1631677319.m3u8";
    Boolean isFollowed = false;
    PlayerView playerView;
    SimpleExoPlayer player;
    boolean isPLaying = false;
    ImageView reportBtn;
    String LINK, ID, TITILE, category;
    boolean fullscreen = false;
    ImageView fullscreenButton;
    Dialog mFullScreenDialog;
    SparkButton sparkButton;
    Post.PostModel post;
    TextView like_count;
    RelativeLayout loadingPanel;
    TextView qualityBtn;
    ShowMoreTextView mMediaDetails;
    private boolean mExoPlayerFullscreen = false;
    private TextView mMediaTitle, mMediaDate, mMediaViews, mMediaComments, authorTv;
    private Button mMediaAllComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_play_new);
        getSupportActionBar().hide();
        loadingPanel = findViewById(R.id.loadingPanel);
        qualityBtn = findViewById(R.id.qualitu);
        Intent o = getIntent();
        sparkButton = findViewById(R.id.spark_button);
        TextView textView = findViewById(R.id.titleTV);
        like_count = findViewById(R.id.like_count);
        mMediaTitle = (TextView) findViewById(R.id.media_title);
        authorTv = findViewById(R.id.author);


        //   mMediaDate = (TextView) findViewById(R.id.media_date);
        mMediaViews = (TextView) findViewById(R.id.media_views);
        mMediaComments = (TextView) findViewById(R.id.media_comments);
        mMediaDetails = findViewById(R.id.media_details);
        mMediaAllComments = (Button) findViewById(R.id.media_see_all_comments);
        reportBtn = findViewById(R.id.reportImage);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);
        playerView.setUseArtwork(true);
        post = (Post.PostModel) o.getSerializableExtra("POST");
        //Toast.makeText(getApplicationContext() , "p" + post.getId()  , Toast.LENGTH_LONG).show(); ;
        mMediaDetails.setShowingLine(2);
        mMediaDetails.setShowMoreColor(Color.parseColor("#4169E2"));
        mMediaDetails.addShowMoreText("Continue");


        setUpIcon();
        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Post Details");
        } catch (Exception e) {

        }

        findViewById(R.id.msgIcon).setOnClickListener(
                v -> {
                    Intent intent = new Intent(PostDetailsPage.this, CommentsActivity.class);
                    intent.putExtra("POST_ID", post.getId() + "");
                    intent.putExtra("slug", post.getSlug() + "");
                    startActivity(intent);
                });

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        findViewById(R.id.shareIcon).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, "Check This From NewsRme");
            share.putExtra(Intent.EXTRA_TEXT, "" + AppPreferences.postLinkBUilder(post.getSlug(), post.getLang()));
            startActivity(Intent.createChooser(share, "Share link!"));


        });


//        mMediaAllComments.setOnClickListener(view -> {
//
//            Intent intent = new Intent(PostDetailsPage.this, CommentsActivity.class);
//            intent.putExtra("POST_ID", post.getId() + "");
//            intent.putExtra("slug", post.getSlug() + "");
//
//            startActivity(intent);
//        });


        playerView.setPlayer(PlayerManager.getSharedInstance(PostDetailsPage.this).getPlayerView().getPlayer());


        PlayerManager manager = PlayerManager.getSharedInstance(this);
        manager.setPlayerListener(this);

        PlayerControlView playerControlView = findViewById(R.id.CreateHousePlayerView);
        //ProgressBar audioProgressBar = miniPlayerCardView.findViewById(R.id.audioProgressBar);
        playerControlView.setPlayer(PlayerManager.getSharedInstance(this).getPlayerView().getPlayer());

        playerControlView.setProgressUpdateListener((position, bufferedPosition) -> {

            int  currentDuration = (int) position/1000 ;

            if( currentDuration > 0 && currentDuration%6==0){
                callForGift(currentDuration);
            }

            //   int bufferedProgressBarPosition = (int) ((bufferedPosition * 100) / manager.getDuration());
            //  audioProgressBar.setProgress(progressBarPosition);
            //  audioProgressBar.setSecondaryProgress(bufferedProgressBarPosition);

        });


        if (post.getType().equals("audio")) {

            //Toast.makeText(getApplicationContext() , post.getPostType() + "" , Toast.LENGTH_LONG).show();
            // loadAudioDetails(post.getId());
            playMedia(post.getPath());
            //  playHlsVideo();
        } else if (post.getType().equals("video")) {
            try {
                if (post.getHls().toString().isEmpty() || post.getHls().toString().length() < 5) {
                    qualityBtn.setVisibility(View.GONE);
                    playMedia(post.getPath());
                } else {
                    qualityBtn.setVisibility(View.VISIBLE);
                    playMedia(post.getHls());
                }
            } catch (Exception e) {
                qualityBtn.setVisibility(View.GONE);
                playMedia(post.getPath());
            }


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
            p.putExtra("is_followed", isFollowed);
            startActivity(p);


        });

        setDetails();
        loadPostDetails(post.getSlug());

    }

    private void callForGift(int currentSec) {
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Log.d("TAG", "callForGift: calling "  + currentSec);
        Call<JSONObject> NetworkCall = api.givePoint(
                AppPreferences.getUSerID(getApplicationContext()) + "", post.getId(), currentSec
        );

        NetworkCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.code());

                    Gson gson = new Gson();
                    String str = gson.toJson(response.body());
                    Log.d("TAG", "onResponse: " + str);

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });


    }

    private void setDetails() {
        mMediaTitle.setText(post.getTitle() + "");
        SimpleDateFormat df = new SimpleDateFormat(Constants.CREATED_AT_FORMAT);
//        try {
//            Date date = df.parse(post.getCreated_at());
//            mMediaDate.setText(ConvertTime.getTimeAgo(date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//            mMediaDate.setText(post.getCreated_at() + "");
//        }

        mMediaViews.setText(post.getHit() + " Views");
        if (post.getDescription() == null || post.getDescription().isEmpty()) {
            mMediaDetails.setText("No Description");
        } else {
            mMediaDetails.setText(post.getDescription() + "");
        }

        authorTv.setText(post.getName() + "");
        String userID = post.getUser_id() + "";
        if (userID.equals(SharedPrefManager.getInstance(getApplicationContext()).getUser_ID())) {
            authorTv.setText(SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getName() + "");
        }

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

        qualityBtn.setOnClickListener(v -> TriggerDialogue());


        sparkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                createLike(buttonState);
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });
    }

    private void createLike(boolean buttonState) {

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
                    if (response.body().getMessage().toLowerCase().contains("added")) {
                        like_count.setText((Integer.parseInt(like_count.getText().toString()) + 1) + " likes");
                    } else {
                        if ((Integer.parseInt(like_count.getText().toString()) > 0)) {
                            like_count.setText((Integer.parseInt(like_count.getText().toString()) - 1) + " likes");
                        } else {
                            like_count.setText("0 likes");
                        }
                    }

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
        loadingPanel.setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

        Call<SinglePostResponse> NetworkCall = api.getSinglePost(slug);

        NetworkCall.enqueue(new Callback<SinglePostResponse>() {
            @Override
            public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {

                if (response.isSuccessful()) {
                    SinglePostResponse res = response.body();

                    //  Toast.makeText(getApplicationContext(), "R -> " + res.getPostLikesCheck(), Toast.LENGTH_LONG).show();
                    Log.d("TAG", "onResponse: " + SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

                    isFollowed = res.followerCheck != null;
                    like_count.setText(res.getPostLikesCount() + " likes");

                    try {
                        authorTv.setText(response.body().getData().getAuther().getName());
                    } catch (Exception e) {

                    }


                    if (res.postLikesCheck != null) {
                        sparkButton.setChecked(true);
                    } else {
                        sparkButton.setChecked(false);

                    }

                } else {

                }
                loadingPanel.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SinglePostResponse> call, Throwable t) {
                loadingPanel.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public void TriggerDialogue() {
        Dialog dialog = new Dialog(PostDetailsPage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.quality_dialogue);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView autoBtn = dialog.findViewById(R.id.auto);
        TextView highBtn = dialog.findViewById(R.id.high);
        TextView mediumBtn = dialog.findViewById(R.id.medium);
        TextView lowBtn = dialog.findViewById(R.id.low);

        highBtn.setOnClickListener(v -> {
            PlayerManager.getSharedInstance(PostDetailsPage.this).setStreamBitrate(Constants.HIGH);
            qualityBtn.setText("High");
            dialog.dismiss();
        });
        mediumBtn.setOnClickListener(v -> {
            PlayerManager.getSharedInstance(PostDetailsPage.this).setStreamBitrate(Constants.MEDIUM);
            qualityBtn.setText("Med");
            dialog.dismiss();
        });
        lowBtn.setOnClickListener(v -> {
            PlayerManager.getSharedInstance(PostDetailsPage.this).setStreamBitrate(Constants.LOW);
            qualityBtn.setText("Low");
            dialog.dismiss();
        });


        autoBtn.setOnClickListener(v -> {
                    PlayerManager.getSharedInstance(PostDetailsPage.this).setStreamAutoBitrate();
                    qualityBtn.setText("Auto");
                    dialog.dismiss();
                }

        );


        dialog.show();

    }


}