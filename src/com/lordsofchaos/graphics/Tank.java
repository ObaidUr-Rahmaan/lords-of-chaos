package com.lordsofchaos.graphics;

import com.lordsofchaos.gamelogic.transmission.PowerUpState;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.awt.*;
import java.io.Serializable;

public class Tank extends GraphicObject implements Serializable {
    private static Node tankView;
    private static TankColor color;
    private static Node barrelView;
    private static Node indicView;
    private static Group testView;
    private static StackPane view = new StackPane();
    ImageView forceImgV;

    private int id;
    private boolean invicible;

    /*
    @param primary True is tank object is for the local player
     */
    public Tank(int id, TankColor color, double rotation) {
        super(tankImage(color));
        this.color = color;
        this.id = id;
    }

    /*
    Creates all image views
    Tank made of three elements: base, turret and current player indicator
    All stored in group to allow simultaneous control
     */
    private static Node tankImage(TankColor color) {
        int size = 50;

        Image tankImg = new Image(String.format("file:./resources/graphics/img/tank/%s/new/tank_body_%s.png", color.name(), color.name()), size, size, true, true);
        Image playerIndic = new Image(String.format("file:./resources/graphics/img/activeplayer.png", color.name(), color.name()), size, size, true, true);
        Image barrelImage = new Image(String.format("file:./resources/graphics/img/tank/%s/new/tank_barrel_%s.png", color.name(), color.name()), size, size, true, true);

        tankView = new ImageView(tankImg);
        tankView.setCache(true);
        indicView = new ImageView(playerIndic);
        indicView.setCache(true);
        barrelView = new ImageView(barrelImage);
        barrelView.setCache(true);

        //view.getChildren().addAll(tankView,barrelView);
        testView = new Group(new Group(tankView,indicView),barrelView);
        return testView;

    }

    public void update(double time) {
    }


    /*
    Clears the forcefield
     */
    public void endPowerUp() {
        if (invicible){
            ( (Group) super.view).getChildren().remove(forceImgV);
        }
    }

    public void setPowerUp(PowerUpState state) {
        switch (state){

            case normal:
                break;
            case slow:
                break;
            case invincible:
                invincPower();
                break;
            case multishot:
                break;
            case rocket:
                break;
        }
    }

    public PowerUpState getPowerUp(){
        return null;
    }


    private void invincPower(){

        Image forceImg = new Image(String.format("file:./resources/graphics/img/forcefield.png"), 55, 55, false, true);
        forceImgV = new ImageView(forceImg);
        forceImgV.setCache(true);
        forceImgV.setOpacity(70);

        /*
        Rotate forcefield to animate
         */
        RotateTransition rt = new RotateTransition(Duration.millis(3000),forceImgV);
        rt.setByAngle(360);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        ( (Group) super.view).getChildren().add(forceImgV);
        invicible = true;
        rt.play();

    }


    @Override
    public void setLocation(Point location){
        super.view.setTranslateX(location.getX()-10);
        super.view.setTranslateY(location.getY()-10);
    }

}

