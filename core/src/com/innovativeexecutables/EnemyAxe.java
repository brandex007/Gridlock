package com.innovativeexecutables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.StrictMath.abs;

public class EnemyAxe {
    private float x, y, width, height, destinationX, destinationY;
    private boolean negX = false, negY = false;
    private Texture enemyArrowTexture;
    private boolean isActive = true;
    private double throwInterval;
    private int axeRotateCounter = 0;
    private Sound axeImpactSound;

    public EnemyAxe(float x, float y, float playerx, float playery){
        enemyArrowTexture = new Texture("enemyAxe1.png");

        width = enemyArrowTexture.getWidth();
        height = enemyArrowTexture.getHeight();
        axeImpactSound = Gdx.audio.newSound(Gdx.files.internal("gruntsound.mp3"));

        this.x = x;
        this.y = y;
        this.destinationX = playerx - Player.width / 2;
        this.destinationY = playery - Player.height / 2;

        Random r = new Random();
        // throw more accurate if close
        if((abs(x - playerx) < 100 && abs(y - playery) < 100)){
            throwInterval = 0.95 + r.nextFloat() * (1.05 - 0.95); // determine the ratio (95% to 105%) to throw the axe (such as 90% towards the player or 110%)
        }else if ((abs(x - playerx) < 300 && abs(y - playery) < 300)){
            throwInterval = 0.93 + r.nextFloat() * (1.07 - 0.93);
        } else{
            throwInterval = 0.91 + r.nextFloat() * (1.09 - 0.91);
        }

        // update destination based on throw interval (between 95% and 105% of player's location when first thrown)
       this.destinationX = (float) (throwInterval * this.destinationX);
       this.destinationY = (float) (throwInterval * this.destinationY);

        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task(){
                                                  @Override
                                                  public void run() {
                                                      axeRotateCounter++;
                                                      if(axeRotateCounter == 4)
                                                          axeRotateCounter = 0;

                                                      switch (axeRotateCounter){
                                                          case 0:
                                                              enemyArrowTexture = new Texture("enemyAxe1.png");
                                                              break;
                                                          case 1:
                                                              enemyArrowTexture = new Texture("enemyAxe2.png");
                                                              break;
                                                          case 2:
                                                              enemyArrowTexture = new Texture("enemyAxe3.png");
                                                              break;
                                                          case 3:
                                                              enemyArrowTexture = new Texture("enemyAxe4.png");
                                                              break;
                                                      }                                                  }
                                              }
                , 0        //    (delay)
                , 0.2f     //    (seconds)
        );

    }

    public void update(float delta){

        if(isActive) {

            // move position
            float angle; // angle between play and enemy (right triangle)
            angle = (float) Math.atan2(destinationY - y, destinationX - x); // find polar coordinates (r,theta)
            x += (float) Math.cos(angle) * 125 * delta;
            y += (float) Math.sin(angle) * 125 * delta;

            // handle collision
            if (x + width >= Player.x && x <= Player.x + Player.width && y + height >= Player.y && y <= Player.y + Player.height) {
                Player.setHealth(Player.getHealth() - 7);
                // plays impact sound for axe
                axeImpactSound.play();

                // disposes resource after player death
                if (Player.getHealth()==0)
                {
                    axeImpactSound.dispose();
                }

                remove();

            }else if(abs(destinationY - y) < 1 && abs(destinationX - x) < 1){ // if axe is within 1 pixel from destination, remove axe
                remove();
            }
        }
    }

    public void render (Batch sb){
        // render if active
        if(isActive) {
            sb.draw(enemyArrowTexture, x, y);
        }
    }

    public void remove(){
        isActive = false;
        enemyArrowTexture.dispose();
    }
}
