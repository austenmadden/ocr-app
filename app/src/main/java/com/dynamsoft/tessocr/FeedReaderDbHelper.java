package com.dynamsoft.tessocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lyndis on 12/1/15.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    private SQLiteDatabase mDb;
    private FeedReaderDbHelper mDbHelper;
    private static int key;
    public static final String DATABASE_NAME="FeedReader.db";
    private static final String TEXT_TYPE ="TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES="CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME+" ("+ FeedReaderContract.FeedEntry._ID+" INTEGER PRIMARY KEY, "+ FeedReaderContract.FeedEntry.COLUMN_NAME_DATA+TEXT_TYPE+")";

    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+ FeedReaderContract.FeedEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        Cursor mCursor = mDb.query(FeedReaderContract.FeedEntry.TABLE_NAME,new String[] {FeedReaderContract.FeedEntry._ID, FeedReaderContract.FeedEntry.COLUMN_NAME_DATA},null,null,null,null,null);
        key = mCursor.getCount();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    //maybe deprecated from old display method
    public Cursor fetchAllItems() {
        Cursor mCursor = mDb.query(FeedReaderContract.FeedEntry.TABLE_NAME,new String[] {FeedReaderContract.FeedEntry._ID, FeedReaderContract.FeedEntry.COLUMN_NAME_DATA},null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insert(String data) {
        mDb = mDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry._ID,key);
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATA,data);
        mDb.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null,values);

    }
}