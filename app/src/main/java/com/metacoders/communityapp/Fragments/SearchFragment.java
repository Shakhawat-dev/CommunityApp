package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
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


public class SearchFragment extends Fragment implements ProductListDifferAdapter.ItemClickListener {

    List<Post.PostModel> postsList = new ArrayList<>();
    ProductListDifferAdapter mAdapter;
    String searchTerm = " ";
    Context context;
    RecyclerView recyclerView;
    SearchView searchView;
    boolean isScrolling = false;
    LinearLayoutManager manager;
    ConstraintLayout emptyLayout;
    ProgressBar centerProgressBar, bottomProgressBar;
    int currentItems = 0, totalItems = 0, scrollOutItems = 0;
    int currentPage = 1, endPage = 0;
    View view;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search_bar);
        recyclerView = view.findViewById(R.id.newsfeed);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        centerProgressBar = view.findViewById(R.id.center_progress);
        bottomProgressBar = view.findViewById(R.id.bottom_progress);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter = new ProductListDifferAdapter(getContext(), this, false);
        context = view.getContext();
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        initScrollListener();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
                loadList("news", 1 );
                return false;
            }
        });


        return view;
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
        if (currentPage == endPage) {
            Toast.makeText(getContext(), "Your At The End Of The List!!!", Toast.LENGTH_LONG).show();
        } else {
            currentPage += 1;
            bottomProgressBar.setVisibility(View.VISIBLE);
            loadList(searchTerm, currentPage);
        }

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

                    } else {
                        // the list is empty
                        Log.d("TAG", "Error: List Is Empty  " + response.errorBody());
                        // checking if the list is empty or not
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);

                        // shimmerFrameLayout.stopShimmer();
                    }


                } else {

                    Toast.makeText(getContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<PostResponse.SerachResult> call, Throwable t) {
                Log.d("TAG", "Error On Failed Response: " + t.getMessage());
                Toast.makeText(getContext(), "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onStart() {
        loadList("news" , 1);
        super.onStart();
    }
}