package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;


import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.NewsFeedAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerachActivity extends AppCompatActivity {
    List<Post_Model> postsList = new ArrayList<>();
    List<Post_Model> filteredList = new ArrayList<>();
    List<Post_Model> tempList = new ArrayList<>();
    String searchTerm  = " " ;
    boolean categorySearchEnabled = false;
    String categoryCode = "0" ;

    NewsFeedAdapter.ItemClickListenter itemClickListenter;
    NewsFeedAdapter adapter;
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button audioBtn , imageBtn ;
    SearchView searchView ;
    ShimmerFrameLayout shimmerFrameLayout ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);
        searchView = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.newsfeed);
        shimmerFrameLayout = findViewById(R.id.shimmer_view_container) ;
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = getApplicationContext() ;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                loadList(query.trim() , "" , "" , "");

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        itemClickListenter = new NewsFeedAdapter.ItemClickListenter() {
            @Override
            public void onItemClick(View view, int pos) {

                Post_Model model = new Post_Model() ;
                model = postsList.get(pos) ;
                Intent p = new Intent(getApplicationContext(), PostDetailsPage.class);
                p.putExtra("POST", model);
                startActivity(p);
                try {
                   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } catch (Exception e) {
                    Log.e("TAG", "onItemClick: " + e.getMessage());
                }



            }
        } ;

    }


    private void loadList( String searchTerm , String cat_id , String sub_cat , String lang_id  ) {


//        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext()) ;
//        String   accessTokens = sharedPrefManager.getUserToken();
//        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , "00") ;

        Call<List<Post_Model>> NetworkCall = api.getSearchResult(
                searchTerm , cat_id , sub_cat , lang_id
        ) ;

        NetworkCall.enqueue(new Callback<List<Post_Model>>() {
            @Override
            public void onResponse(Call<List<Post_Model>> call, Response<List<Post_Model>> response) {
                // u have the response
                if (response.code() == 201) {


                    postsList =  response.body();


                    if (postsList != null && !postsList.isEmpty()) {
                        // i know its werid but thats r8 cheaking list is popluted
                        // its not empty


                        // Call the adapter to show the data

                                adapter = new NewsFeedAdapter(context, postsList, itemClickListenter);

                                // setting the adapter ;
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(adapter);
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
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




                    } else {
                        // the list is empty
                        Log.d("TAG", "Error: List Is Empty  " + response.errorBody());
                    }

                } else {
                    Log.d("TAG", "Error: " + response.errorBody() +
                            " Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<List<Post_Model>> call, Throwable t) {
                Log.d("TAG", "Error On Failed Response: " + t.getMessage());
            }
        });
    }
//    private  void setSearch(String searchTerm){
//
//
//        for(int i  = 0 ; i< postsList.size() ; i++){
//
//            if(postsList.get(i).getTitle().toLowerCase().contains(searchTerm)){
//                filteredList.add(postsList.get(i)) ;  // adding the filtered list to the new array list
//            }
//
//        }
//        // if country search  enabled then
//        if(categorySearchEnabled){
//            tempList  = new ArrayList<>(filteredList) ;
//            filteredList.clear();
//
//            for(int y  = 0 ; y< tempList.size() ; y++){
//
//                if(tempList.get(y).getCategoryId().equals(categoryCode)){
//                    filteredList.add(tempList.get(y)) ;  // adding the filtered list to the new array list
//                }
//
//            }
//        }
//
//
//        // Call the adapter to show the data
//
//        adapter = new NewsFeedAdapter(context, filteredList, itemClickListenter);
//
//        // setting the adapter ;
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }

    @Override
    protected void onStart() {

        super.onStart();
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

}