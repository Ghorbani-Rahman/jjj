package com.delaroystodios.metakar.helper;


import android.util.Log;

public class ConvertToPersianNumber
{
    private String number;

    public ConvertToPersianNumber(String number)
    {
        if(number == null || number.equals("null"))
            this.number = "نامشخص";
        else
            this.number = number;
    }

    public String convertToPersian()
    {

        number = number.replace( "0" , "۰")
                .replace( "1" , "۱")
                .replace( "2" , "۲")
                .replace( "3" , "۳")
                .replace( "4" , "۴")
                .replace( "5" , "۵")
                .replace( "6" , "۶")
                .replace( "7" , "۷")
                .replace( "8" , "۸")
                .replace( "9" , "۹");

        return number;

    }

}
