package com.metacoders.communityapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoders.communityapp.adapter.CommentsAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.CommentModel;
import com.metacoders.communityapp.models.RegistrationResponse;
import com.metacoders.communityapp.models.UserModel;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity {
    String post_id;
    EditText commentEt;
    ImageButton sendBtn;
    RecyclerView recyclerView;
    ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        commentEt = findViewById(R.id.write_comment_ed);
        sendBtn = findViewById(R.id.imageButton);
        recyclerView = findViewById(R.id.comments_recyclerview) ;
        progressBar = findViewById(R.id.progress_bar) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

     //   Intent p = getIntent();
        post_id = getIntent().getStringExtra("POST_ID");
       // Toast.makeText(getApplicationContext() , "p = " + post_id  , Toast.LENGTH_LONG).show(); ;


        // send msg
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEt.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(getApplicationContext(), "Enter Commnet", Toast.LENGTH_LONG).show();
                } else {
                    sendTheCommnet(comment.trim());

                }
            }
        });
    }

    private void sendTheCommnet(String commnet) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
        UserModel user = sharedPrefManager.getUser();
        ProgressDialog dialog = new ProgressDialog(CommentsActivity.this)  ;
        dialog.setMessage("Adding Your Comment ...");
        dialog.show();
        dialog.setCancelable(false);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);
//        @Field("post_id") String post_id,
//        @Field("comment") String comment,
//        @Field("email") String email,
//        @Field("name") String name,
//        @Field("user_id") String user_id,
//        @Field("parent_id") String parent_id,
//        @Field("like_count") String like_count,
//        @Field("ip_address") String ip_address
        Call<RegistrationResponse> NetworkCall = api.post_comments(
                post_id, commnet, user.getEmail(),
                user.getName(), user.getId(),
                "0", "0", "0"
        );

        NetworkCall.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                if (response.code() == 201) {
                try{
                    if (!response.body().getError()) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), " Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        commentEt.setText("");
                        loadAllComments();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error  : " + response.body().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }catch (Exception er ){
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Code : " + er.getMessage(), Toast.LENGTH_LONG).show();

                }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Code : " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        loadAllComments() ;
        super.onResume();
    }

    private void loadAllComments() {
        progressBar.setVisibility(View.VISIBLE);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        String accessTokens = sharedPrefManager.getUserToken();
        UserModel user = sharedPrefManager.getUser();
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, accessTokens);
        Call<CommentModel> loadCommnetCall = api.getCommentsList(post_id) ;
        loadCommnetCall.enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if(response.code() == 201){
                    try{
                        progressBar.setVisibility(View.GONE);
                        List<CommentModel.comments> commentsList = new ArrayList<>() ;
                        commentsList = response.body().getComments() ;
                        recyclerView.setAdapter(new CommentsAdapter(getApplicationContext() ,commentsList));

                    }catch (Exception e){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error : " +e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error : " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}