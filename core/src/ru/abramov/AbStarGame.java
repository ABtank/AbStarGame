package ru.abramov;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AbStarGame extends ApplicationAdapter {
	SpriteBatch batch;//выгрузка текстур
	Texture img;
	TextureRegion region;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		region = new TextureRegion(img,10,10,150,150);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();  //передача текстур
		batch.setColor(0.3f,0.8f,0.5f,0.5f);
		batch.draw(img, 0, 0); // пора отрисовывать текстуру img
		batch.setColor(0.9f,0.3f,0.7f,1f);
		batch.draw(region,200,200);
		batch.end(); // выгрузка окончена
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
