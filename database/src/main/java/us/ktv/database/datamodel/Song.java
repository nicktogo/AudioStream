package us.ktv.database.datamodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nick on 15-10-5.
 */
public class Song implements Serializable {
    /**
     * {
     "id":"1",
     "room_id":"192.168.1.1:8989",
     "name":"七里香",
     "album":"七里香",
     "singer":"周杰伦",
     "cover_url":"http://img1.kwcdn.kuwo.cn/star/albumcover/240/22/76/1300305679.jpg"
     }
     */

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("singer")
    public String singer;

    @SerializedName("cover_url")
    public String coverUrl;

    @SerializedName("room_id")
    public String roomId;

    @SerializedName("album")
    public String album;
}
