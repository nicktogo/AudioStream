package us.ktv.network;

import java.net.Socket;

/**
 * Created by nick on 15-10-7.
 */
public interface SocketCallbackListener {
    void onConnect(String roomId, String songList);
    void onError(Exception e);
}
