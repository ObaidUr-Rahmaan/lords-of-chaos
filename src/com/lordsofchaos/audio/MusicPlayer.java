package com.lordsofchaos.audio;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.*;
import javax.sound.sampled.DataLine.Info;


public class MusicPlayer implements Runnable{

	private ArrayList<AudioFile> musicFiles;
	private int currentSongIndex;
	private boolean running;
	public boolean ismute = false;

	public MusicPlayer(String... files){
		musicFiles = new ArrayList<AudioFile>();
		for(String file : files)
			musicFiles.add(new AudioFile("./resources/" + file + ".wav"));

		if (ismute){
			setVolume(0f);
		}

	}


	public void setVolume(float volume){
		for(AudioFile file: musicFiles){
			file.setVolume(volume);
		}
	}
//
//	float currentVolume = 1f;
//
//	public void mute(){
//		setVolume(0f);
//	}
//
//	public void unmute() {
//		setVolume(currentVolume);
//	}

	@Override
	public void run() {
		running = true;
		AudioFile song = musicFiles.get(currentSongIndex);
		song.play();
		while (running) {
			if (!song.isPlaying()) {
				currentSongIndex++;
				if (currentSongIndex >= musicFiles.size()) {
					currentSongIndex = 0;
				}
				song = musicFiles.get(currentSongIndex);
				song.play();
			}

			//To give time for thread to communicate
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
