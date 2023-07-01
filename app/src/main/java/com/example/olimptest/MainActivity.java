package com.example.olimptest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.olimptest.data.ClubOlimpContract;
import com.example.olimptest.data.Gender;
import com.example.olimptest.data.MemberCursorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.olimptest.data.ClubOlimpContract.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEMBER_LOADER = 105780;
    private MemberCursorAdapter memberCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton floatingActionButtonAddUser = findViewById(R.id.addNewUser);
        floatingActionButtonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddUserActivity.class);
                startActivity(intent);
            }
        });


        memberCursorAdapter = new MemberCursorAdapter(this,null);
        getSupportLoaderManager().initLoader(MEMBER_LOADER,null,this);

        ListView allUsersListView = findViewById(R.id.allUsersListView);
        allUsersListView.setAdapter(memberCursorAdapter);
        allUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,AddUserActivity.class);
                Uri currentMemberUri = ContentUris.withAppendedId(ClubOlimpContract.CONTENT_URI,id);
                intent.setData(currentMemberUri);
                startActivity(intent);

            }
        });


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection =  new String[] {memberEntry.KEY_ID,memberEntry.KEY_FIRST_NAME,memberEntry.KEY_SECOND_NAME,memberEntry.KEY_SPORT};
        CursorLoader cursor = new CursorLoader(this,ClubOlimpContract.CONTENT_URI,projection,null,null,null);
        return cursor;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        memberCursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        memberCursorAdapter.swapCursor(null);
    }

//    public void showAllUsers(){
//        TextView allUsersTextView = findViewById(R.id.allUsersTextView);
//        String header = memberEntry.KEY_ID + " " + memberEntry.KEY_FIRST_NAME + " " + memberEntry.KEY_SECOND_NAME + " " +
//                memberEntry.KEY_GENDER + " " +memberEntry.KEY_SPORT +"\n";
//        allUsersTextView.setText(header);
//        ContentResolver  contentResolver = getContentResolver();
//        String[] projection =  new String[] {memberEntry.KEY_ID,memberEntry.KEY_FIRST_NAME,memberEntry.KEY_SECOND_NAME,memberEntry.KEY_GENDER,memberEntry.KEY_SPORT};
//        Cursor cursor = contentResolver.query(ClubOlimpContract.CONTENT_URI,projection,null,null,null);
//        int columnId = cursor.getColumnIndex(memberEntry.KEY_ID);
//        int columnFirstName = cursor.getColumnIndex(memberEntry.KEY_FIRST_NAME);
//        int columnSecondName = cursor.getColumnIndex(memberEntry.KEY_SECOND_NAME);
//        int columnGender = cursor.getColumnIndex(memberEntry.KEY_GENDER);
//        int columnSport = cursor.getColumnIndex(memberEntry.KEY_SPORT);
//        while (cursor.moveToNext()){
//            String userInfo = cursor.getString(columnId) + " " + cursor.getString(columnFirstName) + " " +
//                    cursor.getString(columnSecondName) + " " + Gender.parseGender(Integer.parseInt(cursor.getString(columnGender))) +" " + cursor.getString(columnSport) +"\n";
//            allUsersTextView.append(userInfo);
//        }
//    }
}