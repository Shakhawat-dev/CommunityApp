package com.metacoders.communityapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.RetrofitClient;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.api.TokenInterceptor;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.UserModel;
import com.metacoders.communityapp.utils.SharedPrefManager;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.StringGen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class LoginActivity extends AppCompatActivity {

    private TextView registerTV, forget_pass_tv;
    private TextInputEditText mUsername, mPassword;
    private Button mLoginBtn;
    SharedPrefManager manager;
    ProgressBar pbar;
    Button googleBtn, fbBtn, vk;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        googleBtn = findViewById(R.id.gButton);
        fbBtn = findViewById(R.id.fbBtn);
        pbar = findViewById(R.id.progress_bar);
        pbar.setVisibility(View.GONE);
        manager = new SharedPrefManager(getApplicationContext());
        initializations();
        getSupportActionBar().hide();


        // google sign in builder
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("377780946002-5p5rvr3l34iroj52k02o2i1956ljgb76.apps.googleusercontent.com")
                .requestEmail()
                .build();

        /*
         *if account is null then not yet signed in ;
         *
         */
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        getSupportActionBar().hide();
        initializations();


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });


        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
                callbackManager = CallbackManager.Factory.create();

                LoginManager.getInstance().registerCallback(callbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                // App code
                                Log.d("TAG", "onSuccess: " + loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                // App code
                                Log.d("TAG", "onCancel: Fb Cancel ");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                Log.d("TAG", "Error:  " + exception.getMessage());
                                try {
                                    LoginManager.getInstance().logOut();
                                } catch (Exception e) {

                                }
                            }

                        });
            }
        });


// token traker
        AccessTokenTracker tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                loadUserInfo(currentAccessToken);

            }
        };


        forget_pass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
//                changePassword();
            }
        });
    }

    //TODO: Checking Purpose, functionality will be changed


    private void login() {

        String userName, password;

        if (!TextUtils.isEmpty(mUsername.getText().toString().trim()) && !TextUtils.isEmpty(mPassword.getText().toString().trim())) {

            pbar.setVisibility(View.VISIBLE);
            userName = mUsername.getText().toString().trim();
            password = mPassword.getText().toString().trim();

//            Call<LoginResponse> call = RetrofitClient
//                    .getInstance()
//                    .getApi()
//                    .login(userName, password);


//            sharedPrefManager = new SharedPrefManager(context) ;
//            String   accessTokens = sharedPrefManager.getUserToken();
//            Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

            NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

            Call<LoginResponse> NetworkCall = api.login(userName, password);

            NetworkCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    Log.d(Constants.TAG, "onResponse: " + response.body().toString());

                    LoginResponse res = response.body();

                    if (res.getError()) {
                        // user pass or name wrong
                        pbar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Login Failed ", Toast.LENGTH_SHORT)
                                .show();
                    } else {

                        UserModel userModel = new UserModel();
                        userModel = response.body().getUser();

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel.getId(),
                                userModel.getUsername(),
                                userModel.getEmail(), response.body().getToken(), userModel.getRole(), userModel.getUserType(), userModel.getAvatar());


                        StringGen.token = response.body().getToken();
                        manager.saveUser(userModel.getEmail());
                        Log.d("TAGE", "onResponse: " + response.body().getToken());
                        //   pbar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();

                    }


                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.d(Constants.TAG, "onResponse: " + t.toString());
                }
            });
        }

    }

    private void initializations() {
        registerTV = (TextView) findViewById(R.id.register_tv);
        mUsername = (TextInputEditText) findViewById(R.id.login_username);
        mPassword = (TextInputEditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        forget_pass_tv = findViewById(R.id.forget_passwordBtn);
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

//            String name, String userName, String fb_Id, String email, String google_id)
                    RegisterWithSocial(first_name + " " + last_name, first_name + " " + last_name, id, mail, "null" , "facebook");

                    Log.d("TAG", "onCompleted: " + first_name + " " + last_name + " " + mail + "iD " + id);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ERROR", "onCompleted: " +  e.getMessage());

                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


        super.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//117132451500703402834  112222086677355635371
            // Signed in successfully, show authenticated UI.
            Log.d("TAG", "handleSignInResult: " + account.getDisplayName()
                    + account.getId() + " ");
//            String name, String userName, String fb_Id, String email, String google_id
            RegisterWithSocial(account.getDisplayName(), account.getDisplayName(), "null", account.getEmail(), account.getId() , "google");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(getApplicationContext(), "Failed : Try Again ", Toast.LENGTH_LONG).show();
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());

        }
    }


    public void RegisterWithSocial(String name, String userName, String fb_Id, String email, String google_id ,String type ) {

        pbar.setVisibility(View.VISIBLE);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");
//        @Field("user_name") String userName,
//        @Field("email") String email,
//        @Field("name") String name,
//        @Field("google_id") String google_id,  //facebook_id
//        @Field("") String facebook_id
        Call<LoginResponse> callwd = api.socialReg(
                userName,
                email,
                name,
                google_id,
                fb_Id,
                type
        );

        callwd.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 201) {

                    if (!response.body().getError()) {

                        // reg compelete

                        UserModel userModel = new UserModel();
                        userModel = response.body().getUser();

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(userModel.getId(),
                                userModel.getUsername(),
                                userModel.getEmail(), response.body().getToken(), userModel.getRole(), userModel.getUserType(), userModel.getAvatar());


//                        StringGen.token = userModel.getToken() ;
                        SharedPrefManager.getInstance(getApplicationContext()).saveUser(userModel.getEmail());
                        Log.d("TAGE", "reciveid Mail : " + userModel.getEmail());

                        Log.d("TAGE", "sent mail : " + email + " id" + fb_Id + " GID " + google_id + userName + name);
                        pbar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();

                    } else {
                        // error
                        pbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Error : Try Agian " + response.body().getError(), Toast.LENGTH_LONG)
                                .show();
                        Log.d("TAG", "onResponse: " + response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        });

    }

}