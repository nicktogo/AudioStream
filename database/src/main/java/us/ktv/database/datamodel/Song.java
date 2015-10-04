package us.ktv.database.datamodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nick on 15-10-5.
 */
public class Song {
    /**
     * {
     "id":"1",
     "name":"七里香",
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

}
