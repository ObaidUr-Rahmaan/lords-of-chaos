package com.lordsofchaos.graphics;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class TankBody extends GraphicObject implements Serializable {
    private static Node view;
    private static TankColor color;
    private Point2D velocity;
    //private Rectangle view = new Rectangle(40, 20, Color.RED);
    private boolean brakes = false;

    private int id;

    /*
    @param primary True is tank object is for the local player
     */
    public TankBody(int id, TankColor color, double rotation) {
        super(tImage(color));
        this.color = color;
        this.id = id;
        view.setRotate(rotation);
    }

    public static Node tImage(TankColor color) {
        Image tankImg = new Image(String.format("file:./resources/graphics/img/tank/%s/tank_body_%s.png", color.name(), color.name()), 35, 35, true, true);
        view = new ImageView(tankImg);
        view.setCache(true);
        return view;
    }


}

