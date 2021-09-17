package com.metacoders.communityapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.metacoders.communityapp.R;
import com.metacoders.communityapp.adapter.CategoryAdapter;
import com.metacoders.communityapp.api.NewsRmeApi;
import com.metacoders.communityapp.api.ServiceGenerator;
import com.metacoders.communityapp.models.newModels.CategoryModel;
import com.metacoders.communityapp.models.newModels.SettingsModel;
import com.metacoders.communityapp.singleList;
import com.metacoders.communityapp.utils.SharedPrefManager;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    Context context;
    RecyclerView recyclerView;
    List<CategoryModel> categoryList;
    CategoryAdapter adapter;
    ProgressBar progressBar;

    CategoryAdapter.ItemClickListenter itemClickListenter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        view = inflater.inflate(R.layout.fragment_category, container, false);

        context = view.getContext();
        recyclerView = view.findViewById(R.id.categoryList);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        progressBar = (ProgressBar) view.findViewById(R.id.pbar);
        progressBar.setVisibility(View.GONE);
        itemClickListenter = (view, pos) -> {

            Intent o = new Intent(getContext(), singleList.class);
            o.putExtra("cat_name", categoryList.get(pos).getCategory_name());
            o.putExtra("type", "cat");
            startActivity(o);

        };

        LoadData();


        return view;

    }

    private void LoadData() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        String accessTokens = sharedPrefManager.getUserToken();
        Log.d("TAG", "loadList: activity " + accessTokens);

        NewsRmeApi api = ServiceGenerator.createService(NewsRmeApi.class, "00");

        Call<SettingsModel> catCall = api.getCategories_Countries();

        catCall.enqueue(new Callback<SettingsModel>() {
            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {

                    SettingsModel dataResponse = response.body();

                    try {
                        categoryList = dataResponse.getCategories();
                        Collections.reverse(categoryList);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // send it to adaper

                    adapter = new CategoryAdapter(context, categoryList, itemClickListenter);

                    recyclerView.setAdapter(adapter);


                } else {
                    Toast.makeText(getContext(), "Error : Code " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SettingsModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}