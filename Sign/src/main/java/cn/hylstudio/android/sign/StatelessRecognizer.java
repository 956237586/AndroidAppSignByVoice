package cn.hylstudio.android.sign;

import java.util.ArrayList;
import java.util.Collection;

import cn.hylstudio.android.sign.model.Spectrum;
import cn.hylstudio.android.sign.model.SpectrumFragment;
import cn.hylstudio.android.sign.model.Tone;


public class StatelessRecognizer {

    private Spectrum spectrum;

    private Collection<Tone> tones;

    public StatelessRecognizer() {
        tones = new ArrayList<Tone>();
        fillTones();
    }

    public StatelessRecognizer(Spectrum spectrum) {
        this();
        this.spectrum = spectrum;
    }

    private void fillTones() {
        tones.add(new Tone(45, 77, '1'));
        tones.add(new Tone(45, 86, '2'));
        tones.add(new Tone(45, 95, '3'));
        tones.add(new Tone(49, 77, '4'));
        tones.add(new Tone(49, 86, '5'));
        tones.add(new Tone(49, 95, '6'));
        tones.add(new Tone(55, 77, '7'));
        tones.add(new Tone(55, 86, '8'));
        tones.add(new Tone(55, 95, '9'));
        tones.add(new Tone(60, 77, '*'));
        tones.add(new Tone(60, 86, '0'));
        tones.add(new Tone(60, 95, '#'));
    }

    public void setSpectrum(Spectrum spectrum) {
        this.spectrum = spectrum;
    }

    public char getRecognizedKey() {
        SpectrumFragment lowFragment = new SpectrumFragment(0, 75, spectrum);
        SpectrumFragment highFragment = new SpectrumFragment(75, 150, spectrum);

        int lowMax = lowFragment.getMax();
        int highMax = highFragment.getMax();

        SpectrumFragment allSpectrum = new SpectrumFragment(0, 150, spectrum);
        int max = allSpectrum.getMax();

        if (max != lowMax && max != highMax)
            return ' ';

        for (Tone t : tones) {
            if (t.match(lowMax, highMax))
                return t.getKey();
        }

        return ' ';
    }
}
