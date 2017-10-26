package com.innovativeexecutables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static com.innovativeexecutables.Gridlock.*;

// Player
public class Player{
    public static float x, y;
    private Texture playerLeftTexture, playerRightTexture, playerUpTexture, playerDownTexture, attackLeftTexture, attackRightTexture, curTexture;
    public float regularSpeed = 100;
    public static float speed = 100;
    public boolean UP_TOUCHED, DOWN_TOUCHED, LEFT_TOUCHED, RIGHT_TOUCHED;
    public static float width, height;
    public static int health = 100;
    public boolean isDead = false;
    public boolean isNotAttacking = true;

    // faced left or right last
    public boolean facedRightLast = true;

    String state = "walk";

    public Player(int x, int y) {

        this.x = x;
        this.y = y;

        loadPlayerTextures();
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

        sb.draw(curTexture,x - 10,y);

    }

    public void loadPlayerTextures(){
        playerUpTexture = new Texture("playerUp.png");
        playerDownTexture = new Texture("playerDown.png");
        playerLeftTexture = new Texture("playerLeft.png");
        playerRightTexture = new Texture("playerRight.png");
        attackLeftTexture = new Texture("playerattackleft.png");
        attackRightTexture = new Texture("playerattackright.png");

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

}
