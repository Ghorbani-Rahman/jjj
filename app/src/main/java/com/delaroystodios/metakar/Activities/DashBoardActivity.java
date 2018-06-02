package com.delaroystodios.metakar.Activities;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterItemAdvertisementRecyclerView;
import com.delaroystodios.metakar.Model.Advertisement;
import com.delaroystodios.metakar.Model.DashboardParent;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;
import com.delaroystodios.metakar.utils.PaginationScrollListener;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity
{
    private ProgressBar prgLazyLoadingAdvertisements , prgLazyLoadingAdvertisementsfooter;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Advertisement> listitemes;
    private TextView errorConnectToInternet , amount , name;
    private String nameUser , accessToken;
    private int  offset = 9 , skip = 0 , TOTAL_PAGES = 50;
    private Boolean isLastPage = false ,  isLoading = false;
    private AdapterItemAdvertisementRecyclerView adapterItemAdvertisementRecyclerView;
    private RecyclerView recyclerView;
    private ImageView backDashboard;;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();

        hideSoftKeyboard();

    }

    private void initComponent() {

        prgLazyLoadingAdvertisements = findViewById(R.id.prgLazyLoadingAdvertisements);
        prgLazyLoadingAdvertisementsfooter = findViewById(R.id.prgLazyLoadingAdvertisementsfooter);
        recyclerView = findViewById(R.id.recyclerView_advertisement);
        errorConnectToInternet = findViewById(R.id.errorConnectToInternet);
        backDashboard = findViewById(R.id.back_dashboard);
        name = findViewById(R.id.name);
        amount = findViewById(R.id.amount);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameUser = extras.getString("name");
        }




        prgLazyLoadingAdvertisements.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        prgLazyLoadingAdvertisementsfooter.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        setupRecyclerView(recyclerView);

        backDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                prgLazyLoadingAdvertisements.setVisibility(View.VISIBLE);
                adapterItemAdvertisementRecyclerView.clear();

                if (Utilities.checkNetworkConnection(DashBoardActivity.this))
                {
                    skip = 0;
                    if(getAccessToken())
                    {
                        getDashBoard(accessToken ,9, 0);
                    }
                }else{
                    Toast.makeText(DashBoardActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
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

        if (Utilities.checkNetworkConnection(DashBoardActivity.this))
        {
            if(getAccessToken())
            {
                getDashBoard(accessToken , 9, 0);
            }
        } else {
            Toast.makeText(DashBoardActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }


        listitemes = new ArrayList<>();
        adapterItemAdvertisementRecyclerView = new AdapterItemAdvertisementRecyclerView(DashBoardActivity.this, listitemes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterItemAdvertisementRecyclerView);
        adapterItemAdvertisementRecyclerView.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , DashBoardActivity.this) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection((DashBoardActivity.this)))
                {
                    isLoading = true;

                    errorConnectToInternet.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;
                            if(getAccessToken())
                            {
                                getDashBoard(accessToken , offset, skip);
                            }
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

    private void getDashBoard(String accessToken , final int offset,final int skip){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getDashBoard(accessToken , offset,skip).enqueue(new Callback<DashboardParent>() {
            @Override
            public void onResponse(Call<DashboardParent> call, Response<DashboardParent> response) {
                listitemes = new ArrayList<>();
                if (response.isSuccessful()) {

                    prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);

                    name.setText(nameUser);
                    amount.setText("موجودی شما : " + response.body().getBalance() + " تومان");

                    for (Advertisement advertisement: response.body().getAdvertisements())
                    {
                        listitemes.add(advertisement);
                    }

                    if (listitemes.size() == 0 && skip == 0) {
                        Toast.makeText(DashBoardActivity.this, "لیست آگهی های دیده نشده خالی هست!" , Toast.LENGTH_LONG).show();
                    }else if(listitemes.size() == 0 && skip > 0)
                    {
                        Toast.makeText(DashBoardActivity.this, "مورد بیشتری یافت نشد!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        adapterItemAdvertisementRecyclerView.addAll(listitemes);
                        isLoading = false;
                    }
                }else{
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                    Toast.makeText(DashBoardActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DashboardParent> call, Throwable t) {
                prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                Toast.makeText(DashBoardActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  DashBoardActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if(inputMethodManager != null && DashBoardActivity.this.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(DashBoardActivity.this.getCurrentFocus().getWindowToken(), 0);
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
