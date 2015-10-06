package us.ktv.database.datamodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nick on 15-10-6.
 */
public class RoomHelper extends DatamodelHelper<Room> {

    private static RoomHelper helper;

    private RoomHelper(Context context) {
        super(context);
    }

    public synchronized static RoomHelper getInstance(Context context) {
        if (helper == null) {
            helper = new RoomHelper(context);
        }
        return helper;
    }

    @Override
    protected List<Room> queryList() {
        Cursor cursor = sqLiteDatabase.query(RoomColumn.TABLE_NAME, null, null, null, null, null, RoomColumn.ADD_TIME);
        List<Room> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                room.id = cursor.getString(cursor.getColumnIndexOrThrow(RoomColumn.ID));
                room.name = cursor.getString(cursor.getColumnIndexOrThrow(RoomColumn.NAME));
                room.addTime = cursor.getString(cursor.getColumnIndexOrThrow(RoomColumn.ADD_TIME));
                list.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    protected boolean insert(Room room) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RoomColumn.ID, room.id);
        contentValues.put(RoomColumn.NAME, room.name);
        contentValues.put(RoomColumn.ADD_TIME, new Date().toString());
        return sqLiteDatabase.insert(RoomColumn.TABLE_NAME, null, contentValues) != -1;
    }
}
