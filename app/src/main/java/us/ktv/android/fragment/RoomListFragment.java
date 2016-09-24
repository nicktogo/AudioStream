package us.ktv.android.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import us.ktv.android.BR;
import us.ktv.android.R;
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

    public static RoomListFragment newInstance(int itemLayoutId) {
        RoomListFragment fragment = new RoomListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        recyclerView = (RecyclerView) rootView.findViewById(android.R.id.list);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RoomHelper helper = RoomHelper.getInstance(MicApplication.getInstance());
        list = helper.queryList();
        updateAdapter();

        ButterKnife.findById(view, R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(null, v);
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room_list;
    }

    @Override
    protected int getVariable() {
        return BR.room;
    }

    @Override
    protected void autoRefresh() {

    }

    @Override
    public void onClick(Room room, View view) {
        //TODO SocketHelper connect!
        mListener.onFragmentInteraction(room, view);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }
}
