package com.metacoders.communityapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.api.UploadResult;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

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
    ProgressDialog mprogressDialog;
    private Bitmap compressedImageFile;
    Uri mFilePathUri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false) ;
        context = view.getContext() ;
        mprogressDialog =new ProgressDialog(context);
        mprogressDialog.setMessage("Uploading The Image...");
        // views
        name = view.findViewById(R.id.user_name_txt) ;
        nameHeader = view.findViewById(R.id.profile_name_txt) ;
        phone = view.findViewById(R.id.user_phone_txt) ;
        email = view.findViewById(R.id.user_email_txt) ;
        address = view.findViewById(R.id.user_address_txt) ;
        emailHeader = view.findViewById(R.id.profile_email_txt) ;
        pp = view.findViewById(R.id.profile_pic) ;

        pp.setOnClickListener(v -> {
            // open the gallery to
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    BringImagePicker();



                } else {

                    BringImagePicker();

                }

            } else {

                BringImagePicker();

            }

        });

        LoadData() ;


        return view;
    }

    private void BringImagePicker() {


    CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL) //shaping the image
                .start(context ,this );




    }

    private void LoadData() {
        SharedPrefManager     sharedPrefManager = new SharedPrefManager(context) ;
        String   accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , accessTokens) ;

        Call<Profile_Model.Profile_Response> NetworkCall = api.getProfileInfo() ;

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
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(pp);



        }
    }

    @Override
    public void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mFilePathUri = result.getUri();

                pp.setImageURI(mFilePathUri);
                //    uploadPicToServer(mFilePathUri) ;

                uploadProfilePicToServer(mFilePathUri);

                //sending data once  user select the image

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void uploadProfilePicToServer(Uri mFilePathUri) {

        pp.setImageURI(mFilePathUri);
        mprogressDialog.show();
        mprogressDialog.setMessage("Uploading New Image");

        // call for network

        File file = new File(mFilePathUri.getPath());

        File compressed;

        try {
            compressed = new Compressor(context)
                    .setMaxHeight(600)
                    .setMaxWidth(600)
                    .setQuality(50)
                    .compressToFile(file);
        } catch (Exception e) {
            compressed = file;
        }


        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
        //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);


        Call<UploadResult> call = RetrofitClient
                .getInstance()
                .getApi()
                .uploadImage(requestFile);



        call.enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {


                if (response.code() == 201) {
                    UploadResult result = response.body();

                    if(result.isError())
                    {
                        Toast.makeText(context, result.getMessage() , Toast.LENGTH_LONG).show();

                    }
                    else {
                        Toast.makeText(context, result.getMessage() , Toast.LENGTH_LONG).show();
                    }


                  //  updatedLink = constants.DWLDURL + result.getMsg().toString();
                 //   isImageUploaded = true;
                    mprogressDialog.dismiss();
                }
                else {
                    mprogressDialog.dismiss();
                 //   isImageUploaded = false;
                    Toast.makeText(context, "SomeTHing Went Wrong. Please  Try Again ! " + response.code(), Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                Log.d("RRR", t.getMessage().toUpperCase().toString());

                mprogressDialog.dismiss();
              //  isImageUploaded = false;
                Toast.makeText(context, "SomeTHing Went Wrong Please  Try Again", Toast.LENGTH_LONG)
                        .show();
            }
        });

    }
}