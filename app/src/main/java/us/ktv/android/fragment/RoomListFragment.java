package us.ktv.android.fragment;

import android.os.Bundle;
import android.view.View;

import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.RoomHelper;

/**
 * Created by nick on 15-10-6.
 */
public class RoomListFragment extends BaseListFragment<Room> {

    public RoomListFragment() {
        super();
    }

    public static SongListFragment newInstance(int itemLayoutId) {
        SongListFragment fragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.ITEM_LAYOUT_ID, itemLayoutId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemLayoutId = getArguments().getInt(BaseListFragment.ITEM_LAYOUT_ID);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoomHelper helper = RoomHelper.getInstance(MicApplication.getInstance());
        list = helper.queryList();
        updateAdapter();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected int getVariable() {
        return 0;
    }

    @Override
    public void onClick(Room room) {

    }
}
