package com.metacoders.communityapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.viewPager2_adapter;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.models.newModels.UserModel;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    ImageView hamburger, searchBtn, profileBtn, userImage;
    ViewPager2 viewPager;
    FloatingActionButton emergencyFuel;
    allDataResponse dataResponse;
    TextView lang;
    TextView userNameOnSide;
    Dialog dialog;
    List<String> idlist = new ArrayList<>();
    CardView myActonSide, jobSide, ShopSide, profileSide, logOUtCard, notificationSide;
    List<allDataResponse.LanguageList> langList;
    private DrawerLayout drawerLayout;
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =

            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    //   Fragment selectedFragmnet = null ;

                    switch (menuItem.getItemId()) {
                        case R.id.newsfeed:
                            viewPager.setCurrentItem(0, false);
                            break;
                        case R.id.audio:
                            viewPager.setCurrentItem(2, false);
                            break;
                        case R.id.video:
                            viewPager.setCurrentItem(1, false);
                            break;
                        case R.id.profile:
                            viewPager.setCurrentItem(3, false);
                            break;
                        case R.id.dashboard:
                            if (SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn()) {

                                viewPager.setCurrentItem(4, false);
                            } else {

                                createTheAlertDialogue();
                            }

                            break;

                    }
                    //      getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, selectedFragmnet).commit();

                    return true;

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        viewPager2_adapter viewPager2Adapter;

        getSupportActionBar().hide();
        RequestPermission();
        final BottomNavigationView navigationBar = findViewById(R.id.bottom_navigation_);
        viewPager = findViewById(R.id.view_pager);
        drawerLayout = findViewById(R.id.drawer_layout);
        profileBtn = findViewById(R.id.profileBtn);
        searchBtn = findViewById(R.id.searchBtn);
        lang = findViewById(R.id.langId);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        userImage = navigationView.findViewById(R.id.proImg);
        userNameOnSide = navigationView.findViewById(R.id.user_name);
        hamburger = findViewById(R.id.menu);
        logOUtCard = navigationView.findViewById(R.id.LogOut);
        myActonSide = navigationView.findViewById(R.id.myAcnt);
        ShopSide = navigationView.findViewById(R.id.shop);


        dataResponse = (allDataResponse) getIntent().getSerializableExtra("MISC");
        viewPager2Adapter = new viewPager2_adapter(HomePage.this);
        // navigationView = findViewById(R.id.navigation_view);


        //   Log.d("detais", "onCreate: " + SharedPrefManager.getInstance(getApplicationContext()).getUser().getName());
        //    Log.d("detais", "onCreate: " + SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn());

        navigationBar.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        // getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, new dashboardFragment()).commit();
        viewPager.setAdapter(viewPager2Adapter);
        viewPager.setUserInputEnabled(false);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        navigationBar.getMenu().findItem(R.id.newsfeed).setChecked(true);
                        break;
                    case 1:
                        navigationBar.getMenu().findItem(R.id.video).setChecked(true);
                        break;
                    case 2:
                        navigationBar.getMenu().findItem(R.id.audio).setChecked(true);
                        break;
                    case 3:
                        navigationBar.getMenu().findItem(R.id.profile).setChecked(true);
                        break;
                    case 4:
                        navigationBar.getMenu().findItem(R.id.dashboard).setChecked(true);
                        break;

                }

                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        ShopSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent p = new Intent(getApplicationContext(), ShopPage.class);
                startActivity(p);
            }
        });

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        logOUtCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    LoginManager.getInstance().logOut();
                } catch (Exception e) {
                    Log.d("TAG", "onCreate: " + e.getMessage());
                }
                Intent o = new Intent(getApplicationContext(), HomePage.class);
                SharedPrefManager manager = new SharedPrefManager(getApplicationContext());
                manager.logout();
                o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(o);
                try {
                    finish();
                } catch (Exception e) {

                }
            }
        });

        myActonSide.setOnClickListener(v -> {

            if (SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
                Intent p = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(p);
            } else {
                createTheAlertDialogue();
            }


        });

        searchBtn.setOnClickListener(v -> {
            Intent p = new Intent(getApplicationContext(), SerachActivity.class);
            startActivity(p);
        });
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // trigger a dialogue


//                Toast.makeText(getApplicationContext(), "saved:", Toast.LENGTH_LONG).show();
//                String[] arr = SharedPrefManager.getInstance(getApplicationContext()).getLangPref();
//                Log.d("TAG", "onClick: " + arr[0] + arr[1]);


                // create a dialouge

                dialog = new Dialog(HomePage.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.language_dialouge);

                ListView list = dialog.findViewById(R.id.langlist);


                ArrayAdapter adapter = new ArrayAdapter<String>(HomePage.this,
                        android.R.layout.simple_list_item_1, generateLangArray());

                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);
                        // have to save this setting
                        SharedPrefManager.getInstance(getApplicationContext()).saveLangPref(
                                langList.get(position).getId(),
                                langList.get(position).getShortForm());


                        lang.setText(langList.get(position).getShortForm().toUpperCase() + "");
                        // now save the past
                        int oldid = viewPager.getCurrentItem();

                        dialog.dismiss();
                        // reload the view pager
                        viewPager.setAdapter(viewPager2Adapter);
                        // get back to the past
                        viewPager.setCurrentItem(oldid);

                    }

                });

                dialog.setCancelable(true);
                dialog.show();


            }
        });

        setUpSideBar();
        loadMiscData();

    }

    private void setUpSideBar() {
        navigationView.setNavigationItemSelectedListener(this);

        if (SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
            UserModel model = SharedPrefManager.getInstance(getApplicationContext()).getUserModel();

            userNameOnSide.setText(model.getName());
            try {
                Glide.with(getApplicationContext())
                        .load( model.getImage())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(userImage);
            } catch (Exception er) {
                Toast.makeText(getApplicationContext(), "user image error  " + er.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }


        } else {

        }


    }

    private void createTheAlertDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);

        builder.setTitle("Please Login !!");
        builder.setMessage("You Have To Logged In To View This Please");
        builder.setCancelable(false);
        builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent p = new Intent(HomePage.this, LoginActivity.class);
                startActivity(p);


            }
        });
        builder.setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                try {


                } catch (Exception e) {

                }


            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void loadMiscData() {
        String[] arr = SharedPrefManager.getInstance(getApplicationContext()).getLangPref();
        // load the array  arr[0] = id arr[1] = name
        lang.setText(arr[1] + "");

    }

    private void RequestPermission() {

        Dexter.withContext(HomePage.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    private String[] generateLangArray() {

        langList = dataResponse.getLanguageList();
        // List<String> langlist = new ArrayList<>();
        String[] langArray = new String[langList.size()];

        for (int i = 0; i < langList.size(); i++) {

            langArray[i] = langList.get(i).getName();


        }
        return langArray;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
