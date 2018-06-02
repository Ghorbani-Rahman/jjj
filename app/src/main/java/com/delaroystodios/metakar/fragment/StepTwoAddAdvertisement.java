package com.delaroystodios.metakar.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.delaroystodios.metakar.Activities.FullscreenVideoActivity;
import com.delaroystodios.metakar.Activities.MapActivity;
import com.delaroystodios.metakar.Activities.VideoControllerView;
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

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;

public class StepTwoAddAdvertisement extends Fragment implements View.OnClickListener {
    final int MY_REQUEST_CODE = 1;
    final int MY_REQUEST_CODE_MAP = 654;
    private static final int IMG_REQUEST_ONE = 111;
    private static final int IMG_REQUEST_TWO = 222;
    private static final int IMG_REQUEST_THREE = 333;
    private static final int IMG_REQUEST_FOUR = 444;
    private static final int IMG_REQUEST_FIVE = 555;
    private static final int IMG_REQUEST_SIX = 666;
    private static final int IMG_REQUEST_INDEX = 777;
    private static final int IMG_REQUEST_VIDEO = 888;
    public static String addressAdvertisementMap = "";
    private FragmentTransaction fragmentTransaction;
    public static double Lat;
    public static double Lng;
    private View view;
    private TextView addMapForAdvertisement, showMapAdvertisement;
    private Bitmap bitmap;
    private ImageView backStepTwoAddAdvertisement;
    private TextView stepPrev;
    private Spinner category, subCategory;
    private ArrayList<String> listCategory;
    private List<Category> categoryForId;
    private ArrayList<String> listSubCategory;
    private ProgressDialog progressBar;
    private ArrayList<String> listProvinces;
    private List<Provinces> provincesForId;
    private ArrayList<String> listCities;
    private LinearLayout one, two, three, four, five, oneCategory, twoCategory, threeCategory, fourCategory, fiveCategory;
    private ImageView removeOne, removeTwo, removeThree, removeFour, removeFive;
    private Spinner provinces, cities;
    private String firstSelectScope = "", twoSelectScope = "", threeSelectScope = "", fourSelectScope = "", fiveSelectScope = "";
    private String firstSelectScopeCategory = "", twoSelectScopeCategory = "", threeSelectScopeCategory = "", fourSelectScopeCategory = "", fiveSelectScopeCategory = "";
    private TextView proCityOne, proCityTwo, proCityThree, proCityFour, proCityFive,
            categorySubCategoryOne, categorySubCategoryTwo, categorySubCategoryThree, categorySubCategoryFour, categorySubCategoryFive;
    private ImageView removeOneCategory, removeTwoCategory, removeThreeCategory, removeFourCategory, removeFiveCategory;
    private ImageView btnAddContact, removeTwoContact, removeThreeContact, removeFourContact, removeFiveContact;
    private EditText oneContact, twoContact, threeContact, fourContact, fiveContact;
    private LinearLayout twoContactBody, threeContactBody, fourContactBody, fiveContactBody;
    private ImageView addImageAddAdvertisement, removeSelectedPhotoOne, photoSelectedOneUser, removeSelectedPhotoTwo, photoSelectedTwoUser, removeSelectedPhotoThree, photoSelectedThreeUser, removeSelectedPhotoFour, photoSelectedFourUser, removeSelectedPhotoFive, photoSelectedFiveUser, removeSelectedPhotoSix, photoSelectedSixUser;
    private RelativeLayout selectPhotoOne, selectPhotoTwo, selectPhotoThree, selectPhotoFour, selectPhotoFive, selectPhotoSix;
    private LinearLayout categoryOneImageBody, categoryTwoImageBody, categoryThreeImageBody;
    private TextView placeHolderPhotoOne, placeHolderPhotoTwo, placeHolderPhotoThree, placeHolderPhotoFour, placeHolderPhotoFive, placeHolderPhotoSix;
    private EditText titlePhotoOne, titlePhotoTwo, titlePhotoThree, titlePhotoFour, titlePhotoFive, titlePhotoSix;
    private TextView addIndexPhotoForAdvertisement;
    private ImageView photoIndexSelected;
    private boolean checkEnable = false;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private TextView stepNextAdd;
    private EditText titleAddAdvertisement, emailAdvertisement, addressAdvertisement;
    private TextView addVideoForAdvertisement, addressVideoForAdvertisement;
    private TextView seeVideoSelected;
    private CardView videoTizerBody;
    private String typeAddAdvertisement;
    private Uri selectedVideo;

