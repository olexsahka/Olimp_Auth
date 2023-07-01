package com.example.olimptest.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OlimpContentProvider extends ContentProvider {
    DbHelper dbHelper;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int MEMBERS = 3521;
    private static final int MEMBERS_ID = 3522;
    static {
        uriMatcher.addURI(ClubOlimpContract.AUTHORITY,ClubOlimpContract.PATH_MEMBERS,MEMBERS);
        uriMatcher.addURI(ClubOlimpContract.AUTHORITY,ClubOlimpContract.PATH_MEMBERS +"/#",MEMBERS_ID);
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor ;
        int match = uriMatcher.match(uri);
        switch (match){
            case MEMBERS:
                cursor = sqLiteDatabase.query(ClubOlimpContract.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case MEMBERS_ID:
                selection = ClubOlimpContract.memberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = sqLiteDatabase.query(ClubOlimpContract.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("CANT QUERY INCORRECT"+ uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match){
            case MEMBERS:
                return ClubOlimpContract.CONTENT_MULTIPLE_ITEMS;
            case MEMBERS_ID:
                return ClubOlimpContract.CONTENT_SINGLE_ITEM;
            default:
                throw new IllegalArgumentException("CANT QUERY INCORRECT"+ uri);

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        dataValidation(values);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        if (match == MEMBERS) {
            long new_id = sqLiteDatabase.insert(ClubOlimpContract.TABLE_NAME, null, values);
            if (new_id == -1)
                return null;
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, new_id);
        }
        throw new IllegalArgumentException("CANT QUERY INCORRECT" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int CountRowsDeleted;
        switch (match){
            case MEMBERS:
                CountRowsDeleted = sqLiteDatabase.delete(ClubOlimpContract.TABLE_NAME,selection,selectionArgs);
                break;

            case MEMBERS_ID:
                selection = ClubOlimpContract.memberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                CountRowsDeleted = sqLiteDatabase.delete(ClubOlimpContract.TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("CANT QUERY INCORRECT"+ uri);

        }

        if (CountRowsDeleted !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return CountRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        dataValidation(values);
        int CountRowsUpdated;

        switch (match){
            case MEMBERS:
                CountRowsUpdated = sqLiteDatabase.update(ClubOlimpContract.TABLE_NAME,values,selection,selectionArgs);
                break;

            case MEMBERS_ID:
                selection = ClubOlimpContract.memberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                CountRowsUpdated = sqLiteDatabase.update(ClubOlimpContract.TABLE_NAME,values,selection,selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("CANT QUERY INCORRECT"+ uri);
        }
        if (CountRowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return CountRowsUpdated;
    }


    public void dataValidation(ContentValues contentValues){
        if (contentValues.containsKey(ClubOlimpContract.memberEntry.KEY_FIRST_NAME)){
            if (contentValues.get(ClubOlimpContract.memberEntry.KEY_FIRST_NAME) == null){
                throw new IllegalArgumentException("NOT CORRECT ARG");
            }
        }
        if (contentValues.containsKey(ClubOlimpContract.memberEntry.KEY_SECOND_NAME)) {

            if (contentValues.get(ClubOlimpContract.memberEntry.KEY_SECOND_NAME) == null) {
                throw new IllegalArgumentException("NOT CORRECT ARG");
            }
        }
        if (contentValues.containsKey(ClubOlimpContract.memberEntry.KEY_SPORT)){

            if (contentValues.get(ClubOlimpContract.memberEntry.KEY_SPORT) == null) {
                throw new IllegalArgumentException("NOT CORRECT ARG");
            }
        }
        if (contentValues.containsKey(ClubOlimpContract.memberEntry.KEY_GENDER)) {
            if (contentValues.get(ClubOlimpContract.memberEntry.KEY_GENDER) == null) {
                throw new IllegalArgumentException("NOT CORRECT ARG");
            }
        }
    }
}
