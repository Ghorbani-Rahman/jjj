package com.delaroystodios.metakar.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.delaroystodios.metakar.Activities.AddAdvertisementActivity;
import com.delaroystodios.metakar.Activities.AddTicketActivity;
import com.delaroystodios.metakar.Activities.DashBoardActivity;
import com.delaroystodios.metakar.Activities.ListEarnsActivity;
import com.delaroystodios.metakar.Activities.MyAdvertisementActivity;
import com.delaroystodios.metakar.Activities.NotificationsActivity;
import com.delaroystodios.metakar.Activities.ShowSubsetUserActivity;
import com.delaroystodios.metakar.Activities.ShowWalletActivity;
import com.delaroystodios.metakar.Model.ShowProfile;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.User.EarningsActivity;
import com.delaroystodios.metakar.User.ShowProfileActivity;
import com.delaroystodios.metakar.User.WithdrawsActivity;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.helper.SQLiteHelper;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class PanelUserLoginFragment extends Fragment implements View.OnClickListener , SwipeRefreshLayout.OnRefreshListener
{

    private View view;
    public static SQLiteHelper sqLiteHelper;
    private Cursor cursor;
    private TextView countSubset , countNotificationNoRead , firstLastNameUser;
    private String accessToken;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progressBar;
    private ScrollView section_panel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hideSoftKeyboard();


        if (Utilities.checkNetworkConnection(getActivity())) {

            if(getAccessToken())
            {
                view = inflater.inflate(R.layout.fragment_panel_user, null);
                initComponentUserPanel();
                setProgressBar();
                getUserInfo(accessToken);
            }

        } else {
            Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void initComponentUserPanel()
    {
        sqLiteHelper = new SQLiteHelper(getActivity() , "Image_User.sqlite" , null , 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS Image_User (Id INTEGER PRIMARY KEY AUTOINCREMENT , image BLOG)");


        section_panel = view.findViewById(R.id.section_panel);
        CardView advertisements = view.findViewById(R.id.advertisements);
        CardView profile = view.findViewById(R.id.profile);
        CardView wallet = view.findViewById(R.id.wallet);
 //       CardView ticket = view.findViewById(R.id.ticket);
        CardView notifications = view.findViewById(R.id.notifications);
        CardView withdraws = view.findViewById(R.id.withdraws);
        CardView earnings = view.findViewById(R.id.earnings);
        CardView dashboard = view.findViewById(R.id.dashboard);
        ImageView selected_img_user = view.findViewById(R.id.user_picture);
        firstLastNameUser = view.findViewById(R.id.first_last_user);
        countSubset = view.findViewById(R.id.count_subset);
        countNotificationNoRead = view.findViewById(R.id.count_notification_no_read);
        TextView logout = view.findViewById(R.id.logout);

        mSwipeRefreshLayout = view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue, R.color.purple, R.color.green, R.color.orange);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        cursor = sqLiteHelper.getData("SELECT * FROM Image_User");


        byte[] image = null;
        while (cursor.moveToNext())
        {
            image = cursor.getBlob(1);
            Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0 , image.length);
            selected_img_user.setImageBitmap(bitmap);
        }



        advertisements.setOnClickListener(this);
        profile.setOnClickListener(this);
        wallet.setOnClickListener(this);
 //       ticket.setOnClickListener(this);
        notifications.setOnClickListener(this);
        withdraws.setOnClickListener(this);
        earnings.setOnClickListener(this);
        dashboard.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {

            case R.id.withdraws:
                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    Intent intentRules = new Intent(getActivity() , WithdrawsActivity.class);
                    startActivity(intentRules);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.earnings:
                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    Intent intentHelp = new Intent(getActivity() , EarningsActivity.class);
                    startActivity(intentHelp);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dashboard:
                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    Intent intentDashBoard = new Intent(getActivity() , DashBoardActivity.class);
                    intentDashBoard.putExtra("name" , firstLastNameUser.getText().toString());
                    startActivity(intentDashBoard);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.advertisements:
                setChooseUserInAdvertisements();
                break;
            case R.id.profile:
                setChooseUserProfile();
                break;
            case R.id.wallet:
                setChooseUserٌWallet();
                break;
          /*  case R.id.ticket:
                setChooseUserTicket();
                break;*/
            case R.id.notifications:
                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    Intent notificationsActivity = new Intent(getActivity() , NotificationsActivity.class);
                    startActivity(notificationsActivity);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.logout:
                logoutUser();
                break;
            default:
                break;
        }

    }

    private void logoutUser()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("میخواهید از حساب کاربری خارج شوید؟");

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
                SharedPreferences preferences = getActivity().getSharedPreferences("userLogin", MODE_PRIVATE);
                preferences.edit().clear().apply();


                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.viewpager, new PanelUserNoLoginFragment())
                        .commit();
            }
        });

        builder.create().show();

    }

    private void getUserInfo(String accessToken) {

        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getShowProfile(accessToken).enqueue(new Callback<ShowProfile>() {

            @Override
            public void onResponse(Call<ShowProfile> call, Response<ShowProfile> response) {


                mSwipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful()) {
                    section_panel.setVisibility(View.VISIBLE);
                    progressBar.dismiss();
                    ShowProfile showProfile = response.body();

                    firstLastNameUser.setText(showProfile.getName() + " " + showProfile.getFamily());

                    countNotificationNoRead.setText(new ConvertToPersianNumber(showProfile.getUnread_notifications_count()).convertToPersian());
                    countSubset.setText(new ConvertToPersianNumber(showProfile.getSubset_count()).convertToPersian());
                }
                else
                {
                    progressBar.dismiss();
                    Toast.makeText(getActivity(), "مشکل در خواندن مشخصات کاربری", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ShowProfile> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                progressBar.dismiss();
                Toast.makeText(getActivity(), "خطا در دریافت اطلاعات لطفا دوباره امتحان کنید!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setChooseUserInAdvertisements()
    {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.Advertisments)
                .titleGravity(GravityEnum.END)
                .itemsGravity(GravityEnum.END)
                .items(R.array.Advertisments)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:
                                Intent intent = new Intent(getActivity() , AddAdvertisementActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                if (Utilities.checkNetworkConnection(getActivity())) {
                                    Intent intentMyAdvertisement = new Intent(getActivity() , MyAdvertisementActivity.class);
                                    startActivity(intentMyAdvertisement);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .show();

    }

    private void setChooseUserProfile()
    {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.UserProfile)
                .titleGravity(GravityEnum.END)
                .itemsGravity(GravityEnum.END)
                .items(R.array.UserProfile)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:
                                if (Utilities.checkNetworkConnection(getActivity())) {
                                    Intent intent_edit = new Intent(getActivity() , ShowProfileActivity.class);
                                    startActivity(intent_edit);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if (Utilities.checkNetworkConnection(getActivity())) {
                                    Intent intentEdit = new Intent(getActivity() , ShowProfileActivity.class);
                                    startActivity(intentEdit);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 2:
                                if (Utilities.checkNetworkConnection(getActivity())) {
                                    Intent intentSubSet = new Intent(getActivity() , ShowSubsetUserActivity.class);
                                    startActivity(intentSubSet);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .show();

    }

    private void setChooseUserٌWallet()
    {
        new MaterialDialog.Builder(getActivity())
                .titleGravity(GravityEnum.END)
                .itemsGravity(GravityEnum.END)
                .title(R.string.UserٌWallet)
                .items(R.array.UserٌWallet)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:
                                if (Utilities.checkNetworkConnection(getActivity()))
                                {
                                    Intent intent = new Intent(getActivity(), ShowWalletActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if (Utilities.checkNetworkConnection(getActivity()))
                                {
                                    Intent intent = new Intent(getActivity(), ListEarnsActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .show();
    }

    private void setChooseUserTicket()
    {
        new MaterialDialog.Builder(getActivity())
                .titleGravity(GravityEnum.END)
                .itemsGravity(GravityEnum.END)
                .title(R.string.UserTicket)
                .items(R.array.UserTicket)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which){
                            case 0:
                                if (Utilities.checkNetworkConnection(getActivity()))
                                {
                                    Intent intent = new Intent(getActivity(), AddTicketActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if (Utilities.checkNetworkConnection(getActivity()))
                                {
                                    Intent intent = new Intent(getActivity(), AddTicketActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                })
                .show();

    }

    private void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        if(inputMethodManager != null && getActivity().getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private boolean getAccessToken() {
        SharedPreferences preferences = getActivity().getSharedPreferences("userLogin", MODE_PRIVATE);

        accessToken = "Bearer " + preferences.getString("accessToken", "");

        if(accessToken.equals(""))
        {
            return false;
        }

        return true;
    }

    @Override
    public void onRefresh() {
        if(getAccessToken()) {
            if (Utilities.checkNetworkConnection(getActivity()))
            {
                section_panel.setVisibility(View.GONE);
                getUserInfo(accessToken);
            }
            else
            {
                Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 2000);
    }

    public void setProgressBar()
    {
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage("در حال بارگذاری ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }
}
