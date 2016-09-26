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
        switch (song.name) {
            case "一无所有":
                song.singer = "崔健";
                song.coverUrl = "http://image.cnwest.com/attachement/jpg/site1/20090209/001372d8a3140af9c53711.jpg";
                song.album = "新长征路上的摇滚";
                break;
            case "假如爱有天意":
                song.singer = "李健";
                song.coverUrl = "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3249849380,444900555&fm=58";
                song.album = "我是歌手";
                break;
            case "单纯":
                song.singer = "张惠妹";
                song.coverUrl = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3059212860,302126209&fm=58";
                song.album = "我要快乐";
                break;
            case "发现":
                song.singer = "赵薇";
                song.coverUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=4113596581,2105788036&fm=58";
                song.album = "双";
                break;
            case "情深深雨蒙蒙":
                song.singer = "赵薇";
                song.coverUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1769896624,3408692146&fm=58";
                song.album = "情深深雨蒙蒙";
                break;
            case "想你的夜":
                song.singer = "关喆";
                song.coverUrl = "http://y.gtimg.cn/music/photo_new/T002R300x300M000001msoYJ4PUYZ3.jpg";
                song.album = "身边的故事";
                break;
            case "我的果汁分你一半":
                song.singer = "花儿乐队";
                song.coverUrl = "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1353497666,2886130277&fm=58";
                song.album = "花天喜事";
                break;
            case "我真的可以":
                song.singer = "郑中基";
                song.coverUrl = "https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1359469010,2942329269&fm=58";
                song.album = "我真的可以";
                break;
            case "艺界人生":
                song.singer = "江蕙";
                song.coverUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=803814676,2771015965&fm=58";
                song.album = "江蕙世纪金选";
                break;
            case "过完冬季":
                song.singer = "李玟";
                song.coverUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1987935394,3307718210&fm=58";
                song.album = "最完美影音典藏精选";
                break;
            case "松花江":
                song.singer = "李健";
                song.coverUrl = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=4007465494,1840738027&fm=58";
                song.album = "想念你";
                break;
        }
        songValues.put(SongColumn.SINGER, song.singer);
        songValues.put(SongColumn.COVER_URL, song.coverUrl);
        songValues.put(SongColumn.ALBUM, song.album);
        return sqLiteDatabase.insert(SongColumn.TABLE_NAME, null, songValues) != -1;
    }

    public void insertList(String roomId, List<Song> list) {
        if (list != null) {
            purgeSongList(roomId);
            int id = 1;
            for (Song song : list) {
                song.id = String.valueOf(id++);
                song.roomId = roomId;
                insert(song);
            }
        }
        isUpdated = true;
    }

    private void purgeSongList(String roomId) {
        sqLiteDatabase.delete(SongColumn.TABLE_NAME, SongColumn.ROOM_ID + " = ?", new String[] {roomId});
    }


}
