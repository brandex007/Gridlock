package com.innovativeexecutables;

public class Tile {
    public static int tileSize = Gridlock.VIEWPORT_WIDTH / 32;
    private int x;
    private int y;

    public Tile(int x, int y){
        this.x = 16 + x * 32;
        this.y = 16 + y * 32;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
}
