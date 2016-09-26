package us.ktv.android.fragment;

import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import us.ktv.database.datamodel.Song;
import us.ktv.database.utils.GsonUtils;

/**
 * Created by I321298 on 9/26/2016.
 */

public class AllSongListFragment extends SongListFragment {

    public static String ALL_SONGS = "[\n" +
            "    {\n" +
            "        \"album\":\"新长征路上的摇滚\",\n" +
            "        \"cover_url\":\"http://image.cnwest.com/attachement/jpg/site1/20090209/001372d8a3140af9c53711.jpg\",\n" +
            "        \"id\":\"90\",\n" +
            "        \"name\":\"一无所有\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"崔健\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"我是歌手\",\n" +
            "        \"cover_url\":\"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=3249849380,444900555&fm=58\",\n" +
            "        \"id\":\"91\",\n" +
            "        \"name\":\"假如爱有天意\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"李健\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"我要快乐\",\n" +
            "        \"cover_url\":\"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3059212860,302126209&fm=58\",\n" +
            "        \"id\":\"92\",\n" +
            "        \"name\":\"单纯\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"张惠妹\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"双\",\n" +
            "        \"cover_url\":\"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=4113596581,2105788036&fm=58\",\n" +
            "        \"id\":\"93\",\n" +
            "        \"name\":\"发现\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"赵薇\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"情深深雨蒙蒙\",\n" +
            "        \"cover_url\":\"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1769896624,3408692146&fm=58\",\n" +
            "        \"id\":\"94\",\n" +
            "        \"name\":\"情深深雨蒙蒙\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"赵薇\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"身边的故事\",\n" +
            "        \"cover_url\":\"http://y.gtimg.cn/music/photo_new/T002R300x300M000001msoYJ4PUYZ3.jpg\",\n" +
            "        \"id\":\"95\",\n" +
            "        \"name\":\"想你的夜\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"关喆\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"花天喜事\",\n" +
            "        \"cover_url\":\"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1353497666,2886130277&fm=58\",\n" +
            "        \"id\":\"96\",\n" +
            "        \"name\":\"我的果汁分你一半\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"花儿乐队\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"我真的可以\",\n" +
            "        \"cover_url\":\"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1359469010,2942329269&fm=58\",\n" +
            "        \"id\":\"97\",\n" +
            "        \"name\":\"我真的可以\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"郑中基\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"想念你\",\n" +
            "        \"cover_url\":\"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=4007465494,1840738027&fm=58\",\n" +
            "        \"id\":\"98\",\n" +
            "        \"name\":\"松花江\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"李健\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"江蕙世纪金选\",\n" +
            "        \"cover_url\":\"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=803814676,2771015965&fm=58\",\n" +
            "        \"id\":\"99\",\n" +
            "        \"name\":\"艺界人生\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"江蕙\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"album\":\"最完美影音典藏精选\",\n" +
            "        \"cover_url\":\"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1987935394,3307718210&fm=58\",\n" +
            "        \"id\":\"100\",\n" +
            "        \"name\":\"过完冬季\",\n" +
            "        \"room_id\":\"192.168.1.1:8989\",\n" +
            "        \"singer\":\"李玟\"\n" +
            "    }\n" +
            "]";
}
