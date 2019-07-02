package com.lordsofchaos.audio;

//import static org.lwjgl.openal.AL10.AL_GAIN;
//import static org.lwjgl.openal.AL10.alListenerf;

public class SoundAPI {

    public MusicPlayer menuSound;
    public MusicPlayerSE buttonClickingSound;
    public MusicPlayerSE characterConfirmation;
    public MusicPlayerSE fireSound;
    public MusicPlayerSE healthLow;
    public MusicPlayerSE tankDiedSound;
    public MusicPlayerSE starMode;
    public MusicPlayerSE starModeFireSound;
    public MusicPlayerSE slowDownMode;


    //Menu Sound Volume
    public float MenuSound_volume = 60;
    //Sound Effect Volume
    public float InGame_volume = 60;



    public SoundAPI(){

        //Background Music
        menuSound = new MusicPlayer(
                "BGM_SoundTrack_1_A_Lion's_Legacy",
                "BGM_SoundTrack_2_The_Queen's_Justice",
                "BGM_SoundTrack_3_Shall_We_Begin",
                "BGM_SoundTrack_4_PUBG_MainTheme");

        //Sound Effect
        buttonClickingSound = new MusicPlayerSE("BUTTON_CLICK");
        characterConfirmation = new MusicPlayerSE("CHARACTER_CONFIRMATION");
        fireSound = new MusicPlayerSE("DEFAULT_FIRE_SHOT");
        healthLow = new MusicPlayerSE("HEALTH_LOW");
        tankDiedSound = new MusicPlayerSE("PLAYER_DEAD");

        //Power Ups
        starMode = new MusicPlayerSE("Power_Ups_Star_Mode");
        starModeFireSound = new MusicPlayerSE("Power_Ups_Star_Mode_Fire_Shot");
        slowDownMode = new MusicPlayerSE("Power_Ups_Slow_Down_Mode");

    }

    //Play Background Music
    public void playMenuSound() {

        new Thread(menuSound).start();
    }

    //Sound Effect
    //Button Clicking Sound Effect
    public void buttonClickingSound() {

        new Thread(buttonClickingSound).start();

    }

    //Character Confirmation
    public void characterConfirmation() {

        new Thread(characterConfirmation).start();

    }

    //Fire Shot Sound Effect
    public void defaultFireShotSound() {

        new Thread(fireSound).start();

    }

    //Health Low
    public void healthLow() {

        new Thread(healthLow).start();

    }

    //Player Died Sound Effect
    public void tankDiedSound() {

        new Thread(tankDiedSound).start();

    }


    //Power Ups///////////////////////////
    //Star Mode///////////////////////////
    //On
    public void starModeOn() {

        new Thread(starMode).start();
        starMode.setVolume(InGame_volume);

    }
    //Off
    public void starModeOff() {

        starMode.setVolume(0);

    }

    //Star Mode Fire Shot Sound Effect
    public void starModeFireShotSound() {

        new Thread(starModeFireSound).start();
        starModeFireSound.setVolume(InGame_volume);

    }

    //Slow Down Mode///////////////////////////
    //On
    public void slowDownModeOn() {

        new Thread(slowDownMode).start();
        slowDownMode.setVolume(InGame_volume);
        ///add into SoundAPI()
        menuSound.setVolume(0);

    }
    //Off
    public void slowDownModeOff() {

        slowDownMode.setVolume(0);
        setMenuSoundVolume(MenuSound_volume);

    }

    //Set Volume///////////////////////////
    //Set Menu Sound Volume
    //Volume should between 0 - 80
    public void setMenuSoundVolume(float f){

//        //modify the range (reverse)
//        if(f > 100){
//            System.out.println("[Audio]: Volume Overfloat");
//        }else if(75 < f && f <= 100){
//            //alter the range 75 - 100
//            //to the range of 30 - 100
//            f = f - 75;
//            f = f * 70 / 25;
//            f = f + 30;
//        }else if(50 < f && f <= 75){
//            //alter the range 50 - 75
//            //to the range of 15 - 30
//            f = f - 50;
//            f = f * 15 / 25;
//            f = f + 15;
//        }else if(25 < f && f <= 50){
//            //alter the range 25 - 50
//            //to the range of 5 - 15
//            f = f - 25;
//            f = f * 10 / 25;
//            f = f + 5;
//        }else if(0 <= f && f <= 25){
//            //alter the range 0 - 25
//            //to the range of 0 - 5
//            f = f * 5 / 25;
//        }else{
//            System.out.println("[Audio]: Volume Overfloat");
//        }

        //modify the range
        if(f > 100){
            System.out.println("[Audio]: Volume Overfloat");
        }else if(30 < f && f <= 100){
            //alter the range 30 - 100
            //to the range of 75 - 100
            f = f - 30;
            f = f * 25 / 70;
            f = f + 75;
        }else if(15 < f && f <= 30){
            //alter the range 15 - 30
            //to the range of 50 - 75
            f = f - 15;
            f = f * 25 / 15;
            f = f + 50;
        }else if(5 < f && f <= 15){
            //alter the range 5 - 15
            //to the range of 25 - 50
            f = f - 5;
            f = f * 25 / 10;
            f = f + 25;
        }else if(0 <= f && f <= 5){
            //alter the range 0 - 5
            //to the range of 0 - 25
            f = f * 25 / 5;
        }else{
            System.out.println("[Audio]: Volume Overfloat");
        }

        this.MenuSound_volume = f*80/100;//100 to 80
        menuSound.setVolume(this.MenuSound_volume);
        buttonClickingSound.setVolume(this.MenuSound_volume);
        characterConfirmation.setVolume(this.MenuSound_volume);

    }

