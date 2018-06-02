package com.delaroystodios.metakar.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterListEarn;
import com.delaroystodios.metakar.Model.ListEarn;
import com.delaroystodios.metakar.Model.ListEarnParent;
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


public class ListEarnsActivity extends AppCompatActivity
{

    private ImageView backListEarns;
    private static ArrayList<ListEarn> listShowEarns;
    private AdapterListEarn adapterListEarn;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private String accessToken;
    private int offset = 9;
    private int skip = 0;
    private int TOTAL_PAGES = 50;
    private Boolean isLastPage = false;
    private Boolean isLoading = false;
    private TextView amount , inventory , errorConnectToInternet;
    private ProgressBar prgLazyLoadingAdvertisementsfooter;
    private ProgressBar prgLazyLoadingAdvertisements;
    private EditText valueGettable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_earns);

        initComponent();

    }

    private void initComponent() {

        backListEarns = findViewById(R.id.back_list_earns);
        recyclerView = findViewById(R.id.recyclerView_show_list_earns);
        amount = findViewById(R.id.amount);
        inventory = findViewById(R.id.inventory);
        prgLazyLoadingAdvertisementsfooter = findViewById(R.id.prgLazyLoadingAdvertisementsfooter);
        errorConnectToInternet = findViewById(R.id.errorConnectToInternet);
        prgLazyLoadingAdvertisements = findViewById(R.id.prgLazyLoadingAdvertisements);
        valueGettable = findViewById(R.id.value_gettable);

        prgLazyLoadingAdvertisements.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        prgLazyLoadingAdvertisementsfooter.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);


        backListEarns.setOnClickListener(new View.OnClickListener() {
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
                prgLazyLoadingAdvertisements.setVisibility(View.VISIBLE);
                adapterListEarn.clear();
                valueGettable.setText("");

                if (Utilities.checkNetworkConnection(ListEarnsActivity.this))
                {
                    skip = 0;
                    getListEarns( accessToken,12, 0);
                }else{
                    Toast.makeText(ListEarnsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
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

        if (Utilities.checkNetworkConnection(ListEarnsActivity.this)) {

            if(getAccessToken())
            {
                getListEarns(accessToken , 0 , 9);
            }
        } else {
            Toast.makeText(ListEarnsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }


        listShowEarns = new ArrayList<>();
        adapterListEarn = new AdapterListEarn(ListEarnsActivity.this, listShowEarns);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterListEarn);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , ListEarnsActivity.this) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection((ListEarnsActivity.this)))
                {
                    isLoading = true;
                    errorConnectToInternet.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;
                            getListEarns(accessToken , skip , offset );
                        }

                    }, 300);
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

        adapterListEarn.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , ListEarnsActivity.this) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection((ListEarnsActivity.this)))
                {

                    isLoading = true;

                    errorConnectToInternet.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;
                            getListEarns(accessToken,offset , skip);
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

    private void getListEarns(String accessToken , final int skip , int offset){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getListEarns(accessToken , skip , offset).enqueue(new Callback<ListEarnParent>() {
            @Override
            public void onResponse(Call<ListEarnParent> call, Response<ListEarnParent> response) {
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    listShowEarns = new ArrayList<>();

                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);

                    amount.setText("موجودی شما : " + (response.body().getInventory()) + " تومان ");
                    int am = Integer.parseInt(response.body().getInventory())-10000;

                    if (am <= 0)
                    {
                        inventory.setText("مبلغ قابل برداشت :  0 تومان");
                    }
                    else
                    {
                        inventory.setText("مبلغ قابل برداشت : " + am + " تومان ");
                    }

                    for(ListEarn listEarn : response.body().getWithdraws())
                    {
                        listShowEarns.add(listEarn);

                    }

                    if (listShowEarns.size() == 0 && skip == 0)
                    {
                        Toast.makeText(ListEarnsActivity.this, "لیست برداشت ها خالی هست!", Toast.LENGTH_LONG).show();
                    }
                    else if(listShowEarns.size() == 0 && skip != 0)
                    {
                        Toast.makeText(ListEarnsActivity.this, "مورد دیگری وجود ندارد!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        adapterListEarn.addAll(listShowEarns);
                        isLoading = false;
                    }
                }
                else
                {
                    Toast.makeText(ListEarnsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListEarnParent> call, Throwable t) {
                prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                Toast.makeText(ListEarnsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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
