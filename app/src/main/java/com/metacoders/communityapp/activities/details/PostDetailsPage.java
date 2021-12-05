package com.metacoders.communityapp.activities.details;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.gson.Gson;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.auther.AuthorPageActivity;
import com.metacoders.communityapp.activities.comments.CommentsActivity;
import com.metacoders.communityapp.adapter.new_adapter.NextListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.SinglePostDetails;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.models.newModels.SinglePostResponse;
import com.metacoders.communityapp.models.newModels.UserModel;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.CallBacks;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.PlayerManager;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.skyhope.showmoretextview.ShowMoreTextView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostDetailsPage extends AppCompatActivity implements CallBacks.playerCallBack, NextListDifferAdapter.ItemClickListener {
    public static String LIVETIVELINK = "https://newsrme.s3.ap-southeast-1.amazonaws.com/frontend/video/hls/7xtvXeoDsBi42AH1631677319.m3u8";
    Boolean isFollowed = false;
    PlayerView playerView;
    SimpleExoPlayer player;
    int allReadySentTime = 0;
    long time = 0;
    EditText commentEt;
    Boolean isStopped = true;
    NestedScrollView nestedScrollView;
    boolean isPLaying = false;
    UserModel authermodel;
    TextView followersCount, commentCount;
    ImageView reportBtn;
    RecyclerView nextList;
    int currentPage = 1;
    int totalPage = 1;
    ProgressBar progressBar;
    List<Post.PostModel> newsList = new ArrayList<>();
    String LINK, ID, TITILE, category;
    boolean fullscreen = false;
    ImageView fullscreenButton, profile_image;
    Dialog mFullScreenDialog;
    SparkButton sparkButton;
    Post.PostModel posts;
    TextView like_count;
    int prevSec = 0;
    int newSec = 0;
    RelativeLayout loadingPanel;
    TextView qualityBtn;
    ShowMoreTextView mMediaDetails;
    Boolean forcFinisj = false;
    AppCompatButton followBtn;
    ImageButton addComment;
    //   private CountDownTimer downTimer;
    long GlobarTimer = 0;
    NextListDifferAdapter mNextListDifferAdapter;
    List<Post.PostModel> relatedPosts = new ArrayList<>();
    private boolean mExoPlayerFullscreen = false;
    private TextView mMediaTitle, mMediaDate, mMediaViews, mMediaComments, authorTv;
    private Button mMediaAllComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_video_play_new);
        getSupportActionBar().hide();

        addComment = findViewById(R.id.add_comment);
        mNextListDifferAdapter = new NextListDifferAdapter(this, this, false);
