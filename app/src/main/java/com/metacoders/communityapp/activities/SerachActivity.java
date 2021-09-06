package com.metacoders.communityapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.models.newModels.PostResponse;
import com.metacoders.communityapp.utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerachActivity extends AppCompatActivity implements ProductListDifferAdapter.ItemClickListener {
    List<Post.PostModel> postsList = new ArrayList<>();
    ProductListDifferAdapter mAdapter;
    String searchTerm = " ";
    Context context;
    RecyclerView recyclerView;
    SearchView searchView;
    boolean isScrolling = false;
    LinearLayoutManager manager;
    ShimmerFrameLayout shimmerFrameLayout;
    ConstraintLayout emptyLayout;
    ProgressBar centerProgressBar, bottomProgressBar;
    int currentItems = 0, totalItems = 0, scrollOutItems = 0;
    int currentPage = 1, endPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);
        searchView = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.newsfeed);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.setVisibility(View.GONE);
        emptyLayout = findViewById(R.id.emptyLayout);
        centerProgressBar = findViewById(R.id.center_progress);
        bottomProgressBar = findViewById(R.id.bottom_progress);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new ProductListDifferAdapter(this, this, false);
        context = getApplicationContext();
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        initScrollListener();

        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Search");
        } catch (Exception e) {

        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                shimmerFrameLayout.setVisibility(View.GONE);
                postsList.clear();

                mAdapter.submitlist(postsList);
                currentPage = 1;
                searchTerm = query.trim();
                loadList(query.trim(), currentPage);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                postsList.clear();
                mAdapter.submitlist(postsList);
                searchView.setQueryHint("Search For Anything..");
                return false;
            }
        });

    }


    private void loadList(String searchTerm, int page) {
        if (page == 1) {
            centerProgressBar.setVisibility(View.VISIBLE);
        } else {
            bottomProgressBar.setVisibility(View.VISIBLE);
        }
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<PostResponse.SerachResult> NetworkCall = api.getSearchResult(
                page,
                searchTerm
        );

        NetworkCall.enqueue(new Callback<PostResponse.SerachResult>() {
            @Override
            public void onResponse(Call<PostResponse.SerachResult> call, Response<PostResponse.SerachResult> response) {
                // u have the response
                if (page == 1) {
                    centerProgressBar.setVisibility(View.GONE);
                } else {
                    bottomProgressBar.setVisibility(View.GONE);
                }

                if (response.code() == 200) {


                    Post posts = response.body().getSearchResults();

                    currentPage = posts.getCurrentPage();
                    endPage = posts.getLastPage();
                    postsList.addAll(posts.getData());

                    if (postsList != null && !postsList.isEmpty()) {
                        // checking if the list is empty or not

                        mAdapter.submitlist(postsList);
                        Log.d("TAG", "onResponse: " + mAdapter.getItemCount());

//                        if (mAdapter.getItemCount() == 0) {
//                            emptyLayout.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                        } else {
//                            emptyLayout.setVisibility(View.GONE);
//                            recyclerView.setVisibility(View.VISIBLE);
//
//                        }
                        //shimmerFrameLayout.stopShimmer();


                    } else {
                        // the list is empty
                        Log.d("TAG", "Error: List Is Empty  " + response.errorBody());
                        // checking if the list is empty or not
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                        // shimmerFrameLayout.stopShimmer();
                    }
                    shimmerFrameLayout.setVisibility(View.GONE);


                } else {

                    Toast.makeText(getApplicationContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<PostResponse.SerachResult> call, Throwable t) {
                Log.d("TAG", "Error On Failed Response: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // shimmerFrameLayout.stopShimmer();
    }

    @Override
    public void onPause() {
        //  shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    public void onItemClick(Post.PostModel model) {

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
        if (currentPage == endPage) {
            Toast.makeText(getApplicationContext(), "Your At The End Of The List!!!", Toast.LENGTH_LONG).show();
        } else {
            currentPage += 1;
            bottomProgressBar.setVisibility(View.VISIBLE);
            loadList(searchTerm, currentPage);


        }

    }
}