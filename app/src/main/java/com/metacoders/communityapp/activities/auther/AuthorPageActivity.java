package com.metacoders.communityapp.activities.auther;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.utils.AppPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorPageActivity extends AppCompatActivity implements ProductListDifferAdapter.ItemClickListener {


    View view;
    Context context;
    TextView tpost, tvideo, taudio, name, mail, totalArticle;
    RecyclerView recyclerView;
    CircleImageView circleImageView;
    ShimmerFrameLayout shimmer_view_container_dash;
    List<Post.PostModel> post_modelList = new ArrayList<>();
    List<Post.PostModel> audioList = new ArrayList<>();
    List<Post.PostModel> videoList = new ArrayList<>();
    ProductListDifferAdapter mAdapter;
    ConstraintLayout emptyLayout;
    int videoCount = 0, audioCount = 0, postCount = 0;
    MaterialButton followButton;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_page);

        user_id = getIntent().getIntExtra("author_id", 0);

        setView();


        //  loadUrPost();

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TriggerAuthorFollow(user_id + "");
            }
        });

        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Author Profile");
        } catch (Exception e) {

        }

    }

    private void setView() {
        mAdapter = new ProductListDifferAdapter(this, this, false);
        recyclerView = findViewById(R.id.rlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        context = getApplicationContext();
        tpost = findViewById(R.id.tpost);
        totalArticle = findViewById(R.id.totalAritcle);
        tvideo = findViewById(R.id.tvideos);
        taudio = findViewById(R.id.taudios);
        recyclerView = findViewById(R.id.rlist);
        name = findViewById(R.id.nameTv);
        mail = findViewById(R.id.mailTv);
        shimmer_view_container_dash = findViewById(R.id.shimmer_view_container_dash);
        emptyLayout = findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        circleImageView = findViewById(R.id.profile_pic);
        followButton = findViewById(R.id.followBtn);


    }

    @Override
    public void onItemClick(Post.PostModel model) {
        Intent p;
        if (model.getType().equals("audio") || model.getType().equals("video")) {
            p = new Intent(context, PostDetailsPage.class);
        } else {
            p = new Intent(context, NewsDetailsActivity.class);
        }
        p.putExtra("POST", model);
        startActivity(p);

    }

    public void loadUrPost() {
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(context));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(user_id + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();

                if (response.code() == 200) {
                    try {
                        post_modelList = ownListModelList.getOwnArticles();
                        audioList = ownListModelList.getOwnAudios();
                        videoList = ownListModelList.getOwnVideos();

                        taudio.setText(audioList.size() + "");
                        tvideo.setText(videoList.size() + "");
                        totalArticle.setText(post_modelList.size() + "");

                        post_modelList.addAll(audioList);
                        post_modelList.addAll(videoList);

                        tpost.setText((post_modelList.size() + ""));

                        Collections.sort(post_modelList, new Comparator<Post.PostModel>() {
                            @Override
                            public int compare(Post.PostModel o1, Post.PostModel o2) {
                                return o2.getId() - o1.getId();
                            }
                        });

                        if (post_modelList.size() == 0) {

                            emptyLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            /*
                                loop the whole list for counting post type

                             */


                            mAdapter.submitlist(post_modelList);

                            // checking if the list is empty or not
                            emptyLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        recyclerView.getViewTreeObserver().addOnPreDrawListener(

                                new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {

                                        recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                                        for (int i = 0; i < recyclerView.getChildCount(); i++) {
                                            View v = recyclerView.getChildAt(i);
                                            v.setAlpha(0.0f);
                                            v.animate()
                                                    .alpha(1.0f)
                                                    .setDuration(300)
                                                    .setStartDelay(i * 50)
                                                    .start();
                                        }
                                        return true;
                                    }
                                }
                        );
                    } catch (Exception e) {

                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


                    shimmer_view_container_dash.stopShimmer();
                    shimmer_view_container_dash.setVisibility(View.GONE);

                    try {
                        setViewToData(ownListModelList);
                    } catch (Exception e) {

                    }


                } else {
                    Toast.makeText(context, "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthorPostResponse> call, Throwable t) {
                Toast.makeText(context, "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setViewToData(AuthorPostResponse ownListModelList) {
        name.setText(ownListModelList.getAuthor().getName() + "");
        mail.setText(ownListModelList.getAuthor().getEmail() + "");

        Glide.with(getApplicationContext())
                .load(ownListModelList.getAuthor().getImage() + "")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.placeholder)
                .into(circleImageView);


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
                        Toast.makeText(getApplicationContext(), "Msg  : " + model.getMessage(), Toast.LENGTH_SHORT).show();
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
        loadUrPost();
    }
}