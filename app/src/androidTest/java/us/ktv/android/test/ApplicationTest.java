package us.ktv.android.test;

import android.app.Application;
import android.test.ApplicationTestCase;

import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.RoomHelper;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongHelper;
import us.ktv.database.utils.GsonUtils;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private static final String TEXT_DATA_SONG_JSON = "{\n" +
            "    \"id\":\"1\",\n" +
            "    \"room_id\":\"192.168.1.1:8989\",\n" +
            "    \"name\":\"七里香\",\n" +
            "    \"singer\":\"周杰伦\",\n" +
            "    \"cover_url\":\"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg\"\n" +
            "}";

    private static final String TEXT_DATA_ROOM_JSON = "{\n" +
            "    \"id\":\"192.168.1.1:8989\",\n" +
            "    \"name\":\"这是名字\",\n" +
            "    \"add_time\":\"2015/10/08\"\n" +
            "}";

    private static final String TEXT_DATA_SONG_LIST_JSON = "[{\n" +
            "\"id\": \"1\",\n" +
            "\"room_id\": \"192.168.1.1:9090\",\n" +
            "\"name\":\"七里香\",\n" +
            "\"singer\":\"周杰伦\",\n" +
            "\"cover_url\":\"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg\"\n" +
            "},\n" +
            "{\n" +
            "\"id\": \"2\",\n" +
            "\"room_id\": \"192.168.1.1:9090\",\n" +
            "\"name\":\"七里香\",\n" +
            "\"singer\":\"周杰伦\",\n" +
            "\"cover_url\":\"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg\"\n" +
            "},\n" +
            "{\n" +
            "\"id\": \"3\",\n" +
            "\"room_id\": \"192.168.1.1:9090\",\n" +
            "\"name\":\"七里香\",\n" +
            "\"singer\":\"周杰伦\",\n" +
            "\"cover_url\":\"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg\"\n" +
            "},\n" +
            "{\n" +
            "\"id\": \"4\",\n" +
            "\"room_id\": \"192.168.1.1:9090\",\n" +
            "\"name\":\"七里香\",\n" +
            "\"singer\":\"周杰伦\",\n" +
            "\"cover_url\":\"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg\"\n" +
            "},\n" +
            "{\n" +
            "\"id\": \"5\",\n" +
            "\"room_id\": \"192.168.1.1:9090\",\n" +
            "\"name\":\"七里香\",\n" +
            "\"singer\":\"周杰伦\",\n" +
            "\"cover_url\":\"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg\"\n" +
            "},\n" +
            "{\n" +
            "\"id\": \"6\",\n" +
            "\"room_id\": \"192.168.1.1:9090\",\n" +
            "\"name\":\"七里香\",\n" +
            "\"singer\":\"周杰伦\",\n" +
            "\"cover_url\":\"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg\"\n" +
            "}]";

    public ApplicationTest() {
        super(Application.class);
    }

    private void create() {
        if (getApplication() == null) {
            createApplication();
        }
    }

    public void testSongHelper() {
        create();
        Song song = GsonUtils.JsonToObject(TEXT_DATA_SONG_JSON, Song.class);
        SongHelper helper = SongHelper.getInstance(mContext);
        String[] names = new String[] {
                "日不落",
                "双节棍",
                "青花瓷",
                "七里香",
                "贝加尔湖畔",
                "松花江上",
                "友情岁月",
                "Fight Song",
                "岁月轻狂",
                "Call Me Maybe",
                "Yesterday Once More",
                "夜空中最亮的星",
                "呼啸而过",
                "彩虹金刚",
                "时光",
        };
        for (int i = 1; i < 16; i++) {
            song.id = String.valueOf(i);
            song.name = names[i - 1];
            assertEquals(true, helper.insert(song));
        }
    }

    public void testRoomHelper() {
        create();
        Room room = GsonUtils.JsonToObject(TEXT_DATA_ROOM_JSON, Room.class);
        RoomHelper helper = RoomHelper.getInstance(mContext);
        assertEquals(true, helper.insert(room));
        Room room1 = helper.queryById(room.id);
        assertNotNull(room1);
        assertEquals(room.id, room1.id);
        assertEquals(room.name, room1.name);
        assertEquals(room.addTime, room1.addTime);
    }

}