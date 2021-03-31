package lk.kdu.pulze.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import lk.kdu.pulze.databasemodel.PressureModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    // database name
    public static String DATABASE_NAME = "blood_pressure_logger";
    private static final int DATABASE_VERSION = 1;

    // table name
    private static final String TABLE_NAME = "pressure";

    // column names
    private static final String KEY_ID = "id";
    private static final String SYSTOLIC = "systolic";
    private static final String DIASTOLIC = "diastolic";
    private static final String PULSE = "pulse";
    private static final String DATE = "date";
    private static final String NOTES = "notes";

    // query to create the table
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + SYSTOLIC + " TEXT, "+ DIASTOLIC + " TEXT, "+ PULSE + " TEXT, "+ DATE + " TEXT, "+ NOTES + " TEXT  );";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("table", CREATE_TABLE);
    }

    // creating the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    // dropping the table if it already exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
        onCreate(db);
    }

    // adding the pressure to the database
    public long addPressure(int systolic, int diastolic, int pulse, String date, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(SYSTOLIC, systolic);
        values.put(DIASTOLIC, diastolic);
        values.put(PULSE, pulse);
        values.put(DATE, date);
        values.put(NOTES, notes);
        // insert row in table
        long insert = db.insert(TABLE_NAME, null, values);

        return insert;
    }

    // retrieve added records
    public ArrayList<PressureModel> getPressure() {
        ArrayList<PressureModel> pressureModelArrayList = new ArrayList<PressureModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                PressureModel pressureModel = new PressureModel();
                pressureModel.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                pressureModel.setSystolic(c.getInt(c.getColumnIndex(SYSTOLIC)));
                pressureModel.setDiastolic(c.getInt(c.getColumnIndex(DIASTOLIC)));
                pressureModel.setPulse(c.getInt(c.getColumnIndex(PULSE)));
                pressureModel.setDatetime(c.getString(c.getColumnIndex(DATE)));
                pressureModel.setNotes(c.getString(c.getColumnIndex(NOTES)));
                // adding to list
                pressureModelArrayList.add(pressureModel);
            } while (c.moveToNext());
        }
        return pressureModelArrayList;
    }

    // delete records
    public void deletePressure(int id) {
        // delete row in table based on id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

}
