package com.innovativeexecutables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import static com.innovativeexecutables.Gridlock.*;

// Player
public class Player{
    private float x, y;
    private Texture playerTexture;
    private float speed = 60;
    public static boolean UP_TOUCHED, DOWN_TOUCHED, LEFT_TOUCHED, RIGHT_TOUCHED;
    private float width, height;
    private TiledMapTileLayer collisionLayer;

    public Player(TiledMapTileLayer collisionLayer) {
        this.collisionLayer = collisionLayer;

        x = 10;
        y = 10;

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

        /*// set ship texture:
        if (UP_TOUCHED == true && DOWN_TOUCHED == false) {
            ship = ship_up;
        } else if (DOWN_TOUCHED == true && UP_TOUCHED == false) {
            ship = ship_down;
        } else {
            ship = ship_middle;
        }*/

        checkCollisionMap();
    }

    public void render (Batch sb){

        sb.draw(playerTexture,x,y);
    }

    public void loadPlayerTextures(){
        playerTexture = new Texture("knight.png");
        //ship_up = new Texture("ship_up.png");
        //ship_down = new Texture("ship_down.png");


        width = playerTexture.getWidth();
        height = playerTexture.getHeight();
    }

    public float getSpeed() {
        return speed;
    }

    public void checkCollisionMap(){

        // Check For Collision
        boolean collisionWithMap = false;

        collisionWithMap = isCellBLocked(x + width,y) || isCellBLocked(x + width / 2, y)|| isCellBLocked(x, y);


        // React to Collision
        if (collisionWithMap) {
            System.out.println("player-map collision!!!");
            if(UP_TOUCHED){
                y -= 1;
            }
            if(DOWN_TOUCHED){
                y += 1;
            }
            if(LEFT_TOUCHED){
                x += 1;
            }
            if(RIGHT_TOUCHED){
                x -= 1;
            }
        }
    }

    public boolean isCellBLocked(float x, float y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(
                (int) (x / collisionLayer.getTileWidth()),
                (int) (y / collisionLayer.getTileHeight()));

        return cell != null && cell.getTile() != null
                && cell.getTile().getProperties().containsKey("collision");
    }

}
