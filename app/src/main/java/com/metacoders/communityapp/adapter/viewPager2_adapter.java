package com.metacoders.communityapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.metacoders.communityapp.Fragments.AudioFragment;
import com.metacoders.communityapp.Fragments.NewsFragment;
import com.metacoders.communityapp.Fragments.CategoryFragment;
import com.metacoders.communityapp.Fragments.VideoFragment;

public class viewPager2_adapter extends FragmentStateAdapter {

  //  private String[] titles = new String[]{"Home", "Products", "Cart", "Profile"};

    public viewPager2_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new NewsFragment();
            case 1:
                return new VideoFragment();
            case 2:
                return new AudioFragment();
            case 3:
                return new CategoryFragment();

        }

        return new NewsFragment();
    }


    @Override
    public int getItemCount() {
        return 4;
    }
}
