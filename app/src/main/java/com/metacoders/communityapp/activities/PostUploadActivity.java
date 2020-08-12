package com.metacoders.communityapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.api.UploadResult;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostUploadActivity extends AppCompatActivity {

    Uri mediaUri = null ;
    String uriPath = null ;
    ImageView image_View ;

    private Bitmap compressedImageFile;
    Uri mFilePathUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        Intent o =  getIntent() ;
        uriPath = o.getStringExtra("path");
        //media Uri has the all the link in it ;
        mediaUri = Uri.parse(uriPath) ;
        // now do whatever u  neeed

        findViewById(R.id.add_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open the gallery to
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(PostUploadActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                        BringImagePicker();



                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }
        });


    }
    @Override
    public void onActivityResult(/*int requestCode, int resultCode, @Nullable Intent data*/
            int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mFilePathUri = result.getUri();

                //    uploadPicToServer(mFilePathUri) ;

                createPostServer(mFilePathUri);

                //sending data once  user select the image

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void createPostServer(Uri mFilePathUri) {
        if(mFilePathUri != null){
            // upload the data
            File mediaFile  = null;
            File file = new File(mFilePathUri.getPath());
            if( mediaUri != null)
            {
                mediaFile  = new File(mediaUri.getPath()) ;
            }
            else Toast.makeText(getApplicationContext() , "Video IS empty" , Toast.LENGTH_LONG).show();

            File compressed;

            try {
                compressed = new Compressor(getApplicationContext())
                        .setMaxHeight(600)
                        .setMaxWidth(600)
                        .setQuality(50)
                        .compressToFile(file);
            } catch (Exception e) {
                compressed = file;
            }

            //creating request body for file
           RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
            RequestBody requestMediaFile = RequestBody.create(MediaType.parse("video/mp4"), mediaFile);

            //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);

            MultipartBody.Part body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile) ;
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("video" , mediaFile.getName()  , requestMediaFile) ;

            SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext()) ;
            String   accessTokens = sharedPrefManager.getUserToken();
            NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , accessTokens) ;
            Call<LoginResponse.forgetPassResponse> NetworkCall = api .uploadPost(body2 , createPartFromString("this is form Mobile") , createPartFromString("this is from mobile"),
                    createPartFromString("Content"),
                    createPartFromString("video") ,createPartFromString("1"),
                    createPartFromString("1") , createPartFromString("1"),
                    body1) ;



            NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
                @Override
                public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {

                    if(response.code() == 201){
                        LoginResponse.forgetPassResponse testRes = response.body() ;
                        Toast.makeText(getApplicationContext() , testRes.getMessage() + "\n"+ testRes.getError() , Toast.LENGTH_LONG)
                                .show();

                    }else {
                        Toast.makeText(getApplicationContext() ,""+ response.code() , Toast.LENGTH_LONG)
                                .show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext() , t.getMessage() , Toast.LENGTH_LONG)
                            .show();

                }
            });
        }
    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }
    private void BringImagePicker() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
                .start(this);




    }
}