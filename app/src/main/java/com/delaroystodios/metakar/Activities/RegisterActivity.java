package com.delaroystodios.metakar.Activities;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.fragment.FragmentOneRegister;

public class RegisterActivity extends AppCompatActivity {


    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_register);


        FragmentManager fragmentManager4 = getFragmentManager();
        fragmentTransaction = fragmentManager4.beginTransaction();
        fragmentTransaction.replace(R.id.container_register, new FragmentOneRegister() , "StepOneRegister");
        fragmentTransaction.addToBackStack("StepOneRegister");
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1)
        {

            getFragmentManager().popBackStack();
        }
        else
        {
            exitRegister();
        }
    }

    private void exitRegister()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setMessage("میخواهید از این صفحه خارج شوید؟");

        builder.setNegativeButton("خیر", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });

        builder.create().show();

    }

}