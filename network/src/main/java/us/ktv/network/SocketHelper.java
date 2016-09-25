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

    private boolean isPlaying;

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
                return isConnected = false;
            }
        }
        return isConnected;
    }

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
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        listener.onError(new NetworkException("Socket 未被初始化，没有连接到房间。"));
                    }
                }
                recorder.stop();
                audio = null;
                try {
                    socketOutput.flush();
                    // close the socketOutput will result the app cannot record and stream to server later
                    // socketOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError(new NetworkException("flush 失败，请检查网络"));
                }
            }
        }).start();
    }

    public boolean startPlay(String songName, SocketCallbackListener listener) {
        try {
            socketOutput.writeUTF("PLAY@" + songName);
            // 验证是否播放 TODO 后端修改
            setPlaying(socketInput.readBoolean());
            return isPlaying();
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("无法播放，请检查网络"));
            return isPlaying();
        } catch (NullPointerException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("Socket 未被初始化，没有连接到房间。"));
            return isPlaying();
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setIsRecording(boolean isRecording) {
        this.isRecording = isRecording;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void stopRecording() {
        setIsRecording(false);

    }

    public void stopPlaying(SocketCallbackListener listener) {
        // setPlaying(false);
        try {
            socketOutput.writeUTF("STOP");
            // 验证是否停止 TODO 后端修改
            setPlaying(socketInput.readBoolean());
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("停止播放出错，请检查网络。"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("Socket 未被初始化，没有连接到房间。"));
        }
    }
}