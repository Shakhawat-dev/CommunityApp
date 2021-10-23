package com.metacoders.communityapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metacoders.communityapp.R;

public class ShopFragment extends Fragment {


    public ShopFragment() {
        // Required empty public constructor
    }
    View view ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_shop, container, false);

        return view ;
    }
}