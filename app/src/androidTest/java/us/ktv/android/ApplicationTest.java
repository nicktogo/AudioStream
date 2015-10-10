package us.ktv.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.RoomHelper;
import us.ktv.database.utils.GsonUtils;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongHelper;

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
            "    \"name\":\"192.168.1.1:8989\",\n" +
            "    \"add_time\":\"2015/10/8\"\n" +
            "}";

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
        assertEquals(true, helper.insert(song));
    }

    public void testRoomHelper() {
        create();
        Room room = GsonUtils.JsonToObject(TEXT_DATA_ROOM_JSON, Room.class);
        RoomHelper helper = RoomHelper.getInstance(mContext);
        assertEquals(true, helper.insert(room));
    }

}