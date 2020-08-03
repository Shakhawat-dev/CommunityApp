package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.utils.CallBacks;
import com.metacoders.communityapp.utils.PlayerManager;

import maes.tech.intentanim.CustomIntent;

public class MediaPage extends AppCompatActivity  implements CallBacks.playerCallBack {

    PlayerView playerView;
    SimpleExoPlayer player;
    boolean isPLaying = false;
    ImageView reportBtn;

    String LINK, ID, TITILE, category;
    boolean fullscreen = false;
    ImageView fullscreenButton ;
    Dialog mFullScreenDialog ;
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
        LINK = o.getStringExtra("media_link");

        // play the media
        playerView.setPlayer(PlayerManager.getSharedInstance(MediaPage.this).getPlayerView().getPlayer());
        PlayerManager.getSharedInstance(MediaPage.this).playStream(LINK);
        PlayerManager.getSharedInstance(this).setPlayerListener(this);

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mExoPlayerFullscreen)
                {
                    // not in fullscreen

                    openFullScreenDialog()  ;


                }
                else {

                    closeFullScreenDialog() ;

                }


            }
        });

    }

    @Override
    public void onItemClickOnItem(Integer albumId) {

    }

    @Override
    public void onPlayingEnd() {

    }

    public  void initFullsceen() {

        mFullScreenDialog = new Dialog(MediaPage.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
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
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MediaPage.this , R.drawable.full));




    }

    private void openFullScreenDialog() {


        // opening the dialgoue

        ((ViewGroup) playerView.getParent()).removeView(playerView); // removes the player screen

        mFullScreenDialog.addContentView(playerView , new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT ));
        // change the full screen image
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MediaPage.this , R.drawable.full));

        mExoPlayerFullscreen = true ;

        mFullScreenDialog.show();


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (PlayerManager.getSharedInstance(MediaPage.this).isPlayerPlaying())
        {
            PlayerManager.getSharedInstance(MediaPage.this).stopPlayer();
            PlayerManager.getSharedInstance(MediaPage.this).releasePlayer();



        }
        finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();


        PlayerManager.getSharedInstance(MediaPage.this).stopPlayer();
        PlayerManager.getSharedInstance(MediaPage.this).releasePlayer();


    }
    @Override
    protected void onPause() {
        super.onPause();

        PlayerManager.getSharedInstance(MediaPage.this).pausePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initFullsceen();
    }


}