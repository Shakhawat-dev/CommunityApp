package com.metacoders.communityapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.metacoders.communityapp.utils.CallBacks;
import com.metacoders.communityapp.utils.PlayerManager;
import com.metacoders.communityapp.utils.ProgressRequestBody;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
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
    Uri mFilePathUri = null;
    PlayerView playerView;
    TextView percent ;
    ProgressBar pbarr ;

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

        progressDialog = new Dialog(PostUploadActivity.this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_dialogue);
        percent = progressDialog.findViewById(R.id.progress_state) ;
        pbarr = progressDialog.findViewById(R.id.progress_bar) ;
        progressDialog.setCancelable(false);


        o = getIntent();
        // take the media type ....
        postType = o.getStringExtra("media");
        Toast.makeText(getApplicationContext(), postType, Toast.LENGTH_LONG).show();
        if (!postType.contains("post")) {

            //  Toast.makeText(getApplicationContext(), getIntent().getStringExtra("path"), Toast.LENGTH_SHORT).show();
            chip.setText("Media File Added ");
            try {
//                  URI uri = new URI(getIntent().getStringExtra("path"));
//                  URL videoUrl = uri.toURL();
                //      File tempFile = new File(videoUrl.getFile());
                Uri test = Uri.parse(getIntent().getStringExtra("path"));

                //  Cursor c = getContentResolver().query(test, null, null, null, null);
                //   c.moveToFirst();
                //  String name = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                //  chip.setText(name);

                //c.close();


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

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        playMedia(getIntent().getStringExtra("path"));
                    } catch (Exception e) {

                    }


                }
            });
        } else {
            playerView.setVisibility(View.GONE);
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

    private void createPostServer(Uri mFilePathUri, String postType, String title, String desc, String catid, String langid) {

        if (mFilePathUri != null) {
            // upload the data


            String path2 = null;
            File file, compressed;
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
            progressDialog.show();



            if (postType.equals("post")) {

                //creating request body for file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);

                body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

                //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
                Log.d("TAG", "createPostServer: " + file.getName() + requestFile.contentType());
                api = ServiceGenerator.createService(NewsRmeApi.class, getToken());
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


                //creating request body for file
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), compressed);
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


                body1 = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                Log.d("TAG", "createPostServer: " + mediaFile.getName() + requestMediaFile.contentType());
                //   Toast.makeText(getApplicationContext() , "asdfasf"+ mediaFile.getName() , Toast.LENGTH_LONG ).show();
                //  RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
                api = ServiceGenerator.createService(NewsRmeApi.class, getToken());

                NetworkCall = api.uploadFilePost(body2, createPartFromString(title), createPartFromString(title.toLowerCase()),
                        createPartFromString(desc),
                        createPartFromString(postType), createPartFromString(langid),
                        createPartFromString(catid), createPartFromString("1"),
                        body1);


            }

            NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
                @Override
                public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {

                    Toast.makeText(getApplicationContext(), "CODE" + response.code(), Toast.LENGTH_LONG).show();
                    if (response.code() == 200 || response.code() == 201) {
                        LoginResponse.forgetPassResponse testRes = response.body();
                        Toast.makeText(getApplicationContext(), " jj" + testRes.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent p = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(p);
                        //   finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "" + response.code(), Toast.LENGTH_LONG)
                                .show();
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

        } else {

            Toast.makeText(getApplicationContext(), "Please Pick An Image!!", Toast.LENGTH_LONG).show();
        }


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
            if(percentage == 0){
                pbarr.setIndeterminate(true);
            }
            else {
                pbarr.setIndeterminate(false);
                percent.setText(percentage+" %");
                pbarr.setProgress(percentage);
            }


        }catch (Exception e ){
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