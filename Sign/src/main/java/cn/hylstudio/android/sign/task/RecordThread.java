package cn.hylstudio.android.sign.task;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

import cn.hylstudio.android.sign.model.DataBlock;
import cn.hylstudio.android.sign.presenter.MainPresenter;

/**
 * Created by HYL on 2016/9/14.
 */
public class RecordThread implements Runnable {
    public static final String TAG = "thread";
    private MainPresenter mainPresenter;
    private BlockingQueue<DataBlock> blockingQueue;

    int frequency = 16000;
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    int blockSize = 1024;

    public RecordThread(MainPresenter mainPresenter, BlockingQueue<DataBlock> blockingQueue) {
        this.mainPresenter = mainPresenter;
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

        AudioRecord audioRecord = new AudioRecord(mainPresenter.getAudioSource(), frequency, channelConfiguration, audioEncoding, bufferSize);

        try {

            short[] buffer = new short[blockSize];

            audioRecord.startRecording();

            while (mainPresenter.isStarted()) {
                int bufferReadSize = audioRecord.read(buffer, 0, blockSize);

                DataBlock dataBlock = new DataBlock(buffer, blockSize, bufferReadSize);

//                Log.d(TAG, "doInBackground: put");
                blockingQueue.put(dataBlock);
//                Log.d(TAG, "doInBackground: 111111111111111111111111111111111111");
            }

        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }

        audioRecord.stop();
    }

}
