package us.ktv.database.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import us.ktv.database.datamodel.SongColumn;

/**
 * Created by nick on 15-10-4.
 */
public class MicDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_SONGS_TABLE = "CREATE TABLE " + SongColumn.TABLE_NAME + "(" +
            SongColumn.ID + " text primary key, " +
            SongColumn.NAME + " text, " +
            SongColumn.SINGER + " text, " +
            SongColumn.COVER_URL + " text" +
            ");";

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public MicDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SONGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
