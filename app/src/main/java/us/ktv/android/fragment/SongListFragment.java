package us.ktv.android.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import us.ktv.android.BR;
import us.ktv.android.Presenter;
import us.ktv.android.R;
import us.ktv.android.activity.AddRoomActivity;
import us.ktv.android.transition.SongTransition;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.RoomHelper;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongHelper;
import us.ktv.database.utils.GsonUtils;
import us.ktv.network.SocketCallbackListener;

/**
 * Created by nick on 15-10-6.
 */
public class SongListFragment extends BaseListFragment<Song> {

    private String roomId;

    public SongListFragment() {
        super();
    }

    public static SongListFragment newInstance(int itemLayoutId, String roomId) {
        SongListFragment fragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.ITEM_LAYOUT_ID, itemLayoutId);
        bundle.putString(AddRoomActivity.ROOM_ID, roomId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemLayoutId = getArguments().getInt(BaseListFragment.ITEM_LAYOUT_ID);
            roomId = getArguments().getString(AddRoomActivity.ROOM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.swipe_target);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SongHelper songHelper = SongHelper.getInstance(MicApplication.getInstance());
        list = songHelper.queryListByRoomId(roomId);
        RoomHelper roomHelper = RoomHelper.getInstance(MicApplication.getInstance());
        Room room = roomHelper.queryById(roomId);
        TextView toolBarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toolBarTitle.setText(room.name);
        updateAdapter();
        //refresh();
        autoRefresh();
    }

    // TODO, perform AsyncTask to assist pullToRefresh
    protected void refresh() {
        while (true) {
            SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
            if (helper.isUpdated) {
                helper.isUpdated = false;
                list = helper.queryListByRoomId(roomId);
                updateAdapter();
                break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_list;
    }

    @Override
    protected int getVariable() {
        return BR.song;
    }

    @Override
    protected void autoRefresh() {

        // Connect to client and update song list
        new AsyncTask<String, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                swipeToLoadLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setRefreshing(true);
                    }
                });
            }

            @Override
            protected Boolean doInBackground(String... params) {
                String roomId = params[0];
                String ip = roomId.split(":")[0];
                int port = Integer.parseInt(roomId.split(":")[1]);
                boolean isConnected = Presenter.getPresenter().connect(ip, port, new SocketCallbackListener() {
                    @Override
                    public void onConnect(String roomId, String songList) {
                        List<Song> list = GsonUtils.JsonToObject(songList, new TypeToken<List<Song>>() {
                        }.getType());
                        SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
                        helper.insertList(roomId, list);
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
                return isConnected;
            }

            @Override
            protected void onPostExecute(final Boolean isConnected) {
                swipeToLoadLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setRefreshing(false);
                        if (!isConnected) {
                            Toast.makeText(
                                    MicApplication.getInstance(),
                                    "无法连接到房间，请检查网络。",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
            }
        }.execute(roomId);
    }

    @Override
    public void onClick(Song song, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementReturnTransition(new SongTransition());
            setExitTransition(new Fade());
            SimpleDraweeView cover = (SimpleDraweeView) view.findViewById(R.id.cover);
            ViewCompat.setTransitionName(cover, song.id);
            TextView songName = (TextView) view.findViewById(R.id.name);
            ViewCompat.setTransitionName(songName, song.id + "_" +song.name);
        }
        mListener.onFragmentInteraction(song, view);
    }

    @Override
    public void onLoadMore() {
        Log.d("SongListFragment", "LoadMore");
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        Log.d("SongListFragment", "Refresh");
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
