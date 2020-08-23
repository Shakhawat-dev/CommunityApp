package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.activities.PostDetailsPage;
import com.metacoders.communityapp.activities.PostUploadActivity;
import com.metacoders.communityapp.activities.SerachActivity;
import com.metacoders.communityapp.activities.Voice_Recoder_Activity;
import com.metacoders.communityapp.adapter.NewsFeedAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.News_List_Model;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.models.UserModel;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

   public SharedPrefManager sharedPrefManager;
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NEwsFragment.
     */

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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

    View view;
    List<Post_Model> postsList = new ArrayList<>();
    NewsFeedAdapter.ItemClickListenter itemClickListenter;
    NewsFeedAdapter adapter;
    Context context;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button audioBtn , imageBtn ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.newsfeed);
        context = view.getContext();
        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        audioBtn = view.findViewById(R.id.audioBtn) ;
        imageBtn = view.findViewById(R.id.photoBtn);

        audioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(getContext() , Voice_Recoder_Activity.class);

                startActivity(o);
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent o = new Intent(getContext() , PostUploadActivity.class);
                o.putExtra("media", "post") ;
                startActivity(o);
            }
        });

        view.findViewById(R.id.videoBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent o = new Intent(getContext() , SerachActivity.class);
                startActivity(o);
            }
        });

        loadList();

        // calling the interface for click
        itemClickListenter = new NewsFeedAdapter.ItemClickListenter() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent p = new Intent(context , PostDetailsPage.class);
                p.putExtra("media_link" , Constants.IMAGE_URL +postsList.get(pos).getVideoPath()) ;
                context.startActivity(p);
                try {
                    getActivity().overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
                }
                catch (Exception e )
                {
                    Log.e("TAG", "onItemClick: " + e.getMessage());
                }

            }
        };

        SharedPrefManager manager = new SharedPrefManager(context) ;
        UserModel model = manager.getUser() ;
        Log.d("TAG" , model.getEmail() + " "  + model.getName()+ " " + model.getId()+ " " + model.getRole()) ;
        return view;
    }



    private void loadList() {


        sharedPrefManager = new SharedPrefManager(context) ;
        String   accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


//        Call<News_List_Model> NetworkCall = RetrofitClient
//                .getInstance()
//                .getApi()
//                .getNewsList();

      NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , accessTokens) ;

        Call<News_List_Model> NetworkCall = api.getNewsList() ;

        NetworkCall.enqueue(new Callback<News_List_Model>() {
            @Override
            public void onResponse(Call<News_List_Model> call, Response<News_List_Model> response) {
                // u have the response
                if (response.code() == 201) {
                    News_List_Model model = response.body();

                    postsList = model.getGetNewsList();


                    if (postsList != null && !postsList.isEmpty()) {
                        // i know its werid but thats r8 cheaking list is popluted
                        // its not empty
                        // Call the adapter to show the data

                        adapter = new NewsFeedAdapter(context, postsList, itemClickListenter);

                        // setting the adapter ;

                        recyclerView.setAdapter(adapter);


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
            public void onFailure(Call<News_List_Model> call, Throwable t) {
                Log.d("TAG", "Error On Failed Response: " + t.getMessage());
            }
        });
    }


}