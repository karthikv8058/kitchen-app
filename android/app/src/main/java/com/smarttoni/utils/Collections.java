package com.smarttoni.utils;

import java.util.List;

public class Collections {

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }


    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }
}
