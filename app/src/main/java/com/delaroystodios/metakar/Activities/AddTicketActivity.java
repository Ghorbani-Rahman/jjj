package com.delaroystodios.metakar.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;
import com.delaroystodios.metakar.network.Utilities;

import java.io.IOException;
import java.util.ArrayList;

import static android.Manifest.permission.CAMERA;

public class AddTicketActivity extends AppCompatActivity implements View.OnClickListener
{

    private Spinner priority , sectionDep;
    private ImageView backAddTicket , removeSelectedPhoto , addImageAddTicket , photoSelesced;
    private ArrayList<String> arraySpinnerPriority , arraySpinnerSectionDep;
    private Button sendAddTicket , noTicket , sendAddTicketBottom , noTicketBottom;
    private EditText subjectTicket , explainTicket;
    private LinearLayout sectionForImage;
    private static final int IMG_REQUEST_PHOTO = 15;
    private Bitmap bitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        initComponent();


    }

    private void initComponent()
    {
        priority = (Spinner)findViewById(R.id.priority);
        sectionDep = (Spinner)findViewById(R.id.sectionDep);
        backAddTicket = (ImageView)findViewById(R.id.back_add_ticket);
        sendAddTicket = (Button)findViewById(R.id.send_add_ticket);
        noTicket = (Button)findViewById(R.id.no_ticket);
        sendAddTicketBottom = (Button)findViewById(R.id.send_add_ticket_bottom);
        noTicketBottom = (Button)findViewById(R.id.no_ticket_bottom);
        addImageAddTicket = (ImageView) findViewById(R.id.add_image_add_ticket);
        sectionForImage = (LinearLayout) findViewById(R.id.section_for_image);
        removeSelectedPhoto = (ImageView) findViewById(R.id.remove_selected_photo);
        photoSelesced = (ImageView) findViewById(R.id.photo_selesced);


        priority.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) priority.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        sectionDep.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ((TextView) sectionDep.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        arraySpinnerPriority = new ArrayList<>();
        arraySpinnerSectionDep = new ArrayList<>();

        arraySpinnerPriority.add(new ConvertToPersianNumber("عادی").convertToPersian());
        arraySpinnerPriority.add(new ConvertToPersianNumber("فوری").convertToPersian());

        arraySpinnerSectionDep.add(new ConvertToPersianNumber("مالی").convertToPersian());
        arraySpinnerSectionDep.add(new ConvertToPersianNumber("فنی").convertToPersian());

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                AddTicketActivity.this, android.R.layout.simple_spinner_item, arraySpinnerPriority);

        priority.setAdapter(adapter1);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                AddTicketActivity.this, android.R.layout.simple_spinner_item, arraySpinnerSectionDep);

        sectionDep.setAdapter(adapter2);

        backAddTicket.setOnClickListener(this);
        sendAddTicket.setOnClickListener(this);
        sendAddTicketBottom.setOnClickListener(this);
        noTicket.setOnClickListener(this);
        noTicketBottom.setOnClickListener(this);
        addImageAddTicket.setOnClickListener(this);
        removeSelectedPhoto.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_add_ticket:
            case R.id.no_ticket:
            case R.id.no_ticket_bottom:
                finish();
                break;
            case R.id.add_image_add_ticket:
                if (checkPermission()) {
                    selectImage(IMG_REQUEST_PHOTO);
                } else {
                    requestPermission();
                }
                break;
            case R.id.remove_selected_photo:
                sectionForImage.setVisibility(View.GONE);
                break;
            case R.id.send_add_ticket:
            case R.id.send_add_ticket_bottom:
                if(validateInput())
                {
                    if (Utilities.checkNetworkConnection(AddTicketActivity.this)) {
                        Toast.makeText(this, "با موفقیت ارسال شد", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(AddTicketActivity.this, R.string.network_not_available, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean validateInput()
    {
        int countError = 0;

        if(subjectTicket.length() == 0)
        {
            subjectTicket.setError("موضوع نمی تواند خالی باشد!");
            countError++;
        }

        if(explainTicket.length() == 0)
        {
            explainTicket.setError("شرح تیکت نمی تواند خالی باشد!");
            countError++;
        }

        if(countError == 0)
        {
            return true;
        }else
        {
            return false;
        }
    }

    private void selectImage(final int SelectImage)
    {
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        switch (SelectImage)
        {
            case IMG_REQUEST_PHOTO:
                startActivityForResult(intent , IMG_REQUEST_PHOTO);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(AddTicketActivity.this.getContentResolver(), path);
                photoSelesced.setImageBitmap(bitmap);
                sectionForImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(AddTicketActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(AddTicketActivity.this , new String[] { Manifest.permission.CAMERA}, IMG_REQUEST_PHOTO);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == IMG_REQUEST_PHOTO)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean cameraAccepted1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted1) {
                    selectImage(IMG_REQUEST_PHOTO);
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
