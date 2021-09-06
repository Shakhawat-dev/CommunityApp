package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.Audio_List_Model;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    List<Post_Model> postsList = new ArrayList<>();
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<Post_Model> filteredList = new ArrayList<>();
    ConstraintLayout emptyLayout;
    String id = "1";
    SwipeRefreshLayout swipeContainer;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ShimmerFrameLayout mShimmerViewContainer1;
    public AudioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
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
        view = inflater.inflate(R.layout.fragment_audio, container, false);
        mShimmerViewContainer1 = view.findViewById(R.id.shimmer_view_container_audio);
        recyclerView = view.findViewById(R.id.audioList);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        //  code to refresh the list here.
        swipeContainer.setOnRefreshListener(this::fetchTimelineAsync);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        context = view.getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadMiscData();
        loadList();

        // calling the interface for click


        return view;
    }

    private void loadList() {
        //setting up layout
        emptyLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<Audio_List_Model> NetworkCall = api.getAudioList();

        NetworkCall.enqueue(new Callback<Audio_List_Model>() {
            @Override
            public void onResponse(Call<Audio_List_Model> call, Response<Audio_List_Model> response) {
                // u have the response
                if (response.code() == 201) {
                    Audio_List_Model model = response.body();

                    postsList = model.getGetNewsList();

                    if (postsList != null && !postsList.isEmpty()) {
                        // i know its werid but thats r8 cheaking list is popluted
                        // its not empty
                        // Call the adapter to show the data

                        filteredList.clear();

                        for (Post_Model post : postsList) {
                            if (post.getLangId().equals(id)) {
                                filteredList.add(post);
                            }
                        }

//                        adapter = new NewsFeedAdapter(context, filteredList, itemClickListenter);
//
//                        // setting the adapter ;
//                        recyclerView.setAdapter(adapter);
                        // checking if the list is empty or not
                        if (filteredList.size() == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
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


                        mShimmerViewContainer1.stopShimmer();
                        mShimmerViewContainer1.setVisibility(View.GONE);


                    } else {
                        // the list is empty
                        Log.d("TAG", "Error: List Is Empty  " + response.errorBody());
                    }

                } else {
                    Log.d("TAG", "Error: " + response.errorBody() +
                            " Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Audio_List_Model> call, Throwable t) {
                Log.d("TAG", "Error On Failed Response: " + t.getMessage());
            }
        });
    }

    public void fetchTimelineAsync() {

        mShimmerViewContainer1.setVisibility(View.VISIBLE);
        mShimmerViewContainer1.startShimmer();
        loadList();

        swipeContainer.setRefreshing(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer1.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer1.stopShimmer();
        super.onPause();
    }


    private void loadMiscData() {
        String[] arr = SharedPrefManager.getInstance(context).getLangPref();
        // load the array  arr[0] = id arr[1] = name


        id = arr[0];
        // Toast.makeText(context, id, Toast.LENGTH_LONG).show();


    }

}