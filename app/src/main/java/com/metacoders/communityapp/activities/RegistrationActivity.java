package com.metacoders.communityapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.hbb20.CountryCodePicker;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.newModels.RegistrationResp;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 1000;
    CheckBox isCehcked;
    Button googleBtn, fbBtn, vk;
    GoogleSignInOptions gso;
    Spinner genderSpinner;

    CallbackManager callbackManager;
    CountryCodePicker countryCodePicker;
    private EditText mName, mEmail, mPassword, mConfirmationPass;
    private AppCompatButton mSignUpBtn;
    private String gender = "gender";
    private Boolean isDeepLink = false;
    private String INVITED_USER_ID = "";
    private String deviceID = "xxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_signup_layout);
        isCehcked = findViewById(R.id.termsCheck);
        countryCodePicker = findViewById(R.id.ccp);
// google sign in builder
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("377780946002-5p5rvr3l34iroj52k02o2i1956ljgb76.apps.googleusercontent.com")
                .requestEmail()
                .build();

        isDeepLink = getIntent().getBooleanExtra("IS_DEEP_LINK", false);
        if (isDeepLink) {
            INVITED_USER_ID = getIntent().getStringExtra("ID");
        }


        deviceID = getDeviceID();
        /*
         *if account is null then not yet signed in ;
         *
         */
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        getSupportActionBar().hide();
        initializations();

        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCehcked.isChecked()) {
                    if (gender.contains("gender")) {
                        Toast.makeText(getApplicationContext(), "Please Select Your Gender", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(mName.getText().toString())){
                        mName.setError("Can't Be Empty");
                    }

                    else {
                        if (mPassword.getText().toString().equals(mConfirmationPass.getText().toString())) {
                            register();
                        } else {
                            mPassword.setError("Password Don't Match");
                            mConfirmationPass.setError("Password Don't Match");
                            Toast.makeText(getApplicationContext(), "Password Don't Match !!", Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Agree To ur Terms & Conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        googleBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                googleSignIn();
//            }
//        });
//
//
//        fbBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginManager.getInstance().logInWithReadPermissions(RegistrationActivity.this, Arrays.asList("email", "public_profile"));
//                callbackManager = CallbackManager.Factory.create();
//
//                LoginManager.getInstance().registerCallback(callbackManager,
//                        new FacebookCallback<LoginResult>() {
//                            @Override
//                            public void onSuccess(LoginResult loginResult) {
//                                // App code
//                                Log.d("TAG", "onSuccess: " + loginResult.getAccessToken());
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                // App code
//                                Log.d("TAG", "onCancel: Fb Cancel ");
//                            }
//
//                            @Override
//                            public void onError(FacebookException exception) {
//                                // App code
//                                Log.d("TAG", "Error:  " + exception.getMessage());
//                            }
//
//                        });
//            }
//        });


// token traker
        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    Log.d("TAG", "onCurrentAccessTokenChanged:  nuLl");
                } else loadUserInfo(currentAccessToken);

            }
        };

    }

    private String getDeviceID() {
        String IMEINumber;
//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            IMEINumber = telephonyManager.getImei();
//        } else {
//
//        }
        return Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private void loadUserInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String mail = object.getString("email");
                    String id = object.getString("id");


                    RegisterWithSocial(first_name + " " + last_name, first_name + " " + last_name, id, mail, "null", "facebook");

                    Log.d("TAG", "onCompleted: " + first_name + " " + last_name + " " + mail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }


    private void register() {

        String name, email, password;

        if (!TextUtils.isEmpty(mEmail.getText().toString().trim()) && !TextUtils.isEmpty(mPassword.getText().toString().trim())) {

            name = mName.getText().toString().trim();
            email = mEmail.getText().toString().trim();
            password = mPassword.getText().toString().trim();

            NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

            Call<RegistrationResp> call = api.registration(name, email, gender, password, password, countryCodePicker.getSelectedCountryName().toLowerCase());

            call.enqueue(new Callback<RegistrationResp>() {
                @Override
                public void onResponse(Call<RegistrationResp> call, Response<RegistrationResp> response) {
                    if (response.isSuccessful() && response.code() == 200 && response.body().getMessage().contains("successfull")) {

                        Toast.makeText(RegistrationActivity.this, response.body().getMessage() + " Please Login ", Toast.LENGTH_LONG).show();

                        if (isDeepLink) {
                            NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");
                            Call<JSONObject> in3 = api.invite_friend(INVITED_USER_ID + "", response.body().getUser().getId() + "", deviceID);
                            in3.enqueue(new Callback<JSONObject>() {
                                @Override
                                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                                    Log.d("TAG", "onResponse: " + response.body().toString());
                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                                @Override
                                public void onFailure(Call<JSONObject> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();

//                                   Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
//                                   startActivity(intent);
//                                   finish();


                                }
                            });

                        } else {
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }


                    } else {

                        Toast.makeText(RegistrationActivity.this, "Error :  " + response.code(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResp> call, Throwable t) {
                    Toast.makeText(RegistrationActivity.this, "Please Check Your Entered Data!!!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            if (TextUtils.isEmpty(mEmail.getText().toString())) {
                mEmail.setError("Can't be Empty");
            }
            if (TextUtils.isEmpty(mPassword.getText().toString())) {
                mPassword.setError("Can't be Empty");
            }
        }
    }

    private void initializations() {
        gender = "male";
        mName = findViewById(R.id.regi_name);
        mEmail = findViewById(R.id.regi_email);
        mPassword = findViewById(R.id.regi_pass);
        mSignUpBtn = findViewById(R.id.signUPBtn);
        mConfirmationPass = findViewById(R.id.login_confirm_password_et);
        genderSpinner = findViewById(R.id.genderList);
        vk = findViewById(R.id.vkIcon2);
        googleBtn = findViewById(R.id.gButton);
        fbBtn = findViewById(R.id.fbBtn);


        ArrayAdapter<String> catgoery_adapter = new ArrayAdapter<String>(RegistrationActivity.this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.gender));
        catgoery_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(catgoery_adapter);


        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = "gender";
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.


        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.d("TAG", "handleSignInResult: " + account.getDisplayName()
                    + account.getId() + " " + account.getIdToken());
//            String name, String userName, String fb_Id, String email, String google_id
            RegisterWithSocial(account.getDisplayName(), account.getDisplayName(), "null", account.getEmail(), account.getId(), "google");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(getApplicationContext(), "Failed : Try Again ", Toast.LENGTH_LONG).show();
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    public void RegisterWithSocial(String name, String userName, String fb_Id, String email, String google_id, String type) {
        Call<LoginResponse> call = RetrofitClient.getInstance()
                .getApi()
                .socialReg(
                        userName,
                        email,
                        name,
                        google_id,
                        fb_Id,
                        type
                );
//        call.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if(response.code()==201){
//
//                            if(!response.body().get()){
//
//                                // reg compelete
//
//                                UserModel userModel = new UserModel();
//                                userModel = response.body().getUser();
//
//                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel.getId(),
//                                        userModel.getUsername(),
//                                        userModel.getEmail(), response.body().getToken(),userModel.getRole(), userModel.getUserType() , userModel.getAvatar());
//
//
//                                StringGen.token = userModel.getToken() ;
//                                SharedPrefManager.getInstance(getApplicationContext()).saveUser(userModel.getEmail());
//                                Log.d("TAG", "onResponse: " + userModel.getToken());
//                                //   pbar.setVisibility(View.GONE);
//                                Intent intent = new Intent(RegistrationActivity.this, HomePage.class);
//                                startActivity(intent);
//                                finish();
//
//                            }
//                            else {
//                                // error
//                                Toast.makeText(getApplicationContext(), "Error " + response.body().getError() , Toast.LENGTH_LONG)
//                                        .show();
//                            }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Error " + t.getMessage() , Toast.LENGTH_LONG)
//                        .show();
//            }
//        });

    }


}