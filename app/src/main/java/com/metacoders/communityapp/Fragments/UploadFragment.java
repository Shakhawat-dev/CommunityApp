package com.metacoders.communityapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.PostUploadActivity;
import com.metacoders.communityapp.activities.Video_Record_Activity;
import com.metacoders.communityapp.activities.Voice_Recoder_Activity;
import com.metacoders.communityapp.utils.SharedPrefManager;

public class UploadFragment extends Fragment {

    public UploadFragment() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        view.findViewById(R.id.videoUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getContext()).isUserLoggedIn()) {
                    Intent o = new Intent(getContext(), Video_Record_Activity.class);
                    startActivity(o);
                } else {
                 //   createTheAlertDialogue();
                }
            }
        });

        view.findViewById(R.id.audioUpload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getContext()).isUserLoggedIn()) {
                    Intent o = new Intent(getContext(), Voice_Recoder_Activity.class);
                    startActivity(o);
                } else {
                    //   createTheAlertDialogue();
                }
            }
        });

        view.findViewById(R.id.articlUplaod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(getContext()).isUserLoggedIn()) {
                    Intent o = new Intent(getContext(), PostUploadActivity.class);
                    o.putExtra("media", "post");
                    startActivity(o);
                } else {
                    //   createTheAlertDialogue();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}