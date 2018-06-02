package com.delaroystodios.metakar.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterNotifications;
import com.delaroystodios.metakar.Model.Notifications;
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

public class NotificationsActivity extends AppCompatActivity {

    private ImageView backNotifications;
    private ArrayList<Notifications> listShowNotifications;
    private AdapterNotifications adapterShowNotifications;
    private RecyclerView recyclerView;
    private String accessToken;
    private int offset = 9;
    private int skip = 0;
    private int TOTAL_PAGES = 50;
    private Boolean isLastPage = false;
    private Boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);



    }

    private void initComponent() {

        backNotifications = findViewById(R.id.back_notifications);
        recyclerView = findViewById(R.id.recyclerView_show_notification);


        backNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupRecyclerView(recyclerView);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {


        listShowNotifications = new ArrayList<>();

        if (Utilities.checkNetworkConnection(NotificationsActivity.this)) {

            if(getAccessToken())
            {
                getNotification(accessToken , 0 , 9);
            }
        } else {
            Toast.makeText(NotificationsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        adapterShowNotifications = new AdapterNotifications(NotificationsActivity.this, listShowNotifications);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

     /*   adapterShowNotifications.setOnClickItemRecyclerListener(new DeleteNotification() {

            @Override
            public void onActionClicked(String position)
            {
                deleteMessage(position);
            }
        });*/


        recyclerView.setAdapter(adapterShowNotifications);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , NotificationsActivity.this) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection((NotificationsActivity.this)))
                {
                    isLoading = true;

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;
                            getNotification(accessToken , offset , skip);
                        }

                    }, 300);
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

        adapterShowNotifications.notifyDataSetChanged();

    }

    private void getNotification(String accessToken , int skip , int offset){
        ApiServices mAPIService = ApiUtils.getAPIService();


        mAPIService.getNotifications(accessToken , skip , offset).enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Call<List<Notifications>> call, Response<List<Notifications>> response) {

                listShowNotifications = new ArrayList<>();

                if (response.isSuccessful())
                {

                    listShowNotifications.addAll(response.body());

                    if (listShowNotifications.size() == 0)
                    {
                        Toast.makeText(NotificationsActivity.this, "لست پیام های دریافتی شما خالی هست!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        adapterShowNotifications.addAll(listShowNotifications);
                        isLoading = false;
                    }

                }else{

                    Toast.makeText(NotificationsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Notifications>> call, Throwable t) {
                Toast.makeText(NotificationsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
    }
}


