package com.metacoders.communityapp.activities.details;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.metacoders.communityapp.models.newModels.UserModel;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.ConvertTime;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsDetailsActivity extends AppCompatActivity {
    public static String LIVETIVELINK = "https://newsrme.s3.ap-southeast-1.amazonaws.com/frontend/video/hls/7xtvXeoDsBi42AH1631677319.m3u8";
    ImageView playerView;
    SimpleExoPlayer player;
    TextView like_count;
    boolean isPLaying = false;
    ImageView reportBtn;
    SparkButton sparkButton;
    String LINK, ID, TITILE, category;
    boolean fullscreen = false;
    ImageView fullscreenButton;
    Dialog mFullScreenDialog;
    Post.PostModel post;
    RelativeLayout loadingPanel;
    Boolean isFollowed = false;
    CircleImageView autherImage;
    UserModel authermodel;
    ImageView profile_image;
    EditText commentEt;
    TextView followersCount ;

    private boolean mExoPlayerFullscreen = false;
    private TextView mMediaTitle, mMediaDate, followBtn, mMediaComments, mMediaDetails, authorTv;
    private Button mMediaAllComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_page_new);

        Intent o = getIntent();
        followersCount = findViewById(R.id.followerCount);
        loadingPanel = findViewById(R.id.loadingPanel);
        autherImage = findViewById(R.id.profile_image);
        TextView textView = findViewById(R.id.titleTV);
        mMediaTitle = (TextView) findViewById(R.id.media_title);
        mMediaDate = (TextView) findViewById(R.id.media_date);
        followBtn = findViewById(R.id.followBtn);
        commentEt = findViewById(R.id.commnetET);
        // mMediaViews = (TextView) findViewById(R.id.media_views);
        mMediaComments = (TextView) findViewById(R.id.media_comments);
        mMediaDetails = (TextView) findViewById(R.id.media_details);
        authorTv = findViewById(R.id.author);
        like_count = findViewById(R.id.like_count);
        sparkButton = findViewById(R.id.spark_button);
        reportBtn = findViewById(R.id.reportImage);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);

        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Post Details");
        } catch (Exception e) {

        }

        setUpIcon();

        findViewById(R.id.add_comment).setOnClickListener(v -> {
            String commnet = commentEt.getText().toString();
            if (!commnet.isEmpty()) {
                sendTheCommnet(commnet);
            }
        });

        findViewById(R.id.media_see_all_comments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailsActivity.this, CommentsActivity.class);
                intent.putExtra("POST_ID", post.getId() + "");
                intent.putExtra("slug", post.getSlug() + "");
                startActivity(intent);
            }
        });

        findViewById(R.id.msgIcon).setOnClickListener(
                v -> {
                    Intent intent = new Intent(NewsDetailsActivity.this, CommentsActivity.class);
                    intent.putExtra("POST_ID", post.getId() + "");
                    intent.putExtra("slug", post.getSlug() + "");
                    startActivity(intent);
                });

        post = (Post.PostModel) o.getSerializableExtra("POST");

//       Glide.with(getApplicationContext())
//               .load(post.getAuther().getImage())
//               .into(autherImage);


        authorTv.setOnClickListener(v -> {

            Intent p = new Intent(getApplicationContext(), AuthorPageActivity.class);
            p.putExtra("author_id", post.getUser_id());
            p.putExtra("is_followed", isFollowed);
            p.putExtra("auther", authermodel);
            startActivity(p);
        });

        if (SharedPrefManager.getInstance(getApplicationContext()).getUser_ID().equals(post.getUser_id() + "")) {
            followBtn.setVisibility(View.GONE);
        }

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TriggerAuthorFollow(post.getUser_id() + "");
            }
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
                    Toast.makeText(getApplicationContext(), "Msg : " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

        //mMediaViews.setText(post.getHit() + "");
        mMediaComments.setText("0");
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

        loadingPanel.setVisibility(View.VISIBLE);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, SharedPrefManager.getInstance(getApplicationContext()).getUserToken());

        Call<SinglePostResponse> NetworkCall = api.getSinglePost(slug ,1 );

        NetworkCall.enqueue(new Callback<SinglePostResponse>() {
            @Override
            public void onResponse(Call<SinglePostResponse> call, Response<SinglePostResponse> response) {

                if (response.isSuccessful()) {
                    SinglePostResponse res = response.body();
                    isFollowed = res.followerCheck != null;

                    if (isFollowed) {
                        followBtn.setText("Un-Follow");
                    } else {
                        followBtn.setText("Follow");
                    }

                    followersCount.setText(res.getFollowerCount()+" Followers");

                    Glide.with(getApplicationContext())
                            .load(res.getData().getAuther().getImage() + "")
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(autherImage);

                    try {
                        authorTv.setText(response.body().getData().getAuther().getName());
                        authermodel = res.data.getAuther();
                    } catch (Exception e) {

                    }

                    like_count.setText(res.getPostLikesCount() + "");

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
                        } else {
                            followBtn.setText("Un-Follow");
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

    @Override
    protected void onResume() {
        super.onResume();
        loadPostDetails(post.getSlug());
    }

    private void sendTheCommnet(String commnet) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        ProgressDialog dialog = new ProgressDialog(NewsDetailsActivity.this);
        dialog.setMessage("Adding Your Comment ...");
        dialog.show();
        dialog.setCancelable(false);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<LoginResponse.forgetPassResponse> NetworkCall = api.post_comments(
                post.getId() + "", AppPreferences.getUSerID(getApplicationContext()) + "", commnet

        );

        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.code() == 200) {
                    try {
                        dialog.dismiss();
                      //  Toast.makeText(getApplicationContext(), " Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        commentEt.setText("");
                       // loadPostDetails(post.getSlug());

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

}