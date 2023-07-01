package com.example.olimptest.data;

import static com.example.olimptest.data.ClubOlimpContract.DATABASE_NAME;
import static com.example.olimptest.data.ClubOlimpContract.TABLE_NAME;
import static com.example.olimptest.data.ClubOlimpContract.VERSION;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.olimptest.data.ClubOlimpContract.memberEntry;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DB = "CREATE TABLE " + TABLE_NAME + "("+
                memberEntry.KEY_ID +" INTEGER PRIMARY KEY," + memberEntry.KEY_FIRST_NAME  +" TEXT NOT NULL," +
                memberEntry.KEY_SECOND_NAME + " TEXT NOT NULL," + memberEntry.KEY_GENDER +" INTEGET NOT NULL," +
                memberEntry.KEY_SPORT +" TEXT NOT NULL" + " )";
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String UPGRADE_DB = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(UPGRADE_DB);
        this.onCreate(db);

    }
}
