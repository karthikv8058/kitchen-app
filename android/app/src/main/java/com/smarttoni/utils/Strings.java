package com.smarttoni.utils;

import android.annotation.SuppressLint;

public class Strings {

    public static boolean equals(String a,String b){
        return  a != null && a.equals(b);
    }

    public static boolean isEmpty(String input){
        return  input == null || input.isEmpty();
    }

    public static boolean isNotEmpty(String input){
        return  !isEmpty(input);
    }

    @SuppressLint("DefaultLocale")
    public static String formatUnitString(float qty, String unit){
        return  String.format("%.2f ",qty)+unit;
    }

}
