package com.delaroystodios.metakar.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.delaroystodios.metakar.Adapter.AdapterWithdraws;
import com.delaroystodios.metakar.Model.Withdraws;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawsActivity extends AppCompatActivity {

    private ImageView backShowWithdraws;
    private static ArrayList<Withdraws> listWithdraws;
    private AdapterWithdraws adapterWithdraws;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraws);

        initComponent();

    }

    private void initComponent()
    {

        backShowWithdraws = findViewById(R.id.back_show_withdraws);
        recyclerView = findViewById(R.id.recyclerView_show_withdraws);

        backShowWithdraws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupRecyclerView(recyclerView);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        if (Utilities.checkNetworkConnection(WithdrawsActivity.this)) {
                getWithdraws();
        } else {
            Toast.makeText(WithdrawsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        listWithdraws = new ArrayList<>();
        adapterWithdraws = new AdapterWithdraws(listWithdraws);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterWithdraws);


        adapterWithdraws.notifyDataSetChanged();

    }

    private void getWithdraws()
    {

        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getWithdraws().enqueue(new Callback<List<Withdraws>>() {
            @Override
            public void onResponse(Call<List<Withdraws>> call, Response<List<Withdraws>> response) {
                if (response.isSuccessful()) {
                    listWithdraws = new ArrayList<>();


                    for(Withdraws withdraws1: response.body())
                    {
                        listWithdraws.add(withdraws1);
                    }

                    if (listWithdraws.size() == 0)
                    {
                        Toast.makeText(WithdrawsActivity.this, "در حال حاضر لیست پرداختی ها خالی می باشد", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        adapterWithdraws.addAll(listWithdraws);
                    }

                }else{

                    Toast.makeText(WithdrawsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Withdraws>> call, Throwable t) {
                Toast.makeText(WithdrawsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
