package cn.hylstudio.android.sign.task;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import android.os.AsyncTask;
import android.util.Log;

import cn.hylstudio.android.sign.StatelessRecognizer;
import cn.hylstudio.android.sign.model.DataBlock;
import cn.hylstudio.android.sign.model.Recognizer;
import cn.hylstudio.android.sign.model.Spectrum;
import cn.hylstudio.android.sign.presenter.MainPresenter;


public class RecognizerTask extends AsyncTask<Void, Object, Void> {

    private MainPresenter mainPresenter;

    private BlockingQueue<DataBlock> blockingQueue;

    private Recognizer recognizer;

    public RecognizerTask(MainPresenter mainPresenter, BlockingQueue<DataBlock> blockingQueue) {
        this.mainPresenter = mainPresenter;
        this.blockingQueue = blockingQueue;
        recognizer = new Recognizer();
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (mainPresenter.isStarted()) {
            try {
                Log.d("recognizer", "doInBackground: take");
                DataBlock dataBlock = blockingQueue.take();
                Log.d("recognizer", "doInBackground: 222222222222222222222222222222222222222");
                Spectrum spectrum = dataBlock.FFT();

                spectrum.normalize();

                StatelessRecognizer statelessRecognizer = new StatelessRecognizer(spectrum);

                Character key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());
                publishProgress(spectrum, key);

//				SpectrumFragment spectrumFragment = new SpectrumFragment(75, 100, spectrum);
//				publishProgress(spectrum, spectrumFragment.getMax());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("recognizer", "doInBackground: 3333333333333333333333333");
            }
        }

        return null;
    }
//@Override
//    protected Object doInBackground(Object[] params) {
//        while (mainPresenter.isStarted()) {
//            try {
//                DataBlock dataBlock = blockingQueue.take();
//
//                Spectrum spectrum = dataBlock.FFT();
//
//                spectrum.normalize();
//
//                StatelessRecognizer statelessRecognizer = new StatelessRecognizer(spectrum);
//
//                Character key = recognizer.getRecognizedKey(statelessRecognizer.getRecognizedKey());
//
//                publishProgress(spectrum, key);
//
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//            }
//        }
//        return null;
//    }
//

    protected void onProgressUpdate(Object... progress) {
        Spectrum spectrum = (Spectrum) progress[0];
        mainPresenter.spectrumReady(spectrum);

        Character key = (Character) progress[1];
        mainPresenter.keyReady(key);
    }
}
