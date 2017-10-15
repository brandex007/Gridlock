package com.innovativeexecutables;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
<<<<<<< HEAD
<<<<<<< HEAD
import com.badlogic.gdx.graphics.Color;
=======
import com.badlogic.gdx.Input;
>>>>>>> 00aa74e1e37f41dc1d4b700742b3d1669b30b867
=======
>>>>>>> parent of 610febb... Added collision with hazards and damage, text for score, etc.
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
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
	private int mouseClickX,mouseClickY;
	private Texture playButton;
	private boolean playFlag;
	// TiledMap
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tileMapRenderer;

	// Collision
	private TiledMapTileLayer obstaclesCollisionLayer, hazardsCollisionLayer;

	// Tile
	Tile[][] tileList = new Tile[32][32];
	List<Enemy> enemies;




	@Override
	public void create () {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		//creates play button object
		 playButton=new Texture("play.png");

		tileMap = new TmxMapLoader().load("tiledmap1.tmx");
		tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

		sb = tileMapRenderer.getBatch();
		sb.setProjectionMatrix(cam.combined);

		obstaclesCollisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Obstacles");
		hazardsCollisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Hazards");
		player = new Player(obstaclesCollisionLayer, hazardsCollisionLayer);





		enemies = new ArrayList<Enemy>();

		// tiles
		for(int i = 0; i<Tile.tileSize; i++){
			for(int j = 0; j<Tile.tileSize; j++){
				tileList[i][j] = new Tile(i,j);
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
	}

	@Override
	public void render () {
		/*if left mouse button is clicked game will begin
		 * when mouse clicked location of click is saved and
		  * click location is checked to see if the play button was clicked*/

<<<<<<< HEAD
		delta = Gdx.graphics.getRawDeltaTime();
		//creates play button object
		Texture playButton=new Texture("play.png");
		// updates
		player.update(delta);
		checkPlayerCollisionMap();

		checkForCharCollisions();
		cam.update();
=======

		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT))
		{
>>>>>>> 00aa74e1e37f41dc1d4b700742b3d1669b30b867

			mouseClickX=Gdx.input.getX();
			mouseClickY=Gdx.input.getY();

<<<<<<< HEAD

<<<<<<< HEAD
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

=======
>>>>>>> parent of 610febb... Added collision with hazards and damage, text for score, etc.
		sb.begin();
		player.render(sb);
		//draws play button object
		sb.draw(playButton,100,976);

		for(Enemy enemy : enemies){
			enemy.render(sb);
		}

		sb.end();
<<<<<<< HEAD
=======
			if ((mouseClickX<=702&&mouseClickX>=383)&&(mouseClickY<=501&&mouseClickY>=406))
			{
				playFlag=true;
			}

		}



		// provides updates if play has been pressed
		if(playFlag){
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			delta = Gdx.graphics.getRawDeltaTime();

			// updates
			player.update(delta);
			checkPlayerCollisionMap();

			checkForCharCollisions();
			cam.update();

		}

			// rendering
			tileMapRenderer.setView(cam);
			tileMapRenderer.render();

			sb = tileMapRenderer.getBatch();

			sb.begin();
			player.render(sb);
			// draws play button at start or when game is stopped
			if(playFlag==false) {
				//draws play button object
				sb.draw(playButton, 384, 512);
			}
			for (Enemy enemy : enemies) {
				enemy.render(sb);
			}

			sb.end();
>>>>>>> 00aa74e1e37f41dc1d4b700742b3d1669b30b867

=======
>>>>>>> parent of 610febb... Added collision with hazards and damage, text for score, etc.
	}






	
	@Override
	public void dispose () {

	}

	public void checkForCharCollisions(){
		for(Enemy enemy : enemies) {
			if (player.getX() > enemy.getX() - 150 && player.getX() < enemy.getX() + 150){
				if (player.getY() > enemy.getY() - 150 && player.getY() < enemy.getY() + 150){
					System.out.print("Met boss");
				}
			}
		}

	}

	public void checkPlayerCollisionMap(){

		boolean collisionWithObstacles = false;
		boolean collisionWithHazards = false;

		collisionWithObstacles = isCellBLocked(player.getX() + player.getWidth(),player.getY()) || isCellBLocked(player.getX() + player.getWidth()/ 2, player.getY())|| isCellBLocked(player.getX(), player.getY());

		// React to Collision
		if (collisionWithObstacles) {
			if(UP_TOUCHED){
				player.setY(player.getY() - 1);
			}
			if(DOWN_TOUCHED){
				player.setY(player.getY() + 1);
			}
			if(LEFT_TOUCHED){
				player.setX(player.getX() + 1);
			}
			if(RIGHT_TOUCHED){
				player.setX(player.getX() - 1);
			}
		}

		TiledMapTileLayer.Cell cell = hazardsCollisionLayer.getCell(
				(int) (player.getX() / hazardsCollisionLayer.getTileWidth()),
				(int) (player.getY() / hazardsCollisionLayer.getTileHeight()));

		if(collisionWithHazards){
			if(cell.getTile().getProperties().containsKey("fire")){

			}
			if(cell.getTile().getProperties().containsKey("lava")){

			}
		}
	}

	public boolean isCellBLocked(float x, float y) {
		TiledMapTileLayer.Cell cell = obstaclesCollisionLayer.getCell(
				(int) (x / obstaclesCollisionLayer.getTileWidth()),
				(int) (y / obstaclesCollisionLayer.getTileHeight()));

		return cell != null && cell.getTile() != null
				&& cell.getTile().getProperties().containsKey("collision");
	}

}
