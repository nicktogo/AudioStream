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

import mehdi.sakout.fancybuttons.FancyButton;
import us.ktv.android.BR;
import us.ktv.android.Presenter;
import us.ktv.android.R;
import us.ktv.android.activity.AddRoomActivity;
import us.ktv.android.activity.MainActivity;
import us.ktv.android.transition.SongTransition;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.RoomHelper;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongHelper;
import us.ktv.database.utils.GsonUtils;
import us.ktv.network.NetworkException;
import us.ktv.network.SocketCallbackListener;

/**
 * Created by nick on 15-10-6.
 */
public class SongListFragment extends BaseListFragment<Song> {

    private String roomId;
    private boolean isAll;

    public static String IS_ALL = "is_all";

    public SongListFragment() {
        super();
    }

    public static SongListFragment newInstance(int itemLayoutId, String roomId, boolean isAll) {
        SongListFragment fragment = new SongListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.ITEM_LAYOUT_ID, itemLayoutId);
        bundle.putString(AddRoomActivity.ROOM_ID, roomId);
        bundle.putBoolean(IS_ALL, isAll);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemLayoutId = getArguments().getInt(BaseListFragment.ITEM_LAYOUT_ID);
            roomId = getArguments().getString(AddRoomActivity.ROOM_ID);
            isAll = getArguments().getBoolean(IS_ALL);
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
        getData(isAll);
        //refresh();
        autoRefresh();
    }

    protected void getData(boolean isAll) {
        if (isAll) {
            list = GsonUtils.JsonToObject(AllSongListFragment.ALL_SONGS, new TypeToken<List<Song>>() {
            }.getType());
        } else {
            SongHelper songHelper = SongHelper.getInstance(MicApplication.getInstance());
            list = songHelper.queryListByRoomId(roomId);
        }
        updateAdapter();
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
                boolean isRefreshed = Presenter.getPresenter().refresh(getRoomId(), new SocketCallbackListener() {
                    @Override
                    public void onConnect(String roomId, String songList) {
                        List<Song> list = GsonUtils.JsonToObject(songList, new TypeToken<List<Song>>() {
                        }.getType());
                        SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
                        helper.insertList(roomId, list);
                    }

                    @Override
                    public void onError(Exception e) {
                        if (e instanceof NetworkException) {
                            NetworkException ne = (NetworkException) e;
                            ((MainActivity) getActivity()).showSnackbar(ne.getMessage());
                        }
                    }
                });
                return isRefreshed;
            }

            @Override
            protected void onPostExecute(final Boolean isRefreshed) {
                if (!isAll) {
                    list = SongHelper.getInstance(MicApplication.getInstance()).queryListByRoomId(roomId);
                    updateAdapter();
                }
                swipeToLoadLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeToLoadLayout.setRefreshing(false);
                        if (!isRefreshed) {
                            ((MainActivity) getActivity()).showSnackbar("无法连接到房间，请检查网络。");
                        } else {
                            ((MainActivity) getActivity()).showSnackbar("列表已经更新。");
                        }
                    }
                });
            }
        }.execute(roomId);
    }

    @Override
    public void onClick(final Song song, View view) {
        if (isAll) {
            Presenter.getPresenter().addSong(song.name, new SocketCallbackListener() {
                @Override
                public void onConnect(String roomId, String songList) {
                    ((MainActivity) getActivity()).showSnackbar("歌曲：" + song.name + " 已经添加");
                    song.roomId = getRoomId();
                    SongHelper.getInstance(MicApplication.getInstance()).insert(song);
                }

                @Override
                public void onError(Exception e) {
                    if (e instanceof NetworkException) {
                        NetworkException ne = (NetworkException) e;
                        ((MainActivity) getActivity()).showSnackbar(ne.getMessage());
                    }
                }
            });
        } else {
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
//        swipeToLoadLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeToLoadLayout.setRefreshing(true);
//            }
//        });
//        if (!isAll) {
//            list = SongHelper.getInstance(MicApplication.getInstance()).queryListByRoomId(getRoomId());
//            updateAdapter();
//        }
//        swipeToLoadLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeToLoadLayout.setRefreshing(false);
//            }
//        });
//        swipeToLoadLayout.setRefreshing(true);
//        new Thread() {
//            @Override
//            public void run() {
//                Presenter.getPresenter().refresh(getRoomId(), new SocketCallbackListener() {
//                    @Override
//                    public void onConnect(String roomId, String songList) {
//                        List<Song> list = GsonUtils.JsonToObject(songList, new TypeToken<List<Song>>() {
//                        }.getType());
//                        SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
//                        helper.insertList(roomId, list);
//                        swipeToLoadLayout.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                swipeToLoadLayout.setRefreshing(false);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        if (e instanceof NetworkException) {
//                            final NetworkException ne = (NetworkException) e;
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ((MainActivity) getActivity()).showSnackbar(ne.getMessage());
//                                    swipeToLoadLayout.setRefreshing(false);
//                                }
//                            });
//                        }
//                    }
//                });
//            }
//        }.start();
        autoRefresh();
    }

    private String getRoomId() {
        return this.roomId;
    }
}
