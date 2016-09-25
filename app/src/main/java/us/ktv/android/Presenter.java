package us.ktv.android;

import us.ktv.network.SocketCallbackListener;
import us.ktv.network.SocketHelper;

/**
 * Created by nick on 15-10-6.
 */
public class Presenter {
    private SocketHelper helper;

    private static Presenter presenter;

    private Presenter() {
        helper = new SocketHelper();
    }

    public synchronized static Presenter getPresenter() {
        if (presenter == null) {
            presenter = new Presenter();

        }
        return presenter;
    }

    public boolean connect(String ip, int port, SocketCallbackListener listener) {
        return helper.connect(ip, port, listener);
    }

    public void startRecord(String songName, SocketCallbackListener listener) {
        helper.startRecord(songName, listener);
    }

    public void stopRecord() {
        helper.stopRecording();
    }

    public void startPlay(String songName, SocketCallbackListener listener) {
        helper.startPlay(songName, listener);
    }

    public void stopPlay(SocketCallbackListener listener) {
        helper.stopPlaying(listener);
    }

}
