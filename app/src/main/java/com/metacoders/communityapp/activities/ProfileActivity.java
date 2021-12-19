package com.metacoders.communityapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100, PICK_IMAGE_DOC = 580;
    Context context;
    TextView nameHeader, emailHeader, name, phone, email, address, user_bio, zipCode, country, gender, website, company;
    CircleImageView pp;
    ProgressDialog mprogressDialog;
    CardView changePassCard, LogOutCard, addDoc;
    Uri mFilePathUri;
    ProgressBar progressBar;

    Profile_Model model = null;
    File file;
    private Bitmap compressedImageFile;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = ProfileActivity.this;

        mprogressDialog = new ProgressDialog(context);
        mprogressDialog.setMessage("Uploading The Image...");
        progressBar = findViewById(R.id.pbar);

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
        //   addDoc = findViewById(R.id.add_card);
        user_bio = findViewById(R.id.user_bio);
        zipCode = findViewById(R.id.zipCodeTxt);
        country = findViewById(R.id.country_name);
        gender = findViewById(R.id.gender);
        website = findViewById(R.id.websiteName);
        company = findViewById(R.id.companyName);
        RequestPermission();

        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Profile");
        } catch (Exception e) {

        }

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

        LogOutCard.setOnClickListener(v -> {

            try {
                LoginManager.getInstance().logOut();
            } catch (Exception e) {
                Log.d("TAG", "onCreate: " + e.getMessage());
            }
            Intent o = new Intent(context, LoginActivity.class);
            SharedPrefManager manager = new SharedPrefManager(context);
            manager.logout();
            o.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(o);
            try {
                finish();
            } catch (Exception e) {

            }
        });

        findViewById(R.id.edit_myProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent o = new Intent(context, EditProfile.class);
                startActivity(o);


            }
        });

        changePassCard.setOnClickListener(v -> {

            Intent o = new Intent(context, ChangePasswordActivity.class);
            startActivity(o);

        });

//        addDoc.setOnClickListener(view -> {
//
//            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            startActivityForResult(i, PICK_IMAGE_DOC);
//        });

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


        RequestPermission();
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);


    }

    public void loadUrPost() {

        progressBar.setVisibility(View.VISIBLE);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(context));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(SharedPrefManager.getInstance(getApplicationContext()).getUser_ID() + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    try {

                        AuthorPostResponse ownListModelList = response.body();
                        SharedPrefManager.getInstance(getApplicationContext()).saveUserModel(ownListModelList.getAuthor());
                        setData(ownListModelList);

                    } catch (Exception e) {

                        Toast.makeText(context, "Error : Code " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(context, "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthorPostResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setData(AuthorPostResponse singleProfile) {
        if (singleProfile != null) {
            nameHeader.setText(singleProfile.getAuthor().getName());
            name.setText(singleProfile.getAuthor().getName());
            address.setText(singleProfile.getAuthor().getAddress());
            email.setText(singleProfile.getAuthor().getEmail());
            emailHeader.setText(singleProfile.getAuthor().getEmail());
            phone.setText(singleProfile.getAuthor().getPhone());
            user_bio.setText(singleProfile.getAuthor().getBio());
            zipCode.setText(singleProfile.getAuthor().getZip_code());
            country.setText(singleProfile.getAuthor().getCountry());
            gender.setText(singleProfile.getAuthor().getGender());
            website.setText(singleProfile.getAuthor().getWebsite());
            company.setText(singleProfile.getAuthor().getCompany());

            // load the proifle image

            String gender = singleProfile.getAuthor().getGender();
            String link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/TgBDz5Ti5AZiposXXwvRmTKP1VpIJouIctyaILih.png";
            try {
                if (gender.toLowerCase(Locale.ROOT).contains("fe")) {
                    link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/Vzsa4eUZNCmRvuNVUWToGyu0Xobb6DyQgcX4oDoI.png\n";
                } else {
                    link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/TgBDz5Ti5AZiposXXwvRmTKP1VpIJouIctyaILih.png";
                }
            } catch (Exception e) {

            }
            Glide.with(context).load(singleProfile.getAuthor().getImage())
                    .placeholder(R.drawable.placeholder)
                    .error(link)
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
                RequestPermission();
                mFilePathUri = result.getUri();

                //    uploadPicToServer(mFilePathUri) ;
                pp.setImageURI(mFilePathUri);

                uploadProfilePicToServer(mFilePathUri);

                // createPostServer(mFilePathUri , postType);

                //sending data once  user select the image

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PICK_IMAGE) {

            //   mFilePathUri = data.getData();
            // Toast.makeText(getApplicationContext() , "TEst" + mFilePathUri.toString(), Toast.LENGTH_LONG) .show();
            try {
                Uri selectedMediaUri = data.getData();
                if (!Uri.EMPTY.equals(selectedMediaUri)) {
                    mFilePathUri = selectedMediaUri;
                    // sendTheFile(selectedMediaUri);
                    RequestPermission();

                    CropImage.activity(mFilePathUri)
                            .setAspectRatio(1, 1)
                            .setCropShape(CropImageView.CropShape.OVAL) //shaping the image
                            .start(this);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Choose Image To Upload " + selectedMediaUri.toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please Choose Image " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        } else if (requestCode == PICK_IMAGE_DOC) {

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
        //  Log.d("TAG", "loadList: activity " + accessTokens);


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
        mprogressDialog.setMessage("Uploading New Image...");
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

        String name = SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getName();

        MultipartBody.Part body1 = MultipartBody.Part.createFormData("image", compressed.getName(), requestFile);
        ///  take token
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Call<LoginResponse.forgetPassResponse> call = api.uploadImage(
                AppPreferences.getUSerID(getApplicationContext()), createPartFromString(name), body1,
                createPartFromString(SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getCountry()),
                createPartFromString(SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getGender())

        );

        call.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {


                if (response.code() == 200) {
                    LoginResponse.forgetPassResponse result = response.body();
                    Toast.makeText(context, "Msg" + result.getMessage(), Toast.LENGTH_LONG)
                            .show();
                    mprogressDialog.dismiss();
                } else {
                    mprogressDialog.dismiss();
                    //   isImageUploaded = false;
                    Toast.makeText(context, "SomeThing Went Wrong.Please Try Again !!!" + response.code(), Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
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

    private RequestBody createPartFromString(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUrPost();
    }


}