package com.delaroystodios.metakar.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterItemCategoryRecyclerView;
import com.delaroystodios.metakar.Model.Category;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {


    private ProgressBar prgLazyLoadingAdvertisements;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Category> listitemes;
    private AdapterItemCategoryRecyclerView adapterItemAdvertisementRecyclerView;
    private RecyclerView recyclerView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_category, container, false);

        hideSoftKeyboard();


        initComponent();

        return view;
    }

    private void initComponent() {

        prgLazyLoadingAdvertisements = (ProgressBar)view.findViewById(R.id.prgLazyLoadingAdvertisements);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_advertisement);


        setupRecyclerView(recyclerView);


        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prgLazyLoadingAdvertisements.setVisibility(View.VISIBLE);
                adapterItemAdvertisementRecyclerView.clear();

                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    getListCategory();
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
            getListCategory();
        } else {
            Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }


        listitemes = new ArrayList<>();
        adapterItemAdvertisementRecyclerView = new AdapterItemCategoryRecyclerView(getActivity(), listitemes , recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterItemAdvertisementRecyclerView);

        adapterItemAdvertisementRecyclerView.notifyDataSetChanged();


    }

    private void getListCategory(){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getListCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                listitemes = new ArrayList<>();
                if (response.isSuccessful()) {

                    for (Category category: response.body()) {
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
            public void onFailure(Call<List<Category>> call, Throwable t) {
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        if(inputMethodManager != null && getActivity().getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

}