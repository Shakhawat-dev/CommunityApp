package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.details.NewsDetailsActivity;
import com.metacoders.communityapp.activities.details.PostDetailsPage;
import com.metacoders.communityapp.adapter.new_adapter.ProductListDifferAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.AuthorPostResponse;
import com.metacoders.communityapp.models.newModels.Post;
import com.metacoders.communityapp.utils.AppPreferences;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment implements ProductListDifferAdapter.ItemClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    Context context;
    TextView tpost, tvideo, taudio, name, mail;
    RecyclerView recyclerView;
    CircleImageView circleImageView;
    ShimmerFrameLayout shimmer_view_container_dash;
    List<Post.PostModel> post_modelList = new ArrayList<>();
    ProductListDifferAdapter mAdapter;
    ConstraintLayout emptyLayout;
    int videoCount = 0, audioCount = 0, postCount = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = view.getContext();
        tpost = view.findViewById(R.id.tpost);
        tvideo = view.findViewById(R.id.tvideos);
        taudio = view.findViewById(R.id.taudios);
        recyclerView = view.findViewById(R.id.rlist);
        name = view.findViewById(R.id.name);
        mail = view.findViewById(R.id.mail_id);
        shimmer_view_container_dash = view.findViewById(R.id.shimmer_view_container_dash);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        circleImageView = view.findViewById(R.id.dashboard_profile_pic);

        mAdapter = new ProductListDifferAdapter(getContext(), DashboardFragment.this, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
        if (SharedPrefManager.getInstance(context).isUserLoggedIn()) {
            setDetails();
            //loadSummary();
            loadUrPost();

        }


        return view;
    }


    private void setDetails() {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        // String   accessTokens = ;
        name.setText(sharedPrefManager.getUserModel().getName() + " ");
        mail.setText(sharedPrefManager.getUserModel().getEmail() + " ");
        Glide.with(context)
                .load(sharedPrefManager.getUserModel().getImage())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(circleImageView);
    }


    public void loadUrPost() {
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, AppPreferences.getAccessToken(getContext()));
        Call<AuthorPostResponse> catCall = api.getAuthorPost(SharedPrefManager.getInstance(getContext()).getUser_ID() + "");

        catCall.enqueue(new Callback<AuthorPostResponse>() {
            @Override
            public void onResponse(Call<AuthorPostResponse> call, Response<AuthorPostResponse> response) {
                AuthorPostResponse ownListModelList = response.body();
                if (response.code() == 200) {
                    try {
                        post_modelList = ownListModelList.getOwnVideos();
                        post_modelList.addAll(ownListModelList.getOwnArticles());
                        post_modelList.addAll(ownListModelList.getOwnAudios());

                        Collections.sort(post_modelList, (o1, o2) -> o2.getId() - o1.getId());

                        if (post_modelList.size() == 0) {

                            emptyLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            /*
                                loop the whole list for counting post type

                             */

                            try {
                                taudio.setText(ownListModelList.getOwnAudios().size() + "");
                                tvideo.setText(ownListModelList.getOwnVideos().size() + "");
                                tpost.setText(ownListModelList.getAuthor().getTotal_point() + "");

                            } catch (Exception e) {

                            }


                            mAdapter.submitlist(post_modelList);

                            // checking if the list is empty or not
                            emptyLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        recyclerView.getViewTreeObserver().addOnPreDrawListener(

                                new ViewTreeObserver.OnPreDrawListener() {
                                    @Override
                                    public boolean onPreDraw() {

                                        recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                                        for (int i = 0; i < recyclerView.getChildCount(); i++) {
                                            View v = recyclerView.getChildAt(i);
                                            v.setAlpha(0.0f);
                                            v.animate()
                                                    .alpha(1.0f)
                                                    .setDuration(300)
                                                    .setStartDelay(i * 50)
                                                    .start();
                                        }
                                        return true;
                                    }
                                }
                        );
                    } catch (Exception e) {

                        emptyLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


                    shimmer_view_container_dash.stopShimmer();
                    shimmer_view_container_dash.setVisibility(View.GONE);


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

    @Override
    public void onResume() {
        super.onResume();
        shimmer_view_container_dash.startShimmer();
    }

    @Override
    public void onPause() {
        shimmer_view_container_dash.stopShimmer();
        super.onPause();
    }

    @Override
    public void onItemClick(Post.PostModel model) {
        Intent p;
        if (model.getType().equals("audio") || model.getType().equals("video")) {
            p = new Intent(context, PostDetailsPage.class);
        } else {
            p = new Intent(context, NewsDetailsActivity.class);
        }
        p.putExtra("POST", model);
        startActivity(p);

    }
}