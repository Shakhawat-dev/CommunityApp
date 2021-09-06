package com.metacoders.communityapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.CategoryResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class singleList extends AppCompatActivity implements ProductListDifferAdapter.ItemClickListener {

    RecyclerView recyclerView;
    int countryID = 0;
    boolean isScrolling = false;
    SharedPrefManager sharedPrefManager;
    List<Post.PostModel> postsList = new ArrayList<>();
    List<Post.PostModel> filterList = new ArrayList<>();
    ProductListDifferAdapter productListDifferAdapter;
    ConstraintLayout emptyLayout;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayoutManager llm;
    ProgressBar bottomProgressBar;
    String typeMeta = "";
    String cateGoryName = "";
    int currentItems = 0, totalItems = 0, scrollOutItems = 0;
    int currentPage = 1, endPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list);
        llm = new LinearLayoutManager(getApplicationContext());
        recyclerView = findViewById(R.id.list);
        emptyLayout = findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        bottomProgressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(llm);
        productListDifferAdapter = new ProductListDifferAdapter(getApplicationContext(), this, true);
        recyclerView.setAdapter(productListDifferAdapter);

        Intent o = getIntent();
        typeMeta = o.getStringExtra("type");


        if (typeMeta != null && typeMeta.contains("cat")) {
            cateGoryName = o.getStringExtra("cat_name");
            loadCateogryedList(cateGoryName, currentPage);
            try {
                AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, cateGoryName);
            } catch (Exception e) {

            }
        } else {
            countryID = o.getIntExtra("id", 0);
        }

        initScrollListener();

    }


    private void loadCateogryedList(String cateGoryName, int currPage) {
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (currentPage == 1) {
            shimmerFrameLayout.startShimmer();
            shimmerFrameLayout.setVisibility(View.VISIBLE);

        } else {
            bottomProgressBar.setVisibility(View.VISIBLE);
        }


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<CategoryResponse> NetworkCall = api.getCategoricalPost(cateGoryName, currPage);

        NetworkCall.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                // u have the response
                if (response.code() == 200) {
                    CategoryResponse model = response.body();
                    currentPage = model.getCateooryPost().getCurrentPage();
                    endPage = model.getCateooryPost().getLastPage();


                    Post mPost = model.getCateooryPost();
                    postsList.addAll(mPost.getData());

                    if (postsList != null && !postsList.isEmpty()) {

                        productListDifferAdapter.submitlist(postsList);
                        // setting the adapter ;

                        // checking if the list is empty or not

                        if (postsList.size() == 0 && currPage == 1) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        if (currentPage == 1) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);

                        } else {
                            bottomProgressBar.setVisibility(View.GONE);
                        }



                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);


                    } else {
                        // the list is empty
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.d("TAG", "Error On Failed Response: " + t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.stopShimmer();
    }

    @Override
    public void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    public void onItemClick(Post.PostModel model) {
        Intent p;
        if (model.getType().equals("post")) {
            p = new Intent(getApplicationContext(), NewsDetailsActivity.class);
        } else {
            p = new Intent(getApplicationContext(), PostDetailsPage.class);
        }
        p.putExtra("POST", model);
        startActivity(p);

        try {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } catch (Exception e) {
            Log.e("TAG", "onItemClick: " + e.getMessage());
        }

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
                    currentItems = llm.getChildCount();
                    totalItems = llm.getItemCount();
                    scrollOutItems = llm.findFirstVisibleItemPosition();

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
            loadCateogryedList(cateGoryName, currentPage);


        }

    }
}