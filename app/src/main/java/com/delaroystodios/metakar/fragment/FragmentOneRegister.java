package com.delaroystodios.metakar.fragment;


import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.SQLiteHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class FragmentOneRegister extends Fragment implements View.OnClickListener
{

    private static SQLiteHelper sqLiteHelper;
    private static int MY_REQUEST_CODE = 0;
    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    private ImageView select_img_user;
    private Bitmap thumbnail;
    private View mView;
    private ImageView backOneRegister;
    private Button continueRegister;
    private EditText name , family;
    private FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_one_register, container, false);

        initComponent();


        return mView;
    }

    private void initComponent() {

        sqLiteHelper = new SQLiteHelper(getActivity() , "Image_User.sqlite" , null , 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS Image_User (Id INTEGER PRIMARY KEY AUTOINCREMENT , image BLOG)");

        select_img_user = (ImageView)mView.findViewById(R.id.select_img_user);
        backOneRegister = (ImageView)mView.findViewById(R.id.back_one_register);
        continueRegister = (Button)mView.findViewById(R.id.continue_register);
        name = (EditText)mView.findViewById(R.id.name);
        family = (EditText)mView.findViewById(R.id.family);

        select_img_user.setOnClickListener(this);
        backOneRegister.setOnClickListener(this);
        continueRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.back_one_register:
                getActivity().finish();
                break;
            case R.id.select_img_user:

                if (!(checkPermission())) {
                    requestPermission();
                }else
                {
                    setPictureUserProfile();
                }
                break;
            case R.id.continue_register:
                String firstName , lastName;
                firstName = name.getText().toString().trim();
                lastName = family.getText().toString().trim();
                if(ValidateRegisterStepOne(firstName , lastName))
                {
                    FragmentManager fragmentManager4 = getFragmentManager();
                    fragmentTransaction = fragmentManager4.beginTransaction();
                    FragmentTwoRegister fragmentTwoRegister = new FragmentTwoRegister();
                    fragmentTransaction.add(R.id.container_register, fragmentTwoRegister , "StepTwoRegister");
                    fragmentTransaction.addToBackStack("StepTwoRegister");
                    Bundle bundle = new Bundle();
                    bundle.putString("name" , firstName);
                    bundle.putString("family" , lastName);
                    fragmentTwoRegister.setArguments(bundle);
                    fragmentTransaction.commit();
                }
                break;
            default:
                break;
        }
    }

    private void setPictureUserProfile()
    {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.uploadImages)
                .items(R.array.uploadImages)
                .titleGravity(GravityEnum.END)
                .itemsGravity(GravityEnum.END)
                .itemsCallback(new MaterialDialog.ListCallback()
                {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which)
                        {
                            case 0:
                                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                                photoPickerIntent.setType("image/*");
                                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                                break;
                            case 1:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAPTURE_PHOTO);
                                break;
                            case 2:
                                select_img_user.setImageResource(R.drawable.ic_account_circle_black);
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO)
        {
            if(resultCode == RESULT_OK && data!= null)
            {
                final Uri imageUri = data.getData();
                try {
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    select_img_user.setImageBitmap(selectedImage);

                } catch (FileNotFoundException e) {
                    Toast.makeText(getActivity(), "مشکلی در انتخاب تصویر به وجود آمد لطفا دوباره امتحان کنید!!!", Toast.LENGTH_SHORT).show();
                }
            }

        }else if(requestCode == CAPTURE_PHOTO){
            if(resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");

        //set profile picture form camera
        select_img_user.setMaxWidth(200);
        select_img_user.setImageBitmap(thumbnail);

    }

    public boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getActivity(), CAMERA) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE }, MY_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == MY_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                boolean cameraAccepted1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (cameraAccepted1 && cameraAccepted2) {
                    setPictureUserProfile();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean ValidateRegisterStepOne(String fn, String ln)
    {

        int errorCount = 0;

        if (fn == null || fn.trim().length() < 4)
        {
            name.setError("طول نام نمیتواند کمتر از 3 حرف باشد!");
            errorCount++;
        }
        else if(fn.length() > 25)
        {
            name.setError("طول نام نمیتواند بیشتر از 25 حرف باشد!");
            errorCount++;
        }

        if (ln == null || ln.trim().length() < 4)
        {
            family.setError("طول نام خانوادگی نمیتواند کمتر از 3 حرف باشد!");
            errorCount++;
        }  else if(ln.length() > 25)
        {
            family.setError("طول نام خانوادگی نمیتواند بیشتر از 25 حرف باشد!");
            errorCount++;
        }

        if(errorCount == 0)
        {
            return true;
        }
        else
        {
            return false;
        }

}


}
