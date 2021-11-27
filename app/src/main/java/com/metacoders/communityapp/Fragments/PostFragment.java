package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.details.EditPostPage;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
import com.metacoders.communityapp.adapter.new_adapter.OwnPostListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFragment extends Fragment implements OwnPostListDifferAdapter.ItemClickListener {

    OwnPostListDifferAdapter mAdapter;

    View view;
    List<Post.PostModel> post_modelList = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    String id = "1";
    ConstraintLayout emptyLayout;
    SwipeRefreshLayout swipeContainer;
    String type = "";
    String auther_id = "";
    private ShimmerFrameLayout mShimmerViewContainer2;

    public PostFragment(String type, String auther_id) {
        // Required empty public constructor
        this.type = type;
        this.auther_id = auther_id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video, container, false);
        recyclerView = view.findViewById(R.id.list);
        context = view.getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (auther_id.equals(AppPreferences.getUSerID(context))) {
            mAdapter = new OwnPostListDifferAdapter(getContext(), this, true);
        } else {
            mAdapter = new OwnPostListDifferAdapter(getContext(), this, false);
        }


        recyclerView.setAdapter(mAdapter);
        loadMiscData();
        loadUrPost();


        return view;
    }

    public void loadUrPost() {
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getContext()));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(auther_id + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();
                if (response.code() == 200) {
                    try {

                        if (type.contains("video")) {
                            post_modelList = ownListModelList.getOwnVideos();

                        } else if (type.contains("audio")) {

                            post_modelList = ownListModelList.getOwnAudios();

                        } else {
                            post_modelList = ownListModelList.getOwnArticles();
                        }


                        if (post_modelList.size() == 0) {

                            emptyLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            /*
                                loop the whole list for counting post type

                             */


                            mAdapter.submitlist(post_modelList);

                            // checking if the list is empty or not
                            emptyLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error : Code " + e.getMessage(), Toast.LENGTH_LONG).show();
                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


                } else {
                    Toast.makeText(getContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<AuthorPostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error : Code " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void loadMiscData() {
        String[] arr = SharedPrefManager.getInstance(context).getLangPref();
        // load the array  arr[0] = id arr[1] = name
        id = arr[0];


    }

    @Override
    public void onItemClick(Post.PostModel model, Boolean isOPtion) {
        Intent p;
        if (isOPtion) {
            triggerOPtions(model);

        } else {
            if (model.getType().equals("audio") || model.getType().equals("video")) {
                p = new Intent(context, PostDetailsPage.class);

            } else {
                p = new Intent(context, NewsDetailsActivity.class);
            }

            p.putExtra("POST", model);
            if (!auther_id.equals(AppPreferences.getUSerID(context))) {
             //   p.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(p);
        }
    }

    private void triggerOPtions(Post.PostModel model) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.post_options);
        dialog.setDismissWithAnimation(true);
        LinearLayout edit = dialog.findViewById(R.id.edit);
        LinearLayout delete = dialog.findViewById(R.id.Delete);
        LinearLayout cancel = dialog.findViewById(R.id.cancel);

        edit.setOnClickListener(v -> {
            Intent p = new Intent(getContext(), EditPostPage.class);
            p.putExtra("MODEL", model);
            startActivity(p);
        });

        delete.setOnClickListener(v -> {
            unPublish(model.getId(), dialog);
        });

        cancel.setOnClickListener(v -> {
            dialog.setDismissWithAnimation(true);
            dialog.dismiss();
        });


        dialog.show();
    }

    private void unPublish(int id, BottomSheetDialog dialog) {
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getContext()));
/*
 @Field("lang") String lang,
            @Field("category") String category,
            @Field("country") String country,
            @Field("title") String title,
            @Field("description") String description
 */
        Call<JSONObject> NetworkCall = api.ChangeStatus(id + "", "2");

        NetworkCall.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(getContext(), "Post Removed", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    loadUrPost();
                } else {

                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getContext(), "Error " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}