package com.innovativeexecutables;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.innovativeexecutables.Player.*;

public class Gridlock extends ApplicationAdapter {
    // map is 32 by 32 tiles with 32 pixel tiles

    public static int VIEWPORT_WIDTH = 1024;
    public static int VIEWPORT_HEIGHT = 1024;

    private Batch sb;
    public static OrthographicCamera cam;
    private Player player;
    private float delta;
    private int mouseClickX, mouseClickY;
    private Texture playButton,resumeButton, exitButton;

    private Music backgroundMusic, enemyMusic;
    private Sound impactSound;
    private boolean playFlag, backgroundMusicFLag, enemyMusicFlag;
    // TiledMap
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tileMapRenderer;

    // Collision
    private TiledMapTileLayer obstaclesCollisionLayer, hazardsCollisionLayer;

    // Tile
    Tile[][] tileList = new Tile[32][32];
    List<Enemy> enemies;

    // Displays for score, health, and time
    BitmapFont scoreFont, healthFont, timeFont;
    String scoreString, healthString, timeString;

    int time = 0;

    @Override
    public void create() {
        cam = new OrthographicCamera();
        cam.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        //creates play button object
        playButton = new Texture("play.png");

        tileMap = new TmxMapLoader().load("tiledmap1.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        sb = tileMapRenderer.getBatch();
        sb.setProjectionMatrix(cam.combined);

        obstaclesCollisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Obstacles");
        hazardsCollisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Hazards");
        // track created by Marcelo Fernandez  https://soundcloud.com/marcelo-fernandez3
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("medmusic.ogg"));
        enemyMusic = Gdx.audio.newMusic(Gdx.files.internal("enemymusic.mp3"));
        impactSound = Gdx.audio.newSound(Gdx.files.internal("gruntsound.wav"));


        player = new Player(475, 10);

        enemies = new ArrayList<Enemy>();

        // tiles
        for (int i = 0; i < Tile.tileSize; i++) {
            for (int j = 0; j < Tile.tileSize; j++) {
                tileList[i][j] = new Tile(i, j);
            }
        }

		/*
        // spawn enemy at every tile
		for(int i = 0; i<Tile.tileSize; i++){
			for(int j = 0; j<Tile.tileSize; j++){
				enemies.add(new Enemy(tileList[i][j].getX(), tileList[i][j].getY()));
			}
		}*/

        // spawn enemy at tile 20,20
        enemies.add(new Enemy(tileList[20][20].getX(), tileList[20][20].getY()));

        // initiate fonts
        scoreString = "Score: 0";
        scoreFont = new BitmapFont();

        healthString = "Health: 100";
        healthFont = new BitmapFont();

        timeString = "Time: 0";
        timeFont = new BitmapFont();

        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               updateTime();
                           }
                       }
                , 0        //    (delay)
                , 1     //    (seconds)
        );
    }

    public void updateTime() {
        if (playFlag) {
            time = time + 1;
        }


    }

    //plays background music or enemy music while game is running
    public void playBackgroundMusic() {


        if (backgroundMusicFLag) {


            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(.5f);
            backgroundMusic.play();

        } else if (enemyMusicFlag) {
            backgroundMusic.stop();
            enemyMusic.setLooping(true);
            enemyMusic.setVolume(.25f);
            enemyMusic.play();

        }


    }

    public void playImpactSound() {
        // plays impact sound every 10 points of health lost or upon initial impact
        if ((player.getHealth() % 10 == 0 || player.getHealth() == 99) && player.getHealth() != 0) {
            impactSound.play(100, 1.2f, 0);
        }

    }

    @Override
    public void render () {

		playBackgroundMusic();
		/*if left mouse button is clicked game will begin
		 * when mouse clicked location of click is saved and
		  * click location is checked to see if the play button was clicked*/


	if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
	{
		mouseClickX=Gdx.input.getX();
		mouseClickY=Gdx.input.getY();

		if ((mouseClickX<=500&&mouseClickX>=350)&&(mouseClickY<=510&&mouseClickY>=465))
		{
			playFlag=true;
			backgroundMusicFLag=true;
		}
		if ((mouseClickX<=680&&mouseClickX>=530)&&(mouseClickY<=510&&mouseClickY>=465))
		{
			Gdx.app.exit();
		}
	}
	if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
	{
		playFlag=false;
	}

	// provides updates if play has been pressed
        if (playFlag) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            delta = Gdx.graphics.getRawDeltaTime();

            // updates
            player.update(delta);
            for (Enemy enemy : enemies) {
                enemy.update(delta);
            }

            // collisions
            checkPlayerCollisionMap();
            checkForCharCollisions();

            cam.update();

            healthString = "Health: " + player.getHealth();

            // player attacks using spacebar
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

                // only attack when not currently attacking
                if (player.state == "walk") {

                    player.attack();

                    for (Enemy enemy : enemies) {
                        if (enemy.getX() < player.getX() && enemy.getX() < player.getX() + enemy.getWidth() && enemy.getY() < player.getY() && enemy.getY() < player.getY() + enemy.getHeight()) {
                            enemy.setHealth(enemy.getHealth() - 35);
                        }
                    }
                }
            }

            // Get an iterator.
            Iterator<Enemy> iterator = enemies.iterator();

            // check if any enemies are dead and remove them
            while(iterator.hasNext()) {
                Enemy enemy = iterator.next();

                if(enemy.getHealth() < 0){
                    iterator.remove();
                }
            }

        }

        // map rendering
        tileMapRenderer.setView(cam);
        tileMapRenderer.render();
        sb = tileMapRenderer.getBatch();

        // draw text
        sb.begin();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(2.0f);
        scoreFont.draw(sb, scoreString, VIEWPORT_WIDTH - 150, VIEWPORT_HEIGHT - 10);

        timeString = "Time: " + time;
        timeFont.setColor(Color.WHITE);
        timeFont.getData().setScale(2.0f);
        timeFont.draw(sb, timeString, VIEWPORT_WIDTH - 150, VIEWPORT_HEIGHT - 40);

        healthFont.setColor(Color.WHITE);
        healthFont.getData().setScale(2.0f);
        healthFont.draw(sb, healthString, VIEWPORT_WIDTH - 150, VIEWPORT_HEIGHT - 70);
        sb.end();

        sb.begin();
        if (!(player.getX() > 416 && player.getX() < 544 && player.getY() > 480 && player.getY() < 700)) {
            player.setSpeed(player.getRegularSpeed());
            player.render(sb);
        } else {
            player.setSpeed(player.getRegularSpeed() * 3f);
        }

        // draws play button at start or when game is stopped
	if(playFlag==false) {
		if (time == 0){
			//draws play button object
			sb.draw(playButton, 350, 512);
		}else if (time >= 0) {
			resumeButton=new Texture("resume.png");
			exitButton=new Texture("exit.png");
			//draws resume button object
			sb.draw(resumeButton, 350, 512);
			sb.draw(exitButton, 530, 512);
		}
	}

	// update and draw enemies
	for(Enemy enemy : enemies){
		enemy.render(sb);
	}

	sb.end();
    }

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        enemyMusic.dispose();
        impactSound.dispose();

    }

    public void checkForCharCollisions() {

        //// player collision with enemies
        for (Enemy enemy : enemies) {
            // active enemy once when player is nearby
            if (player.getX() > enemy.getX() - 150 && player.getX() < enemy.getX() + 150) {
                if (player.getY() > enemy.getY() - 150 && player.getY() < enemy.getY() + 150) {
                    enemy.setActive(true);
                    backgroundMusicFLag = false;
                    enemyMusicFlag = true;

                }
            }

            // player takes damage from enemy if not attacking
            if (player.getX() > enemy.getX() && player.getX() < enemy.getX() + enemy.getWidth()) {
                if (player.getY() > enemy.getY() - enemy.getHeight() / 2 && player.getY() < enemy.getY() + enemy.getHeight() / 2) {
                    if (player.isNotAttacking) {
                        player.setHealth(player.getHealth() - 1);
                        playImpactSound();

                        // collision
                        if (player.UP_TOUCHED) {
                            player.setY(player.getY() - Player.speed * delta);
                        }
                        if (player.DOWN_TOUCHED) {
                            player.setY(player.getY() + Player.speed * delta);
                        }
                        if (player.LEFT_TOUCHED) {
                            player.setX(player.getX() + Player.speed * delta);
                        }
                        if (player.RIGHT_TOUCHED) {
                            player.setX(player.getX() - Player.speed * delta);
                        }
                    }
                }
            }

        }

    }

    public void checkPlayerCollisionMap() {

        boolean collisionWithObstacles = false;
        boolean collisionWithHazards = false;

        collisionWithObstacles = isCellBLocked(2, player.getX() + player.getWidth(), player.getY()) || isCellBLocked(2, player.getX() + player.getWidth() / 2, player.getY()) || isCellBLocked(2, player.getX(), player.getY());
        collisionWithHazards = isCellBLocked(3, player.getX() + player.getWidth(), player.getY()) || isCellBLocked(3, player.getX() + player.getWidth() / 2, player.getY()) || isCellBLocked(3, player.getX(), player.getY());

        // React to Collision
        if (collisionWithObstacles) {
            if (player.UP_TOUCHED) {
                player.setY(player.getY() - Player.speed * delta);
            }
            if (player.DOWN_TOUCHED) {
                player.setY(player.getY() + Player.speed * delta);
            }
            if (player.LEFT_TOUCHED) {
                player.setX(player.getX() + Player.speed * delta);
            }
            if (player.RIGHT_TOUCHED) {
                player.setX(player.getX() - Player.speed * delta);
            }
        }

        TiledMapTileLayer.Cell cell = hazardsCollisionLayer.getCell(
                (int) (player.getX() / hazardsCollisionLayer.getTileWidth()),
                (int) (player.getY() / hazardsCollisionLayer.getTileHeight()));

        if (collisionWithHazards) {
            player.setHealth(player.getHealth() - 1);
            playImpactSound();

            System.out.print("lost health to hazard");
        }
    }

    public boolean isCellBLocked(int layer, float x, float y) {
        if (layer == 3) {
            TiledMapTileLayer.Cell cell = hazardsCollisionLayer.getCell(
                    (int) (x / hazardsCollisionLayer.getTileWidth()),
                    (int) (y / hazardsCollisionLayer.getTileHeight()));

            return cell != null && cell.getTile() != null;
        } else { // return layer 2
            TiledMapTileLayer.Cell cell = obstaclesCollisionLayer.getCell(
                    (int) (x / obstaclesCollisionLayer.getTileWidth()),
                    (int) (y / obstaclesCollisionLayer.getTileHeight()));

            return cell != null && cell.getTile() != null;
        }


    }

}
