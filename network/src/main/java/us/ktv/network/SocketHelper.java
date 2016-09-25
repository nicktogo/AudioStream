package us.ktv.network;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by nick on 15-10-7.
 */
public class SocketHelper {

    private Socket socket;
    private DataOutputStream socketOutput;
    private DataInputStream socketInput;

    private boolean isConnected = false;

    private boolean isRecording;

    /**
     *  connect operation should not be performed from another thread,
     *  this method already invoked from a background thread,
     *  shot new thread may cause the origin background thread finished earlier than the new one,
     *  hence cause unexpected result.
     * @param ip
     * @param port
     * @param listener
     * @return
     */
    public boolean connect(final String ip, final int port, final SocketCallbackListener listener) {
        if (socket == null) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, port), 5000);
                socketOutput = new DataOutputStream(socket.getOutputStream());
                socketInput = new DataInputStream(socket.getInputStream());
                //TODO
                //send get song list request and receive json
                socketOutput.writeUTF("GET@SONGLIST");
                String songList;
                while (true) {
                    if (!((songList = socketInput.readUTF()) == null)) {
                        listener.onConnect(ip + ":" + port, songList);
                        isConnected = true;
                        break;
                    }
                }
            } catch (Exception e) {
                listener.onError(e);
                return isConnected;
            }
        }
        return isConnected;
    }
    // TODO move to service with thread?
    public void startRecord(String songName, final SocketCallbackListener listener) {

        final int bufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        final AudioRecord recorder = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        // TODO handle exception properly
        try {
            socketOutput.writeUTF("PLAY@" + songName);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("无法播放，请检查网络"));
            return;
        } catch (NullPointerException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("SocketOutput 未被初始化，没有连接到房间。"));
            return;
        }

        recorder.startRecording();
        setIsRecording(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] audio = new byte[bufferSize];
                listener.onConnect(null, null);
                while (isRecording()) {
                    recorder.read(audio, 0, audio.length);
                    try {
                        socketOutput.write(audio, 0, audio.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onError(new NetworkException("无法推流，请检查网络"));
                        recorder.stop();
                        return;
                    }
                }
                recorder.stop();
                audio = null;
                try {
                    socketOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(new NetworkException("推流关闭失败，请检查网络"));
                }
            }
        }).start();
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public void stop() {
        setIsRecording(false);
        try {
            socketOutput.writeUTF("STOP");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}