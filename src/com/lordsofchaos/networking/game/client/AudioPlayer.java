package com.lordsofchaos.networking.game.client;

import com.lordsofchaos.audio.SoundAPI;
import com.lordsofchaos.gamelogic.transmission.GameState;
import com.lordsofchaos.gamelogic.transmission.Sounds;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Audio Thread (plays in-game Music and in-game SFX)
 *
 * @author Obaid Ur-Rahmaan
 */
public class AudioPlayer implements Runnable {

    private GameState latestGameState;

    public AudioPlayer(GameState latestGameState) {
        this.latestGameState = latestGameState;
    }

    @Override
    public void run() {

        // Start playing in-game Musics
        SoundAPI soundAPI = new SoundAPI();
        soundAPI.playMenuSound(); // just for testing (should be playInGameSound later)

        // Constantly access the latestGameState and play all in-game SFX
        while (true) {
            CopyOnWriteArrayList<Sounds> gameSounds = this.latestGameState.getSoundsToStart();

            for (Sounds sound: gameSounds) {
                switch (sound) {
                    case Walk:
//                        soundAPI.playWalkingSound();
                        break;
                    case bulletFire:
                        soundAPI.defaultFireShotSound();
                        break;
                    default:
                        // do nothing
                            break;
                }
            }
        }

    }
}
