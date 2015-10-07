package us.ktv.android.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.InjectView;
import us.ktv.android.BR;
import us.ktv.android.R;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongHelper;

/**
 * Created by nick on 15-10-6.
 */
public class SongListFragment extends BaseListFragment<Song> {

    public SongListFragment() {
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
        SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
        list = helper.queryList();
        updateAdapter();
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
    public void onClick(Song song) {
        mListener.onFragmentTransaction(song, this);
    }
}
