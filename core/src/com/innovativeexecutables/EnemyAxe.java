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
        this.destinationX = playerx;
        this.destinationY = playery;

        Random r = new Random();
        throwInterval = 0.95 + r.nextFloat() * (1.05 - 0.95); // determine the ratio (90% to 110%) to throw the axe (such as 90% towards the player or 110%)

        // update destination based on throw interval (between 95% and 105% of player's location when first thrown)
        this.destinationX = (float) (throwInterval * this.destinationX);
        this.destinationY = (float) (throwInterval * this.destinationY);

        // rotate every 0.2 seconds
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Hello World");
            }
        }, 0, 5000);
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
