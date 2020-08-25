package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.models.SinglePostDetails;
import com.metacoders.communityapp.utils.CallBacks;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.PlayerManager;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostDetailsPage extends AppCompatActivity  implements CallBacks.playerCallBack {

    PlayerView playerView;
    SimpleExoPlayer player;
    boolean isPLaying = false;
    ImageView reportBtn;

    String LINK, ID, TITILE, category;
    boolean fullscreen = false;
    ImageView fullscreenButton ;
    Dialog mFullScreenDialog ;
    Post_Model post ;
    private boolean mExoPlayerFullscreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_page);
        Intent o = getIntent();

        TextView textView = findViewById(R.id.titleTV);
        reportBtn = findViewById(R.id.reportImage);
        playerView = findViewById(R.id.player_view);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);
        playerView.setUseArtwork(true);
        post = (Post_Model) o.getSerializableExtra("POST");

        playerView.setPlayer(PlayerManager.getSharedInstance(PostDetailsPage.this).getPlayerView().getPlayer());

        PlayerManager.getSharedInstance(this).setPlayerListener(this);


        if(post.getPostType().equals("audio")) {

            Toast.makeText(getApplicationContext() , post.getPostType() + "" , Toast.LENGTH_LONG).show();
            loadAudioDetails(post.getId());
        }
        else {
            // all other model
            //TODO Check if its post or video

            if(post.getPostType().equals("video"))
            {
                playMedia(post.getVideoPath());
            }

        }
        // play the media

        fullscreenButton.setOnClickListener(v -> {

            if (!mExoPlayerFullscreen)
            {
                // not in fullscreen

                openFullScreenDialog()  ;


            }
            else {

                closeFullScreenDialog() ;

            }


        });

    }

    private void playMedia(String Path) {

        if( PlayerManager.getSharedInstance(PostDetailsPage.this).isPlayerPlaying())
        {
            PlayerManager.getSharedInstance(PostDetailsPage.this).stopPlayer();
        }

        PlayerManager.getSharedInstance(PostDetailsPage.this).playStream(Constants.IMAGE_URL + Path);
    }

    @Override
    public void onItemClickOnItem(Integer albumId) {

    }

    @Override
    public void onPlayingEnd() {

    }

    public  void initFullsceen() {

        mFullScreenDialog = new Dialog(PostDetailsPage.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        {
            public  void onBackPressed()
            {
                if(mExoPlayerFullscreen)
                {
                    closeFullScreenDialog();

                    super.onBackPressed();
                }

            }


        }  ;



    }
    private void closeFullScreenDialog() {


        ((ViewGroup) playerView.getParent()).removeView(playerView); // removes the player screen


        ((FrameLayout) findViewById(R.id.parent_relative )).addView(playerView) ;

        mExoPlayerFullscreen = false ;

        mFullScreenDialog.dismiss();


        // change the full screen image
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PostDetailsPage.this , R.drawable.full));




    }

    private void openFullScreenDialog() {


        // opening the dialgoue

        ((ViewGroup) playerView.getParent()).removeView(playerView); // removes the player screen

        mFullScreenDialog.addContentView(playerView , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT ));
        // change the full screen image
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PostDetailsPage.this , R.drawable.full));

        mExoPlayerFullscreen = true ;

        mFullScreenDialog.show();


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (PlayerManager.getSharedInstance(PostDetailsPage.this).isPlayerPlaying()) {
            PlayerManager.getSharedInstance(PostDetailsPage.this).stopPlayer();
            PlayerManager.getSharedInstance(PostDetailsPage.this).releasePlayer();

        }
        finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);


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

    private   void loadAudioDetails(String id){

//        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext()) ;
//        String   accessTokens = sharedPrefManager.getUserToken();
      //  Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , "00") ;

        Call<SinglePostDetails> NetworkCall = api.getPostDetails(
               id
        ) ;


        NetworkCall.enqueue(new Callback<SinglePostDetails>() {
            @Override
            public void onResponse(Call<SinglePostDetails> call, Response<SinglePostDetails> response) {

                if(response.code() == 201 ){

                    SinglePostDetails postDetails = response.body() ;

                    List< SinglePostDetails.GetNewsAudioDetail > data = postDetails.getGetNewsAudioDetails() ;

                  if(data.size() > 0 ){
                      playMedia(data.get(0).getAudioPath());

                  }



                }
            }

            @Override
            public void onFailure(Call<SinglePostDetails> call, Throwable t) {

            }
        });


    }

}