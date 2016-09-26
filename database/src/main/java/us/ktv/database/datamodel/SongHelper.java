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
            case "日不落":
                song.singer = "蔡依林";
                song.coverUrl = "http://star.kuwo.cn/star/starheads/180/18/57/2779975859.jpg";
                song.album = "特务J";
                break;
            case "双节棍":
                song.singer = "周杰伦";
                song.coverUrl = "http://img3.kuwo.cn/star/albumcover/240/73/57/3936595753.jpg";
                song.album = "范特西";
                break;
            case "青花瓷":
                song.singer = "周杰伦";
                song.coverUrl = "http://img3.kuwo.cn/star/albumcover/240/16/26/1662595718.jpg";
                song.album = "我很忙";
                break;
            case "七里香":
                song.singer = "周杰伦";
                song.coverUrl = "http://img4.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg";
                song.album = "七里香";
                break;
            case "贝加尔湖畔":
                song.singer = "李健";
                song.coverUrl = "http://img2.kuwo.cn/star/albumcover/240/12/65/2189344037.jpg";
                song.album = "我是歌手第三季 第四期";
                break;
            case "松花江上":
                song.singer = "军旅歌曲";
                song.coverUrl = "http://img1.kuwo.cn/star/albumcover/240/46/59/1795729620.jpg";
                song.album = "革命老歌";
                break;
            case "友情岁月":
                song.singer = "汪晨蕊";
                song.coverUrl = "http://img3.kuwo.cn/star/albumcover/240/42/49/3045228253.jpg";
                song.album = "中国新歌声 第十二期";
                break;
            case "Fight Song":
                song.singer = "李佩玲";
                song.coverUrl = "http://img3.kuwo.cn/star/albumcover/240/42/49/3045228253.jpg";
                song.album = "中国新歌声 第十二期";
                break;
            case "岁月轻狂":
                song.singer = "张信哲";
                song.coverUrl = "http://img4.kuwo.cn/star/albumcover/240/92/76/1392616598.jpg";
                song.album = "歌 时代";
                break;
            case "Call Me Maybe":
                song.singer = "Carly Rae Jepsen";
                song.coverUrl = "http://img3.kuwo.cn/star/albumcover/240/75/73/1724812713.jpg";
                song.album = "Now That's What I Call Feel Good - Various Artists";
                break;
            case "Yesterday Once More":
                song.singer = "Carpenters";
                song.coverUrl = "http://img4.kuwo.cn/star/albumcover/240/10/2/2859754071.jpg";
                song.album = "Yesterday Once More: Greatest Hits 1969-1983";
                break;
            case "夜空中最亮的星":
                song.singer = "G.E.M.邓紫棋";
                song.coverUrl = "http://img2.kuwo.cn/star/albumcover/240/96/40/1928244274.jpg";
                song.album = "夜空中最亮的星";
                break;
            case "呼啸而过":
                song.singer = "陈翔";
                song.coverUrl = "http://img2.kuwo.cn/star/albumcover/240/7/11/3145343873.jpg";
                song.album = "呼啸而过";
                break;
            case "彩虹金刚":
                song.singer = "孙燕姿";
                song.coverUrl = "http://img1.kuwo.cn/star/albumcover/240/70/68/2606671502.jpg";
                song.album = "彩虹金刚";
                break;
            case "时光":
                song.singer = "许巍";
                song.coverUrl = "http://img4.kuwo.cn/star/albumcover/240/57/57/4110030229.jpg";
                song.album = "今天 许巍 2002-2008生活作品集";
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
