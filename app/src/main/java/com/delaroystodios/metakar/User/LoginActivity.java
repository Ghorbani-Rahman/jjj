package com.delaroystodios.metakar.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Model.Login;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.SQLiteHelper;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    private EditText user_email , user_password;
    private TextView forgot_password;
    private ProgressDialog progressBar;
    public static SQLiteHelper sqLiteHelper;
    private ImageView back_login;
    private TextView errorLogin;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(checkPermission())
        {
            sqLiteHelper = new SQLiteHelper(this , "Image_User.sqlite" , null , 1);
            sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS Image_User (Id INTEGER PRIMARY KEY AUTOINCREMENT , image BLOG)");

            Cursor cursor = sqLiteHelper.getData("SELECT * FROM Image_User");

            byte[] image;
            while (cursor.moveToNext())
            {
                CircleImageView selected_img_user = (CircleImageView) findViewById(R.id.selected_img_user);
                image = cursor.getBlob(1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0 , image.length);
                selected_img_user.setImageBitmap(bitmap);
            }
        }

        initComponent();

    }

    public boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(LoginActivity.this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private void initComponent()
    {

        user_email = (EditText)findViewById(R.id.user_email);
        user_password = (EditText)findViewById(R.id.user_password);
        Button login_user = (Button) findViewById(R.id.login_user);
        forgot_password = (TextView)findViewById(R.id.forgot_password);
        back_login = (ImageView)findViewById(R.id.back_login);
        errorLogin = (TextView)findViewById(R.id.errorLogin);

        login_user.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        back_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_user:
            {
                String username = user_email.getText().toString();
                String password = user_password.getText().toString();

                if(ValidateLogin(username , password))
                {
                    if (Utilities.checkNetworkConnection(this))
                    {
                        setProgressBar();
                        doLogin(username , password);
                    }
                    else
                    {
                        Toast.makeText(this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case R.id.forgot_password:
            {
                Intent intentLogin = new Intent(this , ForgatPasswordActivity.class);
                startActivity(intentLogin);
                break;
            }
            case R.id.back_login:
                finish();
                break;
        }
    }

    private boolean ValidateLogin(String email, String password)
    {

        int errorCount = 0;

        if (email == null || email.trim().length() == 0)
        {
            user_email.setError("مقدار ایمیل نمی تواند خالی باشد!");
            errorCount++;
        }
        else if(!isEmailValid(email))
        {
            user_email.setError("ایمیل وارد شده معتبر نمی باشد!");
            errorCount++;
        }

        if(password == null || password.trim().length() == 0)
        {
            user_password.setError(" رمز نمیتواند خالی باشد!");
            errorCount++;
        }
        else if (password.trim().length() < 5)
        {
            user_password.setError("رمز عبور اشتباه می باشد!");
            errorCount++;
        }

        if(errorCount == 0)
        {
            return true;
        }else
        {
         return false;
        }
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void doLogin (final String username , final String password)
    {

        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.login(username,password).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {


                if(response.isSuccessful())
                {
                    Login resObj = response.body();

                    Log.i("LOG RESULT " , String.valueOf(resObj));


                    if(resObj.getResult().equals("ok"))
                    {
                        progressBar.dismiss();
                        Toast.makeText(LoginActivity.this, "ورود شما با موفقیت انجام شد", Toast.LENGTH_LONG).show();
                        SharedPreferences mSharedPreferences = getSharedPreferences("userLogin", MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                        mEditor.putBoolean("login", true);
                        mEditor.putString("accessToken", resObj.getToken());
                        Log.i("LOGTOKEN : " , resObj.getToken());
                        mEditor.putString("nameFamily", resObj.getUser().getName() + " " + resObj.getUser().getFamily());
                        mEditor.putString("countSubset", resObj.getUser().getSubset_count());
                        mEditor.putString("countNotification", resObj.getNotifications_count());

                        mEditor.apply();
                        finish();
                    }
                }
                else
                {

                    progressBar.dismiss();

                    JSONObject jObjError = null;

                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        errorLogin.setVisibility(View.VISIBLE);
                        errorLogin.setText(jObjError.getString("message"));
                        Thread splash = new Thread(){
                            @Override
                            public void run() {
                                try
                                {
                                    sleep(10000);
                                }catch (InterruptedException whatIsTheProblem)
                                {
                                    whatIsTheProblem.printStackTrace();
                                }finally {

                                    finish();
                                }
                            }
                        };

                        splash.start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(LoginActivity.this, "مشکلی در ورود به جود آمده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void setProgressBar()
    {
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setMessage("لطفا منتظر بمانید...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }
}
