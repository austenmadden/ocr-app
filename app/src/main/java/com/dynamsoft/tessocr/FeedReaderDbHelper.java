package com.dynamsoft.tessocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lyndis on 12/1/15.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=3;
    private SQLiteDatabase mDb;
    private static SQLiteDatabase db;
    private FeedReaderDbHelper mDbHelper;
    private static int key=1;
    private Context ctx;
    public static final String DATABASE_NAME= Environment.getExternalStorageDirectory() +"/FeedReader.db";
    private static final String TEXT_TYPE =" TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES="CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME+" ( ID INT PRIMARY KEY NOT NULL, " +FeedReaderContract.FeedEntry.COLUMN_NAME_DATA+TEXT_TYPE+" )";

    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+ FeedReaderContract.FeedEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
        try {
            db.execSQL(SQL_DELETE_ENTRIES);
            db.execSQL(SQL_CREATE_ENTRIES);
        }catch (Exception e) {

        }
        db.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
       // db.execSQL(SQL_CREATE_ENTRIES);
        db.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
        db.execSQL(SQL_DELETE_ENTRIES);
        db.close();
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    //maybe deprecated from old display method
    public Cursor fetchAllItems() {
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
        Cursor mCursor = db.rawQuery("SELECT * FROM tessData",null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public void insert(String data1) {
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
        key+=1;
        if ((db.isOpen())) {
            ContentValues values = new ContentValues();
            values.put("ID",key);
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATA, data1);
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }
        else {
            Log.e("db", "db is not open for write");
        }
        db.close();
    }

    public void exec(String data1) {
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
        key+=1;
        if ((db.isOpen())) {
            String query = "INSERT INTO tessData VALUES (" + key + ", " + "'" + data1 + "' );";
            Log.d("Full Query", query);
            db.execSQL(query);
        }
        else {
           Log.e("db", "db is not open for write");
        }
        db.close();
    }
}