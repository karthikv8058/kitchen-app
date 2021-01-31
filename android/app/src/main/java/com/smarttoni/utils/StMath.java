package com.smarttoni.utils;

public class StMath {

    static int roundQuantity(float qty, float unit)
    {
        if (unit == 0)
            return (int)qty;

        int nos = (int) Math.floor(qty / unit);

        float remainder = qty % unit;
        if (remainder == 0)
            return nos;

        return nos  + 1;
    }
}
