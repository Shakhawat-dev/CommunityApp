package com.metacoders.communityapp.activities.details;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.CategoryModel;
import com.metacoders.communityapp.models.newModels.CountryModel;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONObject;

import java.util.Collections;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditPostPage extends AppCompatActivity {
    Uri mediaUri = null;
    String uriPath = null;
    ImageView addImage;
    String postType;
    TextView percent;
    Dialog progressDialog;
    MultipartBody.Part body1;
    String countyID = "22";
    Spinner catgoerySelector, langugaNameSelector;
    String catid = "null", langid = "null";
    MultipartBody.Part body2;
    Uri FileUriSizeChecker = null;
    NewsRmeApi api;
    Intent o;
    SettingsModel settingsModel;
    Button submitBtn;
    TextInputEditText title, desc;
    String Title, Desc;
    SearchableSpinner searchableCountySpinner;
    Post.PostModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_page);
        catgoerySelector = findViewById(R.id.categoryList);
        settingsModel = SharedPrefManager.getInstance(getApplicationContext()).getAppSettingsModel();
        langugaNameSelector = findViewById(R.id.langList);
        searchableCountySpinner = findViewById(R.id.countrySpinner);
        title = findViewById(R.id.title_et);
        desc = findViewById(R.id.desc_et);
        model = (Post.PostModel) getIntent().getSerializableExtra("MODEL");

        AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Edit Post");

        loadMiscData();

        title.setText(model.getTitle() + "");
        desc.setText(model.getDescription() + "");

        catgoerySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CategoryModel categoryModel = (CategoryModel) parent.getSelectedItem();
                catid = categoryModel.getCategory_name();
                // Toast.makeText(getApplicationContext(), catid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                catid = "null";
            }
        });


        langugaNameSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                langid = parent.getSelectedItem().toString();

                if (langid.contains("English")) {
                    langid = "en";
                } else {
                    langid = "bn";
                }
                // Toast.makeText(getApplicationContext() ,catid + " "+ langid, Toast.LENGTH_SHORT ).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langid = "null";
            }
        });


        searchableCountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                CountryModel countryModel = (CountryModel) parent.getSelectedItem();

                countyID = countryModel.getName() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langid = "null";
            }
        });

        if (model.getLang().contains("en")) {
            langugaNameSelector.setSelection(0);
        } else {
            langugaNameSelector.setSelection(1);
        }
        catgoerySelector.setSelection(AppPreferences.getIndexOfSpinner(catgoerySelector, model.getCategory_id()));
        searchableCountySpinner.setSelection(AppPreferences.getCountrySpinner(searchableCountySpinner, model.getCountry().toString()));


        findViewById(R.id.publish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getType().contains("video")) {
                    uploadVideo();
                } else if (model.getType().contains("audio")) {
                    updateAudioFile();
                }else {
                    updatePost();
                }
            }
        });

    }

    private void updatePost() {
        api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Call<JSONObject> NetworkCall = api.updateArticle(model.getId() + "",
                langid, catid, countyID, title.getText().toString(), desc.getText().toString());

        NetworkCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Post Updated", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateAudioFile() {
        api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Call<JSONObject> NetworkCall = api.updateAudioPost(model.getId() + "",
                langid, catid, countyID, title.getText().toString(), desc.getText().toString());

        NetworkCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Post Updated", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadVideo() {
        api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
/*
 @Field("lang") String lang,
            @Field("category") String category,
            @Field("country") String country,
            @Field("title") String title,
            @Field("description") String description
 */
        Call<JSONObject> NetworkCall = api.updateVideo(model.getId() + "",
                langid, catid, countyID, title.getText().toString(), desc.getText().toString());

        NetworkCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(getApplicationContext(), "Post Updated", Toast.LENGTH_LONG).show();
                    finish();
                } else {

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    private void loadMiscData() {
        Collections.reverse(settingsModel.getCategories());
        Collections.reverse(settingsModel.getCountries());
        ArrayAdapter<CategoryModel> catgoery_adapter = new ArrayAdapter<CategoryModel>(EditPostPage.this,
                android.R.layout.simple_spinner_item, settingsModel.getCategories());
        catgoery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catgoerySelector.setAdapter(catgoery_adapter);

        String[] languages = {"English", "Bangla"};
        ArrayAdapter<String> langiage_adapter = new ArrayAdapter<String>(EditPostPage.this,
                android.R.layout.simple_spinner_item, languages);
        langiage_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langugaNameSelector.setAdapter(langiage_adapter);

        searchableCountySpinner.setTitle("Select Country");

        ArrayAdapter<CountryModel> country_adapter = new ArrayAdapter<CountryModel>(EditPostPage.this,
                android.R.layout.simple_spinner_item, settingsModel.getCountries());
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchableCountySpinner.setAdapter(country_adapter);


    }
}