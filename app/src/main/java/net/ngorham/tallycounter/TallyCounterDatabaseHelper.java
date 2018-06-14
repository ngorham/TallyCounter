package net.ngorham.tallycounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Tally Counter
 * TallyCounterDatabaseHelper.java
 * Utility
 * Purpose: Builds SQLiteDatabase for long term storage
 *
 * @author Neil Gorham
 * @version 1.0 06/11/2018
 *
 */

public class TallyCounterDatabaseHelper extends SQLiteOpenHelper {
    //Private constants
    private static final String DB_NAME = "TallyCounter";
    private static final int DB_VERSION = 7;
    //DB Schema
    //Tally table
    private final String TALLY_TABLE = "TALLY";
    private final String COLUMN_ID = "_id";
    private final String COLUMN_NAME = "NAME";
    private final String COLUMN_COUNT = "COUNT";
    private final String COLUMN_START_COUNT = "START_COUNT";
    private final String COLUMN_INCREMENT_BY = "INCREMENT_BY";
    private final String COLUMN_CATEGORY = "CATEGORY";
    private final String COLUMN_COLOR = "COLOR";
    private final String COLUMN_CREATED_ON = "CREATED_ON";
    private final String COLUMN_LAST_MODIFIED = "LAST_MODIFIED";
    private final String TALLY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TALLY_TABLE + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_COUNT + " REAL, "
            + COLUMN_START_COUNT + " REAL, "
            + COLUMN_INCREMENT_BY + " REAL, "
            + COLUMN_CATEGORY + " TEXT, "
            + COLUMN_COLOR + " INTEGER, "
            + COLUMN_CREATED_ON + " TEXT, "
            + COLUMN_LAST_MODIFIED + " TEXT"
            + ");";
    //Event table
    private final String EVENT_TABLE = "EVENT";
    //COLUMN_ID
    private final String COLUMN_TALLY_ID = "TALLY_ID";
    private final String COLUMN_DESCRIPTION = "DESCRIPTION";
    //COLUMN_COUNT
    //COLUMN_COLOR
    //COLUMN_CREATED_ON
    private final String EVENT_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + EVENT_TABLE + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TALLY_ID + " INTEGER, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_COUNT + " REAL, "
            + COLUMN_COLOR + " INTEGER, "
            + COLUMN_CREATED_ON + " TEXT"
            + ");";

    //Default constructor
    public TallyCounterDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS TALLY");
        db.execSQL("DROP TABLE IF EXISTS EVENT");
        db.execSQL(TALLY_TABLE_CREATE);
        db.execSQL(EVENT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        updateDatabase(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
    }
}
