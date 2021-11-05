package com.metacoders.communityapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.allDataResponse;
import com.metacoders.communityapp.models.newModels.CategoryModel;
import com.metacoders.communityapp.models.newModels.CountryModel;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.CallBacks;
import com.metacoders.communityapp.utils.PlayerManager;
import com.metacoders.communityapp.utils.ProgressRequestBody;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import id.zelory.compressor.Compressor;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostUploadActivity extends AppCompatActivity implements CallBacks.playerCallBack,
        ProgressRequestBody.UploadCallbacks {
    private static final int PICK_IMAGE = 100;
    Uri mediaUri = null;
    String uriPath = null;
    ImageView addImage;
    String postType;
    Dialog progressDialog;
    MultipartBody.Part body1;
    String countyID = "22";
    MultipartBody.Part body2;
    Uri FileUriSizeChecker = null;
    NewsRmeApi api;
    Intent o;
    Button submitBtn;
    Chip chip;
    TextInputEditText title, desc;
    String Title, Desc;
    SearchableSpinner searchableCountySpinner;

    List<allDataResponse.Category> categoryList;
    List<String> categoryNameList = new ArrayList<>();
    List<allDataResponse.LanguageList> languageList = new ArrayList<>();
    List<String> languageNameList = new ArrayList<>();
    Call<LoginResponse.forgetPassResponse> NetworkCall;
    Spinner catgoerySelector, langugaNameSelector;
    String catid = "null", langid = "null";
    Uri mFilePathUri = null;
    PlayerView playerView;
    TextView percent;
    ProgressBar pbarr;
    ImageView fullscreenButton;
    Dialog mFullScreenDialog;
    SettingsModel settingsModel;
    private boolean mExoPlayerFullscreen = false;
    private Bitmap compressedImageFile;

    //AlertDialog.Builder builder = new AlertDialog.Builder(PostUploadActivity.this);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_post_upload);
        chip = findViewById(R.id.chip);
        playerView = findViewById(R.id.player_view);
        playerView.setUseArtwork(true);
        playerView.setPlayer(PlayerManager.getSharedInstance(PostUploadActivity.this).getPlayerView().getPlayer());
        PlayerManager.getSharedInstance(this).setPlayerListener(this);
        fullscreenButton = findViewById(R.id.exo_fullscreen_icon);
        progressDialog = new Dialog(PostUploadActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialogue);
        percent = progressDialog.findViewById(R.id.progress_state);
        pbarr = progressDialog.findViewById(R.id.progress_bar);
        searchableCountySpinner = findViewById(R.id.countrySpinner);
        progressDialog.setCancelable(false);
        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Upload POst");
        } catch (Exception e) {

        }

        settingsModel = SharedPrefManager.getInstance(getApplicationContext()).getAppSettingsModel();

        o = getIntent();
        // take the media type ....
        postType = o.getStringExtra("media");
        // Toast.makeText(getApplicationContext(), postType, Toast.LENGTH_LONG).show();
        if (!postType.contains("post")) {

            //  Toast.makeText(getApplicationContext(), getIntent().getStringExtra("path"), Toast.LENGTH_SHORT).show();
//            chip.setText("Media File Added ");
            try {


                if (!postType.contains("audio")) {
                    FileUriSizeChecker = Uri.parse(getIntent().getStringExtra("path"));
                    //  chip.setText(rand + ".mp3");
                } else {
                    FileUriSizeChecker = Uri.parse(getIntent().getStringExtra("OR_PATH"));
                    // chip.setText(rand + ".mp4");
                }


            } catch (Exception e) {
                Log.d("TAG", e.getMessage() + "");
                //   String rand = getSaltString();

                if (postType.contains("audio")) {

                    //  chip.setText(rand + ".mp3");
                } else {
                    // chip.setText(rand + ".mp4");
                }


            }
            try {
                playMedia(getIntent().getStringExtra("path"));
            } catch (Exception e) {

            }


            fullscreenButton.setOnClickListener(v -> {

                if (!mExoPlayerFullscreen) {
                    // not in fullscreen

                    openFullScreenDialog();


                } else {

                    closeFullScreenDialog();

                }


            });


            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        // playMedia(getIntent().getStringExtra("path"));
                    } catch (Exception e) {

                    }


                }
            });
        } else {
            findViewById(R.id.parent_relative).setVisibility(View.GONE);
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

                /*
                    check video sizE
                 */
                // Get file from uri
                try {
                    String path2 = null;
                    File file;
                    try {
                        path2 = getPath(PostUploadActivity.this, FileUriSizeChecker);
                        file = new File(path2);
                    } catch (Exception e) {
                        path2 = mFilePathUri.getPath();
                        file = new File(path2);
                    }
                    // Get length of file in bytes
                    long fileSizeInBytes = file.length();
                    // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;
                    Log.d("TAG", "FIle SIze :  " + fileSizeInMB);
                    if (fileSizeInMB > 205) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder
                                .setCancelable(false)
                                .setTitle("Error !! Too Big File")
                                .setMessage("Please Upload File Which are less Than  205 MB . ")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        createPostServer(mFilePathUri, postType, Title, Desc, catid, langid);
                    }
                } catch (Exception e) {

                    createPostServer(mFilePathUri, postType, Title, Desc, catid, langid);

                }


            }

        });

        // spinner click listener
        catgoerySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                CategoryModel categoryModel = (CategoryModel) parent.getSelectedItem();
                catid = categoryModel.getCategory_name();
                // Toast.makeText(getApplicationContext(), catid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                catid = "null";
            }
        });


        langugaNameSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                langid = parent.getSelectedItem().toString();

                if (langid.contains("English")) {
                    langid = "en";
                } else {
                    langid = "bn";
                }
                // Toast.makeText(getApplicationContext() ,catid + " "+ langid, Toast.LENGTH_SHORT ).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langid = "null";
            }
        });


        searchableCountySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                CountryModel countryModel = (CountryModel) parent.getSelectedItem();

                countyID = countryModel.getName() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                langid = "null";
            }
        });



        findViewById(R.id.closeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
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
                RequestPermission();
                mFilePathUri = result.getUri();

                //    uploadPicToServer(mFilePathUri) ;
                addImage.setImageURI(mFilePathUri);

                TriggerImageConfirmDialouge();
                // createPostServer(mFilePathUri , postType);

                //sending data once  user select the image

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
//        else if (requestCode == PICK_IMAGE) {
//
//            //   mFilePathUri = data.getData();
//            // Toast.makeText(getApplicationContext() , "TEst" + mFilePathUri.toString(), Toast.LENGTH_LONG) .show();
//            try {
//
//                Uri selectedMediaUri = data.getData();
//                if (!Uri.EMPTY.equals(selectedMediaUri)) {
//                    mFilePathUri = selectedMediaUri;
//                    // sendTheFile(selectedMediaUri);
//                    RequestPermission();
//
//                    CropImage.activity(mFilePathUri)
//                            .start(this);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please Choose Image To Upload ", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Please Choose Image ", Toast.LENGTH_SHORT).show();
//            }
//
//
//        }
        else if (resultCode == Activity.RESULT_OK) {
            try {
                Uri selectedMediaUri = data.getData();
                if (!Uri.EMPTY.equals(selectedMediaUri)) {
                    mFilePathUri = selectedMediaUri;
                    // sendTheFile(selectedMediaUri);
                    RequestPermission();

                    CropImage.activity(mFilePathUri)
                            .start(this);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Choose Image To Upload ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please Try Again !! ", Toast.LENGTH_LONG).show();
            }
        }

    }


    private void closeFullScreenDialog() {


        ((ViewGroup) playerView.getParent()).removeView(playerView); // removes the player screen


        ((FrameLayout) findViewById(R.id.parent_relative)).addView(playerView);

        mExoPlayerFullscreen = false;

        mFullScreenDialog.dismiss();


        // change the full screen image
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PostUploadActivity.this, R.drawable.full));


    }

    public void initFullsceen() {

        mFullScreenDialog = new Dialog(PostUploadActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen) {
                    closeFullScreenDialog();

                    super.onBackPressed();
                }

            }


        };


    }

    private void openFullScreenDialog() {


        // opening the dialgoue

        ((ViewGroup) playerView.getParent()).removeView(playerView); // removes the player screen

        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // change the full screen image
        fullscreenButton.setImageDrawable(ContextCompat.getDrawable(PostUploadActivity.this, R.drawable.full));

        mExoPlayerFullscreen = true;

        mFullScreenDialog.show();


    }

    private void createPostServer(Uri mFilePathUri, String postType, String title, String
            desc, String catid, String langid) {


            // upload the data
            String path2 = null;
            File file, compressed;
         if(mFilePathUri != null){
             try {
                 path2 = getPath(PostUploadActivity.this, mFilePathUri);
                 file = new File(path2);
             } catch (Exception e) {
                 path2 = mFilePathUri.getPath();
                 file = new File(path2);
                 //

             }
             try {
                 compressed = new Compressor(getApplicationContext())
                         .setMaxHeight(600)
                         .setMaxWidth(600)
                         .setQuality(40)
                         .compressToFile(file);
             } catch (Exception e) {
                 compressed = file;
             }
         }
         else {
             compressed = null ;
             file = null ;
         }
            progressDialog.show();


            if (postType.equals("post")) {

                //creating request body for file
                RequestBody requestFile = null ;
                if(mFilePathUri != null){

                    requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
                    body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                }else {
                    body1 = null ;
                }
                //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
                api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

                NetworkCall = api.uploadPost(
                        createPartFromString(title),
                        createPartFromString(desc),
                        createPartFromString(langid),
                        createPartFromString(countyID),
                        createPartFromString(catid),
                        body1);


            }
            else {
                //getting the vido uri
                // mediaUri = mFilePathUri;
                mediaUri = Uri.parse(getIntent().getStringExtra("path"));
                ;

                File mediaFile = null, backupFile = null;
                String path = null;

                if (mediaUri != null) {

                    try {
                        path = getPath(PostUploadActivity.this, mediaUri);
                        mediaFile = new File(path);
                        backupFile = mediaFile;
                    } catch (Exception e) {
                        path = mediaUri.getPath();
                        mediaFile = new File(path);
                        backupFile = mediaFile;
                        Log.d("TAG", "createPostServer: im here " + mediaFile + " path" + path);
                    }
                } else
                    Toast.makeText(getApplicationContext(), "Media File  is empty", Toast.LENGTH_LONG).show();

                RequestBody requestFile = null ;
                //creating request body for file
                if(mFilePathUri != null){
                     requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
                }

                // changing media_type
                RequestBody requestMediaFile;

                if (postType.equals("audio")) {
                    String orginaPath = getIntent().getStringExtra("OR_PATH");
                    Log.d("EE", "createPostServer: " + orginaPath);
                    if (orginaPath != null) {
                        mediaFile = new File(orginaPath);
                        Log.d("EE", "createPostServer: ");
                        //   Toast.makeText(getApplicationContext(), " " + orginaPath , Toast.LENGTH_LONG).show();
                        if (mediaFile == null) {
                            //  Toast.makeText(getApplicationContext(), " M3edia File Null"  , Toast.LENGTH_LONG).show();
                            requestMediaFile = RequestBody.create(backupFile, MediaType.parse("audio/*"));
                            // ProgressRequestBody fileBody = new ProgressRequestBody(mediaFile, "audio/mp3*",this);
                            ProgressRequestBody fileBody = new ProgressRequestBody(mediaFile, "audio/*", this);

                            body2 = MultipartBody.Part.createFormData("audio", mediaFile.getName(), fileBody);
                        } else {
                            requestMediaFile = RequestBody.create(mediaFile, MediaType.parse("audio/*"));
                            // ProgressRequestBody fileBody = new ProgressRequestBody(mediaFile, "audio/mp3*",this);
                            ProgressRequestBody fileBody = new ProgressRequestBody(mediaFile, "audio/*", this);
                            body2 = MultipartBody.Part.createFormData("audio", mediaFile.getName(), fileBody);
                        }

                    } else {
                        requestMediaFile = RequestBody.create(mediaFile, MediaType.parse("audio/*"));
                        // ProgressRequestBody fileBody = new ProgressRequestBody(mediaFile, "audio/mp3*",this);
                        ProgressRequestBody fileBody = new ProgressRequestBody(mediaFile, "audio/*", this);
                        body2 = MultipartBody.Part.createFormData("audio", mediaFile.getName(), fileBody);
                    }


                } else {
                    requestMediaFile = RequestBody.create(MediaType.parse("video/mp4"), mediaFile);
                    ProgressRequestBody fileBody = new ProgressRequestBody(mediaFile, "video/mp4", this);
                    body2 = MultipartBody.Part.createFormData("video", mediaFile.getName(), fileBody);
                }

                if(mFilePathUri != null){
                    body1 = MultipartBody.Part.createFormData("thumb_image", file.getName(), requestFile);
                }else {
                    body1 = null ;
                }

                Log.d("TAG", "createPostServer: " + mediaFile.getName() + requestMediaFile.contentType());
                api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));




                if (postType.equals("audio")) {
                    // audio update
                    /*
                                                             @Part MultipartBody.Part audio,
                                                          @Part("title") RequestBody title,
                                                          @Part("description") RequestBody content,
                                                          @Part("lang") RequestBody lang_id,
                                                          @Part("category") RequestBody category_id,
                                                          @Part("country") RequestBody sub_category_id,
                                                          @Part MultipartBody.Part image);
                     */

                    NetworkCall = api.uploadAudioFilePost(body2, createPartFromString(title),
                            createPartFromString(desc),
                            createPartFromString(langid), createPartFromString(catid),
                            createPartFromString(countyID),
                            body1);

                } else {
/*
                                                                @Part MultipartBody.Part file,
                                                               @Part("title") RequestBody title,
                                                               @Part("description") RequestBody description,
                                                               @Part("lang") RequestBody lang_id,
                                                               @Part("category") RequestBody category_id,
                                                               @Part("country") RequestBody sub_category_id,
                                                               @Part MultipartBody.Part image);
 */
                    NetworkCall = api.uploadVideoFilePost(body2, createPartFromString(title),
                            createPartFromString(desc), createPartFromString(langid),
                            createPartFromString(catid), createPartFromString(countyID),
                            body1);

                }


            }

            if(postType.equals("post") && mFilePathUri == null){
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Please Pick An Image!!", Toast.LENGTH_LONG).show();
            }else {
                NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {

                        //   Toast.makeText(getApplicationContext(), "CODE" + response.code(), Toast.LENGTH_LONG).show();
                        if (response.code() == 200 || response.code() == 201) {
                            LoginResponse.forgetPassResponse testRes = response.body();
                            Toast.makeText(getApplicationContext(), "" + testRes.getMessage(), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent p = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(p);
                            //   finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Error Server Code : " + response.code() + " Please Try Again !!", Toast.LENGTH_LONG).show();
                            Log.d("TAG", "onResponse: " + response.raw());
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

//        }
//        else {
//
//            Toast.makeText(getApplicationContext(), "Please Pick An Image!!", Toast.LENGTH_LONG).show();
//        }


    }

    @NonNull
    private RequestBody createPartFromString(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }

    private void BringImagePicker() {

        RequestPermission();
        ImagePicker.Companion.with(this)
                //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
//        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, PICK_IMAGE);
//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .setAspectRatio(1, 1)
//                .setCropShape(CropImageView.CropShape.RECTANGLE) //shaping the image
//                .start(this);


    }

    public String getToken() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "getToken: " + accessTokens);
        return accessTokens;
    }

    private void loadMiscData() {
        Collections.reverse(settingsModel.getCategories());
        Collections.reverse(settingsModel.getCountries());
        ArrayAdapter<CategoryModel> catgoery_adapter = new ArrayAdapter<CategoryModel>(PostUploadActivity.this,
                android.R.layout.simple_spinner_item, settingsModel.getCategories());
        catgoery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catgoerySelector.setAdapter(catgoery_adapter);

        String[] languages = {"English", "Bangla"};
        ArrayAdapter<String> langiage_adapter = new ArrayAdapter<String>(PostUploadActivity.this,
                android.R.layout.simple_spinner_item, languages);
        langiage_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langugaNameSelector.setAdapter(langiage_adapter);

        searchableCountySpinner.setTitle("Select Country");

        ArrayAdapter<CountryModel> country_adapter = new ArrayAdapter<CountryModel>(PostUploadActivity.this,
                android.R.layout.simple_spinner_item, settingsModel.getCountries());
        country_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchableCountySpinner.setAdapter(country_adapter);


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
        PlayerManager.getSharedInstance(PostUploadActivity.this).pausePlayer();

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

    @Override
    protected void onResume() {
        super.onResume();

        initFullsceen();
    }

    private void RequestPermission() {

        Dexter.withContext(PostUploadActivity.this)
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

    private void TriggerImageConfirmDialouge() {
        PrettyDialog pDialog = new PrettyDialog(PostUploadActivity.this);
        pDialog.setCancelable(false);
        pDialog
                .setTitle("Confirm This Image")
                .setMessage("")

                .setAnimationEnabled(true)

                .addButton(
                        "✔️ Yes",                    // button text
                        R.color.pdlg_color_white,        // button text color
                        R.color.pdlg_color_green, // button background color
                        new PrettyDialogCallback() {        // button OnClick listener
                            @Override
                            public void onClick() {
                                // Do what you gotta do
                                pDialog.dismiss();
                            }
                        }
                )
                .addButton(
                        "❌ NO",
                        R.color.pdlg_color_white,
                        R.color.pdlg_color_red,
                        new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                pDialog.dismiss();
                                BringImagePicker();
                                //selectImage(PostUploadActivity.this );
                            }
                        }
                )

                .show();

    }

    @Override
    public void onProgressUpdate(int percentage) {
        //  Log.d("TAG", "onProgressUpdate: " + " : " + percentage);
        try {
            if (percentage == 0) {
                pbarr.setIndeterminate(true);
            } else {
                pbarr.setIndeterminate(false);
                percent.setText(percentage + " %");
                pbarr.setProgress(percentage);
            }


        } catch (Exception e) {
            Log.d("TAG", "onProgressUpdate: " + e.getMessage());
        }

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }
}