package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.utils.Constants;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view ;
    Context context ;
    TextView  nameHeader , emailHeader , name , phone , email , address ;
    CircleImageView pp  ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false) ;
        context = view.getContext() ;
        // views
        name = view.findViewById(R.id.user_name_txt) ;
        nameHeader = view.findViewById(R.id.profile_name_txt) ;
        phone = view.findViewById(R.id.user_phone_txt) ;
        email = view.findViewById(R.id.user_email_txt) ;
        address = view.findViewById(R.id.user_address_txt) ;
        emailHeader = view.findViewById(R.id.profile_email_txt) ;
        pp = view.findViewById(R.id.profile_pic) ;


        LoadData() ;


        return view;
    }

    private void LoadData() {
        Call<Profile_Model.Profile_Response> NetworkCall = RetrofitClient
                .getInstance()
                .getApi()
                .getProfileInfo();

        NetworkCall.enqueue(new Callback<Profile_Model.Profile_Response>() {
            @Override
            public void onResponse(Call<Profile_Model.Profile_Response> call, Response<Profile_Model.Profile_Response> response) {

                if(response.code() == 201)
                {
                    // model the response
                    Profile_Model.Profile_Response  models  = response.body() ;
                    // now get into it
                    Profile_Model singleProfile = models.getProfileInfo() ;

                    setData(singleProfile) ;

                }
                else {
                    Log.d("TAG", "onFailure: " +  response.errorBody());
                }


            }

            @Override
            public void onFailure(Call<Profile_Model.Profile_Response> call, Throwable t) {
                Log.d("TAG", "onFailure: " +  t.getMessage());
            }
        });
    }

    private void setData(Profile_Model singleProfile) {
        if(singleProfile != null) {
           nameHeader.setText(singleProfile.getName());
           name.setText(singleProfile.getName());
           address.setText(singleProfile.getAddress());
           email.setText(singleProfile.getEmail());
           emailHeader.setText(singleProfile.getEmail());
            // load the proifle image
            Glide.with(context).load(Constants.IMAGE_URL + singleProfile.getAvatar())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(pp);



        }
    }
}