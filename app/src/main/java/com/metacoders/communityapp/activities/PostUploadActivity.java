package com.metacoders.communityapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.CategoryAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.utils.CallBacks;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.PlayerManager;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

public class PostUploadActivity extends AppCompatActivity implements CallBacks.playerCallBack {

    Uri mediaUri = null;
    String uriPath = null;
    ImageView addImage;
    String postType;
    ProgressDialog progressDialog;
    MultipartBody.Part body1;
    MultipartBody.Part body2;
    NewsRmeApi api;
    Intent o;
    Button submitBtn;
    Chip chip;
    TextInputEditText title, desc;
    String Title, Desc;
    List<allDataResponse.Category> categoryList;
    List<String> categoryNameList = new ArrayList<>();
    List<allDataResponse.LanguageList> languageList = new ArrayList<>();
    List<String> languageNameList = new ArrayList<>();
    Call<LoginResponse.forgetPassResponse> NetworkCall;
    Spinner catgoerySelector, langugaNameSelector;
    String catid = "null", langid = "null";
    private Bitmap compressedImageFile;
    Uri mFilePathUri = null;
    PlayerView playerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        chip = findViewById(R.id.chip);
        playerView = findViewById(R.id.player_view);
        playerView.setUseArtwork(true);
        playerView.setPlayer(PlayerManager.getSharedInstance(PostUploadActivity.this).getPlayerView().getPlayer());
        PlayerManager.getSharedInstance(this).setPlayerListener(this);
        progressDialog = new ProgressDialog(PostUploadActivity.this);
        progressDialog.setMessage("Uploading...");

