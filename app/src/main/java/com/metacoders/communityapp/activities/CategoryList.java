package com.metacoders.communityapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.CategoryAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.CategoryModel;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.singleList;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryList extends AppCompatActivity {
    RecyclerView recyclerView;
    List<CategoryModel> categoryList;
    CategoryAdapter adapter;
    ProgressBar progressBar;
    CategoryAdapter.ItemClickListenter itemClickListenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        progressBar = findViewById(R.id.pbar);
        progressBar.setVisibility(View.GONE);
        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Categories");
        } catch (Exception e) {

        }
        itemClickListenter = (view, pos) -> {

            Intent o = new Intent(getApplicationContext(), singleList.class);
            o.putExtra("cat_name", categoryList.get(pos).getSlug());
            o.putExtra("name", categoryList.get(pos).getCategory_name());
            o.putExtra("type", "cat");
            startActivity(o);

        };

        LoadData();
    }

    private void LoadData() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<SettingsModel> catCall = api.getCategories_Countries();

        catCall.enqueue(new Callback<SettingsModel>() {
            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {

                    SettingsModel dataResponse = response.body();

                    try {
                        categoryList = dataResponse.getCategories();
                        Collections.reverse(categoryList);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // send it to adaper

                    adapter = new CategoryAdapter(getApplicationContext(), categoryList, itemClickListenter);

                    recyclerView.setAdapter(adapter);


                } else {
                    Toast.makeText(getApplicationContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SettingsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}