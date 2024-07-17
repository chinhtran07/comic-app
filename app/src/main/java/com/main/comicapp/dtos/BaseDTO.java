package com.main.comicapp.dtos;

import java.util.Map;

public class BaseDTO {
    public static boolean validateObject(Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() == null)
                return false;
        }
        return true;
    }
}
