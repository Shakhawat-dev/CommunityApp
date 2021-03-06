package com.metacoders.communityapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.metacoders.communityapp.BuildConfig;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    SharedPrefManager manager;
    TextView versionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        versionTv = findViewById(R.id.versionName);
        manager = new SharedPrefManager(getApplicationContext());

        getSupportActionBar().hide();

        try {
            versionTv.setText(BuildConfig.VERSION_NAME + "");
        } catch (Exception e) {

        }


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean fstart = prefs.getBoolean("firstStart", true);
        if (fstart) {
            //  show_Dialog_after_Install();

        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                LoadData();

                //  createAShortLink();
                // throw new RuntimeException("Test Crash");

            }
        }, 200);

        //printKeyHash();


    }


    private void printKeyHash() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.metacoders.communityapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {


                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    private void show_Dialog_after_Install() {

        SharedPrefManager.getInstance(getApplicationContext()).logout();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }


    private void LoadData() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<SettingsModel> catCall = api.getCategories_Countries();

        catCall.enqueue(new Callback<SettingsModel>() {
            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {

                if (response.code() == 200) {

                    SettingsModel dataResponse = response.body();
                    try {
                        manager.saveAppSettings(dataResponse);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // send it to adaper

                    try {
                        checkDynamicData();
                    }catch (Exception e){
                        goToHome();
                    }


                } else {

                    Toast.makeText(getApplicationContext(), "Please Check Server Error " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SettingsModel> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Please Check Server Error " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }


    private void checkDynamicData() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).
                addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {

                        // now we  get the dynamic link
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {

                            deepLink = pendingDynamicLinkData.getLink();

                            String id = deepLink.getQueryParameter("id");
                            Log.d("TAG", "onSuccess: " + id);
                            if (id != null) {
                                // came from link
                                // cehck if is logged in
                                if (SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
                                    Toast.makeText(getApplicationContext(), "You Are All ready Registered User", Toast.LENGTH_SHORT).show();
                                    goToHome();
                                }else {
                                    Intent p = new Intent(getApplicationContext(), RegistrationActivity.class);
                                     p.putExtra("ID", id);
                                     p.putExtra("IS_DEEP_LINK", true );
                                    startActivity(p);
                                    finish();

                                }


                            } else {
                                goToHome();
                            }

                        } else {

                            goToHome();

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                goToHome();
            }
        });

    }

    private void goToHome() {

        if (SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
            // to the activity
            Intent p = new Intent(getApplicationContext(), HomePage.class);
            // p.putExtra("MISC", dataResponse);
            startActivity(p);
            finish();
        } else {
            // to the activity
            Intent p = new Intent(getApplicationContext(), LoginActivity.class);
            // p.putExtra("MISC", dataResponse);
            startActivity(p);
            finish();
        }
    }


}