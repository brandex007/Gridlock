package com.innovativeexecutables;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import static com.innovativeexecutables.Player.*;

public class Gridlock extends ApplicationAdapter {
	// map is 32 by 32 tiles with 32 pixel tiles

	public static int VIEWPORT_WIDTH = 1024;
	public static int VIEWPORT_HEIGHT = 1024;

	private SpriteBatch sb;
	public static OrthographicCamera cam;
	private Player player;
	private float delta;
	private float cam_Ystart;

	// TiledMap
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer tileMapRenderer;

	// Collision things
	private TiledMapTileLayer collisionLayer;
	public static float SCROLLTRACKER_X;
	public static float SCROLLTRACKER_Y;

	@Override
	public void create () {
		cam = new OrthographicCamera();
		cam.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		sb = new SpriteBatch();
		sb.setProjectionMatrix(cam.combined);
		cam_Ystart = cam.position.y;

		tileMap = new TmxMapLoader().load("tiledmap1.tmx");
		tileMapRenderer = new OrthogonalTiledMapRenderer(tileMap);

		collisionLayer = (TiledMapTileLayer) tileMap.getLayers().get("Tile Layer 2");
		player = new Player(collisionLayer);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		delta = Gdx.graphics.getDeltaTime();

		// updates
		player.update(delta);

		// rendering
		tileMapRenderer.setView(cam);
		tileMapRenderer.render();
		sb.begin();
		player.render(sb);
		sb.end();
		sb.begin();
		player.render(sb);
		sb.end();
	}
	
	@Override
	public void dispose () {

	}

}
