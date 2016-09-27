package us.ktv.android.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import us.ktv.android.Presenter;
import us.ktv.android.R;
import us.ktv.android.activity.MainActivity;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Room;
import us.ktv.database.datamodel.RoomHelper;
import us.ktv.network.NetworkException;
import us.ktv.network.SocketCallbackListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends Fragment {

    private static final String ROOM_ID = "room_id";

    private String roomId;

    private OnFragmentInteractionListener mListener;

    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param roomId room id.
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance(String roomId) {
        RoomFragment fragment = new RoomFragment();
        Bundle args = new Bundle();
        args.putString(ROOM_ID, roomId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomId = getArguments().getString(ROOM_ID);
            Presenter.getPresenter(roomId.split(":")[0], Integer.parseInt(roomId.split(":")[1]));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_room, container, false);
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.song_list_tab_layout);
        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.song_list_view_pager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText(R.string.all_song_tab);
        tabLayout.getTabAt(1).setText(R.string.user_song_tab);

        Room room = RoomHelper.getInstance(MicApplication.getInstance()).queryById(roomId);
        TextView toolBarTitle = (TextView) rootView.findViewById(R.id.toolbar_title);
        toolBarTitle.setText(room.name);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.room_toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            boolean isStarted = false;

            @Override
            public boolean onMenuItemClick(final MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_record:
                        if (!isStarted) {
                            Presenter.getPresenter().startRecord(null, new SocketCallbackListener() {
                                @Override
                                public void onConnect(String roomId, String songList) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            item.setIcon(R.drawable.ic_mic_white_24dp);
                                            ((MainActivity) getActivity()).showSnackbar("麦克风已经开启");
                                        }
                                    });
                                    isStarted = true;
                                }

                                @Override
                                public void onError(Exception e) {
                                    if (e instanceof NetworkException) {
                                        final NetworkException ne = (NetworkException) e;
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((MainActivity) getActivity()).showSnackbar(ne.getMessage());
                                            }
                                        });
                                    }
                                }
                            });
//                            isStarted = true;
                            Log.d("RoomFragment", "start");
                        } else {
                            Presenter.getPresenter().stopRecord();
                            item.setIcon(R.drawable.ic_mic_none_white_24dp);
                            ((MainActivity) getActivity()).showSnackbar("麦克风已经关闭");
                            isStarted = false;
                            Log.d("RoomFragment", "stop");
                        }
                        break;
                }
                return false;
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        int tabCount = 2;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SongListFragment.newInstance(R.layout.item_all_song, roomId, true);
                case 1:
                    return SongListFragment.newInstance(R.layout.item_song, roomId, false);
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
