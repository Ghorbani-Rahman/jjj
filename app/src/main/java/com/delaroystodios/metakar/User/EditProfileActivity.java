package com.delaroystodios.metakar.User;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.Model.Cities;
import com.delaroystodios.metakar.Model.EditProfile;
import com.delaroystodios.metakar.Model.Provinces;
import com.delaroystodios.metakar.Model.ShowProfile;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.helper.Roozh;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private Button sendEditProfile , noEdit , sendEditProfileFooter , noEditFooterr;
    private EditText name , family , phoneNumber , cartNumber;
    private TextView email;
    private CheckBox showRule;
    private Spinner provinces , cities;
    private ProgressDialog progressBar;
    private ImageView backEditProfile;
    private ArrayList<String> listProvinces;
    private ArrayList<String> listCities;
    private List<Provinces>  provincesForId;
    private String firstName , lastName , username , phone , cartNemberUser ,provinceUser ,  cityUser 
            , rulesConfirmed , accessToken;
    private CardView sectionRulesConfirmed;
    private EditProfile editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            firstName = extras.getString("name");
            lastName = extras.getString("family");
            username = extras.getString("email");
            phone = extras.getString("phone");
            provinceUser = extras.getString("province");
            cityUser = extras.getString("city");
            cartNemberUser = extras.getString("cartNumber");
            rulesConfirmed = extras.getString("rulesConfirmed");

            initComponent();
        }
    }

    private void initComponent()
    {
        sendEditProfile = findViewById(R.id.send_edit_profile);
        noEdit = findViewById(R.id.no_edit);
        name = findViewById(R.id.name);
        family = findViewById(R.id.family);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        cartNumber = findViewById(R.id.cart_number);
        provinces = findViewById(R.id.provinces);
        cities = findViewById(R.id.cities);
        backEditProfile = findViewById(R.id.back_edit_profile);
        sendEditProfileFooter = findViewById(R.id.send_edit_profile_footer);
        noEditFooterr = findViewById(R.id.no_edit_footer);
        sectionRulesConfirmed = findViewById(R.id.section_rules_confirmed);
        showRule = findViewById(R.id.show_rule);


        name.setText(firstName);
        family.setText(lastName);
        email.setText(username);
        phoneNumber.setText(phone);
        cartNumber.setText(cartNemberUser);

        if(rulesConfirmed.equals("0"))
        {
            sectionRulesConfirmed.setVisibility(View.VISIBLE);
            rulesConfirmed = "no";
        }
        else if(rulesConfirmed.equals("1")){
            rulesConfirmed = "yes";
        }

        if (Utilities.checkNetworkConnection(EditProfileActivity.this)) {
            setProgressBar();
            getProvinces();
        }
        else
        {
            Toast.makeText(EditProfileActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

        showRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowRule();
            }
        });


        sendEditProfile.setOnClickListener(this);
        noEdit.setOnClickListener(this);
        backEditProfile.setOnClickListener(this);
        sendEditProfileFooter.setOnClickListener(this);
        noEditFooterr.setOnClickListener(this);

        provinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0)
                {
                    provinces.setSelection(0);
                    listCities = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditProfileActivity.this, android.R.layout.simple_spinner_item, listCities);

                    cities.setAdapter(adapter);
                    return;
                }
                else
                {
                    if (Utilities.checkNetworkConnection(EditProfileActivity.this))
                    {
                        setProgressBar();
                        provinces.setSelection(position);
                        getCities(provincesForId.get(position - 1).getId());
                    }
                    else
                    {
                        provinces.setSelection(0);
                        Toast.makeText(EditProfileActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void ShowRule() {

        AlertDialog.Builder alert = new AlertDialog.Builder(EditProfileActivity.this);

        final TextView titleDialog = new TextView(EditProfileActivity.this);
        titleDialog.setTextSize(22);
        titleDialog.setText("قوانین");
        titleDialog.setPadding(0 , 80 , 60 , 70);
        titleDialog.setGravity(Gravity.START);
        alert.setCustomTitle(titleDialog);
        alert.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.dialog_view_rule, null);
        final TextView tv = view.findViewById(R.id.message_rule);

        tv.setText(getResources().getString(R.string.message_rule_confirmed));

        alert.setView(view);

        alert.setPositiveButton("پذیرفتن قوانین", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showRule.setChecked(true);
            }
        });

        alert.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showRule.setChecked(false);
            }
        });

        alert.show();
    }

    public void setProgressBar()
    {
        progressBar = new ProgressDialog(EditProfileActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("درحال بازگذاری...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }

    private void getProvinces ()
    {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getProvinces().enqueue(new Callback<List<Provinces>>()
        {
            @Override
            public void onResponse(Call<List<Provinces>> call, Response<List<Provinces>> response)
            {
                progressBar.dismiss();
                listProvinces = new ArrayList<>();

                if(response.isSuccessful())
                {

                    provincesForId = response.body();
                    listProvinces.add("انتخاب کنید");
                    for(Provinces province : response.body())
                    {
                        listProvinces.add(province.getTitle());
                    }

                    if(listProvinces.size() == 0)
                    {
                        Toast.makeText(EditProfileActivity.this, "لیست استان ها خالی هست!", Toast.LENGTH_SHORT).show();
                    }else
                    {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditProfileActivity.this, android.R.layout.simple_spinner_item, listProvinces);

                        provinces.setAdapter(adapter);

                        if(provinceUser.equals(""))
                        {
                            provinces.setSelection(0);
                        }
                        else
                        {
                            provinces.setSelection(listProvinces.indexOf(provinceUser));
                        }
                    }
                }else
                {
                    Toast.makeText(EditProfileActivity.this, "خطا در دریافت", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Provinces>> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "مشکلی در ورود به جود آمده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getCities (String idProvince)
    {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getCities(idProvince).enqueue(new Callback<List<Cities>>() {
            @Override
            public void onResponse(Call<List<Cities>> call, Response<List<Cities>> response) {
                listCities = new ArrayList<>();

                progressBar.dismiss();
                if(response.isSuccessful())
                {
                    listCities.add("انتخاب کنید");
                    for(Cities city: response.body())
                    {
                        listCities.add(city.getTitle());
                    }

                    if(listCities.size() == 0)
                    {
                        Toast.makeText(EditProfileActivity.this, "لیست شهرها ها خالی هست!", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                EditProfileActivity.this, android.R.layout.simple_spinner_item, listCities);

                        cities.setAdapter(adapter);

                        if(cityUser.equals(""))
                        {
                            cities.setSelection(0);
                        }
                        else
                        {
                            cities.setSelection(listCities.indexOf(cityUser));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cities>> call, Throwable t) {
                provinces.setSelection(0);
                progressBar.dismiss();
                Toast.makeText(EditProfileActivity.this, "مشکلی در ورود به جود آمده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.no_edit:
            case R.id.back_edit_profile:
            case R.id.no_edit_footer:
                finish();
                break;
            case R.id.send_edit_profile:
            case R.id.send_edit_profile_footer:
                if(ValidateInputProfile())
                {
                    if (Utilities.checkNetworkConnection(EditProfileActivity.this))
                    {
                        if(getAccessToken())
                        {
                            String fName = name.getText().toString().trim();
                            String lName = family.getText().toString().trim();
                            String phone = phoneNumber.getText().toString().trim();
                            String cardNum = cartNumber.getText().toString().trim();
                            String city = cities.getSelectedItem().toString();
                            String province = provinces.getSelectedItem().toString();
                            SendEditProfile(accessToken , fName , lName , phone , province+" , "+ city , cardNum , rulesConfirmed);
                        }
                    }else
                    {
                        Toast.makeText(EditProfileActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void SendEditProfile(String accessToken , String fName , String lName , String mobile , final String city , String cartNum , final String rulesConfirmed) {
            ApiServices mAPIService = ApiUtils.getAPIService();

            mAPIService.sendEditProfile(accessToken , fName , lName , mobile , city , cartNum , rulesConfirmed).enqueue(new Callback<EditProfile>() {

                @Override
                public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {

                    if (response.isSuccessful()) {

                        editProfile = response.body();

                        if (editProfile.getResult().equals("ok")) {
                            Toast.makeText(EditProfileActivity.this, "اطلاعات با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditProfileActivity.this, editProfile.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<EditProfile> call, Throwable t) {
                   
                    Toast.makeText(EditProfileActivity.this, "خطا در دریافت اطلاعات لطفا دوباره امتحان کنید!", Toast.LENGTH_SHORT).show();
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

    private boolean ValidateInputProfile() {
        String uName = name.getText().toString().trim();
        String uFamily = family.getText().toString().trim();
        String uPhone = phoneNumber.getText().toString().trim();
        String uProvince = provinces.getSelectedItem().toString();
        String uCity = null;


        int errorCount = 0;

        String uCart = cartNumber.getText().toString().trim();

        if (uName.length() == 0) {
            name.setError("نام نمیتواند خالی باشد");
            errorCount++;
        } else if (uName.length() < 3) {
            name.setError("نام نمی تواند کمتر از 3 حرف باشد");
            errorCount++;
        }

        if (uFamily.length() == 0) {
            family.setError("نام خانوادگی نمی تواند خالی باشد");
            errorCount++;
        } else if (uFamily.length() < 3) {
            family.setError("نام خانوادگی نمی تواند کمتر از 3 حرف باشد");
            errorCount++;
        }

        if (uPhone.length() == 0) {
            phoneNumber.setError("شماره نمیتواند خالی باشد");
            errorCount++;
        } else if (uPhone.length() < 11) {
            phoneNumber.setError("شماره تلفن کمتر از حد مجاز است");
            errorCount++;
        }

        if (uProvince.equals("انتخاب کنید")) {
            Toast.makeText(this, "استان خود را انتخاب کنید", Toast.LENGTH_LONG).show();
            errorCount++;
        }else
        {
            uCity = cities.getSelectedItem().toString();
        }

        if (!uProvince.equals("انتخاب کنید") && uCity.equals("انتخاب کنید"))
        {
            Toast.makeText(this, "شهر خود را انتخاب کنید", Toast.LENGTH_LONG).show();
            errorCount++;
        }

        if(uCart.length() == 0)
        {
            cartNumber.setError("شماره کارت نمی تواند خالی باشد");
            errorCount++;
        }else if(uCart.length() <13)
        {
            cartNumber.setError("شماره کارت به درستی وارد نشده است");
            errorCount++;
        }

        if(rulesConfirmed.equals("0") && !(showRule.isChecked()))
        {
            showRule.setError("شما قوانین را مشاهده نکردید!");
            errorCount++;
        }


        if(errorCount == 0 )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
