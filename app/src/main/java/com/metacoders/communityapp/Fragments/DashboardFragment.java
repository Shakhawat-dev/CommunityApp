package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.NewsFeedAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.OwnListModel;
import com.metacoders.communityapp.models.Post_Model;
import com.metacoders.communityapp.models.post_summary;
import com.metacoders.communityapp.utils.Constants;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    View view ;
    Context context  ;
    TextView  tpost , tvideo , taudio  , name , mail ;
    NewsFeedAdapter.ItemClickListenter itemClickListenter ;
    RecyclerView recyclerView   ;
    CircleImageView circleImageView ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false) ;
        context  = view.getContext();
        tpost = view.findViewById(R.id.tpost) ;
        tvideo = view.findViewById(R.id.tvideos) ;
        taudio =view.findViewById(R.id.taudios) ;
        recyclerView = view.findViewById(R.id.rlist) ;
        name =view.findViewById(R.id.name) ;
        mail = view.findViewById(R.id.mail_id) ;
        circleImageView = view.findViewById(R.id.dashboard_profile_pic);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context) ;
       // String   accessTokens = ;

        name.setText(sharedPrefManager.getUser().getUsername()+ " ");
        mail.setText(sharedPrefManager.getUser().getEmail()+ " ");
        Glide.with(context)
                .load(Constants.IMAGE_URL + sharedPrefManager.getUser().getAvatar())
                .into(circleImageView) ;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        loadSummary();
        loadUrLost();
        itemClickListenter = new NewsFeedAdapter.ItemClickListenter() {
            @Override
            public void onItemClick(View view, int pos) {

            }
        } ;
        return view;
    }

    public  void loadSummary(){

        SharedPrefManager sharedPrefManager = new SharedPrefManager(context) ;
        String   accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , accessTokens) ;
            Call<post_summary> catCall = api.get_post_summary();

            catCall.enqueue(new Callback<post_summary>() {
                @Override
                public void onResponse(Call<post_summary> call, Response<post_summary> response) {
                    if(response.code() == 201){

                        post_summary post = response.body() ;
                        List<post_summary.Audio> audio  = post.getAudio() ;
                        List<post_summary.Video> video = post.getVideo() ;
                        List<post_summary.Post> posst =  post.getPost() ;

                        tpost.setText(posst.get(0).getTtl() + "");
                        taudio.setText(audio.get(0).getTtl() + "");
                        tvideo.setText(video.get(0).getTtl() + "");




                    }
                }

                @Override
                public void onFailure(Call<post_summary> call, Throwable t) {

                }
            });
    }

    public  void loadUrLost(){
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context) ;
        String   accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);


        NewsRmeApi api  = ServiceGenerator.createService(NewsRmeApi.class , accessTokens) ;
        Call<OwnListModel> catCall = api.get_post_list();

        catCall.enqueue(new Callback<OwnListModel>() {
            @Override
            public void onResponse(Call<OwnListModel> call, Response<OwnListModel> response) {
               OwnListModel ownListModelList = response.body() ;
               if(response.code() == 201 ){
                   List<Post_Model> post_modelList = ownListModelList.getGetNewsList() ;

                  NewsFeedAdapter  adapter = new NewsFeedAdapter(context, post_modelList, itemClickListenter);

                   // setting the adapter ;

                   recyclerView.setAdapter(adapter);

               }

            }

            @Override
            public void onFailure(Call<OwnListModel> call, Throwable t) {

            }
        });
    }
}