package us.ktv.database.datamodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 15-10-6.
 */
public class SongHelper extends DatamodelHelper<Song> {

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
    protected List<Song> queryList() {
        Cursor cursor = sqLiteDatabase.query(SongColumn.TABLE_NAME, null, null, null, null, null, "id");
        List<Song> songs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.id = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.ID));
                song.name = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.NAME));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.SINGER));
                song.coverUrl = cursor.getString(cursor.getColumnIndexOrThrow(SongColumn.COVER_URL));
                songs.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return songs;
    }

    @Override
    protected boolean insert(Song song) {
        ContentValues songValues = new ContentValues();
        songValues.put(SongColumn.ID, song.id);
        songValues.put(SongColumn.NAME, song.name);
        songValues.put(SongColumn.SINGER, song.singer);
        songValues.put(SongColumn.COVER_URL, song.coverUrl);
        return sqLiteDatabase.insert(SongColumn.TABLE_NAME, null, songValues) != -1;
    }
}
