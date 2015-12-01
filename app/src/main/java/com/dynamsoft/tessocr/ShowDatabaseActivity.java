package com.dynamsoft.tessocr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class ShowDatabaseActivity extends Activity {

    private FeedReaderDbHelper dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private SQLiteDatabase dataBase;
    private ArrayList<String> data = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();
    private ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_database);
        dbHelper = new FeedReaderDbHelper(this);
        dbHelper.insert("hello world");
        dbHelper.insert("Goodbye world");
        userList = (ListView) findViewById(R.id.listView);
        dbHelper = new FeedReaderDbHelper(this);

    }
    protected void onResume() {
        displayData();
        super.onResume();
    }

    private void displayData() {
        dataBase = dbHelper.getWritableDatabase();
        Cursor mCursor = dataBase.rawQuery("SELECT * FROM "+ FeedReaderContract.FeedEntry.TABLE_NAME,null);

        data.clear();
        id.clear();

        if (mCursor.moveToFirst()) {
            do {
                data.add(mCursor.getString(mCursor.getColumnIndex(FeedReaderContract.FeedEntry.TABLE_NAME)));
                id.add(mCursor.getString(mCursor.getColumnIndex(FeedReaderContract.FeedEntry._ID)));
            } while(mCursor.moveToNext());
        }

        DisplayAdapter disadpt = new DisplayAdapter(ShowDatabaseActivity.this, data, id);
        userList.setAdapter(disadpt);
        mCursor.close();
    }

}
