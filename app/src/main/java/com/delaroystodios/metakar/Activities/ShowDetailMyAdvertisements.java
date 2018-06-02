package com.delaroystodios.metakar.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.delaroystodios.metakar.Model.Advertisement;
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

public class ShowDetailMyAdvertisements extends AppCompatActivity implements View.OnClickListener {

    private String Advertisement_ID , accessToken;
    private double map_x , map_y;
    private ProgressBar prgLazyLoadingAdvertisements;
    private ImageView indexPhoto , backShowMyAdvertisement;
    private TextView payment , titleAdvertisements , status , typeAdvertisment , statusShow
            , totalVisits , cost , durationFarsi , remaningVisit , createdAt , updatedAt , linkSiteMyAdvertisements, citiesMyAdvertisement , categories
            , showGalleryAdvertisement , showAddressOnMap , siteOrAparat
            , phone1 , phone2 , phone3 , phone4 , phone5 , telegramContact , instagramContact , emailContact
            , addressContact, editIndexPhoto , editInfoAdvertisement , editInfoContact ,
            editGalleryAdvertisement , editAddressOnMap;
    private AppBarLayout appbar;
    private CardView sectionIntoAdvertisement , sectionPhoto , sectionInfoContact;
    private LinearLayout sectionPhone2 , sectionPhone3 , sectionPhone4 , sectionPhone5;
    private String[] imagesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Advertisement_ID = extras.getString("id");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utilities.checkNetworkConnection(ShowDetailMyAdvertisements.this)) {

            if(getAccessToken())
            {
                getMyAdvertisements(accessToken , Advertisement_ID);
            }
        }
        else {
            Toast.makeText(ShowDetailMyAdvertisements.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    private void getMyAdvertisements(String accessToken , String Advertisement_ID){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getMyAdvertisements(accessToken , Advertisement_ID).enqueue(new Callback<Advertisement>() {
            @Override
            public void onResponse(Call<Advertisement> call, Response<Advertisement> response) {

                if (response.isSuccessful()) {

                    Advertisement advertisement = response.body();

                    setContentView(R.layout.activity_show_detail_my_advertisements_2);
                    initComponentVisitSite();

                    titleAdvertisements.setText(new ConvertToPersianNumber(advertisement.getTitle()).convertToPersian());
                    status.setText(new ConvertToPersianNumber(advertisement.getStatus()).convertToPersian());
                    typeAdvertisment.setText(new ConvertToPersianNumber(advertisement.getType_farsi()).convertToPersian());

                    if(advertisement.getEnable().equals("0"))
                        statusShow.setText("غیرفعال");
                    else
                    {
                        statusShow.setText("فعال");
                    }

                    totalVisits.setText(new ConvertToPersianNumber(advertisement.getTotal_visits()).convertToPersian());
                    cost.setText(new ConvertToPersianNumber(advertisement.getCost()).convertToPersian());
                    if(advertisement.getDuration() != null)
                    {
                        durationFarsi.setText(new ConvertToPersianNumber(advertisement.getDuration_farsi()).convertToPersian());
                    }

                remaningVisit.setText(advertisement.getRemaning_visit());

                if(advertisement.getCreated_at() != null)
                {
                    Roozh jCalCreate = new Roozh();
                    String created_at = advertisement.getCreated_at();
                    int c_year = Integer.parseInt(created_at.substring(0 , 4));
                    int c_Month = Integer.parseInt(created_at.substring(6 , 7));
                    int c_day = Integer.parseInt(created_at.substring(9 , 10));
                    jCalCreate.GregorianToPersian(c_year, c_Month, c_day);
                    ConvertToPersianNumber conCreateToPersion = new ConvertToPersianNumber(jCalCreate.toString());
                    createdAt.setText(conCreateToPersion.convertToPersian());
                }

                if(advertisement.getUpdated_at() != null)
                {
                    Roozh jCalCreate = new Roozh();
                    String created_at = advertisement.getCreated_at();
                    int c_year = Integer.parseInt(created_at.substring(0 , 4));
                    int c_Month = Integer.parseInt(created_at.substring(6 , 7));
                    int c_day = Integer.parseInt(created_at.substring(9 , 10));
                    jCalCreate.GregorianToPersian(c_year, c_Month, c_day);
                    ConvertToPersianNumber conCreateToPersion = new ConvertToPersianNumber(jCalCreate.toString());
                    updatedAt.setText(conCreateToPersion.convertToPersian());
                }

                  if(advertisement.getType().equals("visit"))
                  {
                      siteOrAparat.setText("سایت : ");
                      linkSiteMyAdvertisements.setText(advertisement.getSite());

                  }
                  else if(advertisement.getType().equals("aparat"))
                  {
                      siteOrAparat.setText("لینک آپارات : ");
                      linkSiteMyAdvertisements.setText(advertisement.getAparat());
                  }
                  else if(advertisement.getType().equals("text"))
                  {
                      showAddressOnMap.setVisibility(View.VISIBLE);
                      showGalleryAdvertisement.setVisibility(View.VISIBLE);

                      int k = advertisement.getPictures().size();
                      for(int i = 0; i < k; i++)
                      {
                          imagesList[i] = advertisement.getPictures().get(i).getPicture();
                      }

                      siteOrAparat.setText("شرح : ");
                      linkSiteMyAdvertisements.setText(advertisement.getDescription());
                  }
                  else
                  {
                      showAddressOnMap.setVisibility(View.GONE);
                      showGalleryAdvertisement.setVisibility(View.GONE);
                  }

                  if(advertisement.getType().equals("tizer") || advertisement.getType().equals("text"))
                  {
                      sectionInfoContact.setVisibility(View.VISIBLE);

                      map_x = advertisement.getMap().getX();
                      map_y = advertisement.getMap().getY();

                      if(advertisement.getPhones().size() == 1) {
                          phone1.setText(new ConvertToPersianNumber(advertisement.getPhones().get(0)).convertToPersian());
                      }
                      else if(advertisement.getPhones().size() == 2) {
                          phone1.setText(new ConvertToPersianNumber(advertisement.getPhones().get(0)).convertToPersian());
                          phone2.setText(new ConvertToPersianNumber(advertisement.getPhones().get(1)).convertToPersian());
                          sectionPhone2.setVisibility(View.VISIBLE);
                      }
                      else if(advertisement.getPhones().size() == 3) {
                          phone1.setText(new ConvertToPersianNumber(advertisement.getPhones().get(0)).convertToPersian());
                          phone2.setText(new ConvertToPersianNumber(advertisement.getPhones().get(1)).convertToPersian());
                          phone3.setText(new ConvertToPersianNumber(advertisement.getPhones().get(2)).convertToPersian());
                          sectionPhone2.setVisibility(View.VISIBLE);
                          sectionPhone3.setVisibility(View.VISIBLE);
                      }
                      else if(advertisement.getPhones().size() == 4) {
                          phone1.setText(new ConvertToPersianNumber(advertisement.getPhones().get(0)).convertToPersian());
                          phone2.setText(new ConvertToPersianNumber(advertisement.getPhones().get(1)).convertToPersian());
                          phone3.setText(new ConvertToPersianNumber(advertisement.getPhones().get(2)).convertToPersian());
                          phone4.setText(new ConvertToPersianNumber(advertisement.getPhones().get(3)).convertToPersian());
                          sectionPhone2.setVisibility(View.VISIBLE);
                          sectionPhone3.setVisibility(View.VISIBLE);
                          sectionPhone4.setVisibility(View.VISIBLE);
                      }
                      else if(advertisement.getPhones().size() == 5) {
                          phone1.setText(new ConvertToPersianNumber(advertisement.getPhones().get(0)).convertToPersian());
                          phone2.setText(new ConvertToPersianNumber(advertisement.getPhones().get(1)).convertToPersian());
                          phone3.setText(new ConvertToPersianNumber(advertisement.getPhones().get(2)).convertToPersian());
                          phone4.setText(new ConvertToPersianNumber(advertisement.getPhones().get(3)).convertToPersian());
                          phone5.setText(new ConvertToPersianNumber(advertisement.getPhones().get(4)).convertToPersian());

                          sectionPhone2.setVisibility(View.VISIBLE);
                          sectionPhone3.setVisibility(View.VISIBLE);
                          sectionPhone4.setVisibility(View.VISIBLE);
                          sectionPhone5.setVisibility(View.VISIBLE);
                      }

                      emailContact.setText(advertisement.getEmail());
                      instagramContact.setText(advertisement.getInstagram());
                      telegramContact.setText(advertisement.getTelegram());
                      addressContact.setText(advertisement.getAddress());
                  }

                  if(advertisement.getType().equals("social"))
                  {
                      linkSiteMyAdvertisements.setText(advertisement.getSite());
                      sectionInfoContact.setVisibility(View.GONE);
                      siteOrAparat.setText("لینک صفحه اجتماعی : ");
                  }

                    Glide.with(ShowDetailMyAdvertisements.this)
                            .load(advertisement.getImage_url())
                            .error(R.drawable.metakar)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                             //       holder.progress.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(indexPhoto);


                    prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                    appbar.setVisibility(View.VISIBLE);
                    sectionIntoAdvertisement.setVisibility(View.VISIBLE);
                    sectionPhoto.setVisibility(View.VISIBLE);
                }else{
                    Log.i("GGGGGGGGGGGg1" , response.body().toString());
                    Toast.makeText(ShowDetailMyAdvertisements.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Advertisement> call, Throwable t) {
               prgLazyLoadingAdvertisements.setVisibility(View.GONE);
                Log.i("GGGGGGGGGGGg" , t.toString());
                Toast.makeText(ShowDetailMyAdvertisements.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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

    private void initComponentVisitSite() {

        prgLazyLoadingAdvertisements = findViewById(R.id.prgLazyLoadingAdvertisements);
        backShowMyAdvertisement = findViewById(R.id.back_show_my_advertisement);
        payment = findViewById(R.id.payment);
        titleAdvertisements = findViewById(R.id.title_advertisements);
        typeAdvertisment = findViewById(R.id.type_advertisement);
        sectionInfoContact = findViewById(R.id.section_info_contact);
        status = findViewById(R.id.status);
        statusShow = findViewById(R.id.status_show);
        totalVisits = findViewById(R.id.total_visits);
        appbar = findViewById(R.id.appbar);
        cost = findViewById(R.id.cost);
        durationFarsi = findViewById(R.id.duration_visit);
        remaningVisit = findViewById(R.id.remaning_visit);
        createdAt = findViewById(R.id.created_at);
        updatedAt = findViewById(R.id.updated_at);
        citiesMyAdvertisement = findViewById(R.id.cities_my_advertisement);
        categories = findViewById(R.id.categories);
        linkSiteMyAdvertisements = findViewById(R.id.link_site_my_advertisements);
        indexPhoto = findViewById(R.id.photo_my_advertisements);
        sectionIntoAdvertisement = findViewById(R.id.section_into_advertisement);
        sectionPhoto = findViewById(R.id.section_photo);
        showGalleryAdvertisement = findViewById(R.id.show_gallery_advertisement);
        showAddressOnMap = findViewById(R.id.show_address_on_map);
        siteOrAparat = findViewById(R.id.siteOrAparat);
        sectionPhone2 = findViewById(R.id.section_phone_2);
        sectionPhone3 = findViewById(R.id.section_phone_3);
        sectionPhone4 = findViewById(R.id.section_phone_4);
        sectionPhone5 = findViewById(R.id.section_phone_5);
        editIndexPhoto = findViewById(R.id.edit_index_photo);
        editInfoAdvertisement = findViewById(R.id.edit_info_advertisement);
        editInfoContact = findViewById(R.id.edit_info_contact);
        editGalleryAdvertisement = findViewById(R.id.edit_gallery_advertisement);
        editAddressOnMap = findViewById(R.id.edit_address_on_map);

        imagesList = new String[6];

        phone1 = findViewById(R.id.phone1);
        phone2 = findViewById(R.id.phone2);
        phone3 = findViewById(R.id.phone3);
        phone4 = findViewById(R.id.phone4);
        phone5 = findViewById(R.id.phone5);

        emailContact = findViewById(R.id.email_contact);
        instagramContact = findViewById(R.id.instagram_contact);
        telegramContact = findViewById(R.id.telegram_contact);
        addressContact = findViewById(R.id.address_contact);


        payment.setOnClickListener(this);
        backShowMyAdvertisement.setOnClickListener(this);
        showGalleryAdvertisement.setOnClickListener(this);
        showAddressOnMap.setOnClickListener(this);
        editInfoAdvertisement.setOnClickListener(this);
        editInfoContact.setOnClickListener(this);
        editIndexPhoto.setOnClickListener(this);
        editGalleryAdvertisement.setOnClickListener(this);
        editAddressOnMap.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        
        switch (v.getId())
        {
            case R.id.back_show_my_advertisement:
                finish();
                break;
            case R.id.payment:
                Toast.makeText(this, "پرداخت", Toast.LENGTH_SHORT).show();
                break;
            case R.id.show_address_on_map:
                Intent intent_map = new Intent(ShowDetailMyAdvertisements.this , ShowAddressMyAdvertisementActivity.class);
                Bundle b = new Bundle();
                b.putDouble("map_x", map_x);
                b.putDouble("map_y", map_y);
                intent_map.putExtras(b);
                startActivity(intent_map);
                break;
            case R.id.show_gallery_advertisement:
                Intent intent_gallery = new Intent(ShowDetailMyAdvertisements.this , ShowGalleryMyAdvertisement.class);
                Bundle b1 = new Bundle();
                b1.putStringArray("listGallery", imagesList);
                intent_gallery.putExtras(b1);
                startActivity(intent_gallery);
                break;
            case R.id.edit_info_advertisement:
                Intent intent_info_advertisement = new Intent(ShowDetailMyAdvertisements.this , EditInfoMyAdvertisementActivity.class);
                intent_info_advertisement.putExtra("which_section" , "info_advertisement");
                intent_info_advertisement.putExtra("title_advertisement" , titleAdvertisements.getText());
                intent_info_advertisement.putExtra("time_advertisement" , durationFarsi.getText().toString().trim());
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add("تهران - اسلامشهر");
                arrayList.add("تهران - رباط کریم");
                arrayList.add("تهران - واوان");
                arrayList.add("تهران - سبزدشت");
                arrayList.add("تهران - سلطان آباد");

                ArrayList<String> arrayList1 = new ArrayList<>();
                arrayList1.add("پزشکی - جراحی");
                arrayList1.add("خوراکی - چیپس");
                arrayList1.add("میوه جات - سیب");
                arrayList1.add("سبزی جات - کرفس");
                arrayList1.add("سبزی جات - جعفری");
                intent_info_advertisement.putStringArrayListExtra("cities_advertisement" , arrayList);
                intent_info_advertisement.putStringArrayListExtra("categories_advertisement" , arrayList1);
               startActivity(intent_info_advertisement);
                break;
            case R.id.edit_index_photo:

                break;
            case R.id.edit_info_contact:

                break;
            case R.id.edit_gallery_advertisement:

                break;
            case R.id.edit_address_on_map:

                break;
                
        }
    }

}
