package us.ktv.android.fragment;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.InjectView;
import mehdi.sakout.fancybuttons.FancyButton;
import us.ktv.android.BR;
import us.ktv.android.Presenter;
import us.ktv.android.R;
import us.ktv.android.activity.MainActivity;
import us.ktv.android.utils.MicApplication;
import us.ktv.database.datamodel.Song;
import us.ktv.database.datamodel.SongColumn;
import us.ktv.database.datamodel.SongHelper;
import us.ktv.network.NetworkException;
import us.ktv.network.SocketCallbackListener;

public class PlaySongFragment extends Fragment implements View.OnClickListener {

    private Song song = null;

    @InjectView(R.id.control)
    FancyButton controlButton;

    public static PlaySongFragment newInstance(String songId) {
        PlaySongFragment fragment = new PlaySongFragment();
        Bundle args = new Bundle();
        args.putString(SongColumn.ID, songId);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaySongFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SongHelper helper = SongHelper.getInstance(MicApplication.getInstance());
            song = helper.queryById(getArguments().getString(SongColumn.ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_play_song, container, false);
        binding.setVariable(BR.playingSong, song);
        controlButton = (FancyButton) binding.getRoot().findViewById(R.id.control);
        controlButton.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(song.name);
    }

    @Override
    public void onClick(View v) {
        Presenter presenter = Presenter.getPresenter();
        final FancyButton fancyButton = (FancyButton) v;
        String text = fancyButton.getText().toString();

        switch (text) {
            case "Stop" :
                presenter.stopPlay(new SocketCallbackListener() {
                    @Override
                    public void onConnect(String roomId, String songList) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fancyButton.setText(getString(R.string.play));
                                fancyButton.setIconResource("\uF04B");
                            }
                        });
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
                presenter.stopRecord();
                break;

            case "Play" :
                SocketCallbackListener listener = new SocketCallbackListener() {
                    @Override
                    public void onConnect(String roomId, String songList) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fancyButton.setText(getString(R.string.stop));
                                fancyButton.setIconResource("\uF04C");
                            }
                        });
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
                };
                presenter.startPlay(song.name, listener);
                presenter.startRecord(song.name, listener);
                break;
        }

    }
}

