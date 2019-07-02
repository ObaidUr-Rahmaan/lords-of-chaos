package com.lordsofchaos.gamelogic;

public class MapSegments {

    public static final boolean[][][] Maps = {
        {
            //The Field
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

    } ,    {
            //Warehouse
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,true,true,true,false,false,false,true,true,true,true,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,true,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
            {false,false,true,true,true,true,false,false,false,true,true,true,true,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

    },  {
        //Pillar Warehouse
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,true,true,false,false,false,false,false,true,true,true,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,true,false,false},
        {false,false,true,true,true,false,false,false,false,false,true,true,true,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

        },{
            //The trap
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,true,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,true,true,true,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,true,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

    } ,   {
            //Marks the spot
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
            {false,true,true,false,false,false,false,false,false,false,false,false,true,true,false},
            {false,true,true,true,false,false,false,false,false,false,false,true,true,true,false},
            {false,false,true,true,true,false,false,false,false,false,true,true,true,false,false},
            {false,false,false,true,true,true,false,false,false,true,true,true,false,false,false},
            {false,false,false,false,true,true,true,false,true,true,true,false,false,false,false},
            {false,false,false,false,false,true,true,true,true,true,false,false,false,false,false},
            {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
            {false,false,false,false,false,true,true,true,true,true,false,false,false,false,false},
            {false,false,false,false,true,true,true,false,true,true,true,false,false,false,false},
            {false,false,false,true,true,true,false,false,false,true,true,true,false,false,false},
            {false,false,true,true,true,false,false,false,false,false,true,true,true,false,false},
            {false,true,true,true,false,false,false,false,false,false,false,true,true,true,false},
            {false,true,true,false,false,false,false,false,false,false,false,false,true,true,false},
            {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

    },       {
            //Railyard
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,true,false,false,false,false,false,true,true,true,true,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,true,true,true,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,true,true,true,false,false,false,false,false,true,true,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,true,true,true,true,true,true,true,true,true,true,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

        },       {
        //Lobster Trap
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,true,true,true,false,false,false,true,true,true,true,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,true,true,true,true,false,false,false,true,true,true,true,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},
        {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false},

        }

    };
}
