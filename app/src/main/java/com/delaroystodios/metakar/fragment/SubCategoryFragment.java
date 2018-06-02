package com.delaroystodios.metakar.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterItemSubCategory;
import com.delaroystodios.metakar.MainActivity;
import com.delaroystodios.metakar.Model.SubCategory;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryFragment extends Fragment {


    private ProgressBar prgLazyLoadingAdvertisements;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<SubCategory> listitemes;
    private AdapterItemSubCategory adapterItemAdvertisementRecyclerView;
    private RecyclerView recyclerView;
    private View view;
    public static String titleCategory = "";
    private TextView titleCategoryText;
    public String idCategory = "-1";
    private ImageView backCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sub_category_fragment, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            titleCategory = bundle.getString("titleCategory", "null");
            idCategory = bundle.getString("idCategory", "-1");
        }

        initComponent();

        return view;
    }

    private void initComponent()
    {

        prgLazyLoadingAdvertisements = (ProgressBar)view.findViewById(R.id.prgLazyLoadingAdvertisements);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_advertisement);
        titleCategoryText = (TextView)view.findViewById(R.id.title_category);


        titleCategoryText.setText(titleCategory);

        setupRecyclerView(recyclerView);

        backCategory = (ImageView)view.findViewById(R.id.back_category);


        backCategory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

                MainActivity.ft = MainActivity.fragmentManager.beginTransaction();
                MainActivity.ft.show(MainActivity.categoryFragment);
                MainActivity.ft.commit();
            }
        });



        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prgLazyLoadingAdvertisements.setVisibility(View.VISIBLE);
                adapterItemAdvertisementRecyclerView.clear();

                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    getListCategory(idCategory);
                }else{
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }

                swipeContainer.setRefreshing(false);
            }

        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setupRecyclerView(final RecyclerView recyclerView) {

        if (Utilities.checkNetworkConnection(getActivity())) {
            getListCategory(idCategory);
        } else {
            Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }


        listitemes = new ArrayList<>();
        adapterItemAdvertisementRecyclerView = new AdapterItemSubCategory(getActivity(), listitemes , recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterItemAdvertisementRecyclerView);

        adapterItemAdvertisementRecyclerView.notifyDataSetChanged();


    }

    private void getListCategory(String idCategory){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getSubCategory(idCategory).enqueue(new Callback<List<SubCategory>>() {
            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                listitemes = new ArrayList<>();
                if (response.isSuccessful()) {
                    //     hideSoftKeyboard();
                    for (SubCategory category: response.body()) {
                        prgLazyLoadingAdvertisements.setVisibility(View.GONE);

                        listitemes.add(category);
                    }
                    if (listitemes.size() == 0) {
                        prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), R.string.empty_list_message_advertisements, Toast.LENGTH_LONG).show();
                    } else {
                        adapterItemAdvertisementRecyclerView.addAll(listitemes);
                    }
                }else{
                    prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}