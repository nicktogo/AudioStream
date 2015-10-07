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

    private boolean isRecording;

    public void connect(final String ip, final String port, final SocketCallbackListener listener) {
        if (socket == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket = new Socket(ip, Integer.parseInt(port));
                        socketOutput = new DataOutputStream(socket.getOutputStream());
                        socketInput = new DataInputStream(socket.getInputStream());
                        //TODO
                        //send get song list request and receive json
                        socketOutput.writeUTF("GET@SONGLIST");
                        String songList = null;
                        while  (!((songList = socketInput.readUTF()) == null)) {
                            listener.onConnect(songList);
                        }
                        listener.onConnect(songList);
                    } catch (Exception e) {
                        listener.onError(e);
                    }
                }
            }).start();
        }
    }

    public void startRecord(final SocketCallbackListener listener) {

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] audio = new byte[bufferSize];
                while (isRecording) {
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
}