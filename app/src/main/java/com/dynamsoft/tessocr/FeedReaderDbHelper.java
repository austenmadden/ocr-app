package com.dynamsoft.tessocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lyndis on 12/1/15.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=3;
    private SQLiteDatabase mDb;
    private static SQLiteDatabase db;
    private FeedReaderDbHelper mDbHelper;

    private Context ctx;
    public static final String DATABASE_NAME= Environment.getExternalStorageDirectory() +"/FeedReader.db";
    private static final String TEXT_TYPE ="TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES="CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME+" (" +FeedReaderContract.FeedEntry.COLUMN_NAME_DATA+TEXT_TYPE+")";

    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+ FeedReaderContract.FeedEntry.TABLE_NAME;

    public FeedReaderDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        db = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    //maybe deprecated from old display method
    public Cursor fetchAllItems() {
        Cursor mCursor = mDb.query(FeedReaderContract.FeedEntry.TABLE_NAME,new String[] {FeedReaderContract.FeedEntry.COLUMN_NAME_DATA},null,null,null,null,null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insert(String data1) {

        mDb = mDbHelper.getWritableDatabase();
        if ((mDb.isOpen())) {
            ContentValues values = new ContentValues();

            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATA, data1);
            mDb.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }
        else {
            System.out.print("db is not open for write");
        }
        mDb.close();
    }
}