package com.lordsofchaos.audio;

import java.util.ArrayList;


public class MusicPlayerSE implements Runnable{

	private AudioFile musicFile;
	private int currentSongIndex;
	private boolean running;

	public MusicPlayerSE(String file){
		musicFile = new AudioFile("./resources/" + file + ".wav");


	}

	public void setVolume(float volume){
		this.musicFile.setVolume(volume);
	}

	@Override
	public void run() {
		running = true;
		AudioFile song = this.musicFile;
		song.play();
		while (running) {
			if (!song.isPlaying()) {

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
