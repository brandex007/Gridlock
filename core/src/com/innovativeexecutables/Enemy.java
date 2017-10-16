package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Enemy {
    private int x, y, width, height;
    private Texture enemyTexture;
    private int health = 100;
    private boolean isActive = false;
    private List<EnemyArrow> arrowList;
    private float delta;


    public Enemy(int x, int y) {
        loadEnemyTextures();
        this.x = x - (width / 2);
        this.y = y - (height / 2);
        arrowList = new ArrayList<EnemyArrow>();
    }

    public void render (Batch sb){
        // render if active
        if(isActive) {
            sb.draw(enemyTexture, x, y);
            for (EnemyArrow arrow : arrowList) {
                arrow.render(sb);
            }
        }
    }

    public void update(float delta){
        this.delta = delta;

        // update arrows
        for(EnemyArrow enemyArrow : arrowList){
            enemyArrow.update(delta);
        }
    }


    public void loadEnemyTextures(){
        enemyTexture = new Texture("enemy.png");

        width = enemyTexture.getWidth();
        height = enemyTexture.getHeight();
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

    // activate enemy!
    public void setActive(boolean isActive){
        // set active only once!
        if(this.isActive == false){
            this.isActive = isActive;

            // add arrow to list
            arrowList.add(new EnemyArrow(x,y));

            /*// add arrow every 2 seconds
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task(){
                                                  @Override
                                                  public void run() {
                                                      arrowList.add(new EnemyArrow(x,y));
                                                  }
                                              }
                , 0        //    (delay)
                , 1     //    (seconds)
            );*/
        }

    }

}
