package com.delaroystodios.metakar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.delaroystodios.metakar.Activities.AddAdvertisementActivity;
import com.delaroystodios.metakar.fragment.CategoryFragment;
import com.delaroystodios.metakar.fragment.HomeFragment;
import com.delaroystodios.metakar.fragment.PanelUserLoginFragment;
import com.delaroystodios.metakar.fragment.PanelUserNoLoginFragment;
import com.delaroystodios.metakar.fragment.SearchFragment;
import com.delaroystodios.metakar.helper.BottomNavigationBehavior;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ImageView fab ;
    private FrameLayout viewPager;
    public static FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    public static CategoryFragment categoryFragment;
    private PanelUserNoLoginFragment panelUserNoLoginFragment;
    private PanelUserLoginFragment panelUserLoginFragment;
    public static FragmentTransaction ft;
    public static Fragment fragmentSub;
    public static Fragment fragmentShowSub;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.fab);


        if (Build.VERSION.SDK_INT >= 17) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }


        viewPager = findViewById(R.id.viewpager);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (Build.VERSION.SDK_INT >= 17) {
            bottomNavigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            viewPager.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        }

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());


        fragmentManager = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        categoryFragment = new CategoryFragment();
        panelUserNoLoginFragment = new PanelUserNoLoginFragment();
        panelUserLoginFragment = new PanelUserLoginFragment();

        addFragmentHome();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogin())
                {
                    Intent intent = new Intent(MainActivity.this, AddAdvertisementActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "برای افزودن آگهی باید وارد پنل کاربری شوید", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                addFragmentHome();
                                return true;
                            case R.id.navigation_search:
                               addFragmentSearch();
                                return true;
                            case R.id.navigation_category:
                              addFragmentCategory();
                                return true;
                            case R.id.navigation_panel_user:
                              addFragmentPanel();
                                return true;
                        }

                        return false;
                    }
                });
    }

    private void addFragmentHome() {
        ft = getSupportFragmentManager().beginTransaction();
        fragmentSub = fragmentManager.findFragmentByTag("sub");
        fragmentShowSub = fragmentManager.findFragmentByTag("showSub");

        if (homeFragment.isAdded())
        {
            ft.show(homeFragment);
        }
        else
        {
            ft.add(R.id.viewpager, homeFragment, "home");
        }

        if(fragmentSub != null)
        {
            ft.hide(fragmentSub);
        }

        if(fragmentShowSub != null)
        {
            ft.hide(fragmentShowSub);
        }


        if(searchFragment.isAdded())
        {
            ft.hide(searchFragment);
        }

        if(categoryFragment.isAdded())
        {
            ft.hide(categoryFragment);
        }

        if(panelUserNoLoginFragment.isAdded())
        {
            ft.hide(panelUserNoLoginFragment);
        }

        if(panelUserLoginFragment.isAdded())
        {
            ft.hide(panelUserLoginFragment);
        }

        ft.commit();

    }

    private void addFragmentSearch()
    {
        ft = getSupportFragmentManager().beginTransaction();
        fragmentSub = fragmentManager.findFragmentByTag("sub");
        fragmentShowSub = fragmentManager.findFragmentByTag("showSub");

        if (searchFragment.isAdded()) { // if the fragment is already in container
            ft.show(searchFragment);
        }
        else { // fragment needs to be added to frame container
            ft.add(R.id.viewpager, searchFragment, "searchFragment");
        }

        if(fragmentSub != null)
        {
            ft.hide(fragmentSub);
        }

        if(fragmentShowSub != null)
        {
            ft.hide(fragmentShowSub);
        }


        if(homeFragment.isAdded())
        {
            ft.hide(homeFragment);
        }

        if(categoryFragment.isAdded())
        {
            ft.hide(categoryFragment);
        }

        if(panelUserNoLoginFragment.isAdded())
        {
            ft.hide(panelUserNoLoginFragment);
        }

        if(panelUserLoginFragment.isAdded())
        {
            ft.hide(panelUserNoLoginFragment);
        }

        ft.commit();

    }

    private void addFragmentCategory()
    {
        ft = getSupportFragmentManager().beginTransaction();
        fragmentSub = fragmentManager.findFragmentByTag("sub");
        fragmentShowSub = fragmentManager.findFragmentByTag("showSub");


        if(fragmentShowSub != null)
        {
            ft.hide(fragmentSub);
            ft.hide(categoryFragment);
            ft.show(fragmentShowSub);
        }
        else if(fragmentSub != null)
        {
            ft.show(fragmentSub);
            ft.hide(categoryFragment);
        }
        else if(categoryFragment.isAdded())
        {
            ft.show(categoryFragment);
        }
        else
        {
            ft.add(R.id.viewpager, categoryFragment, "categoryFragment");
        }


        if(homeFragment.isAdded())
        {
            ft.hide(homeFragment);
        }

        if(searchFragment.isAdded())
        {
            ft.hide(searchFragment);
        }

        if(panelUserNoLoginFragment.isAdded())
        {
            ft.hide(panelUserNoLoginFragment);
        }

        if(panelUserLoginFragment.isAdded())
        {
            ft.hide(panelUserLoginFragment);
        }

        ft.commit();

    }

    private void addFragmentPanel()
    {

        if(checkLogin())
        {
            panelUserLoginFragment = new PanelUserLoginFragment();
            ft = getSupportFragmentManager().beginTransaction();
            fragmentSub = fragmentManager.findFragmentByTag("sub");
            fragmentShowSub = fragmentManager.findFragmentByTag("showSub");

            if (panelUserLoginFragment.isAdded()) {
                ft.show(panelUserLoginFragment);
            } else {
                ft.add(R.id.viewpager, panelUserLoginFragment, "panelUserLoginFragment");
            }

            if (fragmentSub != null) {
                ft.hide(fragmentSub);
            }

            if (fragmentShowSub != null) {
                ft.hide(fragmentShowSub);
            }

            if (homeFragment.isAdded()) {
                ft.hide(homeFragment);
            }

            if (categoryFragment.isAdded()) {
                ft.hide(categoryFragment);
            }

            if (searchFragment.isAdded()) {
                ft.hide(searchFragment);
            }

            ft.commit();
        }
        else
        {
            ft = getSupportFragmentManager().beginTransaction();
            fragmentSub = fragmentManager.findFragmentByTag("sub");
            fragmentShowSub = fragmentManager.findFragmentByTag("showSub");

            if (panelUserNoLoginFragment.isAdded()) {
                ft.show(panelUserNoLoginFragment);
            } else {
                ft.add(R.id.viewpager, panelUserNoLoginFragment, "panelUserNoLoginFragment");
            }

            if (fragmentSub != null) {
                ft.hide(fragmentSub);
            }

            if (fragmentShowSub != null) {
                ft.hide(fragmentShowSub);
            }

            if (homeFragment.isAdded()) {
                ft.hide(homeFragment);
            }

            if (categoryFragment.isAdded()) {
                ft.hide(categoryFragment);
            }

            if (searchFragment.isAdded()) {
                ft.hide(searchFragment);
            }

            ft.commit();
        }
    }


   /* @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {

            getFragmentManager().popBackStack();
        } else {
            exitApp();
        }
    }*/

    private boolean checkLogin() {
        SharedPreferences preferences = getSharedPreferences("userLogin", MODE_PRIVATE);
        boolean userName = preferences.getBoolean("login", false);

        return userName;
    }

    private void exitApp()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("میخواهید از برنامه خارج شوید؟");

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                finish();
            }
        });

        builder.create().show();

    }

}