    @Override
    public void onResume() {
        super.onResume();

        if (addressAdvertisementMap.length() > 5) {
            showMapAdvertisement.setVisibility(View.VISIBLE);
            showMapAdvertisement.setText(addressAdvertisementMap);
            addMapForAdvertisement.setText("حذف");
        }

        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = false;
        network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }


        if (gps_enabled && network_enabled) {
            checkEnable = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_step_two_add_advertisment, container, false);

        initComponent();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            typeAddAdvertisement = bundle.getString("typeAddAdvertisement", "");

            if (typeAddAdvertisement.equals("tizer")) {
                initComponentVideoTizer();
            }
        }

        return view;
    }

    private void initComponentVideoTizer() {
        addVideoForAdvertisement = view.findViewById(R.id.add_video_for_advertisement);
        seeVideoSelected = view.findViewById(R.id.see_video_selected);
        addressVideoForAdvertisement = view.findViewById(R.id.address_video_for_advertisement);
        videoTizerBody = view.findViewById(R.id.video_tizer_body);

        seeVideoSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FullscreenVideoActivity.class);
                intent.putExtra("link_video", selectedVideo.toString());
                startActivity(intent);
            }
        });


        videoTizerBody.setVisibility(View.VISIBLE);
        addVideoForAdvertisement.setOnClickListener(this);

    }

    private void initComponent() {
        backStepTwoAddAdvertisement = view.findViewById(R.id.back_step_two_add_advertisement);
        stepPrev = view.findViewById(R.id.step_prev);
        category = view.findViewById(R.id.category);
        subCategory = view.findViewById(R.id.sub_category);
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);
        removeOne = view.findViewById(R.id.remove_one);
        removeTwo = view.findViewById(R.id.remove_two);
        removeThree = view.findViewById(R.id.remove_three);
        removeFour = view.findViewById(R.id.remove_four);
        removeFive = view.findViewById(R.id.remove_five);
        oneCategory = view.findViewById(R.id.one_category);
        twoCategory = view.findViewById(R.id.two_category);
        threeCategory = view.findViewById(R.id.three_category);
        fourCategory = view.findViewById(R.id.four_category);
        fiveCategory = view.findViewById(R.id.five_category);
        removeOneCategory = view.findViewById(R.id.remove_one_category);
        removeTwoCategory = view.findViewById(R.id.remove_two_category);
        removeThreeCategory = view.findViewById(R.id.remove_three_category);
        removeFourCategory = view.findViewById(R.id.remove_four_category);
        removeFiveCategory = view.findViewById(R.id.remove_five_category);
        categorySubCategoryOne = view.findViewById(R.id.category_subcategory_one);
        categorySubCategoryTwo = view.findViewById(R.id.category_subcategory_two);
        categorySubCategoryThree = view.findViewById(R.id.category_subcategory_three);
        categorySubCategoryFour = view.findViewById(R.id.category_subcategory_four);
        categorySubCategoryFive = view.findViewById(R.id.category_subcategory_five);
        provinces = view.findViewById(R.id.provinces);
        cities = view.findViewById(R.id.cities);
        proCityOne = view.findViewById(R.id.pro_city_one);
        proCityTwo = view.findViewById(R.id.pro_citiy_two);
        proCityThree = view.findViewById(R.id.pro_citiy_three);
        proCityFour = view.findViewById(R.id.pro_citiy_four);
        proCityFive = view.findViewById(R.id.pro_citiy_five);
        removeSelectedPhotoOne = view.findViewById(R.id.remove_selected_photo_one);
        removeSelectedPhotoTwo = view.findViewById(R.id.remove_selected_photo_two);
        removeSelectedPhotoThree = view.findViewById(R.id.remove_selected_photo_three);
        removeSelectedPhotoFour = view.findViewById(R.id.remove_selected_photo_four);
        removeSelectedPhotoFive = view.findViewById(R.id.remove_selected_photo_five);
        removeSelectedPhotoSix = view.findViewById(R.id.remove_selected_photo_six);
        btnAddContact = view.findViewById(R.id.btn_add_contact);
        removeTwoContact = view.findViewById(R.id.remove_two_contact);
        removeThreeContact = view.findViewById(R.id.remove_three_contact);
        removeFourContact = view.findViewById(R.id.remove_four_contact);
        removeFiveContact = view.findViewById(R.id.remove_five_contact);
        oneContact = view.findViewById(R.id.one_contact);
        twoContact = view.findViewById(R.id.two_contact);
        threeContact = view.findViewById(R.id.three_contact);
        fourContact = view.findViewById(R.id.four_contact);
        fiveContact = view.findViewById(R.id.five_contact);
        twoContactBody = view.findViewById(R.id.two_contact_body);
        threeContactBody = view.findViewById(R.id.three_contact_body);
        fourContactBody = view.findViewById(R.id.four_contact_body);
        fiveContactBody = view.findViewById(R.id.five_contact_body);
        addImageAddAdvertisement = view.findViewById(R.id.add_image_add_advertisement);
        categoryOneImageBody = view.findViewById(R.id.category_one_image_body);
        categoryTwoImageBody = view.findViewById(R.id.category_two_image_body);
        categoryThreeImageBody = view.findViewById(R.id.category_three_image_body);
        removeSelectedPhotoOne = view.findViewById(R.id.remove_selected_photo_one);
        selectPhotoOne = view.findViewById(R.id.select_photo_one);
        placeHolderPhotoOne = view.findViewById(R.id.placeholder_photo_one);
        titlePhotoOne = view.findViewById(R.id.title_photo_one);
        selectPhotoTwo = view.findViewById(R.id.select_photo_two);
        placeHolderPhotoTwo = view.findViewById(R.id.placeholder_photo_two);
        titlePhotoTwo = view.findViewById(R.id.title_photo_two);
        removeSelectedPhotoTwo = view.findViewById(R.id.remove_selected_photo_two);
        selectPhotoThree = view.findViewById(R.id.select_photo_three);
        placeHolderPhotoThree = view.findViewById(R.id.placeholder_photo_three);
        titlePhotoThree = view.findViewById(R.id.title_photo_three);
        removeSelectedPhotoThree = view.findViewById(R.id.remove_selected_photo_three);
        selectPhotoFour = view.findViewById(R.id.select_photo_four);
        placeHolderPhotoFour = view.findViewById(R.id.placeholder_photo_four);
        titlePhotoFour = view.findViewById(R.id.title_photo_four);
        removeSelectedPhotoFour = view.findViewById(R.id.remove_selected_photo_four);
        removeSelectedPhotoFive = view.findViewById(R.id.remove_selected_photo_five);
        selectPhotoFive = view.findViewById(R.id.select_photo_five);
        placeHolderPhotoFive = view.findViewById(R.id.placeholder_photo_five);
        titlePhotoFive = view.findViewById(R.id.title_photo_five);
        removeSelectedPhotoSix = view.findViewById(R.id.remove_selected_photo_six);
        selectPhotoSix = view.findViewById(R.id.select_photo_six);
        placeHolderPhotoSix = view.findViewById(R.id.placeholder_photo_six);
        titlePhotoSix = view.findViewById(R.id.title_photo_six);

        photoSelectedOneUser = view.findViewById(R.id.photo_selesced_one_user);
        photoSelectedTwoUser = view.findViewById(R.id.photo_selesced_two_user);
        photoSelectedThreeUser = view.findViewById(R.id.photo_selesced_three_user);
        photoSelectedFiveUser = view.findViewById(R.id.photo_selesced_five_user);
        photoSelectedFourUser = view.findViewById(R.id.photo_selesced_four_user);
        photoSelectedSixUser = view.findViewById(R.id.photo_selesced_six_user);

        addIndexPhotoForAdvertisement = view.findViewById(R.id.add_index_photo_for_advertisement);
        photoIndexSelected = view.findViewById(R.id.photo_index_selected);

        addMapForAdvertisement = view.findViewById(R.id.add_map_for_advertisement);
        showMapAdvertisement = view.findViewById(R.id.show_map_advertisement);
        stepNextAdd = view.findViewById(R.id.step_next_add);
        titleAddAdvertisement = view.findViewById(R.id.title_add_advertisement);
        emailAdvertisement = view.findViewById(R.id.email_advertisement);
        addressAdvertisement = view.findViewById(R.id.address_advertisement);
        titlePhotoOne = view.findViewById(R.id.title_photo_one);
        titlePhotoTwo = view.findViewById(R.id.title_photo_two);
        titlePhotoThree = view.findViewById(R.id.title_photo_three);
        titlePhotoFour = view.findViewById(R.id.title_photo_four);
        titlePhotoFive = view.findViewById(R.id.title_photo_five);
        titlePhotoSix = view.findViewById(R.id.title_photo_six);

        backStepTwoAddAdvertisement.setOnClickListener(this);
        stepPrev.setOnClickListener(this);
        removeOne.setOnClickListener(this);
        removeTwo.setOnClickListener(this);
        removeThree.setOnClickListener(this);
        removeFour.setOnClickListener(this);
        removeFive.setOnClickListener(this);
        btnAddContact.setOnClickListener(this);
        removeTwoContact.setOnClickListener(this);
        removeThreeContact.setOnClickListener(this);
        removeFourContact.setOnClickListener(this);
        removeFiveContact.setOnClickListener(this);
        addImageAddAdvertisement.setOnClickListener(this);
        removeSelectedPhotoOne.setOnClickListener(this);
        selectPhotoOne.setOnClickListener(this);
        removeSelectedPhotoTwo.setOnClickListener(this);
        selectPhotoTwo.setOnClickListener(this);
        removeSelectedPhotoThree.setOnClickListener(this);
        selectPhotoThree.setOnClickListener(this);
        removeSelectedPhotoFour.setOnClickListener(this);
        selectPhotoFour.setOnClickListener(this);
        removeSelectedPhotoFive.setOnClickListener(this);
        selectPhotoFive.setOnClickListener(this);
        selectPhotoSix.setOnClickListener(this);
        removeSelectedPhotoSix.setOnClickListener(this);
        addIndexPhotoForAdvertisement.setOnClickListener(this);
        addMapForAdvertisement.setOnClickListener(this);
        stepNextAdd.setOnClickListener(this);
        removeOneCategory.setOnClickListener(this);
        removeTwoCategory.setOnClickListener(this);
        removeThreeCategory.setOnClickListener(this);
        removeFourCategory.setOnClickListener(this);
        removeFiveCategory.setOnClickListener(this);


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
                    ArrayAdapter<String> adapter = new ArrayAdapter(
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
                    if (firstSelectScope.equals("")) {
                        firstSelectScope = provinces.getSelectedItem() + " - " + cities.getSelectedItem();
                        proCityOne.setText(firstSelectScope);
                        one.setVisibility(View.VISIBLE);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_step_two_add_advertisement:
            case R.id.step_prev:
                getActivity().onBackPressed();
                break;
            case R.id.remove_one:
                firstSelectScope = "";
                proCityOne.setHint("استان  -  شهر");
                proCityOne.setText("");
                break;
            case R.id.remove_two:
                twoSelectScope = "";
                proCityTwo.setHint("استان  -  شهر");
                proCityTwo.setText("");
                two.setVisibility(View.GONE);
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
            case R.id.btn_add_contact:
                showAddContact();
                break;
            case R.id.remove_two_contact:
                twoContactBody.setVisibility(View.GONE);
                break;
            case R.id.remove_three_contact:
                threeContactBody.setVisibility(View.GONE);
                break;
            case R.id.remove_four_contact:
                fourContactBody.setVisibility(View.GONE);
                break;
            case R.id.remove_five_contact:
                fiveContactBody.setVisibility(View.GONE);
                break;
            case R.id.add_image_add_advertisement:
                showImageBodyForSelectImage();
                break;
            case R.id.remove_selected_photo_one:
                if (selectPhotoOne.getVisibility() == View.VISIBLE) {
                    photoSelectedOneUser.setVisibility(View.GONE);
                    placeHolderPhotoOne.setText("انتخاب...");
                }
                break;
            case R.id.remove_selected_photo_two:
                if (selectPhotoTwo.getVisibility() == View.VISIBLE) {
                    photoSelectedTwoUser.setVisibility(View.GONE);
                    placeHolderPhotoTwo.setText("انتخاب...");

                }
                break;
            case R.id.remove_selected_photo_three:
                if (selectPhotoThree.getVisibility() == View.VISIBLE) {
                    photoSelectedThreeUser.setVisibility(View.GONE);
                    placeHolderPhotoThree.setText("انتخاب...");
                }
                break;
            case R.id.remove_selected_photo_four:
                if (selectPhotoFour.getVisibility() == View.VISIBLE) {
                    photoSelectedFourUser.setVisibility(View.GONE);
                    placeHolderPhotoFour.setText("انتخاب...");
                }
                break;
            case R.id.remove_selected_photo_five:
                if (selectPhotoFive.getVisibility() == View.VISIBLE) {
                    photoSelectedFiveUser.setVisibility(View.GONE);
                    placeHolderPhotoFive.setText("انتخاب...");
                }
                break;
            case R.id.remove_selected_photo_six:
                if (selectPhotoSix.getVisibility() == View.VISIBLE) {
                    photoSelectedSixUser.setVisibility(View.GONE);
                    placeHolderPhotoSix.setText("انتخاب...");
                }
                break;
            case R.id.select_photo_one:
                if (checkPermission()) {
                    selectImage(IMG_REQUEST_ONE);
                } else {
                    requestPermission();
                }
                break;
            case R.id.select_photo_two:
                if (checkPermission()) {
                    selectImage(IMG_REQUEST_TWO);
                } else {
                    requestPermission();
                }
                break;
            case R.id.select_photo_three:
                if (checkPermission()) {
                    selectImage(IMG_REQUEST_THREE);
                } else {
                    requestPermission();
                }
                break;
            case R.id.select_photo_four:
                if (checkPermission()) {
                    selectImage(IMG_REQUEST_FOUR);
                } else {
                    requestPermission();
                }
                break;
            case R.id.select_photo_five:
                if (checkPermission()) {
                    selectImage(IMG_REQUEST_FIVE);
                } else {
                    requestPermission();
                }
                break;
            case R.id.select_photo_six:
                if (checkPermission()) {
                    selectImage(IMG_REQUEST_SIX);
                } else {
                    requestPermission();
                }
                break;
            case R.id.add_index_photo_for_advertisement:
                selectImage(IMG_REQUEST_INDEX);
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
            case R.id.add_video_for_advertisement:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, IMG_REQUEST_VIDEO);
                break;
            case R.id.add_map_for_advertisement:

                if (checkPermissionMap()) {
                    getLocationDevice();
                    if (checkEnable) {
                        if (addMapForAdvertisement.getText().equals("افزودن")) {
                            Intent intent = new Intent(getActivity(), MapActivity.class);
                            startActivity(intent);
                        } else if (addMapForAdvertisement.getText().equals("حذف")) {
                            addMapForAdvertisement.setText("افزودن");
                            addressAdvertisementMap = "";
                            showMapAdvertisement.setText("");
                            showMapAdvertisement.setVisibility(View.GONE);
                        }
                    }

                } else {
                    requestPermissionMap();
                }
                break;
            case R.id.step_next_add:
                if (validateInputDataInStepTwoAdvertisement()) {

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
        String descriptionAdvertisement = titleAddAdvertisement.getText().toString().trim();
        String categorySelect = category.getSelectedItem().toString().trim();

        String subCategorySelect = null;
        if (!category.getSelectedItem().equals("انتخاب کنید")) {
            subCategorySelect = subCategory.toString().trim();
        }


        String provincesSelect = provinces.getSelectedItem().toString().trim();
        String citiesSelect = null;
        if (!provincesSelect.equals("انتخاب کنید")) {
            citiesSelect = cities.getSelectedItem().toString().trim();
        }

        String oneContactAdvertisement = oneContact.getText().toString().trim();
        String emailAdvertisementUser = emailAdvertisement.getText().toString();
        String addressAdvertisementUser = addressAdvertisement.getText().toString().trim();


        if (descriptionAdvertisement.length() == 0) {
            titleAddAdvertisement.setError("شرح آگهی نمی تواند خالی باشد!");
            errorCount++;
        }

        if (categorySelect.equals("انتخاب کنید")) {
            Toast.makeText(getActivity(), "شما دسته بندی آگهی را انتخاب نکردید", Toast.LENGTH_SHORT).show();
            errorCount++;
        } else if (subCategorySelect.equals("انتخاب کنید")) {
            Toast.makeText(getActivity(), "شما زیر دسته بندی آگهی را انتخاب نکردید", Toast.LENGTH_SHORT).show();
            errorCount++;
        }

        if (provincesSelect.equals("انتخاب کنید")) {
            Toast.makeText(getActivity(), "شما استان محدوده فعالیت خود را انتخاب نکردید!", Toast.LENGTH_SHORT).show();
            errorCount++;
        } else if (citiesSelect.equals("انتخاب کنید")) {
            Toast.makeText(getActivity(), "شما شهر استان  محدوده فعالیت خود را انتخاب نکردید!", Toast.LENGTH_SHORT).show();
            errorCount++;
        }

        if (oneContactAdvertisement.length() == 0) {
            oneContact.setError("شماره تلفن نمی تواند خالی باشد!");
            errorCount++;
        }

        if (!isEmailValid(emailAdvertisementUser)) {
            emailAdvertisement.setError("ایمیل وارد شده معتبر نیست!");
            errorCount++;
        }

        if (addressAdvertisementUser.length() == 0) {
            addressAdvertisement.setError("آدرس نمی تواند خالی باشد!");
            errorCount++;
        } else if (addressAdvertisementUser.length() == 0) {
            addressAdvertisement.setError("آدرس نمی تواند کمتر از 10 کاراکتر باشد!");
            errorCount++;
        }

        if (photoIndexSelected.getVisibility() == View.GONE) {
            Toast.makeText(getActivity(), "شما تصویر شاخص برای آگهی انتخاب نکردید!", Toast.LENGTH_SHORT).show();
            errorCount++;
        }

        if (typeAddAdvertisement.equals("tizer")) {
            if (addressVideoForAdvertisement.getText().toString().trim().length() == 0) {
                Toast.makeText(getActivity(), "شما برای آگهی ویدیو انتخاب نکردید", Toast.LENGTH_SHORT).show();
                errorCount++;
            }
        }


        if (errorCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void getLocationDevice() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setMessage("تنظیمات مکان یابی شما غیرفعال است!");
            dialog.setPositiveButton("فعال کردن", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);

                }
            });
            dialog.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    checkEnable = false;

                }
            });
            dialog.show();


        }
    }

    private void selectImage(final int witchSelectImage) {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        switch (witchSelectImage) {
            case 111:
                startActivityForResult(intent, IMG_REQUEST_ONE);
                break;
            case 222:
                startActivityForResult(intent, IMG_REQUEST_TWO);
                break;
            case 333:
                startActivityForResult(intent, IMG_REQUEST_THREE);
                break;
            case 444:
                startActivityForResult(intent, IMG_REQUEST_FOUR);
                break;
            case 555:
                startActivityForResult(intent, IMG_REQUEST_FIVE);
                break;
            case 666:
                startActivityForResult(intent, IMG_REQUEST_SIX);
                break;
            case 777:
                startActivityForResult(intent, IMG_REQUEST_INDEX);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST_ONE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoSelectedOneUser.setImageBitmap(bitmap);
                photoSelectedOneUser.setVisibility(View.VISIBLE);
                placeHolderPhotoOne.setText("تعویض...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMG_REQUEST_TWO && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoSelectedTwoUser.setImageBitmap(bitmap);
                photoSelectedTwoUser.setVisibility(View.VISIBLE);
                placeHolderPhotoTwo.setText("تعویض...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMG_REQUEST_THREE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoSelectedThreeUser.setImageBitmap(bitmap);
                photoSelectedThreeUser.setVisibility(View.VISIBLE);
                placeHolderPhotoThree.setText("تعویض...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMG_REQUEST_FOUR && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoSelectedFourUser.setImageBitmap(bitmap);
                photoSelectedFourUser.setVisibility(View.VISIBLE);
                placeHolderPhotoFour.setText("تعویض...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMG_REQUEST_FIVE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoSelectedFiveUser.setImageBitmap(bitmap);
                photoSelectedFiveUser.setVisibility(View.VISIBLE);
                placeHolderPhotoFive.setText("تعویض...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMG_REQUEST_SIX && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoSelectedSixUser.setImageBitmap(bitmap);
                photoSelectedSixUser.setVisibility(View.VISIBLE);
                placeHolderPhotoSix.setText("تعویض...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMG_REQUEST_INDEX && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                photoIndexSelected.setImageBitmap(bitmap);
                photoIndexSelected.setVisibility(View.VISIBLE);
                addIndexPhotoForAdvertisement.setText("تعویض");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMG_REQUEST_VIDEO && resultCode == RESULT_OK && null != data) {

            selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            addressVideoForAdvertisement.setText(selectedVideo + "");

            //       MediaController vidControl = new MediaController(getActivity());
            //       vidControl.setPadding(140 , 0 , 140 , 146);
            //       vidControl.setAnchorView(videoSelected);
            addVideoForAdvertisement.setText("تعویض");

            //      videoSelected.setVideoURI(selectedVideo);
            addressVideoForAdvertisement.setVisibility(View.VISIBLE);
            seeVideoSelected.setVisibility(View.VISIBLE);
            //       videoSelected.setMediaController(vidControl);


            //       videoSelected.start();
            cursor.close();

        }
    }

    private void showAddContact() {
        if (twoContactBody.getVisibility() == View.GONE) {
            twoContactBody.setVisibility(View.VISIBLE);
        } else if (threeContactBody.getVisibility() == View.GONE) {
            threeContactBody.setVisibility(View.VISIBLE);
        } else if (fourContactBody.getVisibility() == View.GONE) {
            fourContactBody.setVisibility(View.VISIBLE);
        } else if (fiveContactBody.getVisibility() == View.GONE) {
            fiveContactBody.setVisibility(View.VISIBLE);
        }
    }

    private void showImageBodyForSelectImage() {
        if (categoryOneImageBody.getVisibility() == View.GONE) {
            categoryOneImageBody.setVisibility(View.VISIBLE);
        } else if (categoryTwoImageBody.getVisibility() == View.GONE) {
            categoryTwoImageBody.setVisibility(View.VISIBLE);
        } else if (categoryThreeImageBody.getVisibility() == View.GONE) {
            categoryThreeImageBody.setVisibility(View.VISIBLE);
        }
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
                        ArrayAdapter<String> adapter = new ArrayAdapter(
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
                        ArrayAdapter<String> adapter = new ArrayAdapter(
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
            public void onResponse(Call<List<Cities>> call, Response<List<Cities>> response) {
                listCities = new ArrayList<>();

                progressBar.dismiss();
                if (response.isSuccessful()) {
                    listCities.add("انتخاب کنید");
                    for (Cities city : response.body()) {
                        listCities.add(city.getTitle());
                    }

                    if (listCities.size() == 0) {
                        Toast.makeText(getActivity(), "لیست شهرها ها خالی هست!", Toast.LENGTH_SHORT).show();
                    } else {
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

    public boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getActivity(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MY_REQUEST_CODE);

    }

    public boolean checkPermissionMap() {
        return (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermissionMap() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, MY_REQUEST_CODE_MAP);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean cameraAccepted1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted1) {
                    //    selectImage();
                }
            }
        } else if (requestCode == MY_REQUEST_CODE_MAP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                boolean cameraAccepted1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted1 && cameraAccepted2) {

                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}