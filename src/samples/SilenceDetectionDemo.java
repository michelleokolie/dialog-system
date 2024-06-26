package samples;

import dataprocessors.Convolve;
import dataprocessors.GaussianSmooth;
import dataprocessors.Log10;
import dataprocessors.RMS;
import dataprocessors.SilenceDetector;
import dataprocessors.loggers.Logger;
import dataproviders.DataProvider;
import dataproviders.audio.MicAudioProvider;

import eventsystem.EventDispatcher;

//demo of lorena's algorithm that prints output
public class SilenceDetectionDemo {

    public static void main(String[] args) {

        EventDispatcher dispatcher = new EventDispatcher();
     
        DataProvider provider = new MicAudioProvider(4000, 2048);
     
        RMS rms = new RMS(1000);
        provider.addListener(rms);

        Log10 log = new Log10();
        rms.addListener(log);

        GaussianSmooth g = new GaussianSmooth(1, 4000);
        log.addListener(g);

        Convolve c = new Convolve(new double[]{-1, -1, 1, 1});
        g.addListener(c);

        SilenceDetector s = new SilenceDetector(8000, 4000, 0.0003);
        c.addListener(s);

        s.addListener(new Logger());

        provider.start();
        dispatcher.run();
    }
    
}
