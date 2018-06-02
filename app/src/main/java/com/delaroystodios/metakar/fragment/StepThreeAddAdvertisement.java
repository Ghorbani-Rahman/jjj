package com.delaroystodios.metakar.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.delaroystodios.metakar.R;
import com.delaroystodios.metakar.helper.ConvertToPersianNumber;

import java.util.ArrayList;

public class StepThreeAddAdvertisement extends Fragment implements View.OnClickListener
{
    private View view;
    private ImageView backStepThreeAddAdvertisement;
    private EditText countVisit , costVisit;
    private TextView upCount , downCount , upCountCost , downCountCost , totalCost , prevStep ,
            sendDataAdvertisement , cancelAddAdvertisement;
    private Spinner durationAdvertisement;
    private ArrayList<String> listDurationAdvertisement;
    private int sumCost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_step_three_add_advertisment, container, false);


        initComponent();

        return view;
    }

    private void initComponent()
    {

        backStepThreeAddAdvertisement = (ImageView)view.findViewById(R.id.back_step_three_add_advertisement);
        countVisit = (EditText) view.findViewById(R.id.count_visit);
        upCount = (TextView)view.findViewById(R.id.up_count);
        downCount = (TextView)view.findViewById(R.id.down_count);
        costVisit = (EditText) view.findViewById(R.id.cost_visit);
        upCountCost = (TextView) view.findViewById(R.id.up_count_cost);
        downCountCost = (TextView) view.findViewById(R.id.down_count_cost);
        durationAdvertisement = (Spinner) view.findViewById(R.id.duration_advertisement);
        totalCost = (TextView) view.findViewById(R.id.total_cost);
        cancelAddAdvertisement = (TextView) view.findViewById(R.id.cancel_add_advertisement);
        prevStep = (TextView) view.findViewById(R.id.prev_step);
        sendDataAdvertisement = (TextView) view.findViewById(R.id.send_data_advertisement);

        countVisit.setText("100");
        costVisit.setText("10");



    countVisit.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            new ConvertToPersianNumber(s.toString()).convertToPersian();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String StringCountVisit= s.toString();
            String StringCostVisit= costVisit.getText().toString();

            int valueCount = 0;
            if(!StringCountVisit.equals(""))
            {
                valueCount = Integer.parseInt(StringCountVisit);
            }

            int valueCost = 0;
            if(!StringCostVisit.equals(""))
            {
                valueCost = Integer.parseInt(StringCostVisit);
            }

                sumCost = valueCount * valueCost;
                afterTextChangedFunction(sumCost + "");
        }
    });

        costVisit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                String StringCostVisit= s.toString();
                String StringCountVisit= countVisit.getText().toString();

                int valueCost = 0;
                if(!StringCostVisit.equals(""))
                {
                    valueCost = Integer.parseInt(StringCostVisit);
                }

                int valueCount = 0;
                if(!StringCountVisit.equals(""))
                {
                    valueCount = Integer.parseInt(StringCountVisit);
                }

                    sumCost = valueCost * valueCount;
                    afterTextChangedFunction(sumCost+"");

            }
        });

        backStepThreeAddAdvertisement.setOnClickListener(this);
        upCount.setOnClickListener(this);
        downCount.setOnClickListener(this);
        upCountCost.setOnClickListener(this);
        downCountCost.setOnClickListener(this);
        cancelAddAdvertisement.setOnClickListener(this);
        prevStep.setOnClickListener(this);
        sendDataAdvertisement.setOnClickListener(this);


        listDurationAdvertisement = new ArrayList<>();
        listDurationAdvertisement.add("مهم نیست");
        listDurationAdvertisement.add(new ConvertToPersianNumber("10 ثانیه").convertToPersian());
        listDurationAdvertisement.add(new ConvertToPersianNumber("30 ثانیه").convertToPersian());
        listDurationAdvertisement.add(new ConvertToPersianNumber("1 دقیقه").convertToPersian());
        listDurationAdvertisement.add(new ConvertToPersianNumber("3 دقیقه").convertToPersian());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, listDurationAdvertisement);

        durationAdvertisement.setAdapter(adapter);

        sumCost = Integer.parseInt(countVisit.getText().toString()) * Integer.parseInt(costVisit.getText().toString());


        afterTextChangedFunction(sumCost+"");


    }

    @SuppressLint("DefaultLocale")
    public void afterTextChangedFunction(String str) {
        String s;

        s = String.format("%,d", Long.parseLong(str));

        totalCost.setText("مجموع : " + new ConvertToPersianNumber(s).convertToPersian() + " تومان");

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_step_three_add_advertisement:
                getActivity().onBackPressed();
                break;
            case R.id.up_count:
                int countTpUp = (Integer.parseInt(countVisit.getText().toString()))+1;
                countVisit.setText(countTpUp+"");
                break;
            case R.id.down_count:
                int countToDown = (Integer.parseInt(countVisit.getText().toString()))-1;

                if(countToDown < 100)
                {
                    countVisit.setError(new ConvertToPersianNumber("حداقل مقدار تعداد بازدید 100 می باشد").convertToPersian());
                    countVisit.setText((countToDown+1)+"");
                }
                else
                {
                    countVisit.setText(countToDown+"");
                }

                break;
            case R.id.up_count_cost:
                int countCostToUp = (Integer.parseInt(costVisit.getText().toString()))+1;
                costVisit.setText(countCostToUp+"");
                break;
            case R.id.down_count_cost:
                int countCostToDown = (Integer.parseInt(costVisit.getText().toString()))-1;

                if(countCostToDown < 10)
                {
                    costVisit.setError(new ConvertToPersianNumber("حداقل مقدار 10 می باشد").convertToPersian());
                    costVisit.setText((countCostToDown+1)+"");
                }
                else
                {
                    costVisit.setText(countCostToDown+"");
                }
                break;
            case R.id.prev_step:
                getActivity().onBackPressed();
                break;
            case R.id.send_data_advertisement:
                if(validateInputData())
                {
                    Toast.makeText(getActivity(), "اطلاعات با موفقیت ثبت شد!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            case R.id.cancel_add_advertisement:
                setCancelAddAdvertisement();
                break;
        }
    }

    private boolean validateInputData()
    {
        int errorCount = 0;
        int countVisitUser = Integer.parseInt(countVisit.getText().toString().trim());
        int costVisitUser = Integer.parseInt(costVisit.getText().toString().trim());

        if(countVisitUser < 100)
        {
            countVisit.setError(new ConvertToPersianNumber("حداقل مقدار تعداد بازدید 100 می باشد").convertToPersian());
            errorCount++;
        }

        if(costVisitUser < 10)
        {
            countVisit.setError(new ConvertToPersianNumber("حداقل مقدار 10 می باشد").convertToPersian());
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

    private void setCancelAddAdvertisement()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("آیا می خواهید از درج آگهی انصراف دهید؟");

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
                getActivity().finish();
            }
        });

        builder.create().show();

    }
}
