package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    SharedPrefManager  manager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        manager = new SharedPrefManager( getApplicationContext()) ;

        getSupportActionBar().hide(); 

       Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {

               // to the activity
               loadMiscData();
           }
       }, 200);



    }

    private void loadMiscData() {
//        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
//        String accessTokens = sharedPrefManager.getUserToken();
//        Log.d("TAG", "loadList: activity " + accessTokens);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<allDataResponse> catCall = api.getCategoryList();

        catCall.enqueue(new Callback<allDataResponse>() {
            @Override
            public void onResponse(Call<allDataResponse> call, Response<allDataResponse> response) {

                if (response.code() == 201) {

                    allDataResponse dataResponse = response.body();

                    Intent p = new Intent(getApplicationContext() , HomePage.class);
                    p.putExtra("MISC" , dataResponse) ;

                    startActivity(p);

//                    categoryList = dataResponse.getCategories();
//                    languageList = dataResponse.getLanguageList();

                    // load all the category name
//                    for (int i = 0; i < categoryList.size(); i++) {
//                        categoryNameList.add(categoryList.get(i).getName().toString());
//
//                    }
//                    for (int i = 0; i < languageList.size(); i++) {
//                        languageNameList.add(languageList.get(i).getName().toString());
//
//                    }

                    // send it to adaper

                } else {
                    loadMiscData();
                }
            }

            @Override
            public void onFailure(Call<allDataResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext() , ""+ t.getMessage() , Toast.LENGTH_LONG).show();

            }
        });
    }
}