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
    private String enemyType;

    public Enemy(int x, int y, String enemyType) {
        this.enemyType = enemyType;
        loadEnemyTextures();


        // 3.3.1 Enemy has an initial health value of 100, and professor boss has health of 120
        if(enemyType.equals("enemy1") || enemyType.equals("enemy3") || enemyType.equals("enemy4") || enemyType.equals("enemy5") || enemyType.equals("enemy6"))
            health = 100;
        else if(enemyType.equals("professorEnemy"))
            health = 125;

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

        // update arrows
        for(EnemyAxe enemyArrow : arrowList){
            enemyArrow.update(delta);
        }
    }


    public void loadEnemyTextures(){
        if(enemyType.equals("enemy1"))
            enemyTexture = new Texture("enemy.png");
        else if(enemyType.equals("professorEnemy"))
            enemyTexture = new Texture("enemy2.png");
        else if(enemyType.equals("enemy3"))
            enemyTexture = new Texture("enemy3.png");
        else if(enemyType.equals("enemy4"))
            enemyTexture = new Texture("enemy4.png");
        else if(enemyType.equals("enemy5"))
            enemyTexture = new Texture("enemy5.png");
        else if(enemyType.equals("enemy6"))
            enemyTexture = new Texture("enemy6.png");

        //System.out.println(enemyType);
        //enemyTexture = new Texture("enemy2.png");


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

            // 3.3.3.1 Boss must begin attacking by throwing axes in all directions, relatively towards player
            // add arrow to list with players position at time of throw
            // add arrow every 2 seconds
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task(){
                                                  @Override
                                                  public void run() {
                                                      arrowList.add(new EnemyAxe(x + (width / 2),y + (height / 2),Player.x + (Player.width / 2), Player.y + (Player.height / 2)));
                                                  }
                                              }
                , 0        //    (delay)
                , 3     //    (seconds)
            );
        }

    }

    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth(){
        return health;
    }

    /*public void dispose(){
        enemyTexture.dispose();

        for(EnemyAxe enemyAxe : arrowList){
            enemyAxe.dispose();
        }
    }*/

    public void remove(){
        for (EnemyAxe enemyAxe : arrowList){
            enemyAxe.remove();
        }
    }
}
