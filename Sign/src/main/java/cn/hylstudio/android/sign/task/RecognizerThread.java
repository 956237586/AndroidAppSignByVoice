package cn.hylstudio.android.sign.task;

import android.os.Handler;
import android.util.Log;

import java.util.concurrent.BlockingQueue;

import cn.hylstudio.android.sign.StatelessRecognizer;
import cn.hylstudio.android.sign.model.DataBlock;
import cn.hylstudio.android.sign.model.Recognizer;
import cn.hylstudio.android.sign.model.Spectrum;
import cn.hylstudio.android.sign.presenter.MainPresenter;

/**
 * Created by HYL on 2016/9/14.
 */
public class RecognizerThread implements Runnable {
    public static final String TAG = "thread";
    private MainPresenter mainPresenter;
    private BlockingQueue<DataBlock> blockingQueue;
    private Recognizer recognizer;
    private Handler handler;
    private StatelessRecognizer statelessRecognizer;
    public RecognizerThread(MainPresenter mainPresenter, BlockingQueue<DataBlock> blockingQueue) {
        this.mainPresenter = mainPresenter;
        this.blockingQueue = blockingQueue;
        statelessRecognizer = new StatelessRecognizer();
        recognizer = new Recognizer();
        handler = new Handler();
    }


    @Override
    public void run() {
        while (mainPresenter.isStarted()) {
            try {
//                Log.d(TAG, "doInBackground: take");
                DataBlock dataBlock = blockingQueue.take();
//                Log.d(TAG, "doInBackground: 222222222222222222222222222222222222222");
                Spectrum spectrum = dataBlock.FFT();

                spectrum.normalize();

                statelessRecognizer.setSpectrum(spectrum);
                Character key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());
                //publishProgress(spectrum, key);
                Log.d(TAG, "run: key = " + key);
                handler.post(new UpdateMessage(spectrum, key));
//                mainPresenter.spectrumReady(spectrum);
//                mainPresenter.keyReady(key);
//				SpectrumFragment spectrumFragment = new SpectrumFragment(75, 100, spectrum);
//				publishProgress(spectrum, spectrumFragment.getMax());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("recognizer", "doInBackground: 3333333333333333333333333");
            }
        }
    }

    class UpdateMessage implements Runnable {
        private Spectrum spectrum;
        private char key;

        public UpdateMessage(Spectrum spectrum, char key) {
            this.spectrum = spectrum;
            this.key = key;
        }

        @Override
        public void run() {
            mainPresenter.spectrumReady(spectrum);
            mainPresenter.keyReady(key);
        }
    }
}
