package com.metacoders.communityapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.ChangePasswordActivity;
import com.metacoders.communityapp.activities.LoginActivity;
import com.metacoders.communityapp.adapter.CategoryAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.api.UploadResult;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.singleList;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
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
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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


    View view;
    Context context;
    RecyclerView recyclerView ;
    List<allDataResponse.Category>categoryList ;
    CategoryAdapter adapter  ;
    CategoryAdapter.ItemClickListenter itemClickListenter ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category, container, false);

        context = view.getContext();
        recyclerView = view.findViewById(R.id.categoryList) ;
        recyclerView.setLayoutManager(new GridLayoutManager(context , 2));

        itemClickListenter = new CategoryAdapter.ItemClickListenter() {
            @Override
            public void onItemClick(View view, int pos) {

                Intent o = new Intent(getContext() , singleList.class) ;
                o.putExtra("id" , categoryList.get(pos).getId()) ;
                startActivity(o);

            }
        } ;

        LoadData() ;


      return  view ;

    }

    private void LoadData() {
       SharedPrefManager sharedPrefManager = new SharedPrefManager(context) ;
        String   accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , accessTokens) ;

        Call<allDataResponse> catCall = api.getCategoryList();

        catCall.enqueue(new Callback<allDataResponse>() {
            @Override
            public void onResponse(Call<allDataResponse> call, Response<allDataResponse> response) {

                if(response.code() == 201){

                    allDataResponse dataResponse = response.body() ;

                    categoryList = dataResponse.getCategories() ;

                    // send it to adaper

                    adapter = new CategoryAdapter(context , categoryList , itemClickListenter) ;

                    recyclerView.setAdapter(adapter);




                }
                else {

                }
            }

            @Override
            public void onFailure(Call<allDataResponse> call, Throwable t) {

            }
        });
    }


}