package com.delaroystodios.metakar.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Model.ShowProfile;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.helper.Roozh;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class ShowProfileActivity extends AppCompatActivity {

        private Button btnGoToEditProfile;
        private ArrayList<ShowProfile> listitemes;
        private ImageView backPanel;
        private ProgressBar prgLazyLoadingAdvertisements;
        private TextView nameFamily , email , phoneNumber , city , create , lastUpdate , cartNumber;
        private String accessToken;
        private ShowProfile showProfile;
        private LinearLayout show_profile_activity;
        private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

    }

        @Override
        protected void onResume() {
            super.onResume();
            initComponent();
        }

        private void initComponent() {

            btnGoToEditProfile = findViewById(R.id.GoToEditProfile);
            backPanel = findViewById(R.id.back_profile);
            nameFamily = findViewById(R.id.nameFamily);
            email = findViewById(R.id.email);
            phoneNumber = findViewById(R.id.phoneNumber);
            create = findViewById(R.id.create);
            city = findViewById(R.id.city);
            lastUpdate = findViewById(R.id.lastUpdate);
            cartNumber = findViewById(R.id.cart_number);
            show_profile_activity = findViewById(R.id.show_profile_activity);
            prgLazyLoadingAdvertisements = findViewById(R.id.prgLazyLoadingAdvertisements);


            if (Utilities.checkNetworkConnection(ShowProfileActivity.this)) {

                if(getAccessToken())
                {
                    getShowProfile(accessToken);
                }

            } else {
                Toast.makeText(ShowProfileActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
            }

            backPanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            btnGoToEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Utilities.checkNetworkConnection(ShowProfileActivity.this)) {
                        Intent intent = new Intent(ShowProfileActivity.this, EditProfileActivity.class);

                        intent.putExtra("name", showProfile.getName());
                        intent.putExtra("family", showProfile.getFamily());
                        intent.putExtra("email", email.getText().toString());
                        intent.putExtra("phone", showProfile.getMetakar_user_personal_information().getMobile());
                        intent.putExtra("rulesConfirmed", showProfile.getMetakar_user_personal_information().getRules_confirmed());

                        if(showProfile.getMetakar_user_personal_information().getCity() != null) {
                            String[] separeteCityProvinces = showProfile.getMetakar_user_personal_information().getCity().split(" , ");

                            intent.putExtra("province", separeteCityProvinces[0]);
                            intent.putExtra("city", separeteCityProvinces[1]);

                        }else
                        {
                            intent.putExtra("province", "");
                            intent.putExtra("city", "");
                        }

                        if(showProfile.getMetakar_user_personal_information().getCart_number()!= null) {
                            String cartNumber = showProfile.getMetakar_user_personal_information().getCart_number();
                            cartNumber = cartNumber.replace("-", "");
                            intent.putExtra("cartNumber", cartNumber);
                        }else
                        {
                            intent.putExtra("cartNumber", "");
                        }
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(ShowProfileActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            swipeContainer = findViewById(R.id.swipeContainer);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    prgLazyLoadingAdvertisements.setVisibility(View.VISIBLE);

                    if (Utilities.checkNetworkConnection(ShowProfileActivity.this))
                    {
                        if(getAccessToken())
                        {
                            getShowProfile(accessToken);
                        }

                    }else{
                        Toast.makeText(ShowProfileActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }

                    swipeContainer.setRefreshing(false);
                }

            });


            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
    }

    private void getShowProfile(String accessToken){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getShowProfile(accessToken).enqueue(new Callback<ShowProfile>() {

            @Override
            public void onResponse(Call<ShowProfile> call, Response<ShowProfile> response) {
                listitemes = new ArrayList<>();
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    showProfile = response.body();


                    nameFamily.setText(showProfile.getName() + " " + showProfile.getFamily());

                    email.setText(showProfile.getEmail());

                   if(showProfile.getMetakar_user_personal_information().getMobile() != null) {
                      phoneNumber.setText(new ConvertToPersianNumber(
                               showProfile.getMetakar_user_personal_information().getMobile()).convertToPersian());
                   }else
                   {
                       phoneNumber.setText("");
                   }

                    city.setText(showProfile.getMetakar_user_personal_information().getCity());


                    Roozh jCalCreate = new Roozh();
                    String created_at = showProfile.getCreated_at();
                    int c_year = Integer.parseInt(created_at.substring(0 , 4));
                    int c_Month = Integer.parseInt(created_at.substring(6 , 7));
                    int c_day = Integer.parseInt(created_at.substring(9 , 10));
                    jCalCreate.GregorianToPersian(c_year, c_Month, c_day);
                    ConvertToPersianNumber conCreateToPersion = new ConvertToPersianNumber(jCalCreate.toString());
                    create.setText(conCreateToPersion.convertToPersian());


                    Roozh jCalupdate = new Roozh();
                    String update_at = showProfile.getUpdated_at();
                    int u_year = Integer.parseInt(update_at.substring(0 , 4));
                    int u_Month = Integer.parseInt(update_at.substring(6 , 7));
                    int u_day = Integer.parseInt(update_at.substring(9 , 10));
                    jCalupdate.GregorianToPersian(u_year, u_Month, u_day);
                    ConvertToPersianNumber updateDateProfile = new ConvertToPersianNumber(jCalupdate.toString());
                    lastUpdate.setText(updateDateProfile.convertToPersian());


                    if(showProfile.getMetakar_user_personal_information().getCart_number() != null)
                    {
                        cartNumber.setText(new ConvertToPersianNumber(
                                showProfile.getMetakar_user_personal_information().getCart_number()).convertToPersian());
                    }else {
                        cartNumber.setText("");
                    }
                }
                else
                {
                    Toast.makeText(ShowProfileActivity.this, "مشکل در خواندن مشخصات کاربری", Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onFailure(Call<ShowProfile> call, Throwable t) {
                prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                Toast.makeText(ShowProfileActivity.this, "خطا در دریافت اطلاعات لطفا دوباره امتحان کنید!", Toast.LENGTH_SHORT).show();
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
