package com.delaroystodios.metakar.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Model.Register;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTwoRegister extends Fragment implements View.OnClickListener{

    private View mView;
    private ImageView backTwoRegister;
    private Button verifyRegister;
    private EditText userName, userPassword, confirmPassword;
    private ProgressDialog progressBar;
    private String name , family;
    private TextView errorEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            name = bundle.getString("name", "");
            family = bundle.getString("family", "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_two_register, container , false);

        userName = (EditText)mView.findViewById(R.id.user_name);
        userPassword = (EditText)mView.findViewById(R.id.user_password);
        confirmPassword = (EditText)mView.findViewById(R.id.retrieve_user_password);
        verifyRegister = (Button)mView.findViewById(R.id.verify_register);
        backTwoRegister = (ImageView)mView.findViewById(R.id.back_two_register);
        errorEmail = (TextView)mView.findViewById(R.id.error_email);


        backTwoRegister.setOnClickListener(this);
        verifyRegister.setOnClickListener(this);


        return mView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_two_register:
                getActivity().onBackPressed();
                break;
            case R.id.verify_register:
                String pass , passConfirem , em;
                pass = userPassword.getText().toString().trim();
                passConfirem = confirmPassword.getText().toString().trim();
                em = userName.getText().toString().trim();

                if(ValidateRegisterStepTwo(pass , passConfirem , em))
                {
                    setProgressBar();
                    doRegister(em , pass , passConfirem , name , family );
                }
                break;
        }
    }

    private boolean ValidateRegisterStepTwo(String p, String cp , String em)
    {

        int errorCount = 0;

        if (p == null || p.trim().length() == 0)
        {
            userPassword.setError("طول پسورد نمی تواند خالی باشد!");
            errorCount++;
        }
        else if(p.length() < 5)
        {
            userPassword.setError("طول رمز نمی تواند کمتر از 5 کاراکتر باشد!");
            errorCount++;
        }

        if (cp == null || cp.trim().length() == 0)
        {
            confirmPassword.setError("طول تکرار رمز نمی تواند خالی باشد!");
            errorCount++;
        }
        else if(cp.length() < 5)
        {
            confirmPassword.setError("طول تکرار رمز نمیتواند کمتر از 5 کاراکتر باشد");
            errorCount++;
        }


        if(!p.equals(cp) && cp != null && cp.length() != 0 && cp.length() >= 5)
        {
            errorCount++;
            confirmPassword.setError("رمز عبور با تکرار آن یکی نیست!");
        }

        if(!isEmailValid(em))
        {
            errorCount++;
            userName.setError("ایمیل وارد شده معتبر نیست!");
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

    private void doRegister(String email, String password, String password_confirmation, String firstName, String lastName)
    {
        ApiServices mApiServices = ApiUtils.getAPIService();

        mApiServices.register(email , password , password_confirmation , firstName , lastName).enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {

                progressBar.dismiss();

                if(response.isSuccessful()) {
                    Register resObj = response.body();

                    if (resObj.getResult().equals("ok")) {
                        errorEmail.setVisibility(View.VISIBLE);
                        errorEmail.setText(resObj.getMessage());

                        Thread splash = new Thread(){
                            @Override
                            public void run() {
                                try
                                {
                                    sleep(8000);
                                }catch (InterruptedException whatIsTheProblem)
                                {
                                    whatIsTheProblem.printStackTrace();
                                }finally {
                                    getActivity().finish();
                                }
                            }
                        };

                        splash.start();
                    }
                }
                else
                {
                    try
                    {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JSONObject jsonObject = new JSONObject(jObjError.getString("error"));
                        JSONArray jsonArray= new JSONArray(jsonObject.getString("email"));

                        errorEmail.setVisibility(View.VISIBLE);
                        errorEmail.setText(jsonArray.get(0).toString());

                    } catch (Exception e)
                    {
                    }
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t)
            {
                progressBar.dismiss();
                Toast.makeText(getActivity(), "خطا در اتصال لطفا دوباره امتحان کنید", Toast.LENGTH_LONG).show();
            }
        });
    }

    boolean isEmailValid(CharSequence email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void setProgressBar() {
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage("لطفا منتظر بمانید...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }
}
