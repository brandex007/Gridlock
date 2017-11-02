package com.innovativeexecutables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.innovativeexecutables.Gridlock.*;

// Player
public class Player{
    public static float x, y;
    private Texture playerLeftTexture, playerRightTexture, playerUpTexture, playerDownTexture, attackLeftTexture, attackRightTexture, curTexture,
            playerLeftTextureSPR, playerRightTextureSPR, playerUpTextureSPR, playerDownTextureSPR, attackLeftTextureSPR, attackRightTextureSPR, curTextureSPR,
            playerLeftTextureAXE, playerRightTextureAXE, playerUpTextureAXE, playerDownTextureAXE, attackLeftTextureAXE, attackRightTextureAXE, curTextureAXE;
    public float regularSpeed = 100;
    public static float speed = 100;
    public boolean UP_TOUCHED, DOWN_TOUCHED, LEFT_TOUCHED, RIGHT_TOUCHED;
    public static float width, height;
    public static int health = 100;
    public boolean isDead = false;
    public boolean isNotAttacking = true;
    public enum Weapon {SWORD,SPEAR,AXE}
    public Weapon weapon = Weapon.SWORD;
    public int attack = 10;
    public Spear spear;
    public Axe axe;
    public List<Enum> weaponsHaventUsedYet;

    // faced left or right last
    public boolean facedRightLast = true;

    String state = "walk";

    public Player(int x, int y) {

        this.x = x;
        this.y = y;

        loadPlayerTextures();
        weaponsHaventUsedYet = new ArrayList<Enum>();

        weaponsHaventUsedYet.add(Weapon.AXE);
        weaponsHaventUsedYet.add(Weapon.SPEAR);
    }

