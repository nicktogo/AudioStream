package us.ktv.database.datamodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import us.ktv.database.R;

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
    public List<Room> queryList() {
        Cursor cursor = sqLiteDatabase.query(RoomColumn.TABLE_NAME, null, null, null, null, null, RoomColumn.ADD_TIME);
        List<Room> list = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Room room = getRoom(cursor);
                list.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public Room queryById(String id) {
        Room room = null;
        if (!TextUtils.isEmpty(id)) {
            Cursor cursor = sqLiteDatabase.query(RoomColumn.TABLE_NAME, null, RoomColumn.ID + " = ?", new String[] {id}, null, null, null);
            if (cursor.moveToFirst()) {
                room = getRoom(cursor);
            }
            cursor.close();
        }
        return room;
    }

    @Override
    public boolean insert(Room room) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RoomColumn.ID, room.id);
        contentValues.put(RoomColumn.NAME, room.name);
        contentValues.put(RoomColumn.ADD_TIME, room.addTime);
        return sqLiteDatabase.insert(RoomColumn.TABLE_NAME, null, contentValues) != -1;
    }
}
