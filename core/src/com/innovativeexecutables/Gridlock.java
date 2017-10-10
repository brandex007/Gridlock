package com.innovativeexecutables;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

	// TiledMap
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tileMapRenderer;

	// Collision
	private TiledMapTileLayer collisionLayer;

	// Tile
	Tile[][] tileList = new Tile[32][32];
	List<Enemy> enemies;

	@Override
	public void create () {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);


		tileMap = new TmxMapLoader().load("tiledmap1.tmx");
		tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

		sb = tileMapRenderer.getBatch();
		sb.setProjectionMatrix(cam.combined);

		collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Tile Layer 2");
		player = new Player(collisionLayer);

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

		// spawn enemy at tile 5,5
		enemies.add(new Enemy(tileList[20][20].getX(), tileList[20][20].getY()));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		delta = Gdx.graphics.getRawDeltaTime();

		// updates
		player.update(delta);
		cam.update();

		// rendering
		tileMapRenderer.setView(cam);
		tileMapRenderer.render();

		sb = tileMapRenderer.getBatch();

		sb.begin();
		player.render(sb);

		for(Enemy enemy : enemies){
			enemy.render(sb);
		}

		sb.end();
	}
	
	@Override
	public void dispose () {

	}

}
