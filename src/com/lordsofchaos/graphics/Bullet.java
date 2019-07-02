package com.lordsofchaos.graphics;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class Bullet extends GraphicObject implements Serializable {
    private static int radius = 5;

    private Point2D velocity;
    //protected static Circle view = new Circle(radius, Color.BLACK);
    //protected static Node view;

    private int id;
    private static Node view;

    public Bullet(int id, TankColor color) {
        super(bulletImg(color));
        this.id = id;
    }

    public Bullet(int id, TankColor tankColor, boolean rocket) {
        super(rocketImg());
        this.id = id;
    }


    public void update(double time) {

    }

    private static Node bulletImg(TankColor color){
        Image tankImg = new Image(String.format("file:./resources/graphics/img/tank/%s/tank_bullet_%s.png", color.name(), color.name()), 20, 20, true, true);
        view = new ImageView(tankImg);
        view.setCache(false);
        return view;

    }

    private static Node rocketImg(){
        //Image rocketImg = new Image("file:./resources/graphics/img/rocket.png", 35, 35, true, true);
        //view = new ImageView(rocketImg);
        //view.setCache(false);
        view = new Circle(6, Color.BLACK);
        return view;

    }

    /*@Override
    public void setLocation(Point location) {
        //System.out.println("Bullet location");
        //this.view.setCenterX(location.getX());
        //this.view.setCenterY(location.getY());
        //view.setTranslateX(location.getX());
        //view.setTranslateY(location.getY());
        view.setLayoutX(location.getX());
        view.setLayoutY(location.getY());
    }*/


}
