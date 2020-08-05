package com.metacoders.communityapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.viewPager2_adapter;

public class HomePage extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    NavigationView navigationView ;
    ImageView hamburger ;
    ViewPager2 viewPager ;
    FloatingActionButton emergencyFuel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getSupportActionBar().hide();

        final BottomNavigationView navigationBar = findViewById(R.id.bottom_navigation_) ;
        viewPager = findViewById(R.id.view_pager) ;
        drawerLayout = findViewById(R.id.drawer_layout);


        // navigationView = findViewById(R.id.navigation_view);
        navigationBar.setOnNavigationItemSelectedListener(navigationItemSelectedListener) ;
        // getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, new dashboardFragment()).commit();
        viewPager.setAdapter(new viewPager2_adapter(HomePage.this));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
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
                }

                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private     BottomNavigationView.OnNavigationItemSelectedListener  navigationItemSelectedListener =

            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    //   Fragment selectedFragmnet = null ;

                    switch ( menuItem.getItemId())
                    {
                        case R.id.newsfeed :
                            viewPager.setCurrentItem(0 ,false);
                            break;
                        case R.id.audio :
                            viewPager.setCurrentItem(2 ,false);
                            break;
                        case R.id.video :
                            viewPager.setCurrentItem(1 ,false);
                            break;
                        case R.id.profile :
                            viewPager.setCurrentItem(3 ,false);
                            break;

                    }
                    //      getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, selectedFragmnet).commit();

                    return  true ;

                }
            } ;
}
