package com.delaroystodios.metakar.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.delaroystodios.metakar.Activities.RegisterActivity;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.User.EarningsActivity;
import com.delaroystodios.metakar.User.LoginActivity;
import com.delaroystodios.metakar.User.WithdrawsActivity;
import com.delaroystodios.metakar.network.Utilities;

import static android.content.Context.MODE_PRIVATE;


public class PanelUserNoLoginFragment extends Fragment implements View.OnClickListener
{

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_guest, null);

        initComponentGuest();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(checkLogin())
        {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.viewpager, new PanelUserLoginFragment())
                    .commit();
        }
    }

    private void initComponentGuest() {

        CardView goToLogin = view.findViewById(R.id.goToLogin);
        CardView goToRegister = view.findViewById(R.id.goToRegister);
        CardView withdraws = view.findViewById(R.id.withdraws);
        CardView earnings = view.findViewById(R.id.earnings);

        goToLogin.setOnClickListener(this);
        goToRegister.setOnClickListener(this);
        withdraws.setOnClickListener(this);
        earnings.setOnClickListener(this);

        hideSoftKeyboard();

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goToLogin:
                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentLogin);
                break;
            case R.id.goToRegister:
                Intent intentRegister = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.withdraws:
                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    Intent intentRules = new Intent(getActivity(), WithdrawsActivity.class);
                    startActivity(intentRules);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.earnings:
                if (Utilities.checkNetworkConnection(getActivity()))
                {
                    Intent intentHelp = new Intent(getActivity(), EarningsActivity.class);
                    startActivity(intentHelp);
                }
                else
                {
                    Toast.makeText(getActivity(), R.string.network_not_available, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean checkLogin() {
        SharedPreferences preferences = getActivity().getSharedPreferences("userLogin", MODE_PRIVATE);
        boolean userName = preferences.getBoolean("login", false);

        return userName;
    }

    private void hideSoftKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        if(inputMethodManager != null && getActivity().getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }
}
