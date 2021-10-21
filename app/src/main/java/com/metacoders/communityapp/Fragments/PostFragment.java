package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment implements ProductListDifferAdapter.ItemClickListener {

    ProductListDifferAdapter mAdapter;

    View view;
    List<Post.PostModel> post_modelList = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String id = "1";
    ConstraintLayout emptyLayout;
    SwipeRefreshLayout swipeContainer;
    String type = "";
    private ShimmerFrameLayout mShimmerViewContainer2;

    public PostFragment(String type) {
        // Required empty public constructor
        this.type = type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = view.findViewById(R.id.list);
        context = view.getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new ProductListDifferAdapter(getContext(), this, false);

        recyclerView.setAdapter(mAdapter);
        loadMiscData();
        loadUrPost();


        return view;
    }

    public void loadUrPost() {
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getContext()));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(SharedPrefManager.getInstance(getContext()).getUser_ID() + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();
                if (response.code() == 200) {
                    try {

                        if (type.contains("video")) {
                            post_modelList = ownListModelList.getOwnVideos();

                        } else if (type.contains("audio")) {

                            post_modelList = ownListModelList.getOwnAudios();

                        } else {
                            post_modelList = ownListModelList.getOwnArticles();
                        }


                        if (post_modelList.size() == 0) {

                            emptyLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else {
                            /*
                                loop the whole list for counting post type

                             */


                            mAdapter.submitlist(post_modelList);

                            // checking if the list is empty or not
                            emptyLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error : Code " + e.getMessage(), Toast.LENGTH_LONG).show();
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


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

    private void loadMiscData() {
        String[] arr = SharedPrefManager.getInstance(context).getLangPref();
        // load the array  arr[0] = id arr[1] = name
        id = arr[0];


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
}