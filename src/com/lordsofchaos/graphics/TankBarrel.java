package com.lordsofchaos.graphics;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class TankBarrel extends GraphicObject implements Serializable {
    private static Node view;
    private static TankColor color;

    private int id;

    /*
    @param primary True is tank object is for the local player
     */
    public TankBarrel(int id, TankColor color, double rotation) {
        super(tImage(color));
        this.color = color;
        this.id = id;
        view.setRotate(rotation);
    }

    public static Node tImage(TankColor color) {
        Image tankImg = new Image(String.format("file:./resources/graphics/img/tank/%s/tank_barrel_%s.png", color.name(), color.name()), 35, 35, true, true);
        view = new ImageView(tankImg);
        view.setCache(true);
        return view;
    }


}

