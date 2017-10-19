package com.innovativeexecutables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.List;

public class Enemy {
    private int x, y, width, height;
    private Texture enemyTexture;
    private int health = 100;
    private boolean isActive = false;
    private List<EnemyAxe> arrowList;
    private float delta;


    public Enemy(int x, int y) {
        loadEnemyTextures();
        this.x = x - (width / 2);
        this.y = y - (height / 2);
        arrowList = new ArrayList<EnemyAxe>();
    }

    public void render (Batch sb){
        // render if active
        if(isActive) {
            sb.draw(enemyTexture, x, y);
            for (EnemyAxe arrow : arrowList) {
                arrow.render(sb);
            }
        }
    }

    public void update(float delta){
        this.delta = delta;

        // update arrows
        for(EnemyAxe enemyArrow : arrowList){
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

            // add arrow to list with players position at time of throw
            // add arrow every 2 seconds
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task(){
                                                  @Override
                                                  public void run() {
                                                      arrowList.add(new EnemyAxe(x + (width / 2),y + (height / 2),Player.x + (Player.width / 2), Player.y + (Player.height / 2)));
                                                  }
                                              }
                , 0        //    (delay)
                , 1     //    (seconds)
            );
        }

    }

}
