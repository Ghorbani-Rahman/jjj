package com.delaroystodios.metakar.Activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterMyShowAdvertisement;
import com.delaroystodios.metakar.Model.MyAdvertisementModel;
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

public class MyAdvertisementActivity extends AppCompatActivity
{

    private ImageView backShowMyAdvertisement;
    private static ArrayList<MyAdvertisementModel> listShowMyAdvertisement;
    private AdapterMyShowAdvertisement adapterMyShowAdvertisement;
    private RecyclerView recyclerView;
    private String accessToken;
    private int offset = 9;
    private int skip = 0;
    private int TOTAL_PAGES = 500;
    private Boolean isLastPage = false;
    private Boolean isLoading = false;
    private ProgressBar prgLazyLoadingAdvertisementsfooter;
    private TextView errorConnectToInternet;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_advertisment);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
    }

    private void initComponent() {

        backShowMyAdvertisement = findViewById(R.id.back_show_my_advertisement);
        recyclerView = findViewById(R.id.recyclerView_show_wallet);
        prgLazyLoadingAdvertisementsfooter = findViewById(R.id.prgLazyLoadingAdvertisementsfooter);
        errorConnectToInternet = findViewById(R.id.errorConnectToInternet);


        backShowMyAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        setupRecyclerView(recyclerView);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        if (Utilities.checkNetworkConnection(MyAdvertisementActivity.this)) {

            if(getAccessToken())
            {
                getMyAdvertisement(accessToken , 0 , 9);
            }
        } else {
            Toast.makeText(MyAdvertisementActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }


        listShowMyAdvertisement = new ArrayList<>();
        adapterMyShowAdvertisement = new AdapterMyShowAdvertisement(MyAdvertisementActivity.this, listShowMyAdvertisement);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterMyShowAdvertisement);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , MyAdvertisementActivity.this) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection((MyAdvertisementActivity.this)))
                {
                    isLoading = true;
                    errorConnectToInternet.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;
                            getMyAdvertisement(accessToken , skip , offset );
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


        adapterMyShowAdvertisement.notifyDataSetChanged();

    }

    private void getMyAdvertisement(String accessToken , final int skip , int offset){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getListMyAdvertisements(accessToken , offset , skip).enqueue(new Callback<List<MyAdvertisementModel>>() {
            @Override
            public void onResponse(Call<List<MyAdvertisementModel>> call, Response<List<MyAdvertisementModel>> response) {
                if (response.isSuccessful()) {
                    listShowMyAdvertisement = new ArrayList<>();

                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);

                    listShowMyAdvertisement.addAll(response.body());

                    if (listShowMyAdvertisement.size() == 0 && skip == 0)
                    {
                        Toast.makeText(MyAdvertisementActivity.this, "لیست آگهی های شما خالی هست!", Toast.LENGTH_LONG).show();
                    }
                    else if(listShowMyAdvertisement.size() == 0 && skip != 0)
                    {
                        Toast.makeText(MyAdvertisementActivity.this, "مورد دیگری وجود ندارد!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        adapterMyShowAdvertisement.addAll(listShowMyAdvertisement);
                        isLoading = false;
                    }
                }
                else
                {
                    Toast.makeText(MyAdvertisementActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MyAdvertisementModel>> call, Throwable t) {
                prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                Toast.makeText(MyAdvertisementActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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
