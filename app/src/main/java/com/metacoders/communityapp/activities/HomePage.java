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
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.contactPage.ContactPage;
import com.metacoders.communityapp.activities.countryWiseList.CountryList;
import com.metacoders.communityapp.adapter.viewPager2_adapter;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.models.newModels.UserModel;
import com.metacoders.communityapp.utils.Constants;
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
    CountryCodePicker countryCodePicker;
    boolean isGo = true;
    TextView logOut;
    List<String> idlist = new ArrayList<>();
    CardView myActonSide, jobSide, ShopSide, profileSide, logOUtCard, notificationSide, contact_us;
    List<String> langList = new ArrayList<>();
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
//                        case R.id.audio:
//                            viewPager.setCurrentItem(2, false);
//                            break;
//                        case R.id.video:
//                            viewPager.setCurrentItem(1, false);
//                            break;
                        case R.id.search:
                            viewPager.setCurrentItem(1, false);
                            break;
                        case R.id.dashboard:
                            if (SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn()) {

                                viewPager.setCurrentItem(2, false);
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
        countryCodePicker = findViewById(R.id.ccp11);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        userImage = navigationView.findViewById(R.id.proImg);
        contact_us = navigationView.findViewById(R.id.contact_us);
        userNameOnSide = navigationView.findViewById(R.id.user_name);
        hamburger = findViewById(R.id.menu);
        logOUtCard = navigationView.findViewById(R.id.LogOut);
        myActonSide = navigationView.findViewById(R.id.myAcnt);
        ShopSide = navigationView.findViewById(R.id.shop);
        logOut = navigationView.findViewById(R.id.LogOutTV);

        dataResponse = (allDataResponse) getIntent().getSerializableExtra("MISC");
        viewPager2Adapter = new viewPager2_adapter(HomePage.this);

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
//                    case 1:
//                        navigationBar.getMenu().findItem(R.id.video).setChecked(true);
//                        break;
//                    case 2:
//                        navigationBar.getMenu().findItem(R.id.audio).setChecked(true);
//                        break;
                    case 1:
                        navigationBar.getMenu().findItem(R.id.search).setChecked(true);
                        break;
                    case 2:
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

                SharedPrefManager pref = new SharedPrefManager(getApplicationContext());

                if (pref.isUserLoggedIn()) {
                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                        Log.d("TAG", "onCreate: " + e.getMessage());
                    }
                    Intent o = new Intent(getApplicationContext(), LoginActivity.class);
                    SharedPrefManager manager = new SharedPrefManager(getApplicationContext());
                    manager.logout();
                    o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(o);
                    finish();
                } else {
                    Intent p = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(p);
                }


            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactPage.class));
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


        countryCodePicker.overrideClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //your code here.....

                //using following code you can launch
                //ccp country selection dialog manually (if required)
                // ccp.launchCountrySelectionDialog();

                Intent p = new Intent(getApplicationContext(), CountryList.class);
                p.putExtra("countryCode", countryCodePicker.getSelectedCountryCode());
                p.putExtra("ph", countryCodePicker.getSelectedCountryNameCode());
                startActivity(p);
            }
        });



        lang.setOnClickListener(v -> {

            langList.add(Constants.ENGLISH_CODE + " English");
            langList.add(Constants.BD_CODE + " Bangla");
            // create a dialouge

            dialog = new Dialog(HomePage.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.language_dialouge);

            ListView list = dialog.findViewById(R.id.langlist);


            ArrayAdapter adapter = new ArrayAdapter<String>(HomePage.this,
                    android.R.layout.simple_list_item_1, langList);

            list.setAdapter(adapter);

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    // have to save this setting
                    if (position == 0) {
                        SharedPrefManager.getInstance(getApplicationContext()).saveLangPref(
                                position + "",
                                "EN");
                        lang.setText(Constants.ENGLISH_CODE);
                    } else {
                        SharedPrefManager.getInstance(getApplicationContext()).saveLangPref(
                                position + "",
                                "BD");

                        lang.setText(Constants.BD_CODE);
                    }


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
                        .load(model.getImage())
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

        try {
            //countryCodePicker.setCountryForNameCode(arr[1]);
        } catch (Exception e) {
            //  countryCodePicker.setCountryForNameCode("GB");
        }

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();


        countryCodePicker.setAutoDetectedCountry(true);
        SharedPrefManager pref = new SharedPrefManager(getApplicationContext());
        if (pref.isUserLoggedIn()) {
            logOut.setText("Log Out");
        } else {
            logOut.setText("Log In");
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

    }
}
