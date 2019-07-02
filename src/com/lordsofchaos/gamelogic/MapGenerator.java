package com.lordsofchaos.gamelogic;

import com.lordsofchaos.graphics.Direction;

import java.util.concurrent.ThreadLocalRandom;

public class MapGenerator {

    int tilesInMapX;
    int tilesInMapY;
    boolean [][] mapWalls;


    public MapGenerator(int tilesInMapX, int tileInMapY){
        this.tilesInMapX = tilesInMapX;
        this.tilesInMapY = tileInMapY;
    }


    @SuppressWarnings("Duplicates")
    private void addMazeBlock(int length, int startX, int startY, Direction direction, boolean checkNeighbours) {
        int neighbours = 0;
        if (direction == Direction.VERTICAL) {
            for (int i = startX; i < startX + length; i++) {
                if (i > tilesInMapX - 1) {
                    break;
                }
                mapWalls[startY][i] = true;
            }
        }
        else {
            for (int i = startY; i < startY + length; i++) {
                if (i > tilesInMapY -1){
                    break;
                }
                mapWalls[i][startX] = true;
            }
        }
    }

    public boolean[][] makeMaze() {
        long startTime = System.nanoTime();
        this.mapWalls = new boolean[tilesInMapY][tilesInMapX];
        /*int maxPieces = 55;
        int minPieces = 45;
        int minLength = 2;
        int maxLength = 9;
        int numPieces = ThreadLocalRandom.current().nextInt(minPieces, maxPieces);
        addMazeBlock(MAP_WIDTH_TILE, 0, 0, Direction.HORIZONTAL, false);
        addMazeBlock(MAP_WIDTH_TILE, MAP_WIDTH_TILE - 1, 0, Direction.HORIZONTAL, false);
        addMazeBlock(MAP_WIDTH_TILE, 0, MAP_HEIGHT_TILE - 1, Direction.VERTICAL, false);
        addMazeBlock(MAP_WIDTH_TILE, 0, 0, Direction.VERTICAL, false);*/


        /*for (int i = 0; i < numPieces; i++) {
            int length = ThreadLocalRandom.current().nextInt(minLength, maxLength);
            int startX = ThreadLocalRandom.current().nextInt(3, tilesInMapX - 3);
            int startY = ThreadLocalRandom.current().nextInt(3, tilesInMapY - 3);
            Direction direction = Direction.values()[ThreadLocalRandom.current().nextInt(Direction.values().length)];
            addMazeBlock(length, startX, startY, direction, true);
        }*/
        //Semi random generation code
        for(int bigX = 0; bigX < 4; bigX++){
            for(int bigY = 0; bigY < 2; bigY++){
                int selectedSegment = ThreadLocalRandom.current().nextInt(MapSegments.Maps.length);
                int oirentation = ThreadLocalRandom.current().nextInt(8);
                for(int smallX = 0; smallX < 15;smallX++ ){
                    for (int smallY = 0; smallY < 15; smallY++){
                        boolean wall;
                        switch (oirentation){
                            case 0:
                                wall = MapSegments.Maps[selectedSegment][smallX][smallY];
                                break;
                            case 1:
                                wall = MapSegments.Maps[selectedSegment][14-smallX][smallY];
                                break;
                            case 2:
                                wall = MapSegments.Maps[selectedSegment][smallX][14-smallY];
                                break;
                            case 3:
                                wall = MapSegments.Maps[selectedSegment][14-smallX][14-smallY];
                                break;
                            case 4:
                                wall = MapSegments.Maps[selectedSegment][smallY][smallX];
                                break;
                            case 5:
                                wall = MapSegments.Maps[selectedSegment][14-smallY][smallX];
                                break;
                            case 6:
                                wall = MapSegments.Maps[selectedSegment][smallY][14-smallX];
                                break;
                            default:
                                wall = MapSegments.Maps[selectedSegment][14-smallY][14-smallX];
                                break;

                        }
                        int applyY = smallY+15*bigY;
                        if(bigY == 0){
                            applyY++;
                        }else if (bigY == 1){
                            applyY--;
                        }
                        int applyX = smallX+15*bigX;
                        if(bigX == 0){
                            applyX++;
                        }else if (bigX == 3){
                            applyX--;
                        }
                        mapWalls[applyY][applyX] = wall;
                    }
                }
            }
        }
        for(int along = 0; along < mapWalls[0].length; along++){
            mapWalls[0][along] = true;
            mapWalls[tilesInMapY-1][along] =true;
        }
        for(int down = 0; down < mapWalls.length; down++){
            mapWalls[down][0] = true;
            mapWalls[down][tilesInMapX-1] =true;
        }

        for(int along = 1; along < mapWalls[0].length-1; along++){
            mapWalls[1][along] = false;
            mapWalls[tilesInMapY-2][along] =false;
            mapWalls[2][along] = false;
            mapWalls[tilesInMapY-3][along] =false;
        }
        for(int down = 1; down < mapWalls.length-1; down++){
            mapWalls[down][1] = false;
            mapWalls[down][tilesInMapX-2] =false;
            mapWalls[down][2] = false;
            mapWalls[down][tilesInMapX-3] =false;

        }
        System.out.println("Map generation time:" + (System.nanoTime() - startTime));
        return mapWalls;
    }


}
