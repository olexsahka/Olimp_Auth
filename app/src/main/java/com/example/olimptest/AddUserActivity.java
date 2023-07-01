package com.example.olimptest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olimptest.data.ClubOlimpContract;
import com.example.olimptest.data.Gender;
import com.example.olimptest.data.MemberCursorAdapter;
import com.google.android.material.textfield.TextInputEditText;

public class AddUserActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Gender genderUser;
    TextInputEditText inputFirstName, inputSecondName, inputGroup;
    Uri currentMemberUri;
    Spinner spinnerGender;

    private static final int EDIT_MEMBER_LOADER = 105781;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user_activity);

        Intent intent = getIntent();
        currentMemberUri = intent.getData();

        if (currentMemberUri == null){
            setTitle("add new User");
            invalidateOptionsMenu();
        }
        else{
            setTitle("edit current user");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER,null,this);

        }

        inputFirstName = findViewById(R.id.inputFirstName);
        inputSecondName = findViewById(R.id.inputSecondName);
        inputGroup = findViewById(R.id.inputGroup);
        spinnerGender = findViewById(R.id.spinnerGender);

        ArrayAdapter  genderArrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.gender_array, android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderArrayAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((String) parent.getItemAtPosition(position)){
                    case "Male":
                        genderUser = Gender.MALE;
                        break;

                    case "Female":
                        genderUser = Gender.FEMALE;
                        break;

                    case "Unknown":
                        genderUser = Gender.UNKNOWN;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_user_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addNewUser:
                saveMember();
                return true;

            case R.id.deleteUser:
                showDeleteDialog();
                return true;

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showDeleteDialog() {
        AlertDialog.Builder  alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Do u wanna delete member");
        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser();
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteUser() {
        if (currentMemberUri!=null){
            int rowDeletedId = getContentResolver().delete(currentMemberUri,null,null);
            if (rowDeletedId != 0){
                Toast.makeText(this,"Delete ok",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Delete not ok",Toast.LENGTH_LONG).show();

            }
        }
        inputGroup.setText("");
        inputSecondName.setText("");
        inputFirstName.setText("");
    }

    private  void saveMember(){
        String FirstName =inputFirstName.getText().toString();
        String SecondName =inputSecondName.getText().toString();
        String Group =inputGroup.getText().toString();
        if (TextUtils.isEmpty(FirstName) || TextUtils.isEmpty(SecondName) || TextUtils.isEmpty(Group) || genderUser == Gender.UNKNOWN){
            Toast.makeText(this,"Not correct input",Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClubOlimpContract.memberEntry.KEY_FIRST_NAME,FirstName);
        contentValues.put(ClubOlimpContract.memberEntry.KEY_SECOND_NAME,SecondName);
        contentValues.put(ClubOlimpContract.memberEntry.KEY_GENDER,genderUser.ordinal());
        contentValues.put(ClubOlimpContract.memberEntry.KEY_SPORT,Group);
        ContentResolver contentResolver = getContentResolver();

        if (currentMemberUri != null){
            int rowChanged = contentResolver.update(currentMemberUri,contentValues,null,null);
            if (rowChanged != 0){
                Toast.makeText(this,"Insert ok",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Insert not ok",Toast.LENGTH_LONG).show();

            }
        }
        else{
            Uri uri = contentResolver.insert(ClubOlimpContract.CONTENT_URI,contentValues);
            if (uri != null){
                Toast.makeText(this,"Insert ok",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(this,"Insert not ok",Toast.LENGTH_LONG).show();

            }        }



    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection =  new String[] {ClubOlimpContract.memberEntry.KEY_ID, ClubOlimpContract.memberEntry.KEY_FIRST_NAME, ClubOlimpContract.memberEntry.KEY_SECOND_NAME,ClubOlimpContract.memberEntry.KEY_GENDER, ClubOlimpContract.memberEntry.KEY_SPORT};
        CursorLoader cursor = new CursorLoader(this, currentMemberUri,projection,null,null,null);
        return cursor;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst() ){
            int ColumnIndexFirstName = data.getColumnIndex(ClubOlimpContract.memberEntry.KEY_FIRST_NAME);
            int ColumnIndexSecondName = data.getColumnIndex(ClubOlimpContract.memberEntry.KEY_SECOND_NAME);
            int ColumnIndexGender = data.getColumnIndex(ClubOlimpContract.memberEntry.KEY_GENDER);
            int ColumnIndexSport = data.getColumnIndex(ClubOlimpContract.memberEntry.KEY_SPORT);

            String firstName = data.getString(ColumnIndexFirstName);
            Log.d("name","name12"+ColumnIndexFirstName +ColumnIndexSecondName +ColumnIndexGender + ColumnIndexSport);
            String secondName = data.getString(ColumnIndexSecondName);
            String sport = data.getString(ColumnIndexSport);
            int gender = data.getInt(ColumnIndexGender);
            inputFirstName.setText(firstName);
            inputSecondName.setText(secondName);
            inputGroup.setText(sport);
            spinnerGender.setSelection(gender);


        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentMemberUri == null){
            MenuItem menuItem = menu.findItem(R.id.deleteUser);
            menuItem.setVisible(false);
        }
        return true;
    }
}
