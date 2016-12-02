package cn.hylstudio.android.sign.task;

import java.util.concurrent.BlockingQueue;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.util.Log;

import cn.hylstudio.android.sign.model.DataBlock;
import cn.hylstudio.android.sign.presenter.MainPresenter;

public class RecordTask extends AsyncTask<Void, Object, Void> {


    int frequency = 16000;
//    int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
    int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    int blockSize = 1024;

    MainPresenter mainPresenter;
    BlockingQueue<DataBlock> blockingQueue;

    public RecordTask(MainPresenter mainPresenter, BlockingQueue<DataBlock> blockingQueue) {
        this.mainPresenter = mainPresenter;
        this.blockingQueue = blockingQueue;
    }

    @Override
    protected Void doInBackground(Void... params) {

        int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);

        AudioRecord audioRecord = new AudioRecord(mainPresenter.getAudioSource(), frequency, channelConfiguration, audioEncoding, bufferSize);

        try {

            short[] buffer = new short[blockSize];

            audioRecord.startRecording();

            while (mainPresenter.isStarted()) {
                int bufferReadSize = audioRecord.read(buffer, 0, blockSize);

                DataBlock dataBlock = new DataBlock(buffer, blockSize, bufferReadSize);

                Log.d("recoding", "doInBackground: put");
                blockingQueue.put(dataBlock);
                notifyAll();
                Log.d("recoding", "doInBackground: 111111111111111111111111111111111111");
            }

        } catch (Throwable t) {
            Log.e("AudioRecord", "Recording Failed");
        }

        audioRecord.stop();

        return null;
    }
}