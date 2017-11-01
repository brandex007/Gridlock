package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class Weapon {
    protected int x, y, width, height;
    protected boolean isActive = true;
    protected Texture texture;

    public Weapon(int x, int y){
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
            sb.draw(texture, x, y);
        }
    }


    public void removeFromChest(){
        isActive = false;
    }



    public void loadTexture(){

        width = texture.getWidth();
        height = texture.getHeight();
    }

}
