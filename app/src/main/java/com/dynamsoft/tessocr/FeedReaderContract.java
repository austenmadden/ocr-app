package com.dynamsoft.tessocr;

import android.provider.BaseColumns;

/**
 * Created by lyndis on 12/1/15.
 */
public final class FeedReaderContract {
    //prevent bad instantiation
    public FeedReaderContract() {}

    //define table contents, table name, and column names
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME="tessData";
        public static final String COLUMN_NAME_DATA="data";
    }
    //statements to create the table, and delete the table
    private static final String TEXT_TYPE ="TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES="CREATE TABLE " +FeedEntry.TABLE_NAME+" ("+FeedEntry._ID+" INTEGER PRIMARY KEY, "+FeedEntry.COLUMN_NAME_DATA+TEXT_TYPE+")";

    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+FeedEntry.TABLE_NAME;

}
