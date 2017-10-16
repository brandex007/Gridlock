package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class EnemyArrow {
    private float x, y, width, height;
    private Texture enemyArrowTexture;
    private boolean isActive = true;
    private int speed = 100;

    public EnemyArrow(float x, float y){
        enemyArrowTexture = new Texture("enemyArrow.png");

        width = enemyArrowTexture.getWidth();
        height = enemyArrowTexture.getHeight();

        this.x = x;
        this.y = y;
    }

    public void update(float delta){
        // rotate

        // move position
        if(isActive)
            x += speed * delta;
    }

    public void render (Batch sb){
        // render if active
        if(isActive) {
            sb.draw(enemyArrowTexture, x, y);
        }
    }

    public void remove(){
        isActive = false;
    }
}
