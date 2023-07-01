package com.example.olimptest.data;

public enum Gender {
    MALE,
    FEMALE,
    UNKNOWN;

    public static String parseGender(int gender){
        switch (gender){
            case 0:
                return Gender.MALE.toString();
            case 1:
                return Gender.FEMALE.toString();
            case 2:
                return Gender.UNKNOWN.toString();
        }
        return null;
    }
};