//        downTimer = new CountDownTimer(16200, 1000) {
//            public void onTick(long millisUntilFinished) {
//                //   Log.d(TAG, "onTick: ");.setText("seconds remaining: " + millisUntilFinished / 1000);
//                // double sec = (double) time / 1000.00 ;
//                //long seconds = (long) (time / 1000);
//                // Log.d("TAG", "onTick: " + millisUntilFinished + " -> "+ sec);
//                // if (seconds % 16 == 0 && seconds !=16) {
//
//                //  }
//            }
//
//            public void onFinish() {
//                if (!forcFinisj) {
//                //    callForGift(16);
//                }
//                downTimer.cancel();
//
//
//            }
//        };
        progressBar = findViewById(R.id.spin_kit);
        progressBar.setVisibility(View.GONE);
        commentCount = findViewById(R.id.commentCount);
        followersCount = findViewById(R.id.followerCount);
        loadingPanel = findViewById(R.id.loadingPanel);
        qualityBtn = findViewById(R.id.qualitu);
        Intent o = getIntent();
        nestedScrollView = findViewById(R.id.nestedScroll);
        nextList = findViewById(R.id.nextList);
        profile_image = findViewById(R.id.profile_image);
        sparkButton = findViewById(R.id.spark_button);
        TextView textView = findViewById(R.id.titleTV);
        like_count = findViewById(R.id.like_count);
        mMediaTitle = (TextView) findViewById(R.id.media_title);
        authorTv = findViewById(R.id.author);
        followBtn = findViewById(R.id.followBtn);
        commentEt = findViewById(R.id.commnetET);
        //   mMediaDate = (TextView) findViewById(R.id.media_date);
        mMediaViews = (TextView) findViewById(R.id.media_views);
        mMediaComments = (TextView) findViewById(R.id.media_comments);
        mMediaDetails = findViewById(R.id.media_details);
        mMediaAllComments = (Button) findViewById(R.id.media_see_all_comments);
        reportBtn = findViewById(R.id.reportImage);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);
        playerView.setUseArtwork(true);
        posts = (Post.PostModel) o.getSerializableExtra("POST");
        //Toast.makeText(getApplicationContext() , "p" + post.getId()  , Toast.LENGTH_LONG).show(); ;
        mMediaDetails.setShowingLine(2);
        mMediaDetails.setShowMoreColor(Color.parseColor("#4169E2"));
        mMediaDetails.addShowMoreText("Continue");

        nextList.setLayoutManager(new LinearLayoutManager(this));
        nextList.setAdapter(mNextListDifferAdapter);

        commentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    addComment.setVisibility(View.GONE);
                } else {
                    addComment.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addComment.setOnClickListener(v -> {
            String commnet = commentEt.getText().toString();
            if (!commnet.isEmpty()) {
                sendTheCommnet(commnet);
            }
        });


        followBtn.setOnClickListener(v -> TriggerAuthorFollow(posts.getUser_id() + ""));


        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Post Details");
        } catch (Exception e) {
        }


        findViewById(R.id.msgIcon).setOnClickListener(
                v -> {
                    Intent intent = new Intent(PostDetailsPage.this, CommentsActivity.class);
                    intent.putExtra("POST_ID", posts.getId() + "");
                    intent.putExtra("slug", posts.getSlug() + "");
                    startActivity(intent);
                });

        findViewById(R.id.backBtn).setOnClickListener(v -> finish());

        findViewById(R.id.shareIcon).setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, "Check This From NewsRme");
            share.putExtra(Intent.EXTRA_TEXT, "" + AppPreferences.postLinkBUilder(posts.getSlug(), posts.getLang()));
            startActivity(Intent.createChooser(share, "Share link!"));

        });


        playerView.setPlayer(PlayerManager.getSharedInstance(PostDetailsPage.this).getPlayerView().getPlayer());

        PlayerManager manager = PlayerManager.getSharedInstance(this);
        manager.setPlayerListener(this);

        PlayerControlView playerControlView = findViewById(R.id.CreateHousePlayerView);
        //ProgressBar audioProgressBar = miniPlayerCardView.findViewById(R.id.audioProgressBar);
        playerControlView.setPlayer(PlayerManager.getSharedInstance(this).getPlayerView().getPlayer());


        PlayerManager.getSharedInstance(this).getPlayer().addListener(new Player.EventListener() {


            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    //timer sta
                    Log.d("TAG", "onPlayerStateChanged: TIMER STARTED ");
                    //downTimer.start();
                    isStopped = false;

                } else if (playWhenReady) {
                    // might be idle (plays after prepare()),
                    // buffering (plays when data available)
                    // or ended (plays when seek away from end)
                    Log.d("TAG", "onPlayerStateChanged: TIMER STOPPED ");
                    //downTimer.cancel();
                    isStopped = true;

                } else {
                    // player paused in any state
                    Log.d("TAG", "onPlayerStateChanged: TIMER STOPPED ");
                    //downTimer.cancel();
                    isStopped = true;

                }

                if (playbackState == Player.STATE_ENDED) {
                    Intent p = new Intent(getApplicationContext(), PostDetailsPage.class);
                    // Toast.makeText(getApplicationContext(), "rr" , Toast.LENGTH_LONG).show();
                    try {
                        if (!relatedPosts.isEmpty() && relatedPosts.size() > 0) {
                            Post.PostModel model = relatedPosts.get(0);
                            if (model.getType().equals("audio") || model.getType().equals("video")) {
                                p = new Intent(getApplicationContext(), PostDetailsPage.class);
                                try {
                                    setDetails(model);
                                    loadPostDetails(model.getSlug(), 1);
                                    nestedScrollView.fling(0);
                                    nestedScrollView.smoothScrollTo(0, 0);
                                } catch (Exception e) {

                                }

                            } else {
                                p = new Intent(getApplicationContext(), NewsDetailsActivity.class);
                            }
                            p.putExtra("POST", model);
                            //  startActivity(p);

                        }
                    } catch (Exception er) {
                        Toast.makeText(getApplicationContext(), "Something Went  Wrong While Trying To Play Next Video", Toast.LENGTH_LONG).show();
                    }
                }

            }


        });
        playerControlView.hide();

        playerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerControlView.show();
            }
        });


        playerControlView.setProgressUpdateListener((position, bufferedPosition) -> {

            //  Log.d("BUFFERED", "onCreate: " + position/1000 + " -> " + bufferedPosition);
            if ((position / 1000) - prevSec == 1) {
                prevSec = newSec;
                newSec = newSec + 1;


                if (newSec % 16 == 0) {
                    callForGift(16);
                    Log.d("BUFFERED", "onCreate: " + newSec + " -> " + prevSec);

                }

            } else {
                prevSec = (int) position / 1000;
            }


//            if (currentDuration > 0 && currentDuration % 16 == 0 && allReadySentTime != currentDuration) {
//                allReadySentTime = currentDuration;
//
//            }
//
//               int bufferedProgressBarPosition = (int) ((bufferedPosition * 100) / manager.getDuration());
//              audioProgressBar.setProgress(progressBarPosition);
//              audioProgressBar.setSecondaryProgress(bufferedProgressBarPosition);

        });


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
            p.putExtra("author_id", posts.getUser_id());
            p.putExtra("is_followed", isFollowed);
            p.putExtra("auther", authermodel);
            startActivity(p);


        });

        setDetails(posts);
        initScrollListener();

    }


    private void callForGift(int currentSec) {
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Log.d("TAG", "call " + currentSec
                + " ");
        Call<LoginResponse.forgetPassResponse> NetworkCall = api.givePoint(
                posts.getUser_id() + "", posts.getId(), currentSec
        );

        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.code());
                    allReadySentTime = currentSec;
                    Gson gson = new Gson();
                    String str = gson.toJson(response.body());
                    Log.d("TAG", "onResponse: " + str);
                    // downTimer.cancel();
                    //downTimer.start();


                }
            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                //   downTimer.cancel();
                //   downTimer.start();

            }
        });


    }

    private void setDetails(Post.PostModel post) {

        if (SharedPrefManager.getInstance(getApplicationContext()).getUser_ID().equals(post.getUser_id() + "")) {
            followBtn.setVisibility(View.GONE);
        }

        if (post.getType().equals("audio")) {

            //Toast.makeText(getApplicationContext() , post.getPostType() + "" , Toast.LENGTH_LONG).show();
            // loadAudioDetails(post.getId());
            playMedia(post.getPath());
            //  playHlsVideo();
        } else if (post.getType().equals("video")) {
            try {
                if (post.getHls().isEmpty() || post.getHls().toString().length() < 5) {
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

        setUpIcon();
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

        //  countDownTimer = null ;
        forcFinisj = true;
        // downTimer.onFinish();


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
        loadPostDetails(posts.getSlug(), 1);

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

    public void TriggerAuthorFollow(String auhter_id) {

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Call<LoginResponse.forgetPassResponse> call = api.followAuthor(auhter_id, AppPreferences.getUSerID(getApplicationContext()));

        call.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {

                try {
                    if (response.isSuccessful() && response.code() == 200) {

                        LoginResponse.forgetPassResponse model = response.body();
                        //  Toast.makeText(getApplicationContext(), "Msg  : " + model.getMessage(), Toast.LENGTH_SHORT).show();
                        if (followBtn.getText().toString().contains("Un-Follow")) {
                            followBtn.setText("Follow");
                            isFollowed = false;
                        } else {
                            followBtn.setText("Un-Follow");
                            isFollowed = true;
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Error : Code :" + response.code(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error : Code :" + e.getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : Code :" + t.getMessage(), Toast.LENGTH_LONG).show();

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
                posts.getId(),
                posts.getUser_id(),
                posts.getId()
        );

        callwd.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    //  Toast.makeText(getApplicationContext(), "Message : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getMessage().toLowerCase().contains("added")) {
                        like_count.setText((Integer.parseInt(like_count.getText().toString()) + 1) + "");
                    } else {
                        if ((Integer.parseInt(like_count.getText().toString()) > 0)) {
                            like_count.setText((Integer.parseInt(like_count.getText().toString()) - 1) + "");
                        } else {
                            like_count.setText("0");
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

    private void loadPostDetails(String slug, int page) {
        if (page == 1) {
            loadingPanel.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

        Call<SinglePostResponse> NetworkCall = api.getSinglePost(slug, page);

        NetworkCall.enqueue(new Callback<SinglePostResponse>() {
            @Override
            public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {

                if (response.isSuccessful()) {
                    SinglePostResponse res = response.body();
                    Log.d("sdaf", "onResponse: " + res.getRelatedPosts().toString());
                    if (page == 1) {
                        //  Toast.makeText(getApplicationContext(), "R -> " + res.getPostLikesCheck(), Toast.LENGTH_LONG).show();
                        Log.d("TAG", "onResponse: " + SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

                        isFollowed = res.followerCheck != null;

                        followersCount.setText(res.getFollowerCount() + " Followers");
                        commentCount.setText(res.getComments().size() + " Comments");
                        if (isFollowed) {
                            followBtn.setText("Un-Follow");
                        } else {
                            followBtn.setText("Follow");
                        }
                        currentPage = res.getRelatedPosts().getCurrent_page();
                        totalPage = res.getRelatedPosts().getLast_page();
                        like_count.setText(res.getPostLikesCount() + "");
                        relatedPosts = res.getRelatedPosts().getRelatedPosts();
                        Collections.shuffle(relatedPosts);
                        mNextListDifferAdapter.submitlist(relatedPosts);

                        authermodel = res.data.getAuther();
                        Glide.with(getApplicationContext())
                                .load(res.getData().getAuther().getImage() + "")
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .into(profile_image);
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
                        currentPage = res.getRelatedPosts().getCurrent_page();

                        totalPage = res.getRelatedPosts().getLast_page();
                        relatedPosts.addAll(res.getRelatedPosts().getRelatedPosts());
                        mNextListDifferAdapter.submitlist(relatedPosts);
                    }

                    loadingPanel.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SinglePostResponse> call, Throwable t) {
                if (page == 1) {
                    loadingPanel.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
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

    private void sendTheCommnet(String commnet) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        ProgressDialog dialog = new ProgressDialog(PostDetailsPage.this);
        dialog.setMessage("Adding Your Comment ...");
        dialog.show();
        dialog.setCancelable(false);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<LoginResponse.forgetPassResponse> NetworkCall = api.post_comments(
                posts.getId() + "", AppPreferences.getUSerID(getApplicationContext()) + "", commnet

        );

        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.code() == 200) {
                    try {
                        dialog.dismiss();
                        //   Toast.makeText(getApplicationContext(), " Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        commentEt.setText("");
                        loadPostDetails(posts.getSlug(), 1);


                    } catch (Exception er) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error Code : " + er.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Code : " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
    }


    private void startTimer() {

    }


    @Override
    public void onItemClick(Post.PostModel model) {
        Intent p;
        if (model.getType().equals("audio") || model.getType().equals("video")) {
            p = new Intent(getApplicationContext(), PostDetailsPage.class);
            try {
                setDetails(model);
                loadPostDetails(model.getSlug(), 1);
                nestedScrollView.fling(0);
                nestedScrollView.smoothScrollTo(0, 0);
            } catch (Exception e) {

            }

        } else {
            p = new Intent(getApplicationContext(), NewsDetailsActivity.class);
        }
        p.putExtra("POST", model);
        //  startActivity(p);


    }


    private void initScrollListener() {
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                    isScrolling = true;
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) { // scroll down
//                    currentItems = manager.getChildCount();
//                    totalItems = manager.getItemCount();
//                    scrollOutItems = manager.findFirstVisibleItemPosition();
//
//                    Toasty.warning(context, "d" , Toasty.LENGTH_LONG)
//                            .show();
//                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
//                        isScrolling = false;
//                        Toasty.warning(context, "d" + currentPage, Toasty.LENGTH_LONG)
//                                .show();
//                        loadMore();
//                    }
//                }
//
//
//            }
//        });re

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    //code to fetch more data for endless scrolling
                    int test = (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight());
                    loadMore();

                }
            }
        });
    }


    private void loadMore() {

        if (totalPage <= currentPage) {
            Toast.makeText(getApplicationContext(), "Your At The Last Page.", Toast.LENGTH_SHORT)
                    .show();
        } else {
//            Toasty.warning(context, "Loading Start", Toasty.LENGTH_SHORT)
//                    .show();
            currentPage = currentPage + 1;
            Log.d("TTTTT", "loadMore: " + currentPage);
            loadPostDetails(posts.getSlug(), currentPage);

        }


    }
}