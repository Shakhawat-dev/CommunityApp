package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.EditProfile;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.adapter.new_adapter.profile_viewpager_adapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment implements ProductListDifferAdapter.ItemClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    Context context;
    TextView name, mail, totalCount, link, followerCount, followingCount, country_name;
    String[] tabTitle = {"Video", "Audio", "Article"};
    CircleImageView circleImageView;
    ProductListDifferAdapter mAdapter;
    TabLayout tabLayout;
    ViewPager2 viewPager2;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = view.getContext();
        name = view.findViewById(R.id.name);
        mail = view.findViewById(R.id.mail_id);
        totalCount = view.findViewById(R.id.postCount);
        followerCount = view.findViewById(R.id.followerCount);
        followingCount = view.findViewById(R.id.folowingCount);
        tabLayout = view.findViewById(R.id.tabMode);
        country_name = view.findViewById(R.id.country_name);
        viewPager2 = view.findViewById(R.id.viewpager2);
        link = view.findViewById(R.id.link);
        circleImageView = view.findViewById(R.id.dashboard_profile_pic);
        viewPager2.setAdapter(new profile_viewpager_adapter(getActivity(), SharedPrefManager.getInstance(getContext()).getUser_ID()));
        viewPager2.setUserInputEnabled(true);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(tabTitle[position])
        ).attach();



        loadUrPost();

        view.findViewById(R.id.edit_myProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditProfile.class));
            }
        });
        return view;
    }


    private void setDetails() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        // String   accessTokens = ;
        name.setText(sharedPrefManager.getUserModel().getName() + " ");

        try {
            if (sharedPrefManager.getUserModel().getBio() == null || sharedPrefManager.getUserModel().getBio().isEmpty()) {
                mail.setText("");
            } else {
                mail.setText(sharedPrefManager.getUserModel().getBio() + "");
            }

        } catch (Exception e) {
            mail.setText("");
        }

        String gender = sharedPrefManager.getUserModel().getGender();
        String link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/TgBDz5Ti5AZiposXXwvRmTKP1VpIJouIctyaILih.png";
      try {
          if (gender.toLowerCase().contains("female")) {
              link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/Vzsa4eUZNCmRvuNVUWToGyu0Xobb6DyQgcX4oDoI.png\n";
          } else {
              link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/TgBDz5Ti5AZiposXXwvRmTKP1VpIJouIctyaILih.png";
          }
      }catch (Exception e){

      }

        Glide.with(context)
                .load(sharedPrefManager.getUserModel().getImage())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(link)
                .placeholder(R.drawable.placeholder)
                .into(circleImageView);
    }

    public void loadUrPost() {

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getContext()));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(SharedPrefManager.getInstance(getContext()).getUser_ID() + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();
                if (response.code() == 200) {
                    ownListModelList = response.body();

                    //   followerCount.setText(ownListModelList.);
                    link.setText(ownListModelList.getAuthor().getAccount_number() + "");
                    followerCount.setText(ownListModelList.otherProfileFollowersCount + "");
                    totalCount.setText("" + ownListModelList.totalPostCount);
                    country_name.setText("Country: " + ownListModelList.getAuthor().getCountry());


                } else {
                    Toast.makeText(getContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthorPostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        setDetails();
    }
}