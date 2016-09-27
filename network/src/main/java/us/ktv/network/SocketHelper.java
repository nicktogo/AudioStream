package us.ktv.network;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
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

    private String ip;
    private int port;

    public SocketHelper(String ip, int port) {
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
        if (socket == null) {
            try {
//                socket = new Socket();
//                socket.connect(new InetSocketAddress(ip, port), 60000);
//                socketOutput = new DataOutputStream(socket.getOutputStream());
//                socketInput = new DataInputStream(socket.getInputStream());
                initSocket();
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

    private void initSocket() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), 5000);
        socketOutput = new DataOutputStream(socket.getOutputStream());
        socketInput = new DataInputStream(socket.getInputStream());
    }

    public void startRecord(String songName, final SocketCallbackListener listener) {

//        try {
//            socketOutput.writeUTF("PLAY@" + songName);
//        } catch (IOException e) {
//            e.printStackTrace();
//            listener.onError(new NetworkException("无法播放，请检查网络"));
//            return;
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            listener.onError(new NetworkException("SocketOutput 未被初始化，没有连接到房间。"));
//            return;
//        }



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
                setIsRecording(true);
                listener.onConnect(null, null);
                byte[] audio = new byte[bufferSize];
                while (isRecording()) {
                    recorder.read(audio, 0, audio.length);
                    try {
                        socketOutput.write(audio, 0, audio.length);
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
                        Log.d("SockerHelper", "NullPointerException");
                        return;
                    }
                }
                recorder.stop();
                recorder.release();
                recorder = null;
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

    public void stopRecording() {
        setIsRecording(false);
        // TODO wirteUTF("STOP")
    }

    public boolean startPlay(String songName, SocketCallbackListener listener) {
        try {
            if (socket == null) {
                initSocket();
            }
            socketOutput.writeUTF("PLAY@" + songName);
            // 验证是否播放 TODO 后端修改
            // setPlaying(socketInput.readBoolean());
            listener.onConnect(null, null);
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

    public void stopPlaying(SocketCallbackListener listener) {
        // setPlaying(false);
        try {
            if (socket == null) {
                initSocket();
            }
            socketOutput.writeUTF("STOP");
            // 验证是否停止 TODO 后端修改
            // setPlaying(socketInput.readBoolean());
            listener.onConnect(null, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("停止播放出错，请检查网络。"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("Socket 未被初始化，没有连接到房间。"));
        }
    }

    public void addSong(String songName, SocketCallbackListener listener) {
        try {
            if (socket == null) {
                initSocket();
            }
            socketOutput.writeUTF("ADD@" + songName);
            listener.onConnect(null, null);
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("添加歌曲出错，请检查网络。"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("Socket 未被初始化，没有连接到房间。"));
        }
    }

    public boolean refresh(String roomId, SocketCallbackListener listener) {
        try {
            if (socket == null) {
                initSocket();
            }
            socketOutput.writeUTF("REFRESH");
            String songList;
            while (true) {
                if (!((songList = socketInput.readUTF()) == null)) {
                    listener.onConnect(roomId, songList);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("刷新歌曲出错，请检查网络。"));
            return false;
        } catch (NullPointerException e) {
            e.printStackTrace();
            listener.onError(new NetworkException("Socket 未被初始化，没有连接到房间。"));
            return false;
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
}