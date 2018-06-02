package com.delaroystodios.metakar.Activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.fragment.StepOneAddAdvertisement;

public class AddAdvertisementActivity extends AppCompatActivity {


    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisment);

        FragmentManager fragmentManager4 = getFragmentManager();
        fragmentTransaction = fragmentManager4.beginTransaction();
        fragmentTransaction.replace(R.id.contianer_add_advertisment, new StepOneAddAdvertisement() , "StepOneAddAdavertisment");
        fragmentTransaction.addToBackStack("StepOneAddAdavertisment");
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
            exitAddAdvertisement();
        }
    }

    private void exitAddAdvertisement()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAdvertisementActivity.this);

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
