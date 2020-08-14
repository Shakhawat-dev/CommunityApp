package com.metacoders.communityapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
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
    ImageView addImage ;
    String postType ;
    ProgressDialog progressDialog ;
    MultipartBody.Part body1 ;
    MultipartBody.Part body2 ;
    NewsRmeApi api;
    Intent o ;
    Button submitBtn ;
    TextInputEditText title , desc ;
    String Title  , Desc ;

    Call<LoginResponse.forgetPassResponse> NetworkCall ;
    private Bitmap compressedImageFile;
    Uri mFilePathUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        progressDialog = new ProgressDialog(PostUploadActivity.this) ;
        progressDialog.setMessage("Uploading...");

        o =  getIntent() ;
        // take the media type ....
        postType = o.getStringExtra("media");
        // define Views ...
        addImage = findViewById(R.id.add_image);
        title = findViewById(R.id.title_et) ;
        desc = findViewById(R.id.desc_et);
        submitBtn = findViewById(R.id.publish_btn);

        addImage.setOnClickListener(v -> {

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

        });

        submitBtn.setOnClickListener(v->{

            Desc = desc.getText().toString() ;
            Title = title.getText().toString() ;

            if(!TextUtils.isEmpty(Title) || !TextUtils.isEmpty(Desc) || mFilePathUri != null){

                createPostServer(mFilePathUri , postType , Title , Desc);

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
                addImage.setImageURI(mFilePathUri);

               // createPostServer(mFilePathUri , postType);

                //sending data once  user select the image

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private void createPostServer(Uri mFilePathUri, String postType  ,String title , String desc ) {

        if(mFilePathUri != null) {
            // upload the data


            File file = new File(mFilePathUri.getPath());
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
            progressDialog.show();
            progressDialog.setCancelable(false);

             api = ServiceGenerator.createService(NewsRmeApi.class, getToken());

            if (postType.equals("post")) {

                //creating request body for file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);

                body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);


                NetworkCall = api.uploadPost( createPartFromString(title), createPartFromString(title.toLowerCase()),
                        createPartFromString(desc),
                        createPartFromString(postType), createPartFromString("1"),
                        createPartFromString("1"), createPartFromString("1"),
                        body1);



            } else {

                Intent p = getIntent() ;
                uriPath = p.getStringExtra("path");
                //media Uri has the all the link in it ;
                mediaUri = Uri.parse(uriPath) ;

                File mediaFile = null;

                if (mediaUri != null) {
                    mediaFile = new File(mediaUri.getPath());
                } else
                    Toast.makeText(getApplicationContext(), "Video Is empty", Toast.LENGTH_LONG).show();

                //creating request body for file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
                // changing media_type
                RequestBody requestMediaFile ;

                if(postType.equals("audio"))
                {
                    requestMediaFile = RequestBody.create(MediaType.parse("audio/mp3*"), mediaFile);

                    body2 = MultipartBody.Part.createFormData("audio", mediaFile.getName(), requestMediaFile);
                }
                else {
                    requestMediaFile = RequestBody.create(MediaType.parse("video/mp4"), mediaFile);
                    body2 = MultipartBody.Part.createFormData("video", mediaFile.getName(), requestMediaFile);
                }

                body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);



                NetworkCall = api.uploadFilePost(body2, createPartFromString(title), createPartFromString(title.toLowerCase()),
                        createPartFromString(desc),
                        createPartFromString(postType), createPartFromString("1"),
                        createPartFromString("1"), createPartFromString("1"),
                        body1);



            }

            NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
                @Override
                public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {

                    if (response.code() == 201) {
                        LoginResponse.forgetPassResponse testRes = response.body();
                        Toast.makeText(getApplicationContext(), testRes.getMessage() + "\n" + testRes.getError(), Toast.LENGTH_LONG)
                                .show();
                        progressDialog.dismiss();

                    } else {
                        Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG)
                                .show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG)
                            .show();
                    progressDialog.dismiss();
                }
            });

        }
        else{

                Toast.makeText(getApplicationContext(), "Please Pick An Image!!", Toast.LENGTH_LONG).show();
            }


    }
    @NonNull
    private RequestBody createPartFromString(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }

    private void BringImagePicker() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
                .start(this);




    }
    public  String getToken(){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
       return  accessTokens ;
    }


}