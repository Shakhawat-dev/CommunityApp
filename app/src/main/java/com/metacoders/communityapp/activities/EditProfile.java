package com.metacoders.communityapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CCPCountry;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.newModels.UserModel;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
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

public class EditProfile extends AppCompatActivity {
    private static final int PICK_IMAGE = 100, PICK_IMAGE_DOC = 580;
    TextInputEditText full_nameIn, zipCodeIn, ssLinkIn, addressIn, emailIn, phoneIn, CompanyIn, lastDegreeIn, latLongIn, cityIN, bioin;
    String full_name, address, email, phone, bio, company, lastDegree, latLong, country, city;
    UserModel model;
    Spinner genderSpinner;
    String gender;
    CountryCodePicker countryCodePicker;
    ProgressDialog mprogressDialog;
    Uri mFilePathUri;
    CircleImageView pp;
    private double lat = 1000, lon = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);
        mprogressDialog = new ProgressDialog(EditProfile.this);
        mprogressDialog.setMessage("Uploading The Image...");

        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "My Profile");
        } catch (Exception e) {

        }

        model = (UserModel) SharedPrefManager.getInstance(getApplicationContext()).getUserModel();

        // sertupView
        setUpUi(model);


        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get Text
                full_name = full_nameIn.getText().toString();
                address = addressIn.getText().toString();
                phone = phoneIn.getText().toString();
                company = CompanyIn.getText().toString();
                bio = bioin.getText().toString();


                if (genderSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Select Gender", Toast.LENGTH_LONG).show();
                } else {
                    sendData();
                }


            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        pp.setOnClickListener(v -> {
            // open the gallery to
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(EditProfile.this,
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

    private void RequestPermission() {

        Dexter.withContext(EditProfile.this)
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


    private void BringImagePicker() {


        RequestPermission();
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);


    }

    private void setUpUi(UserModel model) {
        pp = findViewById(R.id.profile_pic);
        countryCodePicker = findViewById(R.id.ccp);
        ssLinkIn = findViewById(R.id.socialLink);
        zipCodeIn = findViewById(R.id.zip_code);
        genderSpinner = findViewById(R.id.genderList);
        full_nameIn = findViewById(R.id.name);
        addressIn = findViewById(R.id.address);
        emailIn = findViewById(R.id.email_et);
        phoneIn = findViewById(R.id.mobilePhone);
        CompanyIn = findViewById(R.id.company);
        lastDegreeIn = findViewById(R.id.lastDegree);
        latLongIn = findViewById(R.id.latlong);
        bioin = findViewById(R.id.bio);
        cityIN = findViewById(R.id.city);

        ArrayAdapter<String> catgoery_adapter = new ArrayAdapter<String>(EditProfile.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender));
        catgoery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(catgoery_adapter);

        Log.d("TAGGED", "sendData: " + SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getCountry());

        String gendert = SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getGender();

        String link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/TgBDz5Ti5AZiposXXwvRmTKP1VpIJouIctyaILih.png";
        try {
            if (gendert.toLowerCase(Locale.ROOT).contains("fe")) {
                link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/Vzsa4eUZNCmRvuNVUWToGyu0Xobb6DyQgcX4oDoI.png\n";
            } else {
                link = "https://newsrme.s3-ap-southeast-1.amazonaws.com/frontend/profile/TgBDz5Ti5AZiposXXwvRmTKP1VpIJouIctyaILih.png";
            }
        } catch (Exception e) {

        }
        Glide.with(getApplicationContext()).load( SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getImage())
                .placeholder(R.drawable.placeholder)
                .error(link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(pp);



        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try {
            detectCountry(model.getCountry());
            gender = model.getGender();
            if (gender == null || gender.isEmpty()) {

            } else {
                //   genderSpinner.setSelection(getIndexOfSpinner(genderSpinner, gender.toLowerCase()));

                if (gendert.equalsIgnoreCase("male")) {
                    genderSpinner.setSelection(1);
                } else if (gendert.equalsIgnoreCase("female")) {
                    genderSpinner.setSelection(2);
                } else {
                    genderSpinner.setSelection(3);
                }
            }

            if (AppPreferences.isStringIsEmptyORNull(model.getName())) {
                full_nameIn.setHint("Your Name");
            } else {
                full_nameIn.setText(model.getName() + "");
            }

            if (AppPreferences.isStringIsEmptyORNull(model.getAddress())) {
                addressIn.setHint("Your Address");
            } else {
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(addressIn.getId()).getParent().getParent();
                textInputLayout.setHint("Address");
                addressIn.setText(model.getAddress() + "");
            }

            if (AppPreferences.isStringIsEmptyORNull(model.getEmail())) {
                emailIn.setHint("Your Email Address");
            } else {
                emailIn.setText(model.getEmail() + "");
            }

            if (AppPreferences.isStringIsEmptyORNull(model.getPhone())) {
                phoneIn.setHint("Your Phone Number");
            } else {
                phoneIn.setText(model.getPhone() + "");
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(phoneIn.getId()).getParent().getParent();
                textInputLayout.setHint("Phone");
            }

            if (AppPreferences.isStringIsEmptyORNull(model.getBio())) {
                bioin.setHint("Your Bio");
            } else {
                bioin.setText(model.getBio() + "");
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(bioin.getId()).getParent().getParent();
                textInputLayout.setHint("Bio");
            }

            if (AppPreferences.isStringIsEmptyORNull(model.getCompany())) {
                CompanyIn.setHint("Your Company");
            } else {
                CompanyIn.setText(model.getCompany() + "");
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(CompanyIn.getId()).getParent().getParent();
                textInputLayout.setHint("Company");
            }

            if (AppPreferences.isStringIsEmptyORNull(model.getWebsite())) {
                ssLinkIn.setHint("Your Website Link");

            } else {
                ssLinkIn.setText(model.getWebsite() + "");
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(ssLinkIn.getId()).getParent().getParent();
                textInputLayout.setHint("Website");
            }


            if (AppPreferences.isStringIsEmptyORNull(model.getZip_code())) {
                zipCodeIn.setHint("ZipCode");
            } else {

                zipCodeIn.setText(model.getZip_code() + "");
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(zipCodeIn.getId()).getParent().getParent();
                textInputLayout.setHint("ZipCode");
            }


        } catch (Exception e) {

        }


    }

    public void sendData() {

        ProgressDialog dialog = new ProgressDialog(EditProfile.this);
        dialog.setMessage("Updating Profile Data...");
        dialog.setCancelable(false);
        dialog.show();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();

        UserModel model = SharedPrefManager.getInstance(getApplicationContext()).getUserModel();

        model.setAddress(address);
        model.setName(full_name);
        model.setPhone(phone);
        model.setBio(bio);
        model.setCompany(company);
        model.setAddress(address);
        if (gender.contains("male")) {
            model.setGender(gender);
        }
        model.setSocial_link(ssLinkIn.getText().toString());
        model.setZip_code(zipCodeIn.getText().toString());


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);


        Call<RegistrationResponse> NetworkCall = api.
                update_profile(
                        AppPreferences.getUSerID(getApplicationContext()),
                        full_name,
                        phone,
                        bio,
                        company,
                        address,
                        gender,
                        ssLinkIn.getText().toString(),
                        zipCodeIn.getText().toString(),
                        countryCodePicker.getSelectedCountryName()
                        //SharedPrefManager.getInstance(getApplicationContext()).getUserModel().getCountry()
                );


        NetworkCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    // get
                    SharedPrefManager.getInstance(getApplicationContext()).saveUserModel(model);
                    Toast.makeText(getApplicationContext(), "Msg:  " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Error " + response.code(), Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showOnLocationAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOnLocationAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on your location.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private int getIndexOfSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            String compare = ((String) spinner.getItemAtPosition(i)).toString() + "";
            if (compare.equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void detectCountry(String name) {
        if (name != null && !name.isEmpty()) {
            //   CCPCountry country = CCPCountry.getCountryForNameCodeFromLibraryMasterList(getApplicationContext(), CountryCodePicker.Language.ENGLISH, name.toLowerCase()); //xml stores data in string format, but want to allow only numeric value to country code to user.
            List<CCPCountry> countries = new ArrayList<>();
            countries = CCPCountry.getLibraryMasterCountryList(getApplicationContext(), CountryCodePicker.Language.ENGLISH);
            for (CCPCountry ccpCountry : countries) {
                if (ccpCountry.getName().equalsIgnoreCase(name)) {
                    countryCodePicker.setCountryForNameCode(ccpCountry.getNameCode());
                }
            }
        }
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

    private void uploadProfilePicToServer(Uri mFilePathUri) {

        RequestPermission();
        pp.setImageURI(mFilePathUri);
        mprogressDialog.show();
        mprogressDialog.setMessage("Uploading New Image...");
        String path;
        File file;

        try {
            path = getPath(EditProfile.this, mFilePathUri);
            file = new File(path);
        } catch (Exception e) {

            path = mFilePathUri.getPath();
            file = new File(path);
        }

        // call for network
        File compressed;

        try {
            compressed = new Compressor(EditProfile.this)
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

        Call<LoginResponse.forgetPassResponse> call = api.uploadImage(AppPreferences.getUSerID(getApplicationContext()), createPartFromString(name), body1);


        call.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {


                if (response.code() == 200) {
                    LoginResponse.forgetPassResponse result = response.body();
                    Toast.makeText(getApplicationContext(), "Msg" + result.getMessage(), Toast.LENGTH_LONG)
                            .show();
                    mprogressDialog.dismiss();
                } else {
                    mprogressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "SomeThing Went Wrong.Please Try Again !!!" + response.code(), Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                Log.d("RRR", t.getMessage().toUpperCase().toString());

                mprogressDialog.dismiss();
                //  isImageUploaded = false;
                Toast.makeText(getApplicationContext(), "SomeThing Went Wrong Please  Try Again", Toast.LENGTH_LONG)
                        .show();
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
            //uploadDocumentation(selectedMediaUri);


        }


        // uploadDocumentation(mFilePathUri);
    }

    private RequestBody createPartFromString(String value) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, value);
    }
}
