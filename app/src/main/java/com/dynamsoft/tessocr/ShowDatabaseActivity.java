package com.dynamsoft.tessocr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Iterator;

public class ShowDatabaseActivity extends Activity {

    private FeedReaderDbHelper dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private SQLiteDatabase dataBase;
    private ArrayList<String> datal = new ArrayList<String>();
    private ArrayList<String> id = new ArrayList<String>();
    private ListView userList;
    private boolean isSanitized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        isSanitized = i.getBooleanExtra("SANITIZE_TOGGLE", false);
        setContentView(R.layout.activity_show_database);
        dbHelper = new FeedReaderDbHelper(this);
        dbHelper.insert("hello world");
        dbHelper.insert("Goodbye world");
        dbHelper.insert("Peace out world");
        ArrayList<String> sqlQueries = i.getStringArrayListExtra("SQL_QUERIES");
        Iterator<String> it = sqlQueries.iterator();
        while (it.hasNext()) {
            String query = it.next();
            Log.d("Query", query);
            if (!isSanitized) {
                dbHelper.exec(query);
            } else {
                dbHelper.insert(query);
            }
        }
        userList = (ListView) findViewById(R.id.listView);

    }
    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    private void displayData() {
        Cursor mCursor = dbHelper.fetchAllItems();
        datal.clear();
        id.clear();

        if (mCursor.moveToFirst()) {
            do {
                datal.add(mCursor.getString(mCursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATA)));
            } while(mCursor.moveToNext());
        }

        DisplayAdapter disadpt = new DisplayAdapter(ShowDatabaseActivity.this, datal);
        userList.setAdapter(disadpt);
        mCursor.close();
    }

}
