package com.delaroystodios.metakar.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterItemAdvertisementRecyclerView;
import com.delaroystodios.metakar.Model.Advertisement;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;
import com.delaroystodios.metakar.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ConstantConditions")
public class HomeFragment extends Fragment {


    private ProgressBar prgLazyLoadingAdvertisements;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Advertisement> listitemes;
    private ProgressBar prgLazyLoadingAdvertisementsfooter;
    private TextView errorConnectToInternet;
    private int offset = 12;
    private int skip = 0;
    private int TOTAL_PAGES = 500;
    private Boolean isLastPage = false;
    private AdapterItemAdvertisementRecyclerView adapterItemAdvertisementRecyclerView;
    private RecyclerView recyclerView;
    private View view;
    private Boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


            view = inflater.inflate(R.layout.fragment_home, container, false);

            initComponent();

            hideSoftKeyboard();

        return view;
    }

    private void initComponent() {

        prgLazyLoadingAdvertisements = view.findViewById(R.id.prgLazyLoadingAdvertisements);
        prgLazyLoadingAdvertisementsfooter = view.findViewById(R.id.prgLazyLoadingAdvertisementsfooter);
        recyclerView = view.findViewById(R.id.recyclerView_advertisement);
        errorConnectToInternet = view.findViewById(R.id.errorConnectToInternet);


        prgLazyLoadingAdvertisements.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        prgLazyLoadingAdvertisementsfooter.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        setupRecyclerView(recyclerView);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prgLazyLoadingAdvertisements.setVisibility(View.VISIBLE);
                adapterItemAdvertisementRecyclerView.clear();

                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    skip = 0;
                    getDataLimit(12, 0);
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

    private void setupRecyclerView(RecyclerView recyclerView) {

        if (Utilities.checkNetworkConnection(getActivity())) {
            getDataLimit(12, 0);
        } else {
            Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }


        listitemes = new ArrayList<>();
        adapterItemAdvertisementRecyclerView = new AdapterItemAdvertisementRecyclerView(getActivity(), listitemes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterItemAdvertisementRecyclerView);
        adapterItemAdvertisementRecyclerView.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , getContext()) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection((getActivity())))
                {
                    isLoading = true;

                    errorConnectToInternet.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;
                            getDataLimit(offset , skip);
                        }

                    }, 300);

                }else{
                    errorConnectToInternet.setVisibility(View.VISIBLE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                }
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void getDataLimit(final int offset,final int skip){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getAdvertisementsLimit(offset,skip).enqueue(new Callback<List<Advertisement>>() {
            @Override
            public void onResponse(Call<List<Advertisement>> call, Response<List<Advertisement>> response) {
                listitemes = new ArrayList<>();
                if (response.isSuccessful()) {


                    prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);

                    listitemes.addAll(response.body());

                    if (listitemes.size() == 0) {
                        Toast.makeText(getActivity(), R.string.empty_list_message_advertisements, Toast.LENGTH_LONG).show();
                    } else {
                        adapterItemAdvertisementRecyclerView.addAll(listitemes);
                        isLoading = false;

                    }
                }else{
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Advertisement>> call, Throwable t) {
                Log.i("logerror" , t.getMessage());
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
