package us.ktv.android;

import com.google.common.net.PercentEscaper;
import com.google.gson.internal.Streams;

import us.ktv.database.datamodel.Room;
import us.ktv.network.RecordSocketHelper;
import us.ktv.network.SocketCallbackListener;
import us.ktv.network.SocketHelper;

/**
 * Created by nick on 15-10-6.
 */
public class Presenter {
    private SocketHelper helper;
    private RecordSocketHelper recordSocketHelper;

    private static Presenter presenter;
    private static String ip;
    private static int port;

    private Presenter() {
        helper = new SocketHelper(ip, port);
        recordSocketHelper = new RecordSocketHelper(ip, port + 1);
    }

    public synchronized static Presenter getPresenter() {
        if (presenter == null) {
            presenter = new Presenter();

        }
        return presenter;
    }

    public synchronized static Presenter getPresenter(String roomIp, int roomPort) {
        if (presenter == null) {
            ip = roomIp;
            port = roomPort;
            presenter = new Presenter();

        }
        return presenter;
    }

    public boolean connect(String ip, int port, SocketCallbackListener listener) {
        boolean isHelperConnected = helper.connect(ip, port, listener);
        boolean isRecordSocketHelperConnected = recordSocketHelper.connect(ip, port + 1, listener);
        return isHelperConnected && isRecordSocketHelperConnected;
    }

    public void startRecord(String songName, SocketCallbackListener listener) {
        recordSocketHelper.startRecord(listener);
    }

    public void stopRecord() {
        recordSocketHelper.stopRecording();
    }

    public void startPlay(String songName, SocketCallbackListener listener) {
        helper.startPlay(songName, listener);
    }

    public void stopPlay(SocketCallbackListener listener) {
        helper.stopPlaying(listener);
    }

    public void addSong(String songName, SocketCallbackListener listener) {
        helper.addSong(songName, listener);
    }

    public boolean refresh(String roomId, SocketCallbackListener listener) {
        return helper.refresh(roomId, listener);
    }

}
