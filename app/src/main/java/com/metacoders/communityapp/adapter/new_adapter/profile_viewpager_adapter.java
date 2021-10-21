package com.metacoders.communityapp.adapter.new_adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.metacoders.communityapp.Fragments.CategoryFragment;
import com.metacoders.communityapp.Fragments.DashboardFragment;
import com.metacoders.communityapp.Fragments.NewsFragment;
import com.metacoders.communityapp.Fragments.PostFragment;

public class profile_viewpager_adapter extends FragmentStateAdapter {

  //  private String[] titles = new String[]{"Home", "Products", "Cart", "Profile"};

    public profile_viewpager_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new PostFragment("video");
//            case 1:
//                return new PostFragment();
//            case 2:
//                return new AudioFragment();
            case 1:
                return new PostFragment("audio");
            case 2:
                return new PostFragment("post");

        }

        return new PostFragment("news");
    }





    @Override
    public int getItemCount() {
        return 3;
    }

}
