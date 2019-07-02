package com.lordsofchaos.gamelogic.entities.gameworlditems;

import com.lordsofchaos.gamelogic.entities.GameEntity;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.gamelogic.transmission.SpriteType;

import java.awt.*;

public class Explosion extends GameEntity {

    public final int EXPLOSION_TIME = 4;

    private int timeLeft;

    public Explosion(Point location){
        super(location, new Point(0,0));
        this.timeLeft = EXPLOSION_TIME;
    }

    @Override
    public void updateObject(){
        if(timeLeft-- <= 0){
            this.finished = true;
        }
    }

    @Override
    public Sprite getSprite(){
        return new Sprite(this.location, SpriteType.EXPLOSION, this.id, 0);
    }
}
