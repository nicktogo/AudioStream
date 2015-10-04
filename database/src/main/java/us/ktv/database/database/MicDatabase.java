package us.ktv.database.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import us.ktv.database.R;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongColumn;


/**
 * Created by nick on 15-10-4.
 */
public class MicDatabase {

    private static MicDatabase micDatabase;
    private SQLiteDatabase sqLiteDatabase;

    private MicDatabase(Context context) {
        MicDatabaseHelper helper = new MicDatabaseHelper(
                context,
                context.getResources().getString(R.string.database_name),
                null,
                context.getResources().getInteger(R.integer.database_version));
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public synchronized static MicDatabase getInstance(Context context) {
        if (micDatabase == null) {
            micDatabase = new MicDatabase(context);
        }
        return micDatabase;
    }

    public List<Song> querySongList() {
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
        return songs;
    }

    public boolean insertSong(Song song) {

        ContentValues songValues = new ContentValues();
        songValues.put(SongColumn.ID, song.id);
        songValues.put(SongColumn.NAME, song.name);
        songValues.put(SongColumn.SINGER, song.singer);
        songValues.put(SongColumn.COVER_URL, song.coverUrl);

        return sqLiteDatabase.insert(SongColumn.TABLE_NAME, null, songValues) != -1;
    }

}
