package com.example.olimptest.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public final class ClubOlimpContract {
    public  ClubOlimpContract(){
    }
    public static final String TABLE_NAME = "members";
    public static final String DATABASE_NAME = "olimp";
    public static final int VERSION = 1;

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.olimptest";
    public static final String PATH_MEMBERS = "members";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_MEMBERS);

    public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;
    public static final String CONTENT_SINGLE_ITEM = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;

    public static  final class memberEntry implements BaseColumns {


        public static final String KEY_ID = BaseColumns._ID;
        public static final String KEY_FIRST_NAME = "firstName";
        public static final String KEY_SECOND_NAME = "secondName";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_SPORT = "sport";

    }
}
