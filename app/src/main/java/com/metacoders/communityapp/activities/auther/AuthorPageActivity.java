package com.metacoders.communityapp.activities.auther;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.adapter.new_adapter.profile_viewpager_adapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.models.newModels.UserModel;
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
    //    RecyclerView recyclerView;
    String[] tabTitle = {"Video", "Audio", "Post"};
    CircleImageView circleImageView;

    List<Post.PostModel> post_modelList = new ArrayList<>();
    List<Post.PostModel> audioList = new ArrayList<>();
    List<Post.PostModel> videoList = new ArrayList<>();
    ProductListDifferAdapter mAdapter;
    ConstraintLayout emptyLayout;
    int videoCount = 0, audioCount = 0, postCount = 0;
    TextView followButton , followerCount;
    int user_id;
    UserModel authermodel;
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    TextView countyName ,link ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_page);

        user_id = getIntent().getIntExtra("author_id", 0);

        boolean isFollow = getIntent().getBooleanExtra("is_followed", false);
        setView();
        authermodel = (UserModel) getIntent().getSerializableExtra("auther");
        setViewToData(authermodel);



        if (isFollow) {
            followButton.setText("Un-Follow");
        } else {
            followButton.setText("Follow");
        }


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
        tabLayout = findViewById(R.id.tabMode);
        countyName = findViewById(R.id.country_name);
        viewPager2 = findViewById(R.id.rlist);
        link = findViewById(R.id.link);
        followerCount = findViewById(R.id.followerCount);
        viewPager2.setAdapter(new profile_viewpager_adapter(AuthorPageActivity.this, String.valueOf(user_id)));
        viewPager2.setUserInputEnabled(true);
        context = getApplicationContext();
        //recyclerView = findViewById(R.id.rlist);
        name = findViewById(R.id.nameTv);
        mail = findViewById(R.id.mailTv);



        // recyclerView.setVisibility(View.VISIBLE);
        circleImageView = findViewById(R.id.profile_pic);
        followButton = findViewById(R.id.followBtn);



        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabTitle[position])
        ).attach();


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


    private void setViewToData(UserModel authermodel) {
        name.setText(authermodel.getName() + "");
        try {
            if(authermodel.getBio() == null || authermodel.getBio().isEmpty()){
                mail.setText("");
            }else {
                mail.setText(authermodel.getBio() + "");
            }

        }catch (Exception e){
            mail.setText("");
        }
        countyName.setText(authermodel.getCountry()+"");
        link.setText(authermodel.getAccount_number()+"");
       // followerCount.setText(""+authermodel.getFollower_active());

        Glide.with(getApplicationContext())
                .load(authermodel.getImage() + "")
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
                        if (followButton.getText().toString().contains("Un-Follow")) {
                            followButton.setText("Follow");
                        } else {
                            followButton.setText("Un-Follow");
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
        //     loadUrPost();
    }

}