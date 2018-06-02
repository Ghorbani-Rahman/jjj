package com.delaroystodios.metakar.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterSubsetUser;
import com.delaroystodios.metakar.Model.SubsetUser;
import com.delaroystodios.metakar.Model.SubsetUserModel;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSubsetUserActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView backSubset;
    private TextView linkSubset , shareLinkSubsetUserTelegram;
    private static ArrayList<SubsetUserModel> listSubsetUser;
    private AdapterSubsetUser adapterSubsetUser;
    private RecyclerView recyclerView;
    private String accessToken;
    private TextView noSubsetUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_subset_show);



    }

    @Override
    protected void onResume() {
        super.onResume();
        initComponent();
    }

    private void initComponent() {

        backSubset = findViewById(R.id.back_subset);
        shareLinkSubsetUserTelegram = findViewById(R.id.share_link_subset_user_telegram);
        recyclerView = findViewById(R.id.recyclerView_subset);
        linkSubset = findViewById(R.id.link_subset);
        noSubsetUser = findViewById(R.id.no_subset_user);

        noSubsetUser.setVisibility(View.GONE);

        setupRecyclerView(recyclerView);


        backSubset.setOnClickListener(this);
        shareLinkSubsetUserTelegram.setOnClickListener(this);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        if (Utilities.checkNetworkConnection(ShowSubsetUserActivity.this)) {

            if(getAccessToken())
            {
                getSubsetUser(accessToken);
            }
        } else {
            Toast.makeText(ShowSubsetUserActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        listSubsetUser = new ArrayList<>();
        adapterSubsetUser = new AdapterSubsetUser(ShowSubsetUserActivity.this, listSubsetUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterSubsetUser);


        adapterSubsetUser.notifyDataSetChanged();

    }

    private void getSubsetUser(String accessToken){
        noSubsetUser.setVisibility(View.GONE);
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getSubsetUser(accessToken).enqueue(new Callback<SubsetUser>() {
            @Override
            public void onResponse(Call<SubsetUser> call, Response<SubsetUser> response) {
                if (response.isSuccessful()) {
                    listSubsetUser = new ArrayList<>();

                   SubsetUser subsetUser = response.body();

                   linkSubset.setText(subsetUser.getSubset_link());

                   for(SubsetUserModel subsetUserModel: subsetUser.getSubset())
                   {

                       listSubsetUser.add(subsetUserModel);
                   }

                    if (listSubsetUser.size() == 0)
                    {
                        noSubsetUser.setVisibility(View.VISIBLE);
                        noSubsetUser.setText("در حال حاضر شما زیر مجموعه ای ندارید!");
                    }
                    else {
                        if (listSubsetUser.size() > 9)
                        {
                            adapterSubsetUser.addAll(listSubsetUser.subList(0, 10));
                        }
                        else
                        {
                            adapterSubsetUser.addAll(listSubsetUser);
                        }
                    }

                }else{

                    Toast.makeText(ShowSubsetUserActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SubsetUser> call, Throwable t) {
                Toast.makeText(ShowSubsetUserActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_subset:
                finish();
                break;
            case R.id.share_link_subset_user_telegram:
                String text = "https://telegram.me/share/url?url="+linkSubset.getText()+"&text=.سلام همین حالا با لینک بالا تو سایت متاکار عضو شو و کسب درآمد کن";
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(text)));
                break;
        }
    }
}
