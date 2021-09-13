package com.metacoders.communityapp.activities.comments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.new_adapter.CommentAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.CommentModel;
import com.metacoders.communityapp.models.LoginResponse;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity implements CommentAdapter.ItemClickListener {
    String post_id, slug = "";
    EditText commentEt;
    ImageButton sendBtn;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    CommentAdapter commentAdapter;
    RelativeLayout replyContainer;
    ImageView close_reply;
    TextView replayingUserName;
    int comment_id = 0;
    boolean is_reply = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        commentEt = findViewById(R.id.write_comment_ed);
        sendBtn = findViewById(R.id.imageButton);
        recyclerView = findViewById(R.id.comments_recyclerview);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        replyContainer = findViewById(R.id.replyContiner);
        close_reply = findViewById(R.id.replyCloseBtn);
        replayingUserName = findViewById(R.id.replayingUserName);

        //   Intent p = getIntent();

        slug = getIntent().getStringExtra("slug");
        post_id = getIntent().getStringExtra("POST_ID");
        Toast.makeText(getApplicationContext(), "POS "+ post_id , Toast.LENGTH_LONG).show();

        try {
            AppPreferences.setActionbarTextColor(getSupportActionBar(), Color.WHITE, "Comments");
        } catch (Exception e) {
        }

        // send msg
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentEt.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(getApplicationContext(), " Please Enter Comment ", Toast.LENGTH_LONG).show();
                } else {
                    if (is_reply) {
                        sendTheReply(comment);
                    } else sendTheCommnet(comment.trim());


                }
            }
        });

        close_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_reply = false;
                replyContainer.setVisibility(View.GONE);
            }
        });


    }

    private void sendTheCommnet(String commnet) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        ProgressDialog dialog = new ProgressDialog(CommentsActivity.this);
        dialog.setMessage("Adding Your Comment ...");
        dialog.show();
        dialog.setCancelable(false);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<LoginResponse.forgetPassResponse> NetworkCall = api.post_comments(
                post_id, AppPreferences.getUSerID(getApplicationContext()) + "", commnet

        );

        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.code() == 200) {
                    try {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), " Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        commentEt.setText("");
                        loadAllComments();

                    } catch (Exception er) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error Code : " + er.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Code : " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendTheReply(String reply) {

        ProgressDialog dialog = new ProgressDialog(CommentsActivity.this);
        dialog.setMessage("Adding Your Reply ...");
        dialog.show();
        dialog.setCancelable(false);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));

        Call<LoginResponse.forgetPassResponse> NetworkCall = api.post_reply(
                post_id, AppPreferences.getUSerID(getApplicationContext()) + "", comment_id + "", reply
        );

        NetworkCall.enqueue(new Callback<LoginResponse.forgetPassResponse>() {
            @Override
            public void onResponse(Call<LoginResponse.forgetPassResponse> call, Response<LoginResponse.forgetPassResponse> response) {
                if (response.code() == 200) {
                    try {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), " Msg : " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                        commentEt.setText("");
                        loadAllComments();
                        replyContainer.setVisibility(View.GONE);
                        comment_id = 0;
                        is_reply = false;

                    } catch (Exception er) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error Code : " + er.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error Code : " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LoginResponse.forgetPassResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        loadAllComments();
        super.onResume();
    }

    private void loadAllComments() {
        progressBar.setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<CommentModel> loadCommnetCall = api.getCommentsList(slug);
        loadCommnetCall.enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.code() == 200) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        List<CommentModel.comments> commentsList = new ArrayList<>();
                        commentsList = response.body().getComments();
                        recyclerView.setAdapter(new CommentAdapter(commentsList, getApplicationContext(), CommentsActivity.this));

                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                } else {
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

    @Override
    public void onItemClick(CommentModel.comments model) {
        is_reply = true;
        // also set the view
        comment_id = model.getId();

        replyContainer.setVisibility(View.VISIBLE);
        replayingUserName.setText("  Replying to " + model.getUser().getName());
    }
}