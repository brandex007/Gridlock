package com.innovativeexecutables;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Gridlock extends ApplicationAdapter {
    // map is 32 by 32 tiles with 32 pixel tiles

    public static int VIEWPORT_WIDTH = 1024;
    public static int VIEWPORT_HEIGHT = 1024;

    private Batch sb;
    public static OrthographicCamera cam;
    private Player player;
    private float delta;
    private int mouseClickX, mouseClickY;
    private Texture menuButton,gameOverWin,gameOverLoss;

    private Music backgroundMusic, enemyMusic;
    private Sound impactSound,winSound,loseSound,enemyHurt;
    private boolean playFlag, backgroundMusicFLag, enemyMusicFlag,playWin,playLose;
    // TiledMap
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tileMapRenderer;

    // Collision
    private TiledMapTileLayer obstaclesCollisionLayer, hazardsCollisionLayer;

    // Tile
    Tile[][] tileList = new Tile[32][32];

    // Enemies
    List<Enemy> enemies;

    // Chests
    List<Chest> chests;

    // Displays for score, health, and time
    BitmapFont scoreFont, healthFont, timeFont, highScoreFont;
    String scoreString, healthString, timeString, highScoreString;

    int time = 0;

    // Score class
    Score score;

    // Saved data preferences
    Preferences prefs;

    int highScore;

    @Override
    public void create() {
        prefs = Gdx.app.getPreferences("Preferences");

        cam = new OrthographicCamera();
        cam.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        //creates play button object
        menuButton = new Texture("menu.png");
        gameOverWin=new Texture("gameoverwin.png");
        gameOverLoss=new Texture("gameoverlose.png");


        // 3.1.1.2 Map will be loaded
        tileMap = new TmxMapLoader().load("tiledmap1.tmx");
        tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

        sb = tileMapRenderer.getBatch();
        sb.setProjectionMatrix(cam.combined);

        obstaclesCollisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Obstacles");
        hazardsCollisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Hazards");
        // track created by Marcelo Fernandez  https://soundcloud.com/marcelo-fernandez3
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("medmusic.ogg"));
        enemyMusic = Gdx.audio.newMusic(Gdx.files.internal("enemymusic.mp3"));
        impactSound = Gdx.audio.newSound(Gdx.files.internal("gruntsound.mp3"));
        //Level up sound effects" by Bart Kelsey. Commissioned by Will Corwin for OpenGameArt.org
        winSound = Gdx.audio.newSound(Gdx.files.internal("chipquest.wav"));
        //credit Joseph Gilbert / Kistol
        loseSound = Gdx.audio.newSound(Gdx.files.internal("gameo.ogg"));
        enemyHurt = Gdx.audio.newSound(Gdx.files.internal("enemyinjured.mp3"));


        // 3.1.1.1 Game will render a user character
        player = new Player(475, 10);

        enemies = new ArrayList<Enemy>();

        chests = new ArrayList<Chest>();

        // tiles
        for (int i = 0; i < Tile.tileSize; i++) {
            for (int j = 0; j < Tile.tileSize; j++) {
                tileList[i][j] = new Tile(i, j);
            }
        }

        spawnObjectsAndEnemies();

        // initiate fonts
        // 3.1.2 Score must be visible to user during gameplay
        scoreString = "Score: 0";
        scoreFont = new BitmapFont();

        healthString = "Health: 100";
        healthFont = new BitmapFont();

        timeString = "Time: 0";
        timeFont = new BitmapFont();

        highScoreString = "High Score: ";
        highScoreFont = new BitmapFont();

        // 3.1.5 The game will have timer that will be counted upward in seconds upon start of the game
        Timer.schedule(new Timer.Task() {
                           @Override
                           public void run() {
                               updateTime();
                           }
                       }
                , 0        //    (delay)
                , 1     //    (seconds)
        );

        score = new Score();
    }

    public void spawnObjectsAndEnemies() {
        // 3.1.1.4 All bosses will be loaded and spawned invisibly
        enemies.add(new Enemy(tileList[4][4].getX(), tileList[22][22].getY(), "enemy1"));
        enemies.add(new Enemy(tileList[24][24].getX(), tileList[27][27].getY(), "professorEnemy"));

        // 3.1.1.3 All chests will be loaded
        // spawn chests
        chests.add(new Chest(tileList[30][27].getX(), tileList[30][27].getY()));
        chests.add(new Chest(tileList[4][30].getX(), tileList[4][30].getY()));
        chests.add(new Chest(tileList[27][27].getX(), tileList[21][21].getY()));
        chests.add(new Chest(tileList[23][22].getX(), tileList[24][24].getY()));
        chests.add(new Chest(tileList[6][6].getX(), tileList[4][4].getY()));
        chests.add(new Chest(tileList[20][20].getX(), tileList[14][14].getY()));
    }

    public void updateTime() {
        if (playFlag) {
            time = time + 1;
        }


    }

    // 3.1.6 The game will have background music playing during gameplay
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

    // 3.1.7 The game will have sound effects for each impact
    public void playImpactSound() {
        // plays impact sound every 10 points of health lost or upon initial impact
        if ((player.getHealth() % 10 == 0 || player.getHealth() == 99) && player.getHealth() != 0) {
            impactSound.play(100, 1.2f, 0);
        }

    }

    @Override
    public void render() {

        playBackgroundMusic();
        /*if left mouse button is clicked game will begin
		 * when mouse clicked location of click is saved and
		  * click location is checked to see if the play button was clicked*/

        // 	3.1.1 Pressing start button initiates gameplay
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            mouseClickX = Gdx.input.getX();
            mouseClickY = Gdx.input.getY();

            if ((mouseClickX <= 610 && mouseClickX >= 410) && (mouseClickY <= 450 && mouseClickY >= 400)) {
                restartGame();
            } 	// 3.1.4 There must be an exit button, triggered by the escape key
            else if ((mouseClickX <= 610 && mouseClickX >= 410) && (mouseClickY <= 590 && mouseClickY >= 550)) {
                // 3.1.4.1 Game must exit
                Gdx.app.exit();
            } else if ((mouseClickX <= 610 && mouseClickX >= 410) && (mouseClickY <= 525 && mouseClickY >= 475)) {
                playFlag = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            playFlag = false;
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

            scoreString = "Score: " + score.getScore();

            // collisions
            checkPlayerCollisionMap();
            checkForCharCollisions();
            checkForChestCollisions();

            cam.update();

            //	3.1.8 The game must display player health
            healthString = "Health: " + player.getHealth();

            // 	3.2.4 Player must be able to attack by the user pressing space bar
            // player attacks using spacebar
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {

                // only attack when not currently attacking
                if (player.state == "walk") {

                    player.attack();

                    for (Enemy enemy : enemies) {
                        if (enemy.getX() + enemy.getWidth() > player.getX() && enemy.getX() < player.getX() + enemy.getWidth() && enemy.getY() + enemy.getWidth() > player.getY() && enemy.getY() < player.getY() + enemy.getHeight()) {
                            // 	3.3.2 Enemy must lose damage specified by player attack when player is attacking enemy
                            enemy.setHealth(enemy.getHealth() - player.attack);
                            enemyHurt.play(100);


                        }
                    }
                }
            }

            // Get an iterator.
            Iterator<Enemy> iterator = enemies.iterator();

            // check if any enemies are dead and remove them, and update score
            while (iterator.hasNext()) {
                Enemy enemy = iterator.next();

                if (enemy.getHealth() < 0) {
                    iterator.remove();

                    // 200 points for killing enemy
                    score.setScore(score.getScore() + 200);
                    scoreString = "Score: " + score.getScore();
                }
            }

        }

        // 3.1.1.2 Map will be rendered
        // map rendering
        tileMapRenderer.setView(cam);
        tileMapRenderer.render();
        sb = tileMapRenderer.getBatch();

        // draw text
        // 3.1.2 Score must be visible to user during gameplay
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

        // 3.1.1.3 All chests will be rendered
        // draw chests
        for (Chest chest : chests) {
            chest.render(sb);
        }

        if (!(player.getX() > 416 && player.getX() < 544 && player.getY() > 480 && player.getY() < 700)) {
            player.setSpeed(player.getRegularSpeed());
            player.render(sb);
        } else {
            player.setSpeed(player.getRegularSpeed() * 3f);
        }

        // update and draw enemies
        for (Enemy enemy : enemies) {
            enemy.render(sb);

        }

        // 3.2.8 When player dies, player loses game
        if(player.isDead)
        {
            backgroundMusic.stop();
            enemyMusic.stop();
            impactSound.stop();

            // if player is dead displays game over
            if(!playLose){


                // 3.1.12.1 Game over music must play for win or loss
                loseSound.play(100);

                playLose=true;

            }



            // 3.1.12 Upon completion of the game, the game will indicate when the player has won or lost
            playFlag=false;
            // 3.2.8.3 Game should display lose text
            sb.draw(gameOverLoss,300,700);

            highScore = prefs.getInteger("score");
            if(score.getScore() > highScore) {
                prefs.putInteger("score", score.getScore());
                prefs.flush();
            }

        }

        //	3.3.4 Upon death of boss, player wins game
        if(enemies.isEmpty()){
            backgroundMusic.stop();
            enemyMusic.stop();
            impactSound.stop();
            if(!playWin){
                playWin=true;

                //	3.1.12.1 Game over music must play for win or loss
                winSound.play();

                highScore = prefs.getInteger("score");
                if(score.getScore() > highScore) {
                    prefs.putInteger("score", score.getScore());
                    prefs.flush();
                }
            }


            // 	3.1.12 Upon completion of the game, the game will indicate when the player has won or lost
            playFlag=false;
            // 	3.3.4.3 Game should display win text
            sb.draw(gameOverWin,300,700);
        }



        // 3.1.1 Pressing start button initiates gameplay
        // 3.1.3 There must be a menu with “Continue” & “New Game” options triggered by the escape key
        // draws play button at start or when game is stopped
        if (playFlag == false) {
            //draws play button object
            // 3.2.8.1 Game should offer to play again
            // 3.3.4.3 Game should offer to play again

            sb.draw(menuButton, 200, 200);

            // display high score
            if(player.isDead || enemies.isEmpty()) {
                // draw text
                // 3.1.2 Score must be visible to user during gameplay
                highScoreFont.setColor(Color.WHITE);
                highScoreFont.getData().setScale(2f);
                highScoreFont.draw(sb, highScoreString + highScore, 400, 710);
            }
        }


        sb.end();
    }

    // 3.1.11 The game dispose of all objects not in use

    @Override
    public void dispose() {
        backgroundMusic.dispose();
        enemyMusic.dispose();
        impactSound.dispose();
        enemyHurt.dispose();
        winSound.dispose();
        loseSound.dispose();

    }

    public void checkForCharCollisions() {

        //// player collision with enemies
        for (Enemy enemy : enemies) {
            //	3.3.3 When player is within 300 pixels from boss, the boss will become visible
            // active enemy once when player is nearby
            if (player.getX() > enemy.getX() - 300 && player.getX() < enemy.getX() + 300) {
                if (player.getY() > enemy.getY() - 300 && player.getY() < enemy.getY() + 300) {
                    enemy.setActive(true);
                    backgroundMusicFLag = false;
                    enemyMusicFlag = true;

                }
            }

            // player takes damage from enemy if not attacking
            if (player.getX() > enemy.getX() && player.getX() < enemy.getX() + enemy.getWidth()) {
                if (player.getY() > enemy.getY() - enemy.getHeight() / 2 && player.getY() < enemy.getY() + enemy.getHeight() / 2) {
                    if (player.isNotAttacking) {
                        //player.setHealth(player.getHealth() - 1);
                        //playImpactSound();

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

    // 3.2.3 Player must be able to pick up spear, sword, or armor
    public void checkForChestCollisions() {
        for (Chest chest : chests) {
            // show chest when nearby
            if (player.getX() > chest.getX() - 150 && player.getX() < chest.getX() + 150) {
                if (player.getY() > chest.getY() - 150 && player.getY() < chest.getY() + 150) {
                    chest.setActive();
                }
            }

            // open chest if colliding
            if (player.getX() > chest.getX() - 2 && player.getX() < chest.getX() + chest.getWidth() + 2) {
                if (player.getY() > chest.getY() - chest.getHeight() / 2 - 2 && player.getY() < chest.getY() + chest.getHeight() / 2 + 2) {
                    // activate chest if inactive
                    if (chest.isUnopened) {
                        chest.openChest();
                        System.out.println("chest");
                        player.addWeapon(chest.getX() + chest.getWidth() / 2, chest.getY() + chest.getHeight() / 2);

                        // add 50 points for finding item
                        score.setScore(score.getScore() + 50);
                        scoreString = "Score: " + score.getScore();
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

        // 3.2.7 When player comes in contact with hazards, player loses 1 health per frame (every split second)
        if (collisionWithHazards) {
            player.setHealth(player.getHealth() - 1);
            playImpactSound();

            //System.out.print("lost health to hazard");
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

    // 	3.1.10 The game is able to be restarted
    public void restartGame() {
        loseSound.stop();
        playLose=false;
        playWin=false;

        // remove all enemy stuff
        for(Enemy enemy : enemies){
            enemy.remove();
        }

        // remove all chests
        for(Chest chest : chests){
            chest.remove();
        }

        time = 0;
	    score.setScore(200);
        player.resetPlayer();
        player.setX(475);
        player.setY(10);

        enemies = new ArrayList<Enemy>();
        chests = new ArrayList<Chest>();

        scoreString = "Score: 0";
        timeString = "Time: 0";
        healthString = "Health: 100";

        playFlag = true;

        backgroundMusic.stop();
        enemyMusic.stop();

        backgroundMusicFLag = true;
        enemyMusicFlag = false;

        spawnObjectsAndEnemies();
    }

}
