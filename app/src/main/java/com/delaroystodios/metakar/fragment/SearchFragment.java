package com.delaroystodios.metakar.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.delaroystodios.metakar.Adapter.AdapterItemAdvertisementRecyclerView;
import com.delaroystodios.metakar.Model.Advertisement;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.network.ApiServices;
import com.delaroystodios.metakar.network.ApiUtils;
import com.delaroystodios.metakar.network.Utilities;
import com.delaroystodios.metakar.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment
{
    private SearchView searchKey;
    private ImageView  goToSearch , search_type;
    private RecyclerView recyclerView_advertisement;
    private ProgressBar prgLazyLoadingAdvertisementsfooter;
    private ArrayList<Advertisement> listitemes;
    private AdapterItemAdvertisementRecyclerView adapterItemAdvertisementRecyclerView;
    private View mView;
    private TextView type_search_choose;
    private String searchText;
    private String  searchType = "all";
    private Boolean isLoading = false;
    private int offset = 9;
    private int skip;
    private int TOTAL_PAGES = 50;
    private Boolean isLastPage = false;
    private RecyclerView recyclerView;
    private TextView errorConnectToInternet;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_search, container, false);


        skip = 0;

        initComponent();

        return mView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initComponent()
    {

        search_type = mView.findViewById(R.id.search_type);
        goToSearch =  mView.findViewById(R.id.go_to_search);
        searchKey =  mView.findViewById(R.id.search_key);
        type_search_choose = mView.findViewById(R.id.type_search_choose);
        errorConnectToInternet = mView.findViewById(R.id.errorConnectToInternet);
        searchKey.onActionViewExpanded();
        searchKey.setInputType(InputType.TYPE_CLASS_TEXT);
        searchKey.setQueryHint(getString(R.string.search_hint));

        recyclerView_advertisement =  mView.findViewById(R.id.recyclerView_advertisement);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_advertisement.setLayoutManager(linearLayoutManager);

        searchKey.setGravity(Gravity.RIGHT);
        searchKey.setTextDirection(View.TEXT_DIRECTION_RTL);
        searchKey.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        searchKey.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage =  searchKey.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setVisibility(View.GONE);


        prgLazyLoadingAdvertisementsfooter = mView.findViewById(R.id.prgLazyLoadingAdvertisementsfooter);
        recyclerView = mView.findViewById(R.id.recyclerView_advertisement);
        errorConnectToInternet = mView.findViewById(R.id.errorConnectToInternet);

        setupRecyclerView(recyclerView);

        goToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(searchKey);
                searchKey.setInputType(1);

                searchText = searchKey.getQuery().toString().trim();

                    if (Utilities.checkNetworkConnection(getActivity())) {
                        adapterItemAdvertisementRecyclerView.clear();
                        getSearchResult(0, 9, searchType, searchText);
                    }
                    else {
                        Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
            }
        });

        searchKey.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                hideSoftKeyboard(searchKey);
                searchKey.setInputType(1);

                searchText = searchKey.getQuery().toString().trim();

                if (Utilities.checkNetworkConnection(getActivity())) {
                    adapterItemAdvertisementRecyclerView.clear();
                    getSearchResult(0, 9, searchType, searchText);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            public boolean onQueryTextChange(String newText)
            {
                return true;
            }

        });

        search_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChooseTypeSearchInAdvertisments();
            }
        });
    }

    private void setChooseTypeSearchInAdvertisments()
    {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.typeSearch)
                .titleGravity(GravityEnum.END)
                .itemsGravity(GravityEnum.END)
                .items(R.array.typeSearch)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        type_search_choose.setVisibility(View.VISIBLE);
                        switch (which){
                            case 0:
                                searchType = "all";
                                type_search_choose.setText("همه");
                                break;
                            case 1:
                                searchType = "text";
                                type_search_choose.setText("متنی");
                                break;
                            case 2:
                                type_search_choose.setText("تیزر");
                                searchType = "tizer";
                                break;
                            case 3:
                                type_search_choose.setText("بازدیدی");
                                searchType = "visit";
                                break;
                            case 4:
                                type_search_choose.setText("اجتماعی");
                                searchType = "social";
                                break;
                        }
                    }
                })
                .show();

    }

    private void setupRecyclerView(RecyclerView recyclerView)
    {

        listitemes = new ArrayList<>();
        adapterItemAdvertisementRecyclerView = new AdapterItemAdvertisementRecyclerView(getActivity(), listitemes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterItemAdvertisementRecyclerView);
        adapterItemAdvertisementRecyclerView.notifyDataSetChanged();

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager , getContext()) {
            @Override
            protected void loadMoreItems() {

                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    isLoading = true;

                    errorConnectToInternet.setVisibility(View.GONE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            skip = skip + offset;

                            if (Utilities.checkNetworkConnection(getActivity())) {
                                getSearchResult(skip, offset , searchType , searchText);
                            } else {
                                Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }, 300);

                }else{
                    errorConnectToInternet.setVisibility(View.VISIBLE);
                    prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                }
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void getSearchResult(final int skip,final int offset , final String searchType ,final String searchKey){
        ApiServices mAPIService = ApiUtils.getAPIService();


        mAPIService.getAdvertisementSearch(skip , offset , searchType , searchKey).enqueue(new Callback<List<Advertisement>>() {
            @Override
            public void onResponse(Call<List<Advertisement>> call, Response<List<Advertisement>> response) {
                prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                listitemes = new ArrayList<>();
                if (response.isSuccessful()) {
                    for (Advertisement advertisement: response.body()) {
                        listitemes.add(advertisement);
                    }

                    if (listitemes.size() == 0) {

                        Toast.makeText(getActivity(), "موردی بیشتری یافت نشد!!!", Toast.LENGTH_LONG).show();
                    } else {
                        adapterItemAdvertisementRecyclerView.addAll(listitemes);
                        isLoading = false;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Advertisement>> call, Throwable t) {
                prgLazyLoadingAdvertisementsfooter.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.network_error, Toast.LENGTH_SHORT).show();

            }
        });
    }

  /*  private void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(new View(getActivity()).getWindowToken(), 0);

    }*/



    protected void hideSoftKeyboard(SearchView input) {
        input.setInputType(0);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }


}