    public void update (float delta){

        UP_TOUCHED = false;
        DOWN_TOUCHED = false;
        LEFT_TOUCHED = false;
        RIGHT_TOUCHED = false;

        // update player movement
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && x > 0){
            x -= speed * delta;
            LEFT_TOUCHED = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && x < VIEWPORT_WIDTH - width) {
            x += speed * delta;
            RIGHT_TOUCHED = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && y < VIEWPORT_HEIGHT - height){
            y += speed * delta;
            UP_TOUCHED = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && y > 0){
            y -= speed * delta;
            DOWN_TOUCHED = true;
        }

    }

    public void setY(float value){
        y = value;
    }

    public void setX(float value){
        x = value;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public void render (Batch sb){

        if(health <= 0){
            isDead = true;
            System.out.print("game over");
        }

        // load walking textures if in walking state
        if(state == "walk") {
            if (LEFT_TOUCHED) {
                facedRightLast = false;
                curTexture = playerLeftTexture;
            } else if (RIGHT_TOUCHED) {
                facedRightLast = true;
                curTexture = playerRightTexture;
            } else if (UP_TOUCHED) {
                curTexture = playerUpTexture;
            } else {
                curTexture = playerDownTexture;
            }
        }

        // load attacking texture if in attack state
        if(state == "attack"){
            if(facedRightLast == false){
                curTexture = attackLeftTexture;
            }else if(facedRightLast){
                curTexture = attackRightTexture;
            }
        }

        if(weapon == Weapon.AXE) {
            axe.render(sb);
        }else if(weapon == Weapon.SPEAR){
            spear.render(sb);
        }
        sb.draw(curTexture,x - 10,y);

    }

    public void loadPlayerTextures(){
        playerUpTexture = new Texture("playerUp.png");
        playerDownTexture = new Texture("playerDown.png");
        playerLeftTexture = new Texture("playerLeft.png");
        playerRightTexture = new Texture("playerRight.png");
        attackLeftTexture = new Texture("playerattackleft.png");
        attackRightTexture = new Texture("playerattackright.png");

        playerUpTextureSPR = new Texture("playerUpSPR.png");
        playerDownTextureSPR = new Texture("playerDownSPR.png");
        playerLeftTextureSPR = new Texture("playerLeftSPR.png");
        playerRightTextureSPR = new Texture("playerRightSPR.png");
        attackLeftTextureSPR = new Texture("playerattackleftSPR.png");
        attackRightTextureSPR = new Texture("playerattackrightSPR.png");

        playerUpTextureAXE = new Texture("playerUpAXE.png");
        playerDownTextureAXE = new Texture("playerDownAXE.png");
        playerLeftTextureAXE = new Texture("playerLeftAXE.png");
        playerRightTextureAXE = new Texture("playerRightAXE.png");
        attackLeftTextureAXE = new Texture("playerattackleftAXE.png");
        attackRightTextureAXE = new Texture("playerattackrightAXE.png");

        curTexture = playerDownTexture;

        width = playerDownTexture.getWidth() - 20;
        height = playerDownTexture.getHeight();
    }

    public float getSpeed() {
        return speed;
    }

    public float getRegularSpeed(){
        return regularSpeed;
    }

    public void setSpeed(float speed){
        this.speed = speed;
    }

    public static void setHealth(int health){
        if(health >= 0)
            Player.health = health;
    }

    public static int getHealth(){
        return health;
    }

    public void attack(){
        state = "attack";

        // stop attack after 0.3 seconds
        com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                                                  @Override
                                                  public void run() {
                                                        stopAttack();
                                                  }
                                              }
                , 0.3f       //    (delay)
        );
    }

    public void stopAttack(){
        state = "walk";
    }

    public void addWeapon(int chestX, int chestY) {

        // randomize item
        int max = 2;
        int min = 1;

        boolean randomizing = true;
        int choice = 1;

        while(randomizing) {
            Random random = new Random();
            choice = random.nextInt(max - min + 1) + min;

            // try another random number if item was already used
            switch (choice){
                case 1:
                    if(weaponsHaventUsedYet.contains(Weapon.SPEAR))
                        randomizing = false;
                    break;
                case 2:
                    if(!weaponsHaventUsedYet.contains(Weapon.SPEAR))
                        randomizing = false;
                    break;
                default:
                    break;
            }
        }

        switch (choice) {
            case 1:
                weapon = Weapon.SPEAR;
                attack = 30;



                spear = new Spear(chestX, chestY);
                // remove item from chest after 2 seconds
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                                                          @Override
                                                          public void run() {
                                                              spear.removeFromChest();
                                                              // change textures
                                                              playerUpTexture = playerUpTextureSPR;
                                                              playerLeftTexture = playerLeftTextureSPR;
                                                              playerRightTexture = playerRightTextureSPR;
                                                              playerDownTexture = playerDownTextureSPR;
                                                              attackLeftTexture = attackLeftTextureSPR;
                                                              attackRightTexture = attackRightTextureSPR;
                                                          }
                                                      }
                        , 1f       //    (delay)
                );

                weaponsHaventUsedYet.remove(Weapon.SPEAR);
                break;

            case 2:
                weapon = Weapon.AXE;
                attack = 50;

                axe = new Axe(chestX, chestY);
                // remove item from chest after 2 seconds
                com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                                                          @Override
                                                          public void run() {
                                                              axe.removeFromChest();
                                                              // change textures
                                                              playerUpTexture = playerUpTextureAXE;
                                                              playerLeftTexture = playerLeftTextureAXE;
                                                              playerRightTexture = playerRightTextureAXE;
                                                              playerDownTexture = playerDownTextureAXE;
                                                              attackLeftTexture = attackLeftTextureAXE;
                                                              attackRightTexture = attackRightTextureAXE;
                                                          }
                                                      }
                        , 1f       //    (delay)
                );
                weaponsHaventUsedYet.remove(Weapon.AXE);
                break;

            default:
                break;
        }
    }

    public void resetPlayer(){
        speed = 100;
        health = 100;
        isDead = false;
        isNotAttacking = true;
        weaponsHaventUsedYet.add(Weapon.AXE);
        weaponsHaventUsedYet.add(Weapon.SPEAR);
        attack = 10;
        state = "walk";
        facedRightLast = true;
        loadPlayerTextures();
    }

}
