package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Enemy {
    int x, y, width, height;
    private Texture enemyTexture;

    public Enemy(int x, int y) {
        loadEnemyTextures();
        this.x = x - (width / 2);
        this.y = y - (height / 2);
    }

    public void render (Batch sb){

        sb.draw(enemyTexture,x,y);
    }

    public void loadEnemyTextures(){
        enemyTexture = new Texture("enemy.png");

        width = enemyTexture.getWidth();
        height = enemyTexture.getHeight();
    }
}
