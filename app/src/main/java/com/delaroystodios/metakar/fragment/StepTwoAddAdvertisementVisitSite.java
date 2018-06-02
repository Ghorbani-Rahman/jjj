package com.delaroystodios.metakar.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class StepTwoAddAdvertisementVisitSite extends Fragment implements View.OnClickListener
{
    private View view;
    private ImageView backStepTwoAddAdvertisementVisitSite;
    private TextView stepPrevSite;
    private String typeAddAdvertisement;
    private Spinner category, subCategory;
    private ArrayList<String> listCategory;
    private List<Category> categoryForId;
    private ArrayList<String> listSubCategory;
    private ProgressDialog progressBar;
    private ArrayList<String> listProvinces;
    private List<Provinces> provincesForId;
    private ArrayList<String> listCities;
    private Spinner provinces, cities;
    private String firstSelectScopeActivity = "", twoSelectScopeActivity = "", threeSelectScopeActivity = "", fourSelectScopeActivity = "", fiveSelectScopeActivity = "";
    private String firstSelectScopeCategory = "", twoSelectScopeCategory = "", threeSelectScopeCategory = "", fourSelectScopeCategory = "", fiveSelectScopeCategory = "";
    private TextView proCityOne, proCityTwo, proCityThree, proCityFour, proCityFive , categorySubCategoryOne, categorySubCategoryTwo,
            categorySubCategoryThree, categorySubCategoryFour, categorySubCategoryFive;
    private LinearLayout one, two, three, four, five , oneCategory, twoCategory, threeCategory, fourCategory, fiveCategory;
    private ImageView removeOne, removeTwo, removeThree, removeFour, removeFive;
    private ImageView removeOneCategory, removeTwoCategory, removeThreeCategory, removeFourCategory, removeFiveCategory;
    private TextView addIndexPhotoForAdvertisement;
    private static final int IMG_REQUEST_INDEX = 777;
    private Bitmap bitmap;
    private ImageView photoIndexSelected;
    private EditText linkWebSiteVisitAdvertisement;
    private TextView stepNextAdd;
    private FragmentTransaction fragmentTransaction;
    private TextView titleTypeAdvertisement;



    public StepTwoAddAdvertisementVisitSite() {

        // Required empty public constructor
    }

    public static StepTwoAddAdvertisementVisitSite newInstance(String param1, String param2) {
        StepTwoAddAdvertisementVisitSite fragment = new StepTwoAddAdvertisementVisitSite();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_step_two_add_advertisment_visit_site, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            typeAddAdvertisement = bundle.getString("typeAddAdvertisement", "");

            initComponent();

            if(typeAddAdvertisement.equals("apart"))
            {
                titleTypeAdvertisement.setText("لینک صفحه فیلم آپارات");
                linkWebSiteVisitAdvertisement.setHint("لینک صفحه فیلم آپارات");

            }
            else if(typeAddAdvertisement.equals("social"))
            {
                titleTypeAdvertisement.setText("لینک شبکه اجتماعی");
                linkWebSiteVisitAdvertisement.setHint("لینک صفحه اجتماعی");
            }
        }

        return view;
    }

    private void initComponent()
    {
        titleTypeAdvertisement = (TextView)view.findViewById(R.id.title_type_advertisement);
        backStepTwoAddAdvertisementVisitSite = (ImageView)view.findViewById(R.id.back_step_two_add_advertisement_visit_site);
        stepPrevSite = (TextView)view.findViewById(R.id.step_prev_site);
        one = (LinearLayout) view.findViewById(R.id.one);
        two = (LinearLayout) view.findViewById(R.id.two);
        three = (LinearLayout) view.findViewById(R.id.three);
        four = (LinearLayout) view.findViewById(R.id.four);
        five = (LinearLayout) view.findViewById(R.id.five);
        category = (Spinner) view.findViewById(R.id.category);
        subCategory = (Spinner) view.findViewById(R.id.sub_category);
        one = (LinearLayout) view.findViewById(R.id.one);
        two = (LinearLayout) view.findViewById(R.id.two);
        three = (LinearLayout) view.findViewById(R.id.three);
        four = (LinearLayout) view.findViewById(R.id.four);
        five = (LinearLayout) view.findViewById(R.id.five);
        oneCategory = (LinearLayout) view.findViewById(R.id.one_category);
        twoCategory = (LinearLayout) view.findViewById(R.id.two_category);
        threeCategory = (LinearLayout) view.findViewById(R.id.three_category);
        fourCategory = (LinearLayout) view.findViewById(R.id.four_category);
        fiveCategory = (LinearLayout) view.findViewById(R.id.five_category);
        removeOne = (ImageView) view.findViewById(R.id.remove_one);
        removeTwo = (ImageView) view.findViewById(R.id.remove_two);
        removeThree = (ImageView) view.findViewById(R.id.remove_three);
        removeFour = (ImageView) view.findViewById(R.id.remove_four);
        removeFive = (ImageView) view.findViewById(R.id.remove_five);
        removeOneCategory = (ImageView) view.findViewById(R.id.remove_one_category);
        removeTwoCategory = (ImageView) view.findViewById(R.id.remove_two_category);
        removeThreeCategory = (ImageView) view.findViewById(R.id.remove_three_category);
        removeFourCategory = (ImageView) view.findViewById(R.id.remove_four_category);
        removeFiveCategory = (ImageView) view.findViewById(R.id.remove_five_category);
        provinces = (Spinner) view.findViewById(R.id.provinces);
        cities = (Spinner) view.findViewById(R.id.cities);
        proCityOne = (TextView) view.findViewById(R.id.pro_city_one);
        proCityTwo = (TextView) view.findViewById(R.id.pro_citiy_two);
        proCityThree = (TextView) view.findViewById(R.id.pro_citiy_three);
        proCityFour = (TextView) view.findViewById(R.id.pro_citiy_four);
        proCityFive = (TextView) view.findViewById(R.id.pro_citiy_five);
        categorySubCategoryOne = (TextView) view.findViewById(R.id.category_subcategory_one);
        categorySubCategoryTwo = (TextView) view.findViewById(R.id.category_subcategory_two);
        categorySubCategoryThree = (TextView) view.findViewById(R.id.category_subcategory_three);
        categorySubCategoryFour = (TextView) view.findViewById(R.id.category_subcategory_four);
        categorySubCategoryFive = (TextView) view.findViewById(R.id.category_subcategory_five);
        addIndexPhotoForAdvertisement = (TextView) view.findViewById(R.id.add_index_photo_for_advertisement);
        photoIndexSelected = (ImageView) view.findViewById(R.id.photo_index_selected);
        linkWebSiteVisitAdvertisement = (EditText)view.findViewById(R.id.link_web_site_visit_advertisement);
        stepNextAdd = (TextView) view.findViewById(R.id.step_next_add);

        backStepTwoAddAdvertisementVisitSite.setOnClickListener(this);
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
        addIndexPhotoForAdvertisement.setOnClickListener(this);
        stepNextAdd.setOnClickListener(this);
        stepPrevSite.setOnClickListener(this);



        if (Utilities.checkNetworkConnection(getActivity())) {
            setProgressBar();
            getListCategory();
            getListProvinces();
        } else {
            Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
        }


        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    category.setSelection(0);

                    listSubCategory = new ArrayList<>();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getActivity(), android.R.layout.simple_spinner_item, listSubCategory);

                    subCategory.setAdapter(adapter);

                    return;
                } else {
                    if (Utilities.checkNetworkConnection(getActivity())) {
                        setProgressBar();
                        category.setSelection(position);
                        getSubCategory(categoryForId.get(position - 1).getId());

                    } else {
                        category.setSelection(0);
                        Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
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

                if (position == 0)
                {

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
                        Toast.makeText(getActivity(), "شما فقط پنج انتخاب دارید!", Toast.LENGTH_SHORT).show();
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
                            getActivity(), android.R.layout.simple_spinner_item, listCities);

                    cities.setAdapter(adapter);
                    return;
                } else {
                    if (Utilities.checkNetworkConnection(getActivity())) {
                        setProgressBar();
                        provinces.setSelection(position);
                        getCities(provincesForId.get(position - 1).getId());
                    } else {
                        provinces.setSelection(0);
                        Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
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
                    if (firstSelectScopeActivity.equals("")) {
                        firstSelectScopeActivity = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityOne.setText(firstSelectScopeActivity);
                        one.setVisibility(View.VISIBLE);
                    } else if (twoSelectScopeActivity.equals("")) {
                        two.setVisibility(View.VISIBLE);
                        twoSelectScopeActivity = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityTwo.setText(twoSelectScopeActivity);
                    } else if (threeSelectScopeActivity.equals("")) {
                        three.setVisibility(View.VISIBLE);
                        threeSelectScopeActivity = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityThree.setText(threeSelectScopeActivity);
                    } else if (fourSelectScopeActivity.equals("")) {
                        four.setVisibility(View.VISIBLE);
                        fourSelectScopeActivity = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityFour.setText(fourSelectScopeActivity);
                    } else if (fiveSelectScopeActivity.equals("")) {
                        five.setVisibility(View.VISIBLE);
                        fiveSelectScopeActivity = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityFive.setText(fiveSelectScopeActivity);
                    } else {
                        Toast.makeText(getActivity(), "شما فقط پنج انتخاب دارید!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.back_step_two_add_advertisement_visit_site:
            case R.id.step_prev_site:
                getActivity().onBackPressed();
                break;
            case R.id.remove_one:
                firstSelectScopeActivity = "";
                proCityOne.setHint("استان  -  شهر");
                proCityOne.setText("");
                break;
            case R.id.remove_two:
                twoSelectScopeActivity = "";
                proCityTwo.setHint("استان  -  شهر");
                proCityTwo.setText("");
                two.setVisibility(View.GONE);
                break;
            case R.id.remove_three:
                three.setVisibility(View.GONE);
                threeSelectScopeActivity = "";
                proCityThree.setHint("استان  -  شهر");
                proCityThree.setText("");
                break;
            case R.id.remove_four:
                four.setVisibility(View.GONE);
                fourSelectScopeActivity = "";
                proCityFour.setHint("استان  -  شهر");
                proCityFour.setText("");
                break;
            case R.id.remove_five:
                five.setVisibility(View.GONE);
                fiveSelectScopeActivity = "";
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
            case R.id.add_index_photo_for_advertisement:
                selectImage(IMG_REQUEST_INDEX);
                break;
            case R.id.step_next_add:
                if(validateInputDataInStepTwoAdvertisement())
                {
                    FragmentManager fragmentManager5 = getFragmentManager();
                    fragmentTransaction = fragmentManager5.beginTransaction();
                    StepThreeAddAdvertisement stepThreeAddAdvertisement = new StepThreeAddAdvertisement();
                    Bundle bundle = new Bundle();
                    stepThreeAddAdvertisement.setArguments(bundle);
                    fragmentTransaction.add(R.id.contianer_add_advertisment, stepThreeAddAdvertisement, "StepThreeAddAdvertisement");
                    fragmentTransaction.addToBackStack("StepThreeAddAdvertisement");
                    fragmentTransaction.commit();
                }
                break;
        }
    }

    private boolean validateInputDataInStepTwoAdvertisement() {

        int errorCount = 0;
        String categorySelect = category.getSelectedItem().toString().trim();
        String urlSite = linkWebSiteVisitAdvertisement.getText().toString();

        String subCategorySelect = null;
        if (!category.getSelectedItem().equals("انتخاب کنید"))
        {
            subCategorySelect = subCategory.toString().trim();
        }

        String provincesSelect = provinces.getSelectedItem().toString().trim();
        String citiesSelect = null;
        if (!provincesSelect.equals("انتخاب کنید"))
        {
            citiesSelect = cities.getSelectedItem().toString().trim();
        }

        if(categorySelect.equals("انتخاب کنید"))
        {
            Toast.makeText(getActivity(), "شما دسته بندی آگهی را انتخاب نکردید", Toast.LENGTH_SHORT).show();
            errorCount++;
        }
        else if(subCategorySelect.equals("انتخاب کنید"))
        {
            Toast.makeText(getActivity(), "شما زیر دسته بندی آگهی را انتخاب نکردید", Toast.LENGTH_SHORT).show();
            errorCount++;
        }

        if(provincesSelect.equals("انتخاب کنید"))
        {
            Toast.makeText(getActivity(), "شما استان محدوده فعالیت خود را انتخاب نکردید!", Toast.LENGTH_SHORT).show();
            errorCount++;
        }
        else if(citiesSelect.equals("انتخاب کنید"))
        {
            Toast.makeText(getActivity(), "شما شهر استان  محدوده فعالیت خود را انتخاب نکردید!", Toast.LENGTH_SHORT).show();
            errorCount++;
        }

        if(photoIndexSelected.getVisibility() == View.GONE)
        {
            Toast.makeText(getActivity(), "شما تصویر شاخص برای آگهی انتخاب نکردید!", Toast.LENGTH_SHORT).show();
            errorCount++;
        }

        if(urlSite.length() == 0)
        {
            linkWebSiteVisitAdvertisement.setError("آدرس نمی تواند خالی باشد!");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == IMG_REQUEST_INDEX && resultCode == RESULT_OK && data != null)
        {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoIndexSelected.setImageBitmap(bitmap);
                photoIndexSelected.setVisibility(View.VISIBLE);
                addIndexPhotoForAdvertisement.setText("تعویض");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectImage(final int witchSelectImage)
    {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        switch (witchSelectImage)
        {
            case 777:
                startActivityForResult(intent , IMG_REQUEST_INDEX);
                break;
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
                        Toast.makeText(getActivity(), "لیست دسته بندی ها خالی است!", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_spinner_item, listProvinces);

                        provinces.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Provinces>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCities(String idProvince) {
        ApiServices mAPIService = ApiUtils.getAPIService();

        mAPIService.getCities(idProvince).enqueue(new Callback<List<Cities>>() {
            @Override
            public void onResponse(Call<List<Cities>> call, Response<List<Cities>> response)
            {
                listCities = new ArrayList<>();

                progressBar.dismiss();
                if (response.isSuccessful())
                {
                    listCities.add("انتخاب کنید");
                    for (Cities city : response.body())
                    {
                        listCities.add(city.getTitle());
                    }

                    if (listCities.size() == 0)
                    {
                        Toast.makeText(getActivity(), "لیست شهرها ها خالی هست!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_spinner_item, listCities);

                        cities.setAdapter(adapter);


                        cities.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cities>> call, Throwable t) {
                provinces.setSelection(0);
                progressBar.dismiss();
                Toast.makeText(getActivity(), "مشکلی در ورود به جود آمده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(getActivity(), "لیست دسته بندی ها خالی است!", Toast.LENGTH_LONG).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_spinner_item, listCategory);

                        category.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "لیست شهرها ها خالی هست!", Toast.LENGTH_SHORT).show();
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(), android.R.layout.simple_spinner_item, listSubCategory);

                        subCategory.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                progressBar.dismiss();
                category.setSelection(0);
                Toast.makeText(getActivity(), "مشکلی در ورود به جود آمده است لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setProgressBar() {
        progressBar = new ProgressDialog(getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage("لطفا منتظر بمانید...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }

}
