package us.ktv.database.datamodel;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nick on 15-10-6.
 */
public class Room {
    /**
     * {
     "id":"MSEFAPWO",
     "name":"YoYo",
     "add_time":"2015/10/8"
     }
     */

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("add_time")
    public String addTime;
}
