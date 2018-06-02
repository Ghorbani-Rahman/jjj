package com.delaroystodios.metakar.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Model.ResetPassword;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgatPasswordActivity extends AppCompatActivity {

    EditText user_email;
    Button send_password;
    private ProgressDialog progressBar;
    TextView errorResetPassword;
    ImageView back_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back_forgot_password = (ImageView)findViewById(R.id.back_forgot_password);
        user_email = (EditText)findViewById(R.id.user_email);
        send_password = (Button) findViewById(R.id.send_password);
        errorResetPassword = (TextView)findViewById(R.id.errorResetPassword);

        back_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftKeyboard();
                String email = user_email.getText().toString().trim();
                if(ValidateLogin(email))
                {
                    if (Utilities.checkNetworkConnection(ForgatPasswordActivity.this))
                    {
                        setProgressBar();
                        doResetPassword(email);
                    }
                    else
                    {
                        Toast.makeText(ForgatPasswordActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void doResetPassword(String email)
    {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.resetPassword(email).enqueue(new Callback<ResetPassword>(){

           @Override
           public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {

               if(response.isSuccessful())
               {
                   progressBar.dismiss();
                    ResetPassword resetPassword = response.body();

                    if(resetPassword.getResult().equals("ok"))
                    {

                        errorResetPassword.setVisibility(View.VISIBLE);
                        errorResetPassword.setText(resetPassword.getMessage());


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


                    }else if(resetPassword.getResult().equals("error"))
                    {
                        Toast.makeText(ForgatPasswordActivity.this, resetPassword.getMessage() , Toast.LENGTH_LONG).show();
                    }
               }
           }

           @Override
           public void onFailure(Call<ResetPassword> call, Throwable t) {
               progressBar.dismiss();
               Toast.makeText(ForgatPasswordActivity.this, "مشکلی در اتصال به وجود آماده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();
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

    private void hideSoftKeyboard()
    {

        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private boolean ValidateLogin(String email)
    {

        if (email == null || email.trim().length() == 0)
        {
            user_email.setError("فیلد ایمیل خالی می باشد!!!");
            return false;
        }
        else if(!isEmailValid(email))
        {
            user_email.setError("مقدار ایمیل معتبر نیست!");
            return false;
        }

        return true;
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
