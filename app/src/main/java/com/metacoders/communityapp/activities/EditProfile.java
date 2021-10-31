package com.metacoders.communityapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.newModels.UserModel;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {
    TextInputEditText full_nameIn,zipCodeIn , ssLinkIn  , addressIn, emailIn, phoneIn, CompanyIn, lastDegreeIn, latLongIn, cityIN, bioin;
    String full_name, address, email, phone, bio, company, lastDegree, latLong, country, city;
    UserModel model;
    Spinner genderSpinner;
    String gender ;

    private double lat = 1000, lon = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);


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


                sendData();

            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    private void setUpUi(UserModel model) {
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


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        try{
            gender = model.getGender();
            if(gender == null || gender.isEmpty()){

            }else {
               genderSpinner.setSelection( getIndexOfSpinner(genderSpinner , gender.toLowerCase()));
            }

            full_nameIn.setText(model.getName()+"");
            addressIn.setText(model.getAddress()+"");
            emailIn.setText(model.getEmail()+"");
            phoneIn.setText(model.getPhone()+"");
            bioin.setText(model.getBio()+"");
            CompanyIn.setText(model.getCompany()+"");
            ssLinkIn.setText(model.getSocial_link()+"");
            zipCodeIn.setText(model.getZip_code()+"");

        }catch ( Exception e ){

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
        if(gender.contains("male")){
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
                        zipCodeIn.getText().toString()
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

}
