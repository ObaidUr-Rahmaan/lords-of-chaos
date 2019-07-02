package com.lordsofchaos.graphics;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Explosion extends GraphicObject implements Serializable {
    private static Node view;

    //protected static Circle view = new Circle(radius, Color.BLACK);
    //protected static Node view;

    private int id;

    public Explosion(int id) {
        super(powerImage());
        this.id = id;
    }

    private static Node powerImage() {
        Image powerImg = new Image("file:./resources/graphics/img/explosion2.png", 32, 32, true, true);
        view = new ImageView(powerImg);
        view.setCache(true);
        return view;
    }

    public void update(double time) {

    }


}
