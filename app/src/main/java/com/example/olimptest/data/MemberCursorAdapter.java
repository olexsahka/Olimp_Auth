package com.example.olimptest.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.olimptest.R;

public class MemberCursorAdapter extends CursorAdapter {
    public MemberCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.cursor_item,parent,false);
        return view;
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView firstNameTextView = view.findViewById(R.id.firstNameTextView);
        TextView secondNameTextView = view.findViewById(R.id.secondNameTextView);
        TextView sportTextView = view.findViewById(R.id.sportTextView);

        firstNameTextView.setText(cursor.getString(cursor.getColumnIndex(ClubOlimpContract.memberEntry.KEY_FIRST_NAME)));
        secondNameTextView.setText(cursor.getString(cursor.getColumnIndex(ClubOlimpContract.memberEntry.KEY_SECOND_NAME)));
        sportTextView.setText(cursor.getString(cursor.getColumnIndex(ClubOlimpContract.memberEntry.KEY_SPORT)));

    }
}
