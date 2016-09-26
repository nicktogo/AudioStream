package us.ktv.database.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import us.ktv.database.datamodel.RoomColumn;
import us.ktv.database.datamodel.SongColumn;

/**
 * Created by nick on 15-10-4.
 */
public class MicDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_ROOMS = "CREATE TABLE " + RoomColumn.TABLE_NAME + "(" +
            RoomColumn.ID + " text primary key, " +
            RoomColumn.NAME + " text, " +
            RoomColumn.ADD_TIME + " text" +
            ");";


    private static final String CREATE_TABLE_SONGS = "CREATE TABLE " + SongColumn.TABLE_NAME + "(" +
            SongColumn.ID + " text primary key, " +
            SongColumn.ROOM_ID + " text, " +
            SongColumn.NAME + " text, " +
            SongColumn.SINGER + " text, " +
            SongColumn.ALBUM + " text, " +
            SongColumn.COVER_URL + " text" +
            ");";

    public MicDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ROOMS);
        db.execSQL(CREATE_TABLE_SONGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