    //Set Sound Effect Volume
    public void setInGameVolume(float f){

        this.InGame_volume = f*80/100;//100 to 80
        menuSound.setVolume(this.InGame_volume);
        fireSound.setVolume(this.InGame_volume);
        healthLow.setVolume(this.InGame_volume);
        tankDiedSound.setVolume(this.InGame_volume);

    }

    //Mute///////////////////////////
    private boolean mute = false;

    public void mute(){
        if (!this.mute)
            System.out.println("[Audio]: System Now Muted");
        this.mute = true;
        setMenuSoundVolume(0);
        setInGameVolume(0);
    }

    public void unMute(){
        if (this.mute)
            System.out.println("[Audio]: System Now unMuted");
        this.mute = false;
        setMenuSoundVolume(MenuSound_volume);
        setInGameVolume(InGame_volume);
    }

    public boolean isMuted() {
        return mute;
    }

    public float getMenuSoundVolume() {

        float f = MenuSound_volume*100/80;//80 to 100

        //modify the range (reverse)
        if(f > 100){
            System.out.println("[Audio]: MenuSound_volume Error");
        }else if(75 < f && f <= 100){
            //alter the range 75 - 100
            //to the range of 30 - 100
            f = f - 75;
            f = f * 70 / 25;
            f = f + 30;
        }else if(50 < f && f <= 75){
            //alter the range 50 - 75
            //to the range of 15 - 30
            f = f - 50;
            f = f * 15 / 25;
            f = f + 15;
        }else if(25 < f && f <= 50){
            //alter the range 25 - 50
            //to the range of 5 - 15
            f = f - 25;
            f = f * 10 / 25;
            f = f + 5;
        }else if(0 <= f && f <= 25){
            //alter the range 0 - 25
            //to the range of 0 - 5
            f = f * 5 / 25;
        }else{
            System.out.println("[Audio]: MenuSound_volume Error");
        }

        return f;
    }

    public void increaseMenuSoundVolumeBy5() {
        float f = getMenuSoundVolume();
        f = f + 5;
        if (f > 100){
            System.out.println("[Audio]: Volume Overfloat");
        } else {
            setMenuSoundVolume(f);
        }
    }

    public void decreaseMenuSoundVolumeBy5() {
        float f = getMenuSoundVolume();
        f = f - 5;
        if (f < 0){
            System.out.println("[Audio]: Volume Overfloat");
        } else {
            setMenuSoundVolume(f);
        }
    }

    public float getInGameVolume() {
        return InGame_volume*100/80;//80 to 100
    }

    public void increaseInGameVolumeBy5() {
        float f = getInGameVolume();
        f = f + 5;
        if (f > 100){
            System.out.println("[Audio]: Volume Overfloat");
        } else {
            setInGameVolume(f);
        }
    }

    public void decreaseInGameVolumeBy5() {
        float f = getInGameVolume();
        f = f - 5;
        if (f < 0){
            System.out.println("[Audio]: Volume Overfloat");
        } else {
            setInGameVolume(f);
        }
    }












    ////////////old functionds

//    //Set Master Volume
//    public void setMasterVolume(float f){
//
//        this.MenuSound_volume = f;
//        this.SE_volume = f;
//        setMenuSoundVolume(this.MenuSound_volume);
//        setInGameVolume(this.SE_volume);
//
//    }
//
//    //Mute///////////////////////////
//    private boolean mute = false;
//
//    public void mute(){
//        //alListenerf(AL_GAIN, 0f);
//        //if (!this.mute)
//        //    System.out.println("[Audio]: Muted");
//        //this.mute = true;
//        setMenuSoundVolume(0);
//        setInGameVolume(0);
//    }
//
//    public void unMute(){
//        //alListenerf(AL_GAIN, currentVolume);
//        if (this.mute)
//            System.out.println("SystemUnmuted");
//        this.mute = false;
//        setMenuSoundVolume(MenuSound_volume);
//        setInGameVolume(InGame_volume);
//    }
//
//    public boolean isMuted() {
//        return mute;
//    }
//
//    public float getMenuSoundVolume() {
//        return MenuSound_volume;
//    }
//
//    public float getInGameVolume() {
//        return InGame_volume;
//    }
//
//    public void setVolume(float volume) {
//        if (volume == 0.0f) {
//            this.mute();
//        } else {
//            this.unMute();
//        }
//        if (volume >1f || volume<0f){
//            System.err.println("Invalid volume, only accepts a float between 0-1");
//            return;
//        }
//        this.currentVolume = volume;
//        //alListenerf(AL_GAIN, currentVolume);
//    }


}
