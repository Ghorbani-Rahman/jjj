package com.delaroystodios.metakar.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.delaroystodios.metakar.Adapter.AdapterEarnings;
import com.delaroystodios.metakar.Model.Earnings;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsActivity extends AppCompatActivity {

    private ImageView backShowٍEarnings;
    private static ArrayList<Earnings> listEarnings;
    private AdapterEarnings adapterEarnings;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);

        initComponent();

    }

    private void initComponent()
    {

        backShowٍEarnings = findViewById(R.id.back_show_earnings);
        recyclerView = findViewById(R.id.recyclerView_show_earnings);

        backShowٍEarnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupRecyclerView(recyclerView);

    }

    private void setupRecyclerView(RecyclerView recyclerView) {

        if (Utilities.checkNetworkConnection(EarningsActivity.this)) {
            getWithdraws();
        } else {
            Toast.makeText(EarningsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        listEarnings = new ArrayList<>();
        adapterEarnings = new AdapterEarnings(listEarnings);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterEarnings);


        adapterEarnings.notifyDataSetChanged();

    }

    private void getWithdraws()
    {

        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getEarnings().enqueue(new Callback<List<Earnings>>() {
            @Override
            public void onResponse(Call<List<Earnings>> call, Response<List<Earnings>> response) {
                if (response.isSuccessful()) {
                    listEarnings = new ArrayList<>();

                    for(Earnings earnings: response.body())
                    {
                        listEarnings.add(earnings);
                    }

                    if (listEarnings.size() == 0)
                    {
                        Toast.makeText(EarningsActivity.this, "در حال حاضر لیست درآمد ها خالی می باشد", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        adapterEarnings.addAll(listEarnings);
                    }

                }else{

                    Toast.makeText(EarningsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Earnings>> call, Throwable t) {
                Toast.makeText(EarningsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
