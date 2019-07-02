package com.lordsofchaos.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

public class AudioFile implements LineListener{

    private File soundFile;
    private AudioInputStream ais;
    private AudioFormat format;
    private DataLine.Info info;
    private Clip clip;
    private FloatControl gainControl;
    private boolean playing;
    //Default Volume
    private float vol = 60;

    private boolean gainSupport;

    public AudioFile(String fileName){
        soundFile = new File(fileName);
        try {
            ais = AudioSystem.getAudioInputStream(soundFile);
            format = ais.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
            clip.addLineListener(this);

            //Gain control is not accessible on my system; having fixed gain support causes exceptions
            //which prevent me from running the project at all. If you have gain support on your
            //machine the only effect this will have is to make the program run one more check.
            //Please don't remove this!
            //-Max
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainSupport = true;
            } else {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.VOLUME);
                gainSupport = false;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void play(){
        play(vol);
    }

    public void play(float volume){
        setVolume(volume);
//		gainControl.setValue(volume);
        clip.start();

        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setVolume(float volume) {
        vol = volume;
        //60+
        if(gainSupport) {
            gainControl.setValue(vol - 80);
        }
    }

    //In case we pulse the music
    @Override
    public void update(LineEvent event){
        if(event.getType() == LineEvent.Type.START){
            playing = true;
        }else if (event.getType() == LineEvent.Type.STOP) {
            clip.stop();
            clip.flush();
            clip.setFramePosition(0);
            playing = false;
        }
    }

}
