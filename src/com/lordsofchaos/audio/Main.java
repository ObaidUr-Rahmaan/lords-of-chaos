package com.lordsofchaos.audio;

//import static org.lwjgl.openal.AL10.AL_GAIN;
//import static org.lwjgl.openal.AL10.alListenerf;
//import java.util.concurrent.TimeUnit;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws Exception{

		SoundAPI api = new SoundAPI();
		api.playMenuSound();
//		api.slowDownModeOn();
//		api.slowDownModeOff();
		System.out.println(api.getMenuSoundVolume());

		Scanner scanner = new Scanner(System.in);
//		api.mute();
		while (true) {
			api.setMenuSoundVolume(scanner.nextFloat());
		}



//		api.defaultFireShotSound();
//		api.buttonClickingSound();
//		api.characterConfirmation();
//		api.healthLow();
//		api.tankDiedSound();

	}

}
