package com.delaroystodios.metakar.Activities;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.delaroystodios.metakar.Adapter.SlidingImage_Adapter;
import com.delaroystodios.metakar.Model.AdvertisementDetails;
import com.delaroystodios.metakar.Model.DetailsAdvertismentModel;
import com.delaroystodios.metakar.Model.ImageModel;
import com.delaroystodios.metakar.Model.SendMessage;
import com.delaroystodios.metakar.Model.SendSeendAdvertisement;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvertisementDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static ViewPager mPager;
    private LinearLayout appbar;
    private CardView bottomVisitBody;
    private VideoView vidView;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int progressBar;
    private boolean re;
    private ImageView backDetail;
    private ImageView backDetailTizer;
    private RelativeLayout mapSection;
    private String Advertisement_ID;
    private String idShowAdvertisement;
    private String titleShowAdvertisement;
    private String descriptionShowAdvertisement;
    private String imageShowAdvertisement;
    private String Advertisement_Type;
    private WebView view;
    private FloatingActionMenu socialFloatingMenu;
    private SupportMapFragment mapFragment;
    private TextView description;
    private GoogleMap mMap;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<ImageModel> imageModelArrayList;
    private ProgressBar progressBarDuration;
    private int durationAdvertisement;
    public ArrayList<String> imagesList;
    private String accessToken;
    private TextView title;
    private TextView numberPhone1;
    private TextView numberPhone2;
    private TextView email;
    private TextView instagram;
    private TextView telegram;
    private TextView address;
    private TextView all_visits_count;
    private TextView visited;
    private TextView duration;
    private TextView remaning_visit , titleTypeAdvertisement;
    private EditText nameSender;
    private EditText emailSender;
    private EditText messageSender;
    private Button btnSendMessage;
    private CardView visitSocial , infoContact , descriptionSection , mapAdvertisement;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingFacebook, floatingTwitter, floatingLinkedIn, floatingTelegram, floatingGooglePlus, floatingPinterest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Advertisement_ID = extras.getString("Advertisement_ID");
            Advertisement_Type = extras.getString("Advertisement_Type");
        }

        if (Advertisement_Type.equals("visit"))
        {
            setContentView(R.layout.activity_advertisement_webview);
            initComponentWebView();
        } else if(Advertisement_Type.equals("text") || Advertisement_Type.equals("social"))
        {
            setContentView(R.layout.activity_advertisement_details);
            titleTypeAdvertisement = findViewById(R.id.title_type_advertisement);
            if(Advertisement_Type.equals("text")) {
                titleTypeAdvertisement.setText("آگهی متنی");
            }else
            {
                titleTypeAdvertisement.setText("آگهی شبکه اجتماعی");
            }
            initComponent();
        }else if(Advertisement_Type.equals("tizer"))
        {
            setContentView(R.layout.activity_advertisement_tizer);
            initComponentTizer();
        }
    }

    private void initComponentTizer()
    {
        backDetailTizer = findViewById(R.id.back_detail_tizer);

        remaning_visit =  findViewById(R.id.remaning_visit);
        visited =  findViewById(R.id.visited);
        all_visits_count =  findViewById(R.id.all_visits_count);
        duration =  findViewById(R.id.duration);
        vidView = findViewById(R.id.myVideo);
        appbar = findViewById(R.id.appbar);
        progressBarDuration =  findViewById(R.id.progressBarDuration);
        bottomVisitBody =  findViewById(R.id.bottom_visit_body);
        socialFloatingMenu = findViewById(R.id.social_floating_menu);


        backDetailTizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Utilities.checkNetworkConnection(AdvertisementDetailsActivity.this)) {
            getDataTizer(Advertisement_ID);
        } else {
            Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponentWebView() {
        remaning_visit =  findViewById(R.id.remaning_visit);
        backDetail = findViewById(R.id.back_detail);
        visited =  findViewById(R.id.visited);
        all_visits_count =  findViewById(R.id.all_visits_count);
        duration =  findViewById(R.id.duration);
        descriptionSection =  findViewById(R.id.descriptionSection);
        view =  findViewById(R.id.webView);
        progressBarDuration =  findViewById(R.id.progressBarDuration);



        backDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeContainer =  findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                progressBar = -10;
                progressBarDuration.setProgress(0);

                if (Utilities.checkNetworkConnection(getBaseContext())) {
                    getDataWebView(Advertisement_ID);
                } else {
                    Toast.makeText(getBaseContext(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }

                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        view.getSettings().setJavaScriptEnabled(true);

        view.setWebViewClient(new WebViewClient()
        {


            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null)
                {
                    progressDialog = new ProgressDialog(AdvertisementDetailsActivity.this);
                    progressDialog.show();
                    progressDialog.setMessage("در حال دریافت اطلاعات....");
                }
            }

            public void onPageFinished(WebView view, String url)
            {
                progressDialog.dismiss();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("در حال دریافت اطلاعات....");

        if (Utilities.checkNetworkConnection(AdvertisementDetailsActivity.this)) {
            getDataWebView(Advertisement_ID);
        }
        else
        {
            progressDialog.dismiss();
            Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void initComponent() {

        visitSocial = findViewById(R.id.visit_social);
        infoContact = findViewById(R.id.info_contact);
        descriptionSection =  findViewById(R.id.descriptionSection);
        mapAdvertisement =  findViewById(R.id.map_advertisement);


        backDetail = findViewById(R.id.back_detail);
        mapSection =  findViewById(R.id.mapSection);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(AdvertisementDetailsActivity.this);
        title =  findViewById(R.id.title);
        description =  findViewById(R.id.description);
        numberPhone1 =  findViewById(R.id.numberPhone1);
        numberPhone2 =  findViewById(R.id.numberPhone2);
        email =  findViewById(R.id.email);
        instagram =  findViewById(R.id.instagram);
        telegram =  findViewById(R.id.telegram);
        address =  findViewById(R.id.address);
        progressBarDuration =  findViewById(R.id.progressBarDuration);
        remaning_visit =  findViewById(R.id.remaning_visit);
        visited =  findViewById(R.id.visited);
        all_visits_count =  findViewById(R.id.all_visits_count);
        duration =  findViewById(R.id.duration);
        nameSender = findViewById(R.id.name);
        emailSender = findViewById(R.id.email_contact);
        messageSender = findViewById(R.id.message);
        btnSendMessage =  findViewById(R.id.btn_send_message);

        imagesList = new ArrayList<>();

        backDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Utilities.checkNetworkConnection(AdvertisementDetailsActivity.this)) {
            getData(Advertisement_ID);
        } else {
            Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        imageModelArrayList = new ArrayList<>();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = nameSender.getText().toString().trim();
                String e = emailSender.getText().toString().trim();
                String m = messageSender.getText().toString().trim();
                
                if(validateSendMessage(n , e , m))
                {
                    if(getAccessToken())
                    {
                        if (Utilities.checkNetworkConnection(AdvertisementDetailsActivity.this))
                        {
                            sendMessage(accessToken , idShowAdvertisement , n , e , m);

                        }
                        else
                            {
                            Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                            Toast.makeText(AdvertisementDetailsActivity.this, "آگهی برای شما ثبت نشد!!!", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                }
            }
        });
    }

    private void sendMessage(String accessToken, String s, String n, String e, String m) 
    {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.sendMessage(accessToken , s , n , e , m).enqueue(new Callback<SendMessage>() {
            @Override
            public void onResponse(Call<SendMessage> call, Response<SendMessage> response) {
                if (response.isSuccessful())
                {

                    SendMessage sendMessage = response.body();
                    
                    if(sendMessage.getResult().equals("ok"))
                    {
                        Toast.makeText(AdvertisementDetailsActivity.this, sendMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(AdvertisementDetailsActivity.this, sendMessage.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SendMessage> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateSendMessage(String n , String e , String m)
    {
        int errorCount = 0;
       


        if(n.length() == 0)
        {
            nameSender.setError("فیلد نام نمی تواند خالی باشد!");
            errorCount++;
        }

        if(!isEmailValid(e))
        {
            emailSender.setError("ایمیل وارد شده معتبر نیست!");
            errorCount++;
        }

        if(m.length() < 10)
        {
            messageSender.setError(new ConvertToPersianNumber("فیلد متن پیام نمی تواند کمتر از 10 حروف باشد!").convertToPersian());
            errorCount++;
        }


        if(errorCount == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void getDataWebView(String id) {
        ApiServices mAPIService = ApiUtils.getAPIService();

        if(checkLogin())
            getAccessToken();

            mAPIService.getAdvertisementDetails(accessToken , id).enqueue(new Callback<DetailsAdvertismentModel>() {
            @Override
            public void onResponse(Call<DetailsAdvertismentModel> call, Response<DetailsAdvertismentModel> response) {
                if (response.isSuccessful()) {


                    AdvertisementDetails advertisementDetails = response.body().getAdvertisement();


                    if(advertisementDetails.getSite() != null)
                        view.loadUrl(advertisementDetails.getSite());

                    remaning_visit.setText(new ConvertToPersianNumber(advertisementDetails.getRemaning_visit()).convertToPersian());

                    visited.setText(new ConvertToPersianNumber(advertisementDetails.getVisited()).convertToPersian());
                    all_visits_count.setText(new ConvertToPersianNumber(advertisementDetails.getAll_visits_count()).convertToPersian());



                    idShowAdvertisement = String.valueOf(advertisementDetails.getId());
                    titleShowAdvertisement = String.valueOf(advertisementDetails.getTitle());
                    descriptionShowAdvertisement = String.valueOf(advertisementDetails.getDescription());
                    imageShowAdvertisement = String.valueOf(advertisementDetails.getImage());

                    initFloatingSocial();

                    if(checkLogin())
                    {
                        if(!response.body().isSeen())
                        {

                            durationAdvertisement = advertisementDetails.getDuration_value_secound();
                            progressBarDuration.setVisibility(View.VISIBLE);

                            progressBarDuration.setMax(durationAdvertisement);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    for (int i = 0; i < durationAdvertisement + 1; i++) {
                                        try {
                                            Thread.sleep(1000);

                                            if (progressBar < -2)
                                            {
                                                progressBar = 0;
                                                return;
                                            }

                                            myHandler.sendEmptyMessage(0);



                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    }
                                }
                            }).start();

                        }else
                        {
                            progressBarDuration.setVisibility(View.GONE);
                            duration.setVisibility(View.VISIBLE);
                            duration.setText("بازدید شده");
                        }
                    }else
                    {
                        progressBarDuration.setVisibility(View.GONE);
                        duration.setVisibility(View.VISIBLE);
                        duration.setText(advertisementDetails.getDuration_farsi());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsAdvertismentModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendSeenAdvertisement(String accessToken , String idAdvertisement)
    {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.sendSeendAdvertisement(accessToken , idAdvertisement).enqueue(new Callback<SendSeendAdvertisement>() {
            @Override
            public void onResponse(Call<SendSeendAdvertisement> call, Response<SendSeendAdvertisement> response) {
                if (response.isSuccessful())
                {


                    SendSeendAdvertisement sendSeendAdvertisement = response.body();

                    if(sendSeendAdvertisement.getResult().equals("ok"))
                    {
                        Toast.makeText(AdvertisementDetailsActivity.this, sendSeendAdvertisement.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBarDuration.setVisibility(View.GONE);
                        duration.setVisibility(View.VISIBLE);
                        duration.setText("بازدید شد");
                    }else
                    {
                        Toast.makeText(AdvertisementDetailsActivity.this, sendSeendAdvertisement.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SendSeendAdvertisement> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkLogin() {
        SharedPreferences preferences = getSharedPreferences("userLogin", MODE_PRIVATE);
        boolean userName = preferences.getBoolean("login", false);

        return userName;
    }

    private void getData(String id) {
        if(checkLogin())
            getAccessToken();

        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getAdvertisementDetails(accessToken , id).enqueue(new Callback<DetailsAdvertismentModel>() {
            @Override
            public void onResponse(Call<DetailsAdvertismentModel> call, Response<DetailsAdvertismentModel> response) {
                if (response.isSuccessful())
                {

                    final AdvertisementDetails advertisementDetails1 = response.body().getAdvertisement();

                    if (advertisementDetails1.getType().equals("social"))
                    {
                        visitSocial.setVisibility(View.VISIBLE);

                        visitSocial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(advertisementDetails1.getSite())));
                            }
                        });
                    }
                    else if(advertisementDetails1.getType().equals("text")) {
                        infoContact.setVisibility(View.VISIBLE);
                        mapAdvertisement.setVisibility(View.VISIBLE);
                        descriptionSection.setVisibility(View.VISIBLE);

                        title.setText(advertisementDetails1.getTitle());

                        if (advertisementDetails1.getPhones() != null) {
                            if (advertisementDetails1.getPhones().size() > 1) {
                                numberPhone2.setVisibility(View.VISIBLE);
                                numberPhone1.setText("تلفن : " + new ConvertToPersianNumber(advertisementDetails1.getPhones().get(0)).convertToPersian());
                                numberPhone2.setVisibility(View.VISIBLE);
                                numberPhone2.setText("تلفن : " + new ConvertToPersianNumber(advertisementDetails1.getPhones().get(1)).convertToPersian());
                            } else {
                                numberPhone1.setText("تلفن : " + new ConvertToPersianNumber(advertisementDetails1.getPhones().get(0)).convertToPersian());
                            }
                        } else {
                            numberPhone1.setVisibility(View.GONE);
                        }

                        if (advertisementDetails1.getEmail() != null) {
                            email.setText("ایمیل : " + new ConvertToPersianNumber(advertisementDetails1.getEmail()).convertToPersian());
                        } else {
                            email.setVisibility(View.GONE);
                        }

                        if (advertisementDetails1.getInstagram() != null) {
                            instagram.setText("اینستاگرام : " + new ConvertToPersianNumber(advertisementDetails1.getInstagram()).convertToPersian());
                        } else {
                            instagram.setVisibility(View.GONE);
                        }

                        if (advertisementDetails1.getTelegram() != null) {
                            telegram.setText("تله گرام : " + new ConvertToPersianNumber(advertisementDetails1.getTelegram()).convertToPersian());
                        } else {
                            telegram.setVisibility(View.GONE);
                        }

                        if (advertisementDetails1.getAddress() != null) {
                            address.setText("آدرس : " + new ConvertToPersianNumber(advertisementDetails1.getAddress()).convertToPersian());
                        } else {
                            address.setVisibility(View.GONE);
                        }

                        if (advertisementDetails1.getDesription() != null) {
                            description.setText(advertisementDetails1.getDesription());
                        } else {
                            descriptionSection.setVisibility(View.GONE);
                        }

                        if (advertisementDetails1.getMap() != null) {
                            LatLng sydney = null;
                            sydney = new LatLng(advertisementDetails1.getMap().getX(), advertisementDetails1.getMap().getY());
                            mapFragment.getMapAsync(AdvertisementDetailsActivity.this);

                            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker"));
                            goToLocationZoom(sydney, 15);
                        } else {
                            mapSection.setVisibility(View.GONE);
                        }
                    }

                    remaning_visit.setText(new ConvertToPersianNumber(advertisementDetails1.getRemaning_visit().toString()).convertToPersian());
                    visited.setText(new ConvertToPersianNumber(advertisementDetails1.getVisited()).convertToPersian());
                    all_visits_count.setText(new ConvertToPersianNumber(advertisementDetails1.getAll_visits_count()).convertToPersian());


                    idShowAdvertisement = String.valueOf(advertisementDetails1.getId());
                    titleShowAdvertisement = String.valueOf(advertisementDetails1.getTitle());
                    descriptionShowAdvertisement = String.valueOf(advertisementDetails1.getDescription());
                    imageShowAdvertisement = String.valueOf(advertisementDetails1.getImage());

                    durationAdvertisement = advertisementDetails1.getDuration_value_secound();
                    progressBarDuration.setVisibility(View.VISIBLE);
                    progressBarDuration.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                    imagesList.add(advertisementDetails1.getImage_url());
                    imageModelArrayList = populateList();

                    init();
                    initFloatingSocial();

                    progressBarDuration.setMax(durationAdvertisement);

                    if (checkLogin()) {
                        if (!response.body().isSeen()) {
                            durationAdvertisement = advertisementDetails1.getDuration_value_secound();
                            progressBarDuration.setVisibility(View.VISIBLE);

                            progressBarDuration.setMax(durationAdvertisement);

                            new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        for (int i = 0; i < durationAdvertisement + 1; i++) {
                                            try {
                                                Thread.sleep(1000);

                                                if (progressBar < -2) {
                                                    progressBar = 0;
                                                    return;
                                                }

                                                myHandler.sendEmptyMessage(0);


                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                                return;
                                            }
                                        }
                                    }
                                }).start();

                            } else {
                                progressBarDuration.setVisibility(View.GONE);
                                duration.setVisibility(View.VISIBLE);
                                duration.setText("بازدید شده");
                            }
                        } else {
                            progressBarDuration.setVisibility(View.GONE);
                            duration.setVisibility(View.VISIBLE);
                            duration.setText(advertisementDetails1.getDuration_farsi());
                        }
                }
            }

            @Override
            public void onFailure(Call<DetailsAdvertismentModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataTizer(String id) {
        if(checkLogin())
            getAccessToken();

        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getAdvertisementDetails(accessToken , id).enqueue(new Callback<DetailsAdvertismentModel>() {
            @Override
            public void onResponse(Call<DetailsAdvertismentModel> call, Response<DetailsAdvertismentModel> response) {
                if (response.isSuccessful())
                {

                    final AdvertisementDetails advertisementDetails1 = response.body().getAdvertisement();


                    remaning_visit.setText(new ConvertToPersianNumber(advertisementDetails1.getRemaning_visit().toString()).convertToPersian());
                    visited.setText(new ConvertToPersianNumber(advertisementDetails1.getVisited()).convertToPersian());
                    all_visits_count.setText(new ConvertToPersianNumber(advertisementDetails1.getAll_visits_count()).convertToPersian());

                    durationAdvertisement = advertisementDetails1.getDuration_value_secound();
                    progressBarDuration.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

                    idShowAdvertisement = String.valueOf(advertisementDetails1.getId());
                    titleShowAdvertisement = String.valueOf(advertisementDetails1.getTitle());
                    descriptionShowAdvertisement = String.valueOf(advertisementDetails1.getDescription());
                    imageShowAdvertisement = String.valueOf(advertisementDetails1.getImage());

                    initFloatingSocial();

                   String vidAddress = "https://metakar.com/"+advertisementDetails1.getFilm();

                    Uri vidUri = Uri.parse(vidAddress);

                    vidView.setVideoURI(vidUri);

                    MediaController vidControl = new MediaController(AdvertisementDetailsActivity.this);
                    vidControl.setPadding(0 , 0 , 0 , 100);
                    vidControl.setAnchorView(vidView);

                    vidView.setMediaController(vidControl);

                    vidView.start();

                    progressBarDuration.setMax(durationAdvertisement);

                    if (checkLogin()) {
                        if (!response.body().isSeen()) {
                            durationAdvertisement = advertisementDetails1.getDuration_value_secound();
                            progressBarDuration.setVisibility(View.VISIBLE);

                            progressBarDuration.setMax(durationAdvertisement);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    for (int i = 0; i < durationAdvertisement + 1; i++) {
                                        try {
                                            Thread.sleep(1000);

                                            if (progressBar < -2) {
                                                progressBar = 0;
                                                return;
                                            }

                                            myHandler.sendEmptyMessage(0);


                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            return;
                                        }
                                    }
                                }
                            }).start();

                        } else {
                            progressBarDuration.setVisibility(View.GONE);
                            duration.setVisibility(View.VISIBLE);
                            duration.setText("بازدید شده");
                        }
                    } else {
                        progressBarDuration.setVisibility(View.GONE);
                        duration.setVisibility(View.VISIBLE);
                        duration.setText(advertisementDetails1.getDuration_farsi());
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailsAdvertismentModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);

                bottomVisitBody.setVisibility(View.GONE);

                appbar.setVisibility(View.GONE);
                socialFloatingMenu.setVisibility(View.GONE);

            }else
            {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                bottomVisitBody.setVisibility(View.VISIBLE);
                appbar.setVisibility(View.VISIBLE);
                socialFloatingMenu.setVisibility(View.VISIBLE);


            }
        }
    }

     @SuppressLint("HandlerLeak")
     public Handler myHandler = new Handler()
     {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (progressBar >= 0 && progressBar < durationAdvertisement) {
                    progressBar++;

                    progressBarDuration.setProgress(progressBar);
                }
                else if (progressBar >= durationAdvertisement)
                {

                    if(getAccessToken())
                    {
                        if (Utilities.checkNetworkConnection(AdvertisementDetailsActivity.this))
                        {
                            sendSeenAdvertisement(accessToken , String.valueOf(idShowAdvertisement));

                        }else{
                            Toast.makeText(AdvertisementDetailsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                            Toast.makeText(AdvertisementDetailsActivity.this, "آگهی برای شما ثبت نشد!!!", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                }
            }
        }
     };

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < imagesList.size(); i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(imagesList.get(i));
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        final FragmentManager fragmentManager = getFragmentManager();

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(AdvertisementDetailsActivity.this,imageModelArrayList , fragmentManager));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 6000, 6000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NewApi")
    private void initFloatingSocial()
    {
        materialDesignFAM =  findViewById(R.id.social_floating_menu);
        floatingFacebook =  findViewById(R.id.floating_facebook);
        floatingTwitter =  findViewById(R.id.floating_twitter);
        floatingLinkedIn =  findViewById(R.id.floating_linked_in);
        floatingTelegram =  findViewById(R.id.floating_telegram);
        floatingGooglePlus =  findViewById(R.id.floating_google_plus);
        floatingPinterest =  findViewById(R.id.floating_pinterest);

        floatingFacebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "https://www.facebook.com/sharer/sharer.php?u=metakar.com/advertisements/"+idShowAdvertisement;
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(text)));
            }
        });

        floatingTelegram.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "https://telegram.me/share/url?url=metakar.com/advertisements/"+idShowAdvertisement;
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(text)));
            }
        });

        floatingGooglePlus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "https://plus.google.com/share?url=metakar.com/advertisements/"+idShowAdvertisement;
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(text)));
            }
        });

        floatingTwitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "https://twitter.com/home?status=metakar.com/advertisements/"+idShowAdvertisement;
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(text)));
            }
        });

        floatingLinkedIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "https://www.linkedin.com/shareArticle?mini=true&url=metakar.com/advertisements/"+idShowAdvertisement
                        +"&title="+titleShowAdvertisement+"&summary="+descriptionShowAdvertisement;
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(text)));
            }
        });

        floatingPinterest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = "https://pinterest.com/pin/create/button/?url=metakar.com/advertisements/"+idShowAdvertisement
                        +"&media="+imageShowAdvertisement+"&description="+titleShowAdvertisement;
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(text)));
            }
        });
    }

    public void goToLocationZoom(LatLng latLng , float zoom)
    {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng , zoom);
        mMap.moveCamera(update);
    }

    public void onZoom(View view)
    {
        if(view.getId() == R.id.zoomin)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }

        if(view.getId() == R.id.zoomout)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    @Override
    protected void onPause() {
        re = true;
        progressBar = -10;
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (re) {
            super.onResume();
            re = false;
            visited.setText("");
            all_visits_count.setText("");
            duration.setText("");
            remaning_visit.setText("");

            if (Advertisement_Type.equals("text") || Advertisement_Type.equals("social")) {

                address.setText("");
                telegram.setText("");
                instagram.setText("");
                email.setText("");
                numberPhone1.setText("");
                numberPhone2.setText("");
                title.setText("");
                imagesList.clear();

                progressBarDuration.setProgress(0);
                progressBar = 0;


                if (Utilities.checkNetworkConnection(getBaseContext())) {
                    getData(Advertisement_ID);
                } else {
                    Toast.makeText(getBaseContext(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
            }

            if (Advertisement_Type.equals("visit")) {
                progressBarDuration.setProgress(0);
                progressBar = 0;

                if (Utilities.checkNetworkConnection(getBaseContext())) {
                    getDataWebView(Advertisement_ID);
                } else {
                    Toast.makeText(getBaseContext(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {

        progressBar = -10;

        super.onBackPressed();
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
