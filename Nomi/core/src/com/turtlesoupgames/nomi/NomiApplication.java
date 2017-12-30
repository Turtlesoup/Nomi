package com.turtlesoupgames.nomi;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class NomiApplication extends Game implements InputProcessor
{
	float rakeDistanceThreshold;
	
	float screenWidth;
	float screenHeight;
	
	SpriteBatch sceneBatch;
	SpriteBatch rakeBatch;
	FrameBuffer fbo;
	TextureRegion fboRegion;
	
	Texture backgroundTexture;
	Texture rakePatternTexture;
	
	Sprite background;
	
	ArrayList<RakePatternData> rakePatternDatas;
	
	final float DRAG_DISTANCE_THRESHOLD = 3;
	final float CLEAR_HOLD_TIME_IN_SECONDS = 0.5f;

	Point lastTouchposition = new Point();
	boolean touching = false;
	boolean moved = false;
	float currentHoldTime = 0;
	
	@Override
	public void create()
	{
		//get the screen width and height
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		//create the scene and rake batches
		sceneBatch = new SpriteBatch();
		rakeBatch = new SpriteBatch();

		//create background sprite
		backgroundTexture = new Texture("background.png");
		background = new Sprite(backgroundTexture);
		background.setPosition((float)(-screenWidth*0.5), 0);
		background.setOriginCenter();
		float maxSize = Math.max(screenWidth, screenHeight);
		background.setSize(maxSize, maxSize);
		
		//create the rake texture
		rakePatternTexture = new Texture("rakePattern.png");
		rakePatternDatas = new ArrayList<RakePatternData>();
		
		//allow input processing
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render()
	{
		draw();
		update();
	}
	
	private void draw()
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//create the fbo and fbo texture region
		fbo = new FrameBuffer(Format.RGBA8888, (int)screenWidth, (int)screenHeight, false);
		fboRegion = new TextureRegion(fbo.getColorBufferTexture());
		fboRegion.flip(false, true);
		
		//start the fbo
		fbo.begin();
		
		//draw the rake pattern to the fbo
		drawRakePattern();
		
		//end the fbo
		fbo.end();

		//start the scene batch rendering
		sceneBatch.begin();  
		
		//draw the background
		background.draw(sceneBatch);
		
		//draw the rake fbo texture
		sceneBatch.draw(fboRegion, 0, 0, screenWidth, screenHeight); 
		
		//end the scene batch rendering
		sceneBatch.end();
		
		//dispose the fbo
		fbo.dispose();
	}
	
	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		//update the clear hold time and clear the rake patterns if held for the threshold time
		if(touching && !moved)
		{
			currentHoldTime += deltaTime;
			if(currentHoldTime > CLEAR_HOLD_TIME_IN_SECONDS)
			{
				rakePatternDatas.clear();
			}
		}
	}
	
	private void drawRakePattern()
	{
		//setup the rake batch to overlay the current alpha of the most recently added sprite to the batch,
		//rather than blending. allowing for a "raked over" effect when the rake patterns overlap.
		rakeBatch.enableBlending();
		rakeBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
		
		//start the rake batch rendering
		rakeBatch.begin();
		
		//draw the rake patterns
		int rakePatternDatasLength = rakePatternDatas.size();
		for(int rakePatternDatasIndex = 0; rakePatternDatasIndex < rakePatternDatasLength; ++ rakePatternDatasIndex)
		{
			rakePatternDatas.get(rakePatternDatasIndex).draw(rakeBatch);
		}
		
		//end the rake batch rendering
		rakeBatch.end();
	}
	
	private void createRakePattern(Point startPoint, Point endPoint)
	{
		rakePatternDatas.add(new RakePatternData(startPoint, endPoint, rakePatternTexture));
	}

	@Override
	public boolean keyDown(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		//only handle first touch event
		if(pointer == 0)
		{
			touching = true;
			moved = false;
			currentHoldTime = 0;
			lastTouchposition.set(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		//only handle first touch event
		if(pointer == 0)
		{
			touching = false;
			moved = false;
			currentHoldTime = 0;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		//only handle first touch event
		if(pointer == 0)
		{
			if(Point.distance(lastTouchposition, new Point(screenX, screenY)) > DRAG_DISTANCE_THRESHOLD)
			{
				moved = true;
				createRakePattern(new Point(lastTouchposition.x, (int) (screenHeight - lastTouchposition.y)), new Point(screenX, (int) (screenHeight - screenY)));
				lastTouchposition.set(screenX, screenY);
			}
		}
		
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}
