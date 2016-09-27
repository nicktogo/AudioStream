package us.ktv.network;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by I321298 on 9/26/2016.
 */

public class RecordSocketHelper {

    private Socket recordSocket;
    private DataOutputStream recordSocketOutput;
    private DataInputStream recordSocketInput;

    private boolean isConnected = false;

    private boolean isRecording;

    private String ip;
    private int port;

    public RecordSocketHelper(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

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
        if (recordSocket == null) {
            try {
//                recordSocket = new Socket();
//                recordSocket.connect(new InetSocketAddress(ip, port), 60000);
//                recordSocketOutput = new DataOutputStream(recordSocket.getOutputStream());
//                recordSocketInput = new DataInputStream(recordSocket.getInputStream());
                initSocket();
                setConnected(true);
            } catch (IOException e) {
                listener.onError(new NetworkException("连接出错。"));
            }
        }
        return isConnected();
    }

    private void initSocket() throws IOException {
        recordSocket = new Socket();
        recordSocket.connect(new InetSocketAddress(ip, port), 5000);
        recordSocketOutput = new DataOutputStream(recordSocket.getOutputStream());
        recordSocketInput = new DataInputStream(recordSocket.getInputStream());
    }

    public void startRecord(final SocketCallbackListener listener) {

        new Thread(new Runnable() {

            int bufferSize = AudioRecord.getMinBufferSize(
                    44100,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);

            AudioRecord recorder = new AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    44100,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize);

            @Override
            public void run() {
                recorder.startRecording();
                setRecording(true);
                listener.onConnect(null, null);
                byte[] audio = new byte[bufferSize];
                while (isRecording()) {
                    recorder.read(audio, 0, audio.length);
                    try {
                        if (recordSocket == null) {
                            initSocket();
                        }
                        recordSocketOutput.write(audio, 0, audio.length);
                    } catch (IOException e) {
                        e.printStackTrace();
                        listener.onError(new NetworkException("无法推流，请检查网络"));
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        return;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        listener.onError(new NetworkException("Socket 未被初始化，没有连接到房间。"));
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                        Log.d("SocketHelper", "NullPointerException");
                        return;
                    }
                }
                recorder.stop();
                recorder.release();
                recorder = null;
                audio = null;
                try {
                    recordSocketOutput.flush();
                    // close the socketOutput will result the app cannot record and stream to server later
                    // socketOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(new NetworkException("flush 失败，请检查网络"));
                }
            }
        }).start();
    }

    public void stopRecording() {
        setRecording(false);
//        try {
//            recordSocketOutput.writeUTF("STOP");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
