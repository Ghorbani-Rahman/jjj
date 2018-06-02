package com.delaroystodios.metakar.Activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterShowWallet;
import com.delaroystodios.metakar.Model.ShowListWallet;
import com.delaroystodios.metakar.Model.ShowWallet;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;
import com.delaroystodios.metakar.utils.PaginationScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowWalletActivity extends AppCompatActivity{


    private ImageView backShowWallet;
    private static ArrayList<ShowWallet> listShowWallet;
    private AdapterShowWallet adapterShowWallet;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private String accessToken;
    private int offset = 6;
    private int skip = 0;
    private int TOTAL_PAGES = 400;
    private Boolean isLastPage = false;
    private Boolean isLoading = false;
    private TextView totalAmount;
    private ProgressBar prgLazyLoadingAdvertisementsfooter;
    private TextView errorConnectToInternet;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wallet);

        initComponent();

    }

    private void initComponent() {

        backShowWallet = findViewById(R.id.back_show_wallet);
        recyclerView = findViewById(R.id.recyclerView_show_wallet);
        totalAmount = findViewById(R.id.total_amount);
        prgLazyLoadingAdvertisementsfooter = findViewById(R.id.prgLazyLoadingAdvertisementsfooter);
        errorConnectToInternet = findViewById(R.id.errorConnectToInternet);


        backShowWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupRecyclerView(recyclerView);


        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);
                adapterShowWallet.clear();
                totalAmount.setText("");

                if (Utilities.checkNetworkConnection(ShowWalletActivity.this))
                {
                    skip = 0;
                    getWallet(accessToken , 12, 0);
                }else{
                    Toast.makeText(ShowWalletActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
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

        if (Utilities.checkNetworkConnection(ShowWalletActivity.this)) {

            if(getAccessToken())
            {
                getWallet(accessToken , 12 , 0);
            }
        } else {
            Toast.makeText(ShowWalletActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        listShowWallet = new ArrayList<>();
        adapterShowWallet = new AdapterShowWallet(ShowWalletActivity.this, listShowWallet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterShowWallet);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , ShowWalletActivity.this) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection((ShowWalletActivity.this)))
                {
                    isLoading = true;
                    errorConnectToInternet.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);


                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;
                            getWallet(accessToken , offset , skip );
                        }

                    }, 1000);
                }else
                {
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

        adapterShowWallet.notifyDataSetChanged();

    }

    private void getWallet(String accessToken , final int offset , final int skip){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getShowListWallet(accessToken , offset , skip).enqueue(new Callback<ShowListWallet>() {
            @Override
            public void onResponse(Call<ShowListWallet> call, Response<ShowListWallet> response) {
                if (response.isSuccessful()) {
                    listShowWallet = new ArrayList<>();
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                    totalAmount.setText("موجودی شما : " + response.body().getBalance() + " تومان ");

                    for(ShowWallet showWallet: response.body().getWallet())
                    {
                        listShowWallet.add(showWallet);
                    }

                    if (listShowWallet.size() == 0 && skip == 0)
                    {
                        Toast.makeText(ShowWalletActivity.this, "لیست کیف پول شما خالی است!", Toast.LENGTH_LONG).show();
                    }
                    else if(listShowWallet.size() == 0 && skip != 0)
                    {
                        Toast.makeText(ShowWalletActivity.this, "مورد دیگری وجود ندارد!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        adapterShowWallet.addAll(listShowWallet);
                        isLoading = false;
                    }
                }
                else
                {
                    Toast.makeText(ShowWalletActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowListWallet> call, Throwable t) {
                prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                Toast.makeText(ShowWalletActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean getAccessToken() {
        SharedPreferences preferences = getSharedPreferences("userLogin", MODE_PRIVATE);

        accessToken = "Bearer " + preferences.getString("accessToken", "");

        if(accessToken.equals(""))
        {
            return false;
        }

        return true;
    }

}

