package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import static java.lang.StrictMath.abs;

public class Chest {
    public Enum type;
    private int x, y, width, height;
    public boolean isActive = false;
    private Texture closedTexture, openTexture, currTexture;
    public boolean isUnopened = true;

    public Chest(int x, int y){
        this.x = x;
        this.y = y;

        loadTexture();
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }


    public void render (Batch sb){
        if(isActive) {
            sb.draw(currTexture, x, y);
        }
    }

    public void remove(){
        isActive = false;
        closedTexture.dispose();
        openTexture.dispose();
        currTexture.dispose();
    }

    public void loadTexture(){
        closedTexture = new Texture("chestClosed.png");
        openTexture = new Texture("chestOpened.png");

        width = closedTexture.getWidth();
        height = closedTexture.getHeight();

        currTexture = closedTexture;
    }

    public void openChest(){
        isUnopened = false;
        currTexture = openTexture;

    }

    public void setActive(){
        isActive = true;
    }
}
