package com.metacoders.communityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.metacoders.communityapp.activities.PostDetailsPage;
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

public class singleList extends AppCompatActivity {

    RecyclerView recyclerView ;
    String searchMeta  = "" ;
    SharedPrefManager sharedPrefManager ;
    List<Post_Model> postsList = new ArrayList<>();
    List<Post_Model> filterList = new ArrayList<>();
    NewsFeedAdapter.ItemClickListenter itemClickListenter;
    NewsFeedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list);

        recyclerView = findViewById(R.id.list) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this ));
        Intent o  = getIntent();
        searchMeta = o.getStringExtra("id") ;



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

        loadList();
    }


    private void loadList() {


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , "00") ;

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

                        // process the list

                        for(int i = 0 ; i< postsList.size() ; i++){

                            if(postsList.get(i).getCategoryId().equals(searchMeta)){

                                // add to filltered list
                                filterList.add(postsList.get(i)) ;
                            }
                        }


                        adapter = new NewsFeedAdapter(getApplicationContext(), filterList, itemClickListenter);

                        // setting the adapter ;

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

}