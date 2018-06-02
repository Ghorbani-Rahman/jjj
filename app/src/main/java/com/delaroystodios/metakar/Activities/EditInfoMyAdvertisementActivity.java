package com.delaroystodios.metakar.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.delaroystodios.metakar.Model.Category;
import com.delaroystodios.metakar.Model.Cities;
import com.delaroystodios.metakar.Model.Provinces;
import com.delaroystodios.metakar.Model.SubCategory;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class EditInfoMyAdvertisementActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backEditMyAdvertisement , removeOne , removeTwo , removeThree , removeFour , removeFive;
    private TextView cancelEdit , sendEdit , proCityOne , proCityTwo , proCityThree , proCityFour , proCityFive;
    private String titleAdvertisement , timeAdvertisement , whichSection;
    private LinearLayout sectionInfoAdvertisement , sectionIndexPhotoAndDescription;
    private CardView sectionInfoContact;
    private EditText titleAddAdvertisement;
    private LinearLayout two, three, four, five, oneCategory, twoCategory, threeCategory, fourCategory, fiveCategory;
    private String firstSelectScope = "", twoSelectScope = "", threeSelectScope = "", fourSelectScope = "", fiveSelectScope = "";
    private String firstSelectScopeCategory = "", twoSelectScopeCategory = "", threeSelectScopeCategory = "", fourSelectScopeCategory = "", fiveSelectScopeCategory = "";
    private TextView categorySubCategoryOne, categorySubCategoryTwo, categorySubCategoryThree, categorySubCategoryFour, categorySubCategoryFive;
    private ImageView removeOneCategory, removeTwoCategory, removeThreeCategory, removeFourCategory, removeFiveCategory;
    private Spinner category, subCategory;
    private ArrayList<String> listSubCategory , citiesAdvertisement , categoriesAdvertisement;
    private Spinner provinces, cities , durationAdvertisement;
    private List<Category> categoryForId;
    private ProgressDialog progressBar;
    private List<Provinces> provincesForId;
    private ArrayList<String> listCities;
    private ArrayList<String> listCategory;
    private ArrayList<String> listProvinces;
    private boolean canChooseLimit;
    private boolean canProvience;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_advertisements);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            whichSection = extras.getString("which_section");

            if(whichSection.equals("info_advertisement"))
            {
                titleAdvertisement = extras.getString("title_advertisement");
                timeAdvertisement = extras.getString("time_advertisement");
                citiesAdvertisement = extras.getStringArrayList("cities_advertisement");
                categoriesAdvertisement = extras.getStringArrayList("categories_advertisement");


                initComponentInfoAdvertisement();

                init();
                initElements();

            }
        }
    }

    private void initComponentInfoAdvertisement() {

        backEditMyAdvertisement = findViewById(R.id.back_edit_my_advertisement);
        durationAdvertisement = findViewById(R.id.duration_advertisement);
        cancelEdit = findViewById(R.id.cancel_edit);
        sendEdit = findViewById(R.id.send_edit);
        sectionInfoAdvertisement = findViewById(R.id.section_info_advertisement);
        titleAddAdvertisement = findViewById(R.id.title_advertisement);
        proCityOne = findViewById(R.id.pro_city_one);
        proCityTwo = findViewById(R.id.pro_city_two);
        proCityThree = findViewById(R.id.pro_city_three);
        proCityFour = findViewById(R.id.pro_city_four);
        proCityFive = findViewById(R.id.pro_city_five);
        removeOne = findViewById(R.id.remove_one);
        removeTwo = findViewById(R.id.remove_two);
        removeThree = findViewById(R.id.remove_three);
        removeFour = findViewById(R.id.remove_four);
        removeFive = findViewById(R.id.remove_five);
        category = findViewById(R.id.category);
        subCategory = findViewById(R.id.sub_category);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        provinces = findViewById(R.id.provinces);
        cities = findViewById(R.id.cities);
        categorySubCategoryOne = findViewById(R.id.category_subcategory_one);
        categorySubCategoryTwo = findViewById(R.id.category_subcategory_two);
        categorySubCategoryThree = findViewById(R.id.category_subcategory_three);
        categorySubCategoryFour = findViewById(R.id.category_subcategory_four);
        categorySubCategoryFive = findViewById(R.id.category_subcategory_five);
        oneCategory = findViewById(R.id.one_category);
        twoCategory = findViewById(R.id.two_category);
        threeCategory = findViewById(R.id.three_category);
        fourCategory = findViewById(R.id.four_category);
        fiveCategory = findViewById(R.id.five_category);
        removeOneCategory = findViewById(R.id.remove_one_category);
        removeTwoCategory = findViewById(R.id.remove_two_category);
        removeThreeCategory = findViewById(R.id.remove_three_category);
        removeFourCategory = findViewById(R.id.remove_four_category);
        removeFiveCategory = findViewById(R.id.remove_five_category);

        sectionInfoAdvertisement.setVisibility(View.VISIBLE);


        backEditMyAdvertisement.setOnClickListener(this);
        cancelEdit.setOnClickListener(this);
        sendEdit.setOnClickListener(this);
        removeOne.setOnClickListener(this);
        removeTwo.setOnClickListener(this);
        removeThree.setOnClickListener(this);
        removeFour.setOnClickListener(this);
        removeFive.setOnClickListener(this);
        removeOneCategory.setOnClickListener(this);
        removeTwoCategory.setOnClickListener(this);
        removeThreeCategory.setOnClickListener(this);
        removeFourCategory.setOnClickListener(this);
        removeFiveCategory.setOnClickListener(this);

        if (Utilities.checkNetworkConnection(EditInfoMyAdvertisementActivity.this)) {
            setProgressBar();
            getListCategory();
            getListProvinces();
        } else {
            Toast.makeText(EditInfoMyAdvertisementActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }

    }

    private void initElements() {

        titleAddAdvertisement.setText(titleAdvertisement);

        for(int i = 0; i < citiesAdvertisement.size() ; i++)
        {
            switch (i)
            {
                case 0:
                    firstSelectScope = "one";
                    proCityOne.setText(citiesAdvertisement.get(i));
                    break;
                case 1 :
                    twoSelectScope = "two";
                    two.setVisibility(View.VISIBLE);
                    proCityTwo.setText(citiesAdvertisement.get(i));
                    break;
                case 2:
                    threeSelectScope = "three";
                    three.setVisibility(View.VISIBLE);
                    proCityThree.setText(citiesAdvertisement.get(i));
                    break;
                case 3:
                    fourSelectScope = "four";
                    four.setVisibility(View.VISIBLE);
                    proCityFour.setText(citiesAdvertisement.get(i));
                    break;
                case 4:
                    fiveSelectScope = "five";
                    canProvience = false;
                    five.setVisibility(View.VISIBLE);
                    proCityFive.setText(citiesAdvertisement.get(i));
                    break;
            }
        }

        for(int i = 0; i < categoriesAdvertisement.size() ; i++)
        {
            switch (i)
            {
                case 0:
                    firstSelectScopeCategory = "one";
                    categorySubCategoryOne.setText(categoriesAdvertisement.get(i));
                    break;
                case 1:
                    twoCategory.setVisibility(View.VISIBLE);
                    twoSelectScopeCategory = "two";
                    categorySubCategoryTwo.setText(categoriesAdvertisement.get(i));
                    break;
                case 2:
                    threeCategory.setVisibility(View.VISIBLE);
                    threeSelectScopeCategory = "three";
                    categorySubCategoryThree.setText(categoriesAdvertisement.get(i));
                    break;
                case 3:
                    fourSelectScopeCategory = "four";
                    fourCategory.setVisibility(View.VISIBLE);
                    categorySubCategoryFour.setText(categoriesAdvertisement.get(i));
                    break;
                case 4:
                    canChooseLimit = false;
                    fiveSelectScopeCategory = "five";
                    fiveCategory.setVisibility(View.VISIBLE);
                    categorySubCategoryFive.setText(categoriesAdvertisement.get(i));
                    break;
            }
        }


        ArrayList<String> listDurationAdvertisement = new ArrayList<>();
        listDurationAdvertisement.add("مهم نیست");
        listDurationAdvertisement.add(new ConvertToPersianNumber("10 ثانیه").convertToPersian());
        listDurationAdvertisement.add(new ConvertToPersianNumber("30 ثانیه").convertToPersian());
        listDurationAdvertisement.add(new ConvertToPersianNumber("1 دقیقه").convertToPersian());
        listDurationAdvertisement.add(new ConvertToPersianNumber("3 دقیقه").convertToPersian());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                EditInfoMyAdvertisementActivity.this, android.R.layout.simple_spinner_item, listDurationAdvertisement);

        durationAdvertisement.setAdapter(adapter);

        for(int i = 0 ; i < listDurationAdvertisement.size() ; i++)
        {
            if(listDurationAdvertisement.get(i).trim().equals(timeAdvertisement.trim()))
            {
                durationAdvertisement.setSelection(i);
            }
        }
    }

    private void getListProvinces() {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getProvinces().enqueue(new Callback<List<Provinces>>() {
            @Override
            public void onResponse(Call<List<Provinces>> call, Response<List<Provinces>> response) {
                listProvinces = new ArrayList<>();
                progressBar.dismiss();

                if (response.isSuccessful()) {

                    provincesForId = response.body();
                    listProvinces.add("انتخاب کنید");
                    for (Provinces category : response.body()) {
                        listProvinces.add(category.getTitle());
                    }
                    if (listProvinces.size() == 0) {
                        Toast.makeText(EditInfoMyAdvertisementActivity.this, "لیست دسته بندی ها خالی است!", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter(
                                EditInfoMyAdvertisementActivity.this, android.R.layout.simple_spinner_item, listProvinces);

                        provinces.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(EditInfoMyAdvertisementActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Provinces>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(EditInfoMyAdvertisementActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCities(String idProvince) {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getCities(idProvince).enqueue(new Callback<List<Cities>>() {
            @Override
            public void onResponse(Call<List<Cities>> call, Response<List<Cities>> response) {
                listCities = new ArrayList<>();

                progressBar.dismiss();
                if (response.isSuccessful()) {
                    listCities.add("انتخاب کنید");
                    for (Cities city : response.body()) {
                        listCities.add(city.getTitle());
                    }

                    if (listCities.size() == 0) {
                        Toast.makeText(EditInfoMyAdvertisementActivity.this, "لیست شهرها ها خالی هست!", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                EditInfoMyAdvertisementActivity.this, android.R.layout.simple_spinner_item, listCities);

                        cities.setAdapter(adapter);


                        cities.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cities>> call, Throwable t) {
                provinces.setSelection(0);
                progressBar.dismiss();
                Toast.makeText(EditInfoMyAdvertisementActivity.this, "مشکلی در ورود به جود آمده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getListCategory() {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getListCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                listCategory = new ArrayList<>();
                progressBar.dismiss();

                if (response.isSuccessful()) {

                    categoryForId = response.body();
                    listCategory.add("انتخاب کنید");
                    for (Category category : response.body()) {
                        listCategory.add(category.getTitle());
                    }
                    if (listCategory.size() == 0) {
                        Toast.makeText(EditInfoMyAdvertisementActivity.this, "لیست دسته بندی ها خالی است!", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                EditInfoMyAdvertisementActivity.this, android.R.layout.simple_spinner_item, listCategory);

                        category.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(EditInfoMyAdvertisementActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(EditInfoMyAdvertisementActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSubCategory(String idProvince) {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getSubCategory(idProvince).enqueue(new Callback<List<SubCategory>>() {
            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                listSubCategory = new ArrayList<>();

                progressBar.dismiss();
                if (response.isSuccessful()) {
                    listSubCategory.add("انتخاب کنید");
                    for (SubCategory city : response.body()) {
                        listSubCategory.add(city.getTitle());
                    }

                    if (listSubCategory.size() == 0) {
                        Toast.makeText(EditInfoMyAdvertisementActivity.this, "لیست شهرها ها خالی هست!", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter(
                                EditInfoMyAdvertisementActivity.this, android.R.layout.simple_spinner_item, listSubCategory);

                        subCategory.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                progressBar.dismiss();
                category.setSelection(0);
                Toast.makeText(EditInfoMyAdvertisementActivity.this, "مشکلی در ورود به جود آمده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void init() {

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(canChooseLimit)
                {
                    Toast.makeText(EditInfoMyAdvertisementActivity.this, "شما 5 انتخاب خود راانجام داده اید", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (position == 0) {
                        category.setSelection(0);

                        listSubCategory = new ArrayList<>();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                EditInfoMyAdvertisementActivity.this, android.R.layout.simple_spinner_item, listSubCategory);

                        subCategory.setAdapter(adapter);

                        return;
                    } else {
                        if (Utilities.checkNetworkConnection(EditInfoMyAdvertisementActivity.this)) {
                            setProgressBar();
                            category.setSelection(position);
                            getSubCategory(categoryForId.get(position - 1).getId());

                        } else {
                            category.setSelection(0);
                            Toast.makeText(EditInfoMyAdvertisementActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    return;
                } else {
                    if (firstSelectScopeCategory.equals("")) {
                        firstSelectScopeCategory = category.getSelectedItem() + " - " + subCategory.getSelectedItem();
                        categorySubCategoryOne.setText(firstSelectScopeCategory);
                        oneCategory.setVisibility(View.VISIBLE);
                    } else if (twoSelectScopeCategory.equals("")) {
                        twoCategory.setVisibility(View.VISIBLE);
                        twoSelectScopeCategory = category.getSelectedItem() + " - " + subCategory.getSelectedItem();
                        categorySubCategoryTwo.setText(twoSelectScopeCategory);
                    } else if (threeSelectScopeCategory.equals("")) {
                        threeCategory.setVisibility(View.VISIBLE);
                        threeSelectScopeCategory = category.getSelectedItem() + " - " + subCategory.getSelectedItem();
                        categorySubCategoryThree.setText(threeSelectScopeCategory);
                    } else if (fourSelectScopeCategory.equals("")) {
                        fourCategory.setVisibility(View.VISIBLE);
                        fourSelectScopeCategory = category.getSelectedItem() + " - " + subCategory.getSelectedItem();
                        categorySubCategoryFour.setText(fourSelectScopeCategory);
                    } else if (fiveSelectScopeCategory.equals("")) {
                        fiveCategory.setVisibility(View.VISIBLE);
                        fiveSelectScopeCategory = category.getSelectedItem() + " - " + subCategory.getSelectedItem();
                        categorySubCategoryFive.setText(fiveSelectScopeCategory);
                    } else {
                        Toast.makeText(EditInfoMyAdvertisementActivity.this, "شما فقط پنج انتخاب دارید!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        provinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    provinces.setSelection(0);
                    listCities = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditInfoMyAdvertisementActivity.this, android.R.layout.simple_spinner_item, listCities);

                    cities.setAdapter(adapter);
                    return;
                } else {
                    if (Utilities.checkNetworkConnection(EditInfoMyAdvertisementActivity.this)) {
                        setProgressBar();
                        provinces.setSelection(position);
                        getCities(provincesForId.get(position - 1).getId());
                    } else {
                        provinces.setSelection(0);
                        Toast.makeText(EditInfoMyAdvertisementActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                    return;
                } else {
                    if (firstSelectScope.equals("")) {
                        firstSelectScope = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityOne.setText(firstSelectScope);
                    } else if (twoSelectScope.equals("")) {
                        two.setVisibility(View.VISIBLE);
                        twoSelectScope = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityTwo.setText(twoSelectScope);
                    } else if (threeSelectScope.equals("")) {
                        three.setVisibility(View.VISIBLE);
                        threeSelectScope = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityThree.setText(threeSelectScope);
                    } else if (fourSelectScope.equals("")) {
                        four.setVisibility(View.VISIBLE);
                        fourSelectScope = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityFour.setText(fourSelectScope);
                    } else if (fiveSelectScope.equals("")) {
                        five.setVisibility(View.VISIBLE);
                        fiveSelectScope = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityFive.setText(fiveSelectScope);
                    } else {
                        Toast.makeText(EditInfoMyAdvertisementActivity.this, "شما فقط پنج انتخاب دارید!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.back_edit_my_advertisement:
            case R.id.cancel_edit:
                finish();
                break;
            case R.id.remove_one:
                proCityOne.setHint("استان  -  شهر");
                proCityOne.setText("");
                firstSelectScope = "";
                break;
            case R.id.remove_two:
                two.setVisibility(View.GONE);
                twoSelectScope = "";
                proCityTwo.setHint("استان  -  شهر");
                proCityTwo.setText("");
                break;
            case R.id.remove_three:
                three.setVisibility(View.GONE);
                threeSelectScope = "";
                proCityThree.setHint("استان  -  شهر");
                proCityThree.setText("");
                break;
            case R.id.remove_four:
                four.setVisibility(View.GONE);
                fourSelectScope = "";
                proCityFour.setHint("استان  -  شهر");
                proCityFour.setText("");
                break;
            case R.id.remove_five:
                five.setVisibility(View.GONE);
                fiveSelectScope = "";
                proCityFive.setHint("استان  -  شهر");
                proCityFive.setText("");
                break;
            case R.id.remove_one_category:
                firstSelectScopeCategory = "";
                categorySubCategoryOne.setHint("دسته بندی  -  زیر دسته بندی");
                categorySubCategoryOne.setText("");
                break;
            case R.id.remove_two_category:
                twoSelectScopeCategory = "";
                categorySubCategoryTwo.setHint("دسته بندی  -  زیر دسته بندی");
                categorySubCategoryTwo.setText("");
                twoCategory.setVisibility(View.GONE);
                break;
            case R.id.remove_three_category:
                threeCategory.setVisibility(View.GONE);
                threeSelectScopeCategory = "";
                categorySubCategoryThree.setHint("دسته بندی  -  زیر دسته بندی");
                categorySubCategoryThree.setText("");
                break;
            case R.id.remove_four_category:
                fourCategory.setVisibility(View.GONE);
                fourSelectScopeCategory = "";
                categorySubCategoryFour.setHint("دسته بندی  -  زیر دسته بندی");
                categorySubCategoryFour.setText("");
                break;
            case R.id.remove_five_category:
                fiveCategory.setVisibility(View.GONE);
                fiveSelectScopeCategory = "";
                categorySubCategoryFive.setHint("دسته بندی  -  زیر دسته بندی");
                categorySubCategoryFive.setText("");
                break;
            case R.id.send_edit:
                if(validateInput())
                {
                    //send to server
                }
                break;
        }
    }

 /*   private void setCities()
    {

        if(listCitiesUser.length == 1)
        {
            proCityOne.setText(listCitiesUser[0]);
        }
        else if(listCitiesUser.length == 2)
        {
            proCityOne.setText(listCitiesUser[0]);
            proCityTwo.setText(listCitiesUser[1]);
        }
        else if(listCitiesUser.length == 3)
        {
            proCityOne.setText(listCitiesUser[0]);
            proCityTwo.setText(listCitiesUser[1]);
            proCityThree.setText(listCitiesUser[2]);
        }
        else if(listCitiesUser.length == 4)
        {
            proCityOne.setText(listCitiesUser[0]);
            proCityTwo.setText(listCitiesUser[1]);
            proCityThree.setText(listCitiesUser[2]);
            proCityFour.setText(listCitiesUser[3]);
        }
        else if(listCitiesUser.length == 5)
        {
            proCityOne.setText(listCitiesUser[0]);
            proCityTwo.setText(listCitiesUser[1]);
            proCityThree.setText(listCitiesUser[2]);
            proCityFour.setText(listCitiesUser[3]);
            proCityFive.setText(listCitiesUser[4]);
        }

    }*/

    private boolean validateInput() {

        return true;
    }

    public void setProgressBar() {
        progressBar = new ProgressDialog(EditInfoMyAdvertisementActivity.this);
        progressBar.setCancelable(false);
        progressBar.setMessage("لطفا منتظر بمانید...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }
}
