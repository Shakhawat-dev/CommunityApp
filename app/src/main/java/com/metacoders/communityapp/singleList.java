package com.metacoders.communityapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.activities.PostDetailsPage;
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
    SharedPrefManager sharedPrefManager;
    List<Post.PostModel> postsList = new ArrayList<>();
    List<Post.PostModel> filterList = new ArrayList<>();
    ProductListDifferAdapter productListDifferAdapter;
    ConstraintLayout emptyLayout;
    ShimmerFrameLayout shimmerFrameLayout;
    String typeMeta = "";
    String cateGoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list);

        recyclerView = findViewById(R.id.list);
        emptyLayout = findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productListDifferAdapter = new ProductListDifferAdapter(getApplicationContext(), this);



        Intent o = getIntent();
        typeMeta = o.getStringExtra("type");


        if (typeMeta != null && typeMeta.contains("cat")) {
            cateGoryName = o.getStringExtra("cat_name");
            loadCateogryedList(cateGoryName);
            try {
                AppPreferences.setActionbarTextColor(getSupportActionBar() , Color.WHITE ,cateGoryName ) ;
            }catch (Exception e){

            }
        } else {
            countryID = o.getIntExtra("id", 0);
        }

    }


    private void loadCateogryedList(String cateGoryName) {
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<CategoryResponse> NetworkCall = api.getCategoricalPost(cateGoryName);

        NetworkCall.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                // u have the response
                if (response.code() == 200) {
                    CategoryResponse model = response.body();

                    postsList = model.getCateooryPost();

                    if (postsList != null && !postsList.isEmpty()) {
                        // i know its werid but thats r8 cheaking list is popluted
                        // its not empty
                        // Call the adapter to show the data

                        // process the list

                        productListDifferAdapter.submitlist(postsList);
                        // setting the adapter ;
                        recyclerView.setAdapter(productListDifferAdapter);
                        // checking if the list is empty or not
                        if (postsList.size() == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
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
}