package com.lordsofchaos.graphics;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.Serializable;

public class Health extends GraphicObject implements Serializable {

    //protected static Circle view = new Circle(radius, Color.BLACK);
    //protected static Node view;

    private int id;

    public Health() {
        super(new Text(400, 400 , "-10"));
    }


    public void update(double time) {

    }


}
