package lk.kdu.pulze.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.kdu.pulze.SQLiteHelper;
import lk.kdu.pulze.model.Record;

public class RecordDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_SYSTOLE};

    public Record createRecord(String record) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_SYSTOLE, record);

        long insertId = database.insert(SQLiteHelper.TABLE_RECORDS, null, values);

        Cursor cursor = database.query(SQLiteHelper.TABLE_RECORDS, allColumns, SQLiteHelper.COLUMN_ID + "=" + insertId, null, null, null, null);

        cursor.moveToFirst();
        Record newRecord = cursorToRecord(cursor);
        cursor.close();
        return newRecord;
    }

    public void deleteRecord(Record record) {
        long id = record.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_RECORDS, SQLiteHelper.COLUMN_ID + "=" + id, null);
    }

    public RecordDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Record> getAllRecords() {
        List<Record> records = new ArrayList<>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_RECORDS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Record record = cursorToRecord(cursor);
            records.add(record);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }

    private Record cursorToRecord(Cursor cursor) {
        Record record = new Record();
        record.setId(cursor.getLong(0));
        record.setSystole(cursor.getString(1));
        return record;
    }
}
