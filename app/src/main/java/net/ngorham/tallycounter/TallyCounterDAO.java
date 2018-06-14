package net.ngorham.tallycounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Tally Counter
 * TallyCounterDAO.java
 * Utility
 * Purpose: Database Access Object, provides access to local SQLiteDatabase
 *
 * @author Neil Gorham
 * @version 1.0 06/11/2018
 *
 */

public class TallyCounterDAO {
    //Private constants
    private final int CREATED_COLOR = 6, EDITED_COLOR = 6;
    //Private variables
    private Context context;
    private SQLiteDatabase db;
    private TallyCounterDatabaseHelper dbHelper;

    private static final String TAG = "TallyCounterDAO";

    //Constructor
    public TallyCounterDAO(Context context){
        this.context = context;
        dbHelper = new TallyCounterDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    //Close db
    public void close(){ db.close(); }

    //Add new tally to db
    public boolean addTally(Tally tally){
        try{
            //Set tally content values
            ContentValues values = new ContentValues();
            values.put("NAME", tally.getName());
            values.put("COUNT", tally.getCount());
            values.put("START_COUNT", tally.getStartCount());
            values.put("INCREMENT_BY", tally.getIncrementBy());
            values.put("CATEGORY", tally.getCategory());
            values.put("COLOR", tally.getColor());
            values.put("CREATED_ON", tally.getCreatedOn());
            values.put("LAST_MODIFIED", tally.getLastModified());
            //Insert TALLY
            long tallyId = db.insert("TALLY", null, values);
            if(tallyId == -1){ return false; }
            //Set event content values
            ContentValues eventValues = new ContentValues();
            eventValues.put("TALLY_ID", tallyId);
            eventValues.put("DESCRIPTION", "Created");
            eventValues.put("COUNT", tally.getCount());
            eventValues.put("COLOR", CREATED_COLOR);
            eventValues.put("CREATED_ON", tally.getCreatedOn());
            //Insert EVENT
            long results = db.insert("EVENT", null, eventValues);
            return (results > -1);
        } catch(SQLiteException e){
            Toast.makeText(context,
                    "Database unavailable, failed to insert into TALLY table",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Get Tally from db where tallyId matches
    public Tally fetchTally(int id){
        try{
            Tally tally = null;
            String selection = "_id = ?";
            String[] selectionArgs = {String.valueOf(id)};
            Cursor cursor = db.query("TALLY",
                    null,
                    selection, selectionArgs,
                    null, null, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    double count = cursor.getDouble(2);
                    double startCount = cursor.getDouble(3);
                    double incrementBy = cursor.getDouble(4);
                    String category = cursor.getString(5);
                    int color = cursor.getInt(6);
                    String createdOn = cursor.getString(7);
                    String lastModified = cursor.getString(8);
                    tally  = new Tally(id, name, count, startCount,
                            incrementBy, category, color, createdOn, lastModified);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            return tally;
        } catch(SQLiteException e){
            Toast.makeText(context,
                    "Database unavailable, failed to fetch TALLY from table",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //Delete Tally from db where id matches
    public boolean deleteTally(int id){
        try{
            String where = "_id = ?";
            String[] whereArgs = {String.valueOf(id)};
            int results = db.delete("TALLY", where, whereArgs);
            return (results > 0);
        } catch (SQLiteException e){
            Toast.makeText(context,
                    "Database unavailable, failed to delete item from table",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Get all Tallies from db
    public ArrayList<Tally> fetchAllTallies(){
        try{
            ArrayList<Tally> tallies = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM TALLY", null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    double count = cursor.getDouble(2);
                    double startCount = cursor.getDouble(3);
                    double incrementBy = cursor.getDouble(4);
                    String category = cursor.getString(5);
                    int color = cursor.getInt(6);
                    String createdOn = cursor.getString(7);
                    String lastModified = cursor.getString(8);
                    Tally tally  = new Tally(id, name, count, startCount,
                            incrementBy, category, color, createdOn, lastModified);
                    tallies.add(tally);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            return tallies;
        } catch (SQLiteException e){
            Toast.makeText(context,
                    "Database unavailable, failed to fetch all TALLIES from table",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //Update Tally in db
    public boolean updateTally(Tally tally){
        try {
            //Set  tally content values
            ContentValues values = new ContentValues();
            values.put("NAME", tally.getName());
            values.put("COUNT", tally.getCount());
            values.put("START_COUNT", tally.getStartCount());
            values.put("INCREMENT_BY", tally.getIncrementBy());
            values.put("CATEGORY", tally.getCategory());
            values.put("COLOR", tally.getColor());
            values.put("LAST_MODIFIED", tally.getLastModified());
            //Set event content values
            ContentValues eventValues = new ContentValues();
            eventValues.put("TALLY_ID", tally.getId());
            eventValues.put("DESCRIPTION", "Edited");
            eventValues.put("COUNT", tally.getCount());
            eventValues.put("COLOR", EDITED_COLOR);
            eventValues.put("CREATED_ON", tally.getLastModified());
            //Update TALLY where id matches
            int updateResults = db.update("TALLY", values, "_id = ?",
                    new String[] {String.valueOf(tally.getId())});
            if(updateResults <= 0){ return false; }
            //Insert EVENT
            long insertResults = db.insert("EVENT", null, eventValues);
            return (insertResults > -1);
        } catch (SQLiteException e){
            Toast.makeText(context,
                    "Database unavailable, failed to update TALLY in TALLY table",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //Update Tally count in db
    public boolean updateCount(Tally tally, String description, int color){
        try{
            //Set tally content values
            ContentValues values = new ContentValues();
            values.put("COUNT", tally.getCount());
            values.put("LAST_MODIFIED", tally.getLastModified());
            //Set event content values
            ContentValues eventValues = new ContentValues();
            eventValues.put("TALLY_ID", tally.getId());
            eventValues.put("DESCRIPTION", description);
            eventValues.put("COUNT", tally.getCount());
            eventValues.put("COLOR", color);
            eventValues.put("CREATED_ON", tally.getLastModified());
            //Update TALLY COUNT where id matches
            //Insert EVENT
            int updateResults = db.update("TALLY", values, "_id = ?",
                    new String[] {String.valueOf(tally.getId())});
            if(updateResults <= 0){ return false; }
            long insertResults = db.insert("EVENT", null, eventValues);
            return (insertResults > -1);
        } catch (SQLiteException e){
            Toast.makeText(context,
                    "Database unavailable, failed to update count in TALLY table",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    //Get all Tallies from db
    public ArrayList<Event> fetchAllEvents(int tallyId){
        try{
            ArrayList<Event> events = new ArrayList<>();
            String selection = "TALLY_ID = ?";
            String[] selectionArgs = new String[]{String.valueOf(tallyId)};
            Cursor cursor = db.query("EVENT", null, selection,
                    selectionArgs, null, null, null);
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    int id = cursor.getInt(0);
                    int tally_id = cursor.getInt(1);
                    String description = cursor.getString(2);
                    double count = cursor.getDouble(3);
                    int color = cursor.getInt(4);
                    String createdOn = cursor.getString(5);
                    Event event = new Event(id, tally_id, description, count, color, createdOn);
                    events.add(0, event);
                    cursor.moveToNext();
                }
                cursor.close();
            }
            return events;
        } catch (SQLiteException e){
            Toast.makeText(context,
                    "Database unavailable, failed to fetch all EVENTS from table",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    //Delete all Events from db where id matches
    public boolean deleteAllEvents(int tallyId){
        try{
            String where = "TALLY_ID = ?";
            String[] whereArgs = {String.valueOf(tallyId)};
            int results = db.delete("EVENT", where, whereArgs);
            return (results > 0);
        } catch (SQLiteException e) {
            Toast.makeText(context,
                    "Database unavailable, failed to fetch all EVENTS from table",
                    Toast.LENGTH_SHORT).show();
            return false;

        }
    }
}
