package us.ktv.network;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public boolean connect(final String ip, final int port, final SocketCallbackListener listener) {
        if (socket == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(ip, port);
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
                    }
                }
            }).start();
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

        recorder.startRecording();
        setIsRecording(true);

        try {
            socketOutput.writeUTF("PLAY@" + songName);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        listener.onError(e);
                    }
                }
                recorder.stop();
                audio = null;
                try {
                    socketOutput.close();
                } catch (IOException e) {
                    listener.onError(e);
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