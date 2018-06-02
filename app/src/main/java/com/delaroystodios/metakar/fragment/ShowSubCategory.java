package com.delaroystodios.metakar.fragment;


import android.os.Bundle;
import android.os.Handler;
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

import com.delaroystodios.metakar.Adapter.AdapterItemAdvertisementRecyclerView;
import com.delaroystodios.metakar.MainActivity;
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

public class ShowSubCategory extends Fragment {


    private ProgressBar prgLazyLoadingAdvertisements;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Advertisement> listitemes;
    private ProgressBar prgLazyLoadingAdvertisementsfooter;
    private TextView errorConnectToInternet;
    private int offset = 9;
    private int skip;
    private int TOTAL_PAGES = 50;
    private Boolean isLastPage = false;
    private AdapterItemAdvertisementRecyclerView adapterItemAdvertisementRecyclerView;
    private RecyclerView recyclerView;
    private View view;
    private String titleSubCategory = "";
    private int idSubCategory = -1;
    private Boolean isLoading = false;
    private ImageView backSubCategory;
    private TextView titleItemSubCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            titleSubCategory = bundle.getString("titleSubCategory", "null");
            idSubCategory = bundle.getInt("idSubCategory", -1);
        }

        view = inflater.inflate(R.layout.fragment_show_sub_category, container, false);

        skip = 0;

        initComponent();

        return view;
    }


    private void initComponent() {

        prgLazyLoadingAdvertisements = (ProgressBar)view.findViewById(R.id.prgLazyLoadingAdvertisements);
        prgLazyLoadingAdvertisementsfooter = (ProgressBar)view.findViewById(R.id.prgLazyLoadingAdvertisementsfooter);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_advertisement);
        errorConnectToInternet = (TextView)view.findViewById(R.id.errorConnectToInternet);
        backSubCategory = view.findViewById(R.id.back_sub_category);
        titleItemSubCategory = (TextView)view.findViewById(R.id.title_item_sub_category);

        titleItemSubCategory.setText(titleSubCategory);

        backSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

                MainActivity.fragmentSub = MainActivity.fragmentManager.findFragmentByTag("sub");
                MainActivity.ft = MainActivity.fragmentManager.beginTransaction();
                MainActivity.ft.show(MainActivity.fragmentSub);
                MainActivity.ft.commit();
            }
        });

        setupRecyclerView(recyclerView);

        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prgLazyLoadingAdvertisements.setVisibility(View.VISIBLE);
                adapterItemAdvertisementRecyclerView.clear();

                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    skip = 0;
                    getAdvertismentSubCategory(idSubCategory ,9, 0);
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
            getAdvertismentSubCategory(idSubCategory , 9, 0);
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
                            getAdvertismentSubCategory(idSubCategory , offset , skip);
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

    private void getAdvertismentSubCategory(final int idSubCategory , final int skip,final int offset){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getAdvertisementSubCategory(idSubCategory ,offset,skip).enqueue(new Callback<List<Advertisement>>() {
            @Override
            public void onResponse(Call<List<Advertisement>> call, Response<List<Advertisement>> response) {
                listitemes = new ArrayList<>();
                if (response.isSuccessful()) {
                    hideSoftKeyboard();
                    for (Advertisement advertisement: response.body()) {
                        prgLazyLoadingAdvertisements.setVisibility(View.GONE);

                        prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                        listitemes.add(advertisement);

                    }
                    if (listitemes.size() == 0) {
                        prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                        prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "مورد بیشتری یافت نشد!", Toast.LENGTH_LONG).show();
                    } else {
                        adapterItemAdvertisementRecyclerView.addAll(listitemes);
                        isLoading = false;
                    }
                }else{
                    prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Advertisement>> call, Throwable t) {
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideSoftKeyboard()
    {

        //    InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        //    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

}

