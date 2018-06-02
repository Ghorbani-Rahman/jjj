package com.delaroystodios.metakar.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Model.DeleteNotifications;
import com.delaroystodios.metakar.Model.ReadNotifications;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadNotificationsActivity extends AppCompatActivity {

    private ImageView backReadNotifications;
    private String accessToken , idNotification;
    private TextView titleReadNotification , sendDateNotification , deleteMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_notification);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idNotification = extras.getString("idNotifications");

            initComponent();
        }

    }

    private void initComponent() {

        backReadNotifications = findViewById(R.id.back_read_notifications);
        titleReadNotification =  findViewById(R.id.title_read_notification);
        sendDateNotification =  findViewById(R.id.date_send_notification);
        deleteMessage =  findViewById(R.id.delete_message);

        deleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage(idNotification);
            }
        });


        backReadNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Utilities.checkNetworkConnection(ReadNotificationsActivity.this)) {

            if(getAccessToken())
            {
                getReadNotification(accessToken , idNotification);
            }
        } else {
            Toast.makeText(ReadNotificationsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

    }

    private void getReadNotification(String accessToken , String idNotification){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getReadNotifications(accessToken , idNotification).enqueue(new Callback<ReadNotifications>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ReadNotifications> call, Response<ReadNotifications> response) {
                if (response.isSuccessful())
                {
                    ReadNotifications readNotifications =  response.body();

                    titleReadNotification.setText(new ConvertToPersianNumber(readNotifications.getTitle()).convertToPersian());

                    String time = readNotifications.getCreated_at().substring(11);

                    ConvertToPersianNumber conCreateToPersian = new ConvertToPersianNumber("  " + time + "   " + readNotifications.getCreated_at_jalali() + "  ");

                    sendDateNotification.setText(conCreateToPersian.convertToPersian());


                    String htmlText = " %s ";
                    String myData = "<html><body dir=\"rtl\" style=\"text-align:justify\">"+ readNotifications.getContent() +"</body></Html>";
                    WebView webView = (WebView) findViewById(R.id.webView1);
                    webView.loadData(String.format(htmlText,myData), "text/html", "utf-8");
                }
                else
                {
                    Toast.makeText(ReadNotificationsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ReadNotifications> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ReadNotificationsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
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

    private void deleteMessage(final String idNotification)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("آیا میخواهید این پیام را حذف کنید؟");

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (Utilities.checkNetworkConnection(ReadNotificationsActivity.this)) {

                    if(getAccessToken())
                    {
                        sendIdNotificationForDelete(accessToken , idNotification);
                    }
                } else {
                    Toast.makeText(ReadNotificationsActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.create().show();
    }

    private void sendIdNotificationForDelete(final String accessToken , String id){
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.sendForDeleteNotification(accessToken , id).enqueue(new Callback<DeleteNotifications>()
        {
            @Override
            public void onResponse(@NonNull Call<DeleteNotifications> call, @NonNull Response<DeleteNotifications> response)
            {

                if (response.isSuccessful())
                {
                    if (response.body().getResult().equals("ok")) {
                        Toast.makeText(ReadNotificationsActivity.this, "حذف با موفقیت انجام شد!", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                    else
                    {
                        Toast.makeText(ReadNotificationsActivity.this, "حذف با موفقیت انجام نشد!لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                   Toast.makeText(ReadNotificationsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteNotifications> call, Throwable t) {
                Toast.makeText(ReadNotificationsActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}


