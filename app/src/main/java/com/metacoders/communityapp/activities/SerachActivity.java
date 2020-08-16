package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);
        searchView = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.newsfeed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        context = getApplicationContext() ;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setSearch(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }


    private void loadList() {


        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext()) ;
        String   accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , accessTokens) ;

        Call<News_List_Model> NetworkCall = api.getNewsList() ;

        NetworkCall.enqueue(new Callback<News_List_Model>() {
            @Override
            public void onResponse(Call<News_List_Model> call, Response<News_List_Model> response) {
                // u have the response
                if (response.code() == 201) {
                    News_List_Model model = response.body();

                    postsList = model.getGetNewsList();


                    if (postsList != null && !postsList.isEmpty()) {
                        // i know its werid but thats r8 cheaking list is popluted
                        // its not empty


                        // Call the adapter to show the data

                        adapter = new NewsFeedAdapter(context, postsList, itemClickListenter);

                        // setting the adapter ;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);


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
            public void onFailure(Call<News_List_Model> call, Throwable t) {
                Log.d("TAG", "Error On Failed Response: " + t.getMessage());
            }
        });
    }
    private  void setSearch(String searchTerm){


        for(int i  = 0 ; i< postsList.size() ; i++){

            if(postsList.get(i).getTitle().toLowerCase().contains(searchTerm)){
                filteredList.add(postsList.get(i)) ;  // adding the filtered list to the new array list
            }

        }
        // if country search  enabled then
        if(categorySearchEnabled){
            tempList  = new ArrayList<>(filteredList) ;
            filteredList.clear();

            for(int y  = 0 ; y< tempList.size() ; y++){

                if(tempList.get(y).getCategoryId().equals(categoryCode)){
                    filteredList.add(tempList.get(y)) ;  // adding the filtered list to the new array list
                }

            }
        }


        // Call the adapter to show the data

        adapter = new NewsFeedAdapter(context, filteredList, itemClickListenter);

        // setting the adapter ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        loadList();
        super.onStart();
    }
}