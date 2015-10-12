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

    public void start(String songName, SocketCallbackListener listener) {
        helper.startRecord(songName, listener);
    }

    public void stop() {
        helper.stop();
    }

}
