package us.ktv.database.datamodel;

import android.content.Context;
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

    protected abstract List<T> queryList();

    protected abstract boolean insert(T t);

}
