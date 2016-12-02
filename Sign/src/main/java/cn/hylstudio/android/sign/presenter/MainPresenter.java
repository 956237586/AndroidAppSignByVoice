package cn.hylstudio.android.sign.presenter;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import cn.hylstudio.android.sign.activity.MainActivity;
import cn.hylstudio.android.sign.model.DataBlock;
import cn.hylstudio.android.sign.model.Spectrum;
import cn.hylstudio.android.sign.task.RecognizerTask;
import cn.hylstudio.android.sign.task.RecognizerThread;
import cn.hylstudio.android.sign.task.RecordTask;
import cn.hylstudio.android.sign.task.RecordThread;


public class MainPresenter {
    private boolean started;

//    private RecordTask recordTask;
//    private RecognizerTask recognizerTask;

    private MainActivity mainActivity;
    BlockingQueue<DataBlock> blockingQueue;

    private Character lastValue;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void changeState() {
        if (!started) {
            started = true;
            lastValue = ' ';

            blockingQueue = new LinkedBlockingQueue<DataBlock>();

            mainActivity.start();

//
//            recordTask = new RecordTask(this, blockingQueue);
//            recognizerTask = new RecognizerTask(this, blockingQueue);
//
//            recordTask.execute();
//            Log.d("record", "changeState: start1");
//            recognizerTask.execute();
//            Log.d("record", "changeState: start2");
//
            Thread recordThread = new Thread(new RecordThread(this, blockingQueue));
            recordThread.start();
            Thread recognizerThread = new Thread(new RecognizerThread(this, blockingQueue));
            recognizerThread.start();

        } else {
            started = false;
            mainActivity.stop();

//            recognizerTask.cancel(true);
//            recordTask.cancel(true);

        }
    }

    public void clear() {
        mainActivity.clearText();
    }

    public boolean isStarted() {
        return started;
    }


    public int getAudioSource() {
        return mainActivity.getAudioSource();
    }

    public void spectrumReady(Spectrum spectrum) {
//        mainActivity.drawSpectrum(spectrum);
    }

    public void keyReady(char key) {
//        mainActivity.setAciveKey(key);
        if (key != ' ') {
            if (lastValue != key) {
                if (key != '#') {
                    mainActivity.addText(key);
                } else {
                    if (mainActivity.getText().equals("0123456789")) {
                        Toast.makeText(mainActivity, "签到成功！！", Toast.LENGTH_SHORT).show();
                    }
                    mainActivity.clearText();
                }
            }
        }
        lastValue = key;
    }

    public void debug(String text) {
        mainActivity.setText(text);
    }
}
