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

    public void connect(String ip, int port, SocketCallbackListener listener) {
        helper.connect(ip, port, listener);
    }

    public void start(SocketCallbackListener listener) {
        helper.startRecord(listener);
    }

    public void stop(boolean stop) {
        helper.setIsRecording(stop);
    }

}
