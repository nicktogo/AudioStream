package us.ktv.database.datamodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 15-10-6.
 */
public class SongHelper extends DatamodelHelper<Song> {

    public boolean isUpdated = false;

    private static SongHelper helper;

    private SongHelper(Context context) {
        super(context);
    }

    public synchronized static SongHelper getInstance(Context context) {
        if (helper == null) {
            helper = new SongHelper(context);
        }
        return helper;
    }

    @Override
    public List<Song> queryList() {
        Cursor cursor = sqLiteDatabase.query(SongColumn.TABLE_NAME, null, null, null, null, null, "id");
        List<Song> songs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Song song = getSong(cursor);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }

    @Override
    public Song queryById(String id) {
        Song song = null;
        if (!TextUtils.isEmpty(id)) {
            Cursor cursor = sqLiteDatabase.query(SongColumn.TABLE_NAME, null, SongColumn.ID + " = ?", new String[] {id}, null, null, null);
            if (cursor.moveToFirst()) {
                song = getSong(cursor);
            }
            cursor.close();
        }
        return song;
    }

    public List<Song> queryListByRoomId(String roomId) {
        Cursor cursor = sqLiteDatabase.query(SongColumn.TABLE_NAME, null, SongColumn.ROOM_ID + " = ?", new String[] {roomId}, null, null, "id");
        List<Song> songs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Song song = getSong(cursor);
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }

    @Override
    public boolean insert(Song song) {
        ContentValues songValues = new ContentValues();
        songValues.put(SongColumn.ID, song.id);
        songValues.put(SongColumn.ROOM_ID, song.roomId);
        songValues.put(SongColumn.NAME, song.name);
        songValues.put(SongColumn.SINGER, song.singer);
        songValues.put(SongColumn.COVER_URL, song.coverUrl);
        return sqLiteDatabase.insert(SongColumn.TABLE_NAME, null, songValues) != -1;
    }

    public void insertList(String roomId, List<Song> list) {
        if (list != null) {
            int id = 1;
            for (Song song : list) {
                song.id = String.valueOf(id++);
                song.roomId = roomId;
                insert(song);
            }
        }
        isUpdated = true;
    }


}
