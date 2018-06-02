package com.delaroystodios.metakar.fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delaroystodios.metakar.R;

public class StepOneAddAdvertisement extends Fragment implements View.OnClickListener
{
    private View view;
    private ImageView backStepOneAddAdvertisment;
    private EditText titleAddAdvertisment;
    private LinearLayout visitAdvertisment;
    private LinearLayout visitSite;
    private LinearLayout visitTizer;
    private LinearLayout visitAparat;
    private LinearLayout visitSocialNetwork;
    private LinearLayout witchSelect;
    private String lastSelect = "";
    private TextView errorNoSelectTypeAdvertisment , stepNextAdd , noAdd;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_step_one_add_advertisment, container, false);


        initComponent();

        return view;
    }

    private void initComponent()
    {

        backStepOneAddAdvertisment = view.findViewById(R.id.back_step_one_add_advertisement);
        titleAddAdvertisment = view.findViewById(R.id.title_add_advertisment);
        visitAdvertisment = view.findViewById(R.id.visit_advertisement);
        visitSite = view.findViewById(R.id.visit_site);
        visitTizer = view.findViewById(R.id.visit_tizer);
        visitAparat = view.findViewById(R.id.visit_aparat);
        visitSocialNetwork = view.findViewById(R.id.visit_social_network);
        noAdd = view.findViewById(R.id.no_add);
        stepNextAdd = view.findViewById(R.id.step_next_add);
        errorNoSelectTypeAdvertisment = view.findViewById(R.id.error_no_select_type_advertisment);


        backStepOneAddAdvertisment.setOnClickListener(this);
        visitAdvertisment.setOnClickListener(this);
        visitTizer.setOnClickListener(this);
        visitSite.setOnClickListener(this);
        visitSocialNetwork.setOnClickListener(this);
        visitAparat.setOnClickListener(this);
        noAdd.setOnClickListener(this);
        stepNextAdd.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_step_one_add_advertisement:
                getActivity().finish();
                break;
            case R.id.no_add:
                getActivity().finish();
                break;
            case R.id.step_next_add:
                String title = titleAddAdvertisment.getText().toString().trim();
                if(validateStepOneAddAdvertisement(title ,lastSelect ))
                {
                    errorNoSelectTypeAdvertisment.setVisibility(View.GONE);

                    switch (lastSelect)
                    {
                        case "advertisement":
                        case "tizer":
                            FragmentManager advertisement = getFragmentManager();
                            fragmentTransaction = advertisement.beginTransaction();
                            StepTwoAddAdvertisement stepTwoAddAdvertisement = new StepTwoAddAdvertisement();
                            Bundle bundle = new Bundle();
                            bundle.putString("title" , title);
                            if(lastSelect.equals("advertisement"))
                            {
                                bundle.putString("typeAddAdvertisement", "advertisement");
                            }else
                            {
                                bundle.putString("typeAddAdvertisement" , "tizer");
                            }
                            stepTwoAddAdvertisement.setArguments(bundle);
                            fragmentTransaction.add(R.id.contianer_add_advertisment, stepTwoAddAdvertisement, "StepTwoAddAdvertisement");
                            fragmentTransaction.addToBackStack("StepTwoAddAdvertisement");
                            fragmentTransaction.commit();
                            break;
                        case "site":
                        case "apart":
                        case "social":
                            FragmentManager site = getFragmentManager();
                            fragmentTransaction = site.beginTransaction();
                            StepTwoAddAdvertisementVisitSite stepTwoAddAdvertisementVisitSite = new StepTwoAddAdvertisementVisitSite();
                            bundle = new Bundle();
                            bundle.putString("title" , title);
                            if(lastSelect.equals("site"))
                            {
                                bundle.putString("typeAddAdvertisement" , "site");
                            }
                            else if(lastSelect.equals("apart"))
                            {
                                bundle.putString("typeAddAdvertisement" , "apart");
                            }
                            else if(lastSelect.equals("social"))
                            {
                                bundle.putString("typeAddAdvertisement" , "social");
                            }
                            stepTwoAddAdvertisementVisitSite.setArguments(bundle);
                            fragmentTransaction.add(R.id.contianer_add_advertisment, stepTwoAddAdvertisementVisitSite, "stepTwoAddAdvertisementVisit");
                            fragmentTransaction.addToBackStack("stepTwoAddAdvertisementVisit");
                            fragmentTransaction.commit();
                            break;
                    }
                }
                break;
            case R.id.visit_advertisement:
                if(!lastSelect.equals(""))
                {
                    witchSelect.setBackground(getResources().getDrawable(R.drawable.background_select_type_advertisement));
                }
                visitAdvertisment.setBackgroundColor(getResources().getColor(R.color.orange));
                lastSelect = "advertisement";
                witchSelect = visitAdvertisment;
                break;
            case R.id.visit_site:
                if(!lastSelect.equals(""))
                {
                    witchSelect.setBackground(getResources().getDrawable(R.drawable.background_select_type_advertisement));
                }
                visitSite.setBackgroundColor(getResources().getColor(R.color.orange));
                lastSelect = "site";
                witchSelect = visitSite;
                break;
            case R.id.visit_aparat:
                if(!lastSelect.equals(""))
                {
                    witchSelect.setBackground(getResources().getDrawable(R.drawable.background_select_type_advertisement));
                }
                visitAparat.setBackgroundColor(getResources().getColor(R.color.orange));
                lastSelect = "apart";
                witchSelect = visitAparat;
                break;
            case R.id.visit_social_network:
                if(!lastSelect.equals(""))
                {
                    witchSelect.setBackground(getResources().getDrawable(R.drawable.background_select_type_advertisement));
                }
                visitSocialNetwork.setBackgroundColor(getResources().getColor(R.color.orange));
                lastSelect = "social";
                witchSelect = visitSocialNetwork;
                break;
            case R.id.visit_tizer:
                if(!lastSelect.equals(""))
                {
                    witchSelect.setBackground(getResources().getDrawable(R.drawable.background_select_type_advertisement));
                }
                visitTizer.setBackgroundColor(getResources().getColor(R.color.orange));
                lastSelect = "tizer";
                witchSelect = visitTizer;
                break;
        }

    }

    private boolean validateStepOneAddAdvertisement(String title, String lastSelect)
    {

        int errorCount = 0;
        errorNoSelectTypeAdvertisment.setVisibility(View.GONE);
        if(title == null || title.length() == 0)
        {
            titleAddAdvertisment.setError("عنوان آگهی نمی تواند خالی باشد!");
            errorCount++;
        }

        if(lastSelect.equals(""))
        {
            errorNoSelectTypeAdvertisment.setText("شما نوع آگهی را انتخاب نکردید!");
            errorNoSelectTypeAdvertisment.setVisibility(View.VISIBLE);
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
}
