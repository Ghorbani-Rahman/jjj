package com.delaroystodios.metakar.Activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.delaroystodios.metakar.Adapter.SlidingImage_Adapter;
import com.delaroystodios.metakar.Model.ImageModel;
import com.delaroystodios.metakar.R;
import com.viewpagerindicator.CirclePageIndicator;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShowGalleryMyAdvertisement extends AppCompatActivity {

    private ViewPager mPager;
    private ArrayList<ImageModel> imageModelArrayList;
    private static int NUM_PAGES = 0;
    private String[] imagesList;
    private ImageView backGalleryShow;
    private static int currentPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_gallery_advertisement);


        imageModelArrayList = new ArrayList<>();

        Bundle b = getIntent().getExtras();
        assert b != null;
        String[] array=b.getStringArray("listGallery");

        assert array != null;
        imagesList = new String[array.length];

        imageModelArrayList = populateList();

        initComponent();


    }

    private void initComponent() {


        final FragmentManager fragmentManager = getFragmentManager();

        mPager = findViewById(R.id.pager);
        backGalleryShow = findViewById(R.id.back_gallery_show);

        mPager.setAdapter(new SlidingImage_Adapter(getBaseContext(),imageModelArrayList , fragmentManager));

        backGalleryShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CirclePageIndicator indicator = findViewById(R.id.indicator);
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
        }, 6000, 3000);

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


    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < imagesList.length; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(imagesList[i]);
            list.add(imageModel);
        }

        return list;
    }

}
