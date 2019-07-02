package com.lordsofchaos.graphics;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class PowerUp extends GraphicObject implements Serializable {
    private static int size = 20;
    private static Node view;

    private Point2D velocity;
    //protected static Circle view = new Circle(radius, Color.BLACK);
    //protected static Node view;

    private int id;

    public PowerUp(int id) {
        super(powerImage());
        this.id = id;
    }

    private static Node powerImage() {
        Image powerImg = new Image("file:./resources/graphics/img/POWERUP.png", 30, 30, true, true);
        view = new ImageView(powerImg);
        view.setCache(true);
        return view;
    }

    public void update(double time) {

    }


}
