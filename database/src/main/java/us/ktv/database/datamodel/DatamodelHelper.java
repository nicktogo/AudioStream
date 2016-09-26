package us.ktv.database.datamodel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import us.ktv.database.R;
import us.ktv.database.database.MicDatabaseHelper;


/**
 * Created by nick on 15-10-4.
 */
public abstract class DatamodelHelper<T> {

    protected SQLiteDatabase sqLiteDatabase;

    protected DatamodelHelper(Context context) {
        MicDatabaseHelper helper = new MicDatabaseHelper(
                context,
                context.getResources().getString(R.string.database_name),
                null,
                context.getResources().getInteger(R.integer.database_version));
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public abstract List<T> queryList();

    public abstract boolean insert(T t);

    public abstract T queryById(String id);

    protected Song getSong(Cursor cursor) {
        Song song = new Song();
        song.id = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.ID));
        song.roomId = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.ROOM_ID));
        song.name = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.NAME));
        song.singer = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.SINGER));
        song.coverUrl = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.COVER_URL));
        song.album = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.ALBUM));
        return song;
    }

    protected Room getRoom(Cursor cursor) {
        Room room = new Room();
        room.id = cursor.getString(cursor.getColumnIndexOrThrow(RoomColumn.ID));
        room.name = cursor.getString(cursor.getColumnIndexOrThrow(RoomColumn.NAME));
        room.addTime = cursor.getString(cursor.getColumnIndexOrThrow(RoomColumn.ADD_TIME));
        return room;
    }

}
