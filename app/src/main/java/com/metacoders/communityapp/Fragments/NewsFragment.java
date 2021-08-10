package com.metacoders.communityapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.NewsDetailsActivity;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.LoginActivity;
import com.metacoders.communityapp.activities.PostDetailsPage;
import com.metacoders.communityapp.activities.PostUploadActivity;
import com.metacoders.communityapp.activities.Video_Record_Activity;
import com.metacoders.communityapp.activities.Voice_Recoder_Activity;
import com.metacoders.communityapp.adapter.NewsFeedAdapter;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.models.newModels.PostResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment implements ProductListDifferAdapter.ItemClickListener {

    public SharedPrefManager sharedPrefManager;
    ProductListDifferAdapter newAdapter;
    int commonCurrentPage = 1;
    int globalPage = 0;
    boolean isScrolling = false;
    int followerCurrentPage = 1;
    LinearLayoutManager manager;
    int commonLastPage = 1, followerLastPage = 1;
    int currentItems = 0, totalItems = 0, scrollOutItems = 0;
    boolean isFollowerLoaded = false;
    View view;
    List<Post_Model> postsList = new ArrayList<>();
    NewsFeedAdapter.ItemClickListenter itemClickListenter;
    NewsFeedAdapter adapter;
    Context context;
    List<Post.PostModel> newsList = new ArrayList<>();
    List<Post_Model> filteredList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button audioBtn, imageBtn;
    AlertDialog alertDialog;
    CardView insertContainer;
    ConstraintLayout emptyLayout;
    String id = "1";
    SwipeRefreshLayout swipeContainer;
    ProgressBar progressBar;
    private ShimmerFrameLayout mShimmerViewContainer;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.newsfeed);
        progressBar = view.findViewById(R.id.progress_bar);
        context = view.getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        audioBtn = view.findViewById(R.id.audioBtn);
        imageBtn = view.findViewById(R.id.photoBtn);
        insertContainer = view.findViewById(R.id.insertContainer);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        manager = new LinearLayoutManager(getContext());

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        //  code to refresh the list here.
        swipeContainer.setOnRefreshListener(this::fetchTimelineAsync);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        // loadMiscData();
        initScrollListener();
        newAdapter = new ProductListDifferAdapter(getContext(), this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(newAdapter);


        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(context).isUserLoggedIn()) {
                    Intent o = new Intent(getContext(), Voice_Recoder_Activity.class);
                    startActivity(o);
                } else {
                    createTheAlertDialogue();
                }
            }
        });

        imageBtn.setOnClickListener(v -> {

            if (SharedPrefManager.getInstance(context).isUserLoggedIn()) {
                Intent o = new Intent(getContext(), PostUploadActivity.class);
                o.putExtra("media", "post");
                startActivity(o);
            } else {
                createTheAlertDialogue();
            }

        });

        view.findViewById(R.id.videoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharedPrefManager.getInstance(context).isUserLoggedIn()) {
                    Intent o = new Intent(getContext(), Video_Record_Activity.class);
                    startActivity(o);
                } else {
                    createTheAlertDialogue();
                }
            }
        });

        DecideToLoad();

        return view;
    }


    public void fetchTimelineAsync() {
        commonCurrentPage = 1;
        followerCurrentPage = 1;
        globalPage = 0;
        newsList.clear();
        newsList = new ArrayList<>();
        recyclerView.setVisibility(View.GONE);
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmer();
        swipeContainer.setRefreshing(false);
        DecideToLoad();


    }

    private void createTheAlertDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Please Login !!");
        builder.setMessage("You Have To Logged In To View This Please");
        builder.setCancelable(true);
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent p = new Intent(context, LoginActivity.class);
                startActivity(p);


            }
        });
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                alertDialog.dismiss();

            }
        });
        alertDialog = builder.create();
        alertDialog.show();


    }

    private void loadCommonPost(int page) {
        // setting up  layout
        Log.d("TAG", "loadCommonPost: page loading --> " + page);
        emptyLayout.setVisibility(View.GONE);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");
        Call<PostResponse> NetworkCall = api.getCommonNewsList(page);
        NetworkCall.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                // u have the response
                if (response.code() == 200) {
                    PostResponse model = response.body();
                    Post posts = model.getCommonPosts();
                    newsList.addAll(posts.getData());
                    commonLastPage = posts.getLastPage();
                    commonCurrentPage = posts.getCurrentPage();
                    if (newsList != null && !newsList.isEmpty()) {
                        // i know its werid but thats r8 cheaking list is popluted
                        // its not empty
                        // Call the adapter to show the data

                        // filter the list
//                        filteredList.clear();

//                        for (Post_Model post : postsList) {
//                            if (post.getLangId().equals(id)) {
//                                filteredList.add(post);
//                            }

                        newAdapter.submitlist(newsList);

                    }
                    // checking if the list is empty or not
                    if (newAdapter.getItemCount() == 0) {
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }
                    if (globalPage == 0) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        globalPage += 1;
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }


                } else {
                    Toast.makeText(getContext(), "Error : " + response.code(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void loadFollowerPost(int page) {
        // setting up  layout
        Log.d("TAG", "loading FollowerPost: page loading --> " + page);
        emptyLayout.setVisibility(View.GONE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(context));

        Call<PostResponse.FollowerPostResponse> NetworkCall = api.getFollowerPostList(page);
        NetworkCall.enqueue(new Callback<PostResponse.FollowerPostResponse>() {
            @Override
            public void onResponse(Call<PostResponse.FollowerPostResponse> call, Response<PostResponse.FollowerPostResponse> response) {
                // u have the response
                if (response.code() == 200) {
                    PostResponse.FollowerPostResponse model = response.body();
                    Post posts = model.getFollowersPost();
                    newsList.addAll(posts.getData());
                    followerLastPage = posts.getLastPage();
                    followerCurrentPage = posts.getCurrentPage();


                    if (newsList != null && !newsList.isEmpty()) {
                        // i know its werid but thats r8 cheaking list is popluted
                        // its not empty
                        // Call the adapter to show the data

                        // filter the list
//                        filteredList.clear();

//                        for (Post_Model post : postsList) {
//                            if (post.getLangId().equals(id)) {
//                                filteredList.add(post);
//                            }

                        newAdapter.submitlist(newsList);

                    }

                    // here need to check if is it the  1st and last page of the follower post
                    if (followerCurrentPage == 1 && followerCurrentPage == followerLastPage) {
                        // it is the last
                        globalPage = -1;
                    }
                    // checking if the list is empty or not
                    if (newAdapter.getItemCount() == 0) {
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }
                    if (globalPage == 0) {
                        mShimmerViewContainer.stopShimmer();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        globalPage += 1;
                    } else if (globalPage < 0) {
                        globalPage = 0;
                        isFollowerLoaded = false;
                        loadCommonPost(commonCurrentPage);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getContext(), "Error : " + response.code(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<PostResponse.FollowerPostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

    private void loadMiscData() {
        String[] arr = SharedPrefManager.getInstance(context).getLangPref();
        // load the array  arr[0] = id arr[1] = name
        id = arr[0];
        // Toast.makeText(context, id, Toast.LENGTH_LONG).show();

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

    private void initScrollListener() {
        // for nested scroll
//        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if (v.getChildAt(v.getChildCount() - 1) != null) {
//                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
//                        scrollY > oldScrollY) {
//                    //code to fetch more data for endless scrolling
//
//                    int test = (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight());
//                    ;
//                    Log.d("TAG", "initScrollListener: " + test + " old " + oldScrollY + "new " + scrollY);
//                    loadMore();
//
//                }
//            }
//        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) { // scroll down
                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrollOutItems = manager.findFirstVisibleItemPosition();

                    if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                        isScrolling = false;
                        loadMore();
                    }
                }


            }
        });


    }

    private void loadMore() {
        // decide what to do
        // check what we loading here
        if (isFollowerLoaded && SharedPrefManager.getInstance(context).isUserLoggedIn()) {
            // increase the follower

            if (followerLastPage == followerCurrentPage) {
                // end of follower post now
                //load common one now
                isFollowerLoaded = false;
                commonCurrentPage = 1;
                loadCommonPost(commonCurrentPage);
            } else {
                followerLastPage += 1;
                progressBar.setVisibility(View.VISIBLE);
                loadFollowerPost(followerLastPage);
            }


        } else if (!isFollowerLoaded) {
            // user not login and useing common post
            // increase the common
            if (commonCurrentPage == commonLastPage) {
                // show last page
                Toast.makeText(getContext(), "Your At The End Of The List!!!", Toast.LENGTH_LONG).show();
            } else {
                //incerease the common post
                Log.d("TAG", "loadMore: Here To Increase the ");
                commonCurrentPage += 1;
                progressBar.setVisibility(View.VISIBLE);
                loadCommonPost(commonCurrentPage);
            }


        }

    }

    public void DecideToLoad() {
        if (SharedPrefManager.getInstance(context).isUserLoggedIn()) {
            insertContainer.setVisibility(View.VISIBLE);
            isFollowerLoaded = true;
            loadFollowerPost(followerCurrentPage);

        } else {
            insertContainer.setVisibility(View.GONE);
            loadCommonPost(commonCurrentPage);
            isFollowerLoaded = false;
        }
    }


}