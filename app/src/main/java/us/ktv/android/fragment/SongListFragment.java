package us.ktv.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.ktv.android.BR;
import us.ktv.android.R;
import us.ktv.android.activity.AddRoomActivity;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongHelper;

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
        SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
        list = helper.queryListByRoomId(roomId);
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
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onClick(Song song, View view) {
        mListener.onFragmentInteraction(song, view);
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
