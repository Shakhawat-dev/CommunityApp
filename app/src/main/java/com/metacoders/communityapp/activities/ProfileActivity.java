package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.api.UploadResult;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.models.RegistrationResponse;
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

public class ProfileActivity extends AppCompatActivity {

    Context context;
    TextView nameHeader, emailHeader, name, phone, email, address;
    CircleImageView pp;
    ProgressDialog mprogressDialog;
    CardView changePassCard, LogOutCard, addDoc;
    public static int PICK_IMAGE = 50000;
    private Bitmap compressedImageFile;
    Uri mFilePathUri;
    Profile_Model model  = null;

    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = ProfileActivity.this;

        mprogressDialog = new ProgressDialog(context);
        mprogressDialog.setMessage("Uploading The Image...");


        // views
        name = findViewById(R.id.user_name_txt);
        changePassCard = findViewById(R.id.change_pass_card);
        nameHeader = findViewById(R.id.profile_name_txt);
        phone = findViewById(R.id.user_phone_txt);
        email = findViewById(R.id.user_email_txt);
        address = findViewById(R.id.user_address_txt);
        emailHeader = findViewById(R.id.profile_email_txt);
        pp = findViewById(R.id.profile_pic);
        LogOutCard = findViewById(R.id.logout_card);
        addDoc = findViewById(R.id.add_card);
        RequestPermission();

        LogOutCard.setOnClickListener(v -> {

            Intent o = new Intent(context, HomePage.class);
            SharedPrefManager manager = new SharedPrefManager(context);
            manager.logout();
            startActivity(o);
            try {
                finish();
            } catch (Exception e) {

            }
        });

        findViewById(R.id.edit_myProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model != null){
                    Intent o = new Intent(context, EditProfile.class);
                    o.putExtra("MODEL" , model) ;
                    startActivity(o);
                }

            }
        });

        changePassCard.setOnClickListener(v -> {

            Intent o = new Intent(context, ChangePasswordActivity.class);
            startActivity(o);

        });

        addDoc.setOnClickListener(view -> {

            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE);
        });

        pp.setOnClickListener(v -> {
            // open the gallery to
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    BringImagePicker();


                } else {

                    BringImagePicker();

                }

            } else {

                BringImagePicker();

            }

        });




    }


    private void BringImagePicker() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL) //shaping the image
                .start(ProfileActivity.this);


    }

    private void LoadData() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String accessTokens = sharedPrefManager.getUserToken();
//        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);

        Call<Profile_Model.Profile_Response> NetworkCall = api.getProfileInfo();

        NetworkCall.enqueue(new Callback<Profile_Model.Profile_Response>() {
            @Override
            public void onResponse(Call<Profile_Model.Profile_Response> call, Response<Profile_Model.Profile_Response> response) {

                if (response.code() == 201) {
                    // model the response
                    Profile_Model.Profile_Response models = response.body();
                    // now get into it
                    Profile_Model singleProfile = models.getProfileInfo();
                    model = singleProfile ;
                    setData(singleProfile);

                } else {
                    Log.d("TAG", "onFailure: " + response.errorBody());
                }


            }

            @Override
            public void onFailure(Call<Profile_Model.Profile_Response> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }

    private void setData(Profile_Model singleProfile) {
        if (singleProfile != null) {
            nameHeader.setText(singleProfile.getName());
            name.setText(singleProfile.getName());
            address.setText(singleProfile.getAddress());
            email.setText(singleProfile.getEmail());
            emailHeader.setText(singleProfile.getEmail());
            phone.setText(singleProfile.getMobile());
            // load the proifle image
            Glide.with(context).load(Constants.IMAGE_URL + singleProfile.getAvatar())
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(pp);

            Log.d("TAG", "setData:  " + Constants.IMAGE_URL + singleProfile.getDocument());


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
        } else if (requestCode == PICK_IMAGE) {

            //   mFilePathUri = data.getData();
            // Toast.makeText(getApplicationContext() , "TEst" + mFilePathUri.toString(), Toast.LENGTH_LONG) .show();
            Uri selectedMediaUri = data.getData();

            mFilePathUri = selectedMediaUri;
            uploadDocumentation(selectedMediaUri);


        }


       // uploadDocumentation(mFilePathUri);
    }


    private void uploadDocumentation(Uri mFilePathUri) {

        mprogressDialog.show();
        mprogressDialog.setMessage("Uploading Your Document Image");

        // call for network

        File file = new File(getPath(ProfileActivity.this, mFilePathUri));

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

        ///  take token

        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);

        Call<RegistrationResponse> call = api.sendDOc(requestFile);

        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                if (response.code() == 201) {
                    mprogressDialog.dismiss();
                    if (response.body().getError()) {
                        Toast.makeText(getApplicationContext(), "Something Went Wrong !!" + response.body().getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        mprogressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Document Image Uploaded  !!" + response.body().getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    mprogressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something Went Wrong !!" + response.code(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                mprogressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error!" + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void uploadProfilePicToServer(Uri mFilePathUri) {

        RequestPermission();
        pp.setImageURI(mFilePathUri);
        mprogressDialog.show();
        mprogressDialog.setMessage("Uploading New Image");
        String path;
        File file;

        try {
            path = getPath(ProfileActivity.this, mFilePathUri);
            file = new File(path);
        } catch (Exception e) {

            path = mFilePathUri.getPath();
            file = new File(path);
        }

        // call for network
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

        ///  take token

        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);

        Call<UploadResult> call = api.uploadImage(requestFile);


        call.enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {


                if (response.code() == 201) {
                    UploadResult result = response.body();

                    if (result.isError()) {
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, result.getMessage(), Toast.LENGTH_LONG).show();
                    }


                    //  updatedLink = constants.DWLDURL + result.getMsg().toString();
                    //   isImageUploaded = true;
                    mprogressDialog.dismiss();
                } else {
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

    private void RequestPermission() {

        Dexter.withContext(ProfileActivity.this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).onSameThread().check();
    }

    public static String getPath(Context ctx, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(ctx, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadData();
    }
}