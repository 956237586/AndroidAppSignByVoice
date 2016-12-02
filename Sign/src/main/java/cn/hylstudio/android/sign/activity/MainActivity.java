package cn.hylstudio.android.sign.activity;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.hylstudio.android.sign.R;
import cn.hylstudio.android.sign.model.Spectrum;
import cn.hylstudio.android.sign.presenter.MainPresenter;

public class MainActivity extends Activity {

    private Button btn_changeState;
    private Button btn_clear;
    private EditText text_recognized;
//    private NumericKeyboard numKeyboard;
//    private SpectrumView spectrumView;

    private MainPresenter mainPresenter;

    private String recognizedText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mainPresenter = new MainPresenter(this);

        btn_changeState = (Button) this.findViewById(R.id.stateButton);
        btn_changeState.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mainPresenter.changeState();
            }
        });

        btn_clear = (Button) this.findViewById(R.id.clearButton);
        btn_clear.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                mainPresenter.clear();
            }
        });

//        spectrumView = new SpectrumView();
//        spectrumView.setImageView((ImageView) this.findViewById(R.id.spectrum));

        text_recognized = (EditText) this.findViewById(R.id.recognizeredText);
        text_recognized.setFocusable(false);

//        numKeyboard = new NumericKeyboard();
//        numKeyboard.add('0', (Button) findViewById(R.id.button0));
//        numKeyboard.add('1', (Button) findViewById(R.id.button1));
//        numKeyboard.add('2', (Button) findViewById(R.id.button2));
//        numKeyboard.add('3', (Button) findViewById(R.id.button3));
//        numKeyboard.add('4', (Button) findViewById(R.id.button4));
//        numKeyboard.add('5', (Button) findViewById(R.id.button5));
//        numKeyboard.add('6', (Button) findViewById(R.id.button6));
//        numKeyboard.add('7', (Button) findViewById(R.id.button7));
//        numKeyboard.add('8', (Button) findViewById(R.id.button8));
//        numKeyboard.add('9', (Button) findViewById(R.id.button9));
//        numKeyboard.add('0', (Button) findViewById(R.id.button0));
//        numKeyboard.add('#', (Button) findViewById(R.id.buttonHash));
//        numKeyboard.add('*', (Button) findViewById(R.id.buttonAsterisk));

        setEnabled(false);

        recognizedText = "";

    }

    public void start() {
        btn_changeState.setText(R.string.stop);
        setEnabled(true);
    }

    public void stop() {
        btn_changeState.setText(R.string.start);
        setEnabled(false);
    }

    public int getAudioSource() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (telephonyManager.getCallState() != TelephonyManager.PHONE_TYPE_NONE)
            return MediaRecorder.AudioSource.VOICE_DOWNLINK;

        return MediaRecorder.AudioSource.MIC;
    }

//    public void drawSpectrum(Spectrum spectrum) {
//        spectrumView.draw(spectrum);
//    }

    public void clearText() {
        recognizedText = "";
        text_recognized.setText("");
    }

    public void addText(Character c) {
        recognizedText += c;
        text_recognized.setText(recognizedText);
    }

    public void setText(String text) {
        text_recognized.setText(text);
    }

    public String getText() {
        return text_recognized.getText() + "";
    }

    public void setEnabled(boolean enabled) {
        text_recognized.setEnabled(enabled);
//        numKeyboard.setEnabled(enabled);
    }

    public void setAciveKey(char key) {
//        numKeyboard.setActive(key);
    }

    @Override
    protected void onPause() {
        if (mainPresenter.isStarted())
            mainPresenter.changeState();
        super.onPause();
    }
}