        o = getIntent();
        // take the media type ....
        postType = o.getStringExtra("media");
        if (!postType.contains("post")) {

          //  Toast.makeText(getApplicationContext(), getIntent().getStringExtra("path"), Toast.LENGTH_SHORT).show();
            chip.setText("Media File Added ");
            try {

//                  URI uri = new URI(getIntent().getStringExtra("path"));
//                  URL videoUrl = uri.toURL();
//                  File tempFile = new File(videoUrl.getFile());
                Uri test = Uri.parse(getIntent().getStringExtra("path"));

                Cursor c = getContentResolver().query(test, null, null, null, null);
                c.moveToFirst();
                String name = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                chip.setText(name);

                c.close();


            } catch (Exception e) {
                Log.d("TAGERROR", e.getMessage() + "");
                String rand = getSaltString();

                if (postType.contains("audio")) {
                    chip.setText(rand + ".mp3");
                } else {
                    chip.setText(rand + ".mp4");
                }


            }
            try{
                playMedia(getIntent().getStringExtra("path"));
            }
            catch (Exception e ){

            }

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        playMedia(getIntent().getStringExtra("path"));
                    } catch (Exception e) {

                    }


                }
            });
        }
        // define Views ...
        addImage = findViewById(R.id.add_image);
        title = findViewById(R.id.title_et);
        desc = findViewById(R.id.desc_et);

        submitBtn = findViewById(R.id.publish_btn);
        catgoerySelector = findViewById(R.id.categoryList);
        langugaNameSelector = findViewById(R.id.langList);

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

        submitBtn.setOnClickListener(v -> {

            Desc = desc.getText().toString();
            Title = title.getText().toString();

            if (!TextUtils.isEmpty(Title) || !TextUtils.isEmpty(Desc) || mFilePathUri != null || !catid.equals("null") || !langid.equals("null")) {

                createPostServer(mFilePathUri, postType, Title, Desc, catid, langid);


            }

        });

        // spinner click listener
        catgoerySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (categoryList.size() > 0) {

                    catid = categoryList.get(position).getId();

                    //Toast.makeText(getApplicationContext() ,catid , Toast.LENGTH_SHORT ).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                catid = "null";
            }
        });


        langugaNameSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (languageList.size() > 0) {

                    langid = languageList.get(position).getId();
                    // Toast.makeText(getApplicationContext() ,catid + " "+ langid, Toast.LENGTH_SHORT ).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langid = "null";
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

    private void createPostServer(Uri mFilePathUri, String postType, String title, String desc, String catid, String langid) {

        if (mFilePathUri != null) {
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



            if (postType.equals("post")) {

                //creating request body for file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);

                body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);


                NetworkCall = api.uploadPost(createPartFromString(title), createPartFromString(title.toLowerCase()),
                        createPartFromString(desc),
                        createPartFromString(postType), createPartFromString(langid),
                        createPartFromString(catid), createPartFromString("1"),
                        body1);


            } else {

                Intent p = getIntent();
                uriPath = p.getStringExtra("path");
                //media Uri has the all the link in it ;
                mediaUri = Uri.parse(uriPath);

                File mediaFile = null;
                String path = null;

                if (mediaUri != null) {

                    try {
                        path = getPath(PostUploadActivity.this, mediaUri);
                        mediaFile = new File(path);
                    } catch (Exception e) {
                        path = mediaUri.getPath();
                        mediaFile = new File(path);
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Media File  is empty", Toast.LENGTH_LONG).show();


                //creating request body for file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
                // changing media_type
                RequestBody requestMediaFile;

                if (postType.equals("audio")) {
                    requestMediaFile = RequestBody.create(MediaType.parse("audio/mp3*"), mediaFile);

                    body2 = MultipartBody.Part.createFormData("audio", mediaFile.getName(), requestMediaFile);
                } else {
                    requestMediaFile = RequestBody.create(MediaType.parse("video/mp4"), mediaFile);
                    body2 = MultipartBody.Part.createFormData("video", mediaFile.getName(), requestMediaFile);
                }

                body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
                api = ServiceGenerator.createService(NewsRmeApi.class, getToken());

//                @Part MultipartBody.Part file,
//                @Part("title")  RequestBody  title,
//                @Part("title_slug") RequestBody title_slug,
//                @Part("content")  RequestBody content,
//                @Part("post_type")  RequestBody post_type,
//                @Part("lang_id")  RequestBody lang_id,
//                @Part("category_id")   RequestBody category_id,
//                @Part("sub_category_id")   RequestBody sub_category_id,
//                @Part MultipartBody.Part image

                NetworkCall = api.uploadFilePost(body2, createPartFromString(title), createPartFromString(title.toLowerCase()),
                        createPartFromString(desc),
                        createPartFromString(postType), createPartFromString(langid),
                        createPartFromString(catid), createPartFromString("1"),
                        body1);


            }

            NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
                @Override
                public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {


                    if (response.code() == 201) {

                        LoginResponse.forgetPassResponse testRes = response.body();
                        progressDialog.dismiss();

                        Intent p = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(p);
                        //   finish();

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

        } else {

            Toast.makeText(getApplicationContext(), "Please Pick An Image!!", Toast.LENGTH_LONG).show();
        }


    }

    @NonNull
    private RequestBody createPartFromString(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }

    public static String getPath(Context ctx, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(columnIndex);
        cursor.close();
        return s;
    }

    private void BringImagePicker() {


        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
                .start(this);


    }

    public String getToken() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
        return accessTokens;
    }

    private void loadMiscData() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);

        Call<allDataResponse> catCall = api.getCategoryList();

        catCall.enqueue(new Callback<allDataResponse>() {
            @Override
            public void onResponse(Call<allDataResponse> call, Response<allDataResponse> response) {

                if (response.code() == 201) {

                    allDataResponse dataResponse = response.body();

                    categoryList = dataResponse.getCategories();
                    languageList = dataResponse.getLanguageList();

                    // load all the category name
                    for (int i = 0; i < categoryList.size(); i++) {
                        categoryNameList.add(categoryList.get(i).getName().toString());

                    }
                    for (int i = 0; i < languageList.size(); i++) {
                        languageNameList.add(languageList.get(i).getName().toString());

                    }

                    // send it to adaper

                    ArrayAdapter<String> langAdapter = new ArrayAdapter<>(PostUploadActivity.this,
                            android.R.layout.simple_spinner_item, languageNameList);
                    langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    langugaNameSelector.setAdapter(langAdapter);


                    ArrayAdapter<String> catgoery_adapter = new ArrayAdapter<>(PostUploadActivity.this,
                            android.R.layout.simple_spinner_item, categoryNameList);
                    catgoery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    catgoerySelector.setAdapter(catgoery_adapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<allDataResponse> call, Throwable t) {

            }
        });
    }

    private void playMedia(String Path) {

        String path;

        try {
            path = getPath(PostUploadActivity.this, Uri.parse(Path));

        } catch (Exception e) {
            path = Path;

        }
        if (TextUtils.isEmpty(path)) {
            path = Path;

        }

        if (PlayerManager.getSharedInstance(PostUploadActivity.this).isPlayerPlaying()) {
            PlayerManager.getSharedInstance(PostUploadActivity.this).stopPlayer();
        }

        PlayerManager.getSharedInstance(PostUploadActivity.this).playStream(path);
    }

    @Override
    protected void onStart() {
        loadMiscData();
        super.onStart();
    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    @Override
    public void onItemClickOnItem(Integer albumId) {

    }

    @Override
    public void onPlayingEnd() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        PlayerManager.getSharedInstance(PostUploadActivity.this).stopPlayer();
        PlayerManager.getSharedInstance(PostUploadActivity.this).releasePlayer();


    }

    @Override
    protected void onPause() {
        super.onPause();

        PlayerManager.getSharedInstance(PostUploadActivity.this).pausePlayer();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (PlayerManager.getSharedInstance(PostUploadActivity.this).isPlayerPlaying()) {
            PlayerManager.getSharedInstance(PostUploadActivity.this).stopPlayer();
            PlayerManager.getSharedInstance(PostUploadActivity.this).releasePlayer();

        }
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


    }
}