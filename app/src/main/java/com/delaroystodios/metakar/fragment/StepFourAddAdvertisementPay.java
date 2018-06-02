package com.delaroystodios.metakar.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.delaroystodios.metakar.R;

public class StepFourAddAdvertisementPay extends Fragment
{
    private View view;

    public StepFourAddAdvertisementPay() {

        // Required empty public constructor
    }

    public static StepFourAddAdvertisementPay newInstance(String param1, String param2) {
        StepFourAddAdvertisementPay fragment = new StepFourAddAdvertisementPay();
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


        view = inflater.inflate(R.layout.fragment_step_four_add_advertisment, container, false);

        initComponent();

        return view;
    }

    private void initComponent()
    {

    }

}
