package com.metacoders.communityapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.Profile_Model;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class EditProfile extends AppCompatActivity {
    TextInputEditText full_nameIn, addressIn, emailIn, phoneIn, professionIn, lastDegreeIn, latLongIn , cityIN , counryin;
    String full_name, address, email, phone, profession, lastDegree, latLong , country , city ;
    private double lat = 1000, lon = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_layout);

        Profile_Model model = (Profile_Model) getIntent().getSerializableExtra("MODEL") ;
        // sertupView
        setUpUi(model);


        findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get Text
                full_name = full_nameIn.getText().toString();
                address = addressIn.getText().toString();
                email = emailIn.getText().toString();
                phone = phoneIn.getText().toString();
                profession = professionIn.getText().toString();
                lastDegree = lastDegreeIn.getText().toString();
                country = counryin.getText().toString() ;
                city = cityIN.getText().toString() ;


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

    private void setUpUi(Profile_Model model) {


        full_nameIn = findViewById(R.id.name);
        addressIn = findViewById(R.id.address);
        emailIn = findViewById(R.id.email_et);
        phoneIn = findViewById(R.id.mobilePhone);
        professionIn = findViewById(R.id.profession);
        lastDegreeIn = findViewById(R.id.lastDegree);
        latLongIn = findViewById(R.id.latlong);
        counryin = findViewById(R.id.country) ;
        cityIN = findViewById(R.id.city) ;



        cityIN.setText(model.getCity());
        counryin.setText(model.getCountry());
        full_nameIn.setText(model.getName());
        addressIn.setText(model.getAddress());
        emailIn.setText(model.getEmail());
        phoneIn.setText(model.getMobile());
        lastDegreeIn.setText(model.getLastDegree());
        latLongIn.setText(model.getLatitude() + "," + model.getLongitude());
        professionIn.setText(model.getProfession());

    }

    public void sendData() {

        ProgressDialog dialog = new ProgressDialog( EditProfile.this) ;
        dialog.setMessage("Updating Data");
        dialog.show();
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
//        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);
//        @Field("name") String name,
//        @Field("user_name") String username,
//        @Field("email") String email,
//        @Field("latitude") String latitude,
//        @Field("longitude") String longitude,
//        @Field("profession") String profession,
//        @Field("last_degree") String last_degree
        Call<RegistrationResponse> NetworkCall = api.
                update_profile(
                        full_name,
                        phone ,
                        email,
                        lat + "",
                        lon + "",
                        profession,
                        lastDegree,
                        city ,
                        country,
                        address


                );


        NetworkCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                dialog.dismiss();
                if(response.code()==201){
                    // get

                    Toast.makeText(getApplicationContext(), "Msg:  "+ response.body().getMessage(), Toast.LENGTH_LONG) . show();


                }
                else {
                    Toast.makeText(getApplicationContext(), "Error "+ response.code(), Toast.LENGTH_LONG) . show();
                }


            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error "+ t.getMessage(), Toast.LENGTH_LONG) . show();
            }
        });


    }


    private void getCurrentLocation() {

        statusCheck();

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(900000);
        locationRequest.setFastestInterval(20000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(EditProfile.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        //only once
                        LocationServices.getFusedLocationProviderClient(EditProfile.this)
                                .removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            try {
                                int index = locationResult.getLocations().size() - 1;
                                double longitude = locationResult.getLocations().get(index).getLongitude();
                                double latitude = locationResult.getLocations().get(index).getLatitude();

                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                Geocoder geocoder = new Geocoder(EditProfile.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        1
                                );

                                lat = addresses.get(0).getLongitude();
                                lon = addresses.get(0).getLatitude();
                                String locality = "" + addresses.get(0).getAddressLine(0);
                                //String locality = addresses.get(0).getLocality(0);

                                if (!locality.isEmpty()) {

                                    latLongIn.setText(lat + "," + lon);

                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, Looper.getMainLooper());
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

    private void getLatitudeLongitude() {
        if (ActivityCompat.checkSelfPermission(EditProfile.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(EditProfile.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 111);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLatitudeLongitude();
    }
}
