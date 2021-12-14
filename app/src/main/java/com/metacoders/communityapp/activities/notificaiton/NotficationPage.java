package com.metacoders.communityapp.activities.notificaiton;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.new_adapter.notificationAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.NotificationResponse;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.NotificationData;
import com.metacoders.communityapp.utils.AppPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotficationPage extends AppCompatActivity implements notificationAdapter.ItemClickListener {
    RecyclerView list;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notfication_page);
        list = findViewById(R.id.list);
        progressBar = findViewById(R.id.pbar);

        list.setLayoutManager(new LinearLayoutManager(this));

        loadAllComments();

    }

    private void loadAllComments() {
        progressBar.setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getApplicationContext()));
        Call<NotificationResponse> loadCommnetCall = api.getNotifications();
        loadCommnetCall.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.code() == 200) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        List<NotificationData> commentsList = new ArrayList<>();
                        commentsList = response.body().getNotificationList();
                        list.setAdapter(new notificationAdapter(commentsList, getApplicationContext(), NotficationPage.this));
                        if (list.getAdapter().getItemCount() <= 0) {
                            Toast.makeText(getApplicationContext(), "No Notification Found", Toast.LENGTH_LONG).show();
                        }

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
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onItemClick(NotificationData model) {

    }
}