package com.turtlesoupgames.nomi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RakePatternData
{	
	Point startPoint;
	Point endPoint;
	Sprite rateSprite;

	public RakePatternData(Point startPoint, Point endPoint, Texture rakePatternTexture)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		rateSprite = new Sprite(rakePatternTexture);
		
		//set position of rake sprite mid way between the start and end point
		rateSprite.setPosition(startPoint.x + (endPoint.x - startPoint.x), endPoint.y + (endPoint.y - startPoint.y));
		
		//set origin to sprite centre
		rateSprite.setOriginCenter();
		
		//we want to scale the pattern such that it can fit 20 times along the minimum screen side
		float magnitude = (float) Point.distance(startPoint, endPoint);
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float minSize = Math.min(screenWidth, screenHeight);
		float rakeSize = (float)Math.ceil(minSize / 10);
		rateSprite.setSize(rakeSize, (float)(magnitude*1.25)); //multiplied by 1.25 to create slight overlap between connecting segments
		
		//get the rotation angle of the rake pattern based on the direction of motion
		float angle = (float)(Math.atan2(endPoint.y - startPoint.y, endPoint.x - startPoint.x) * (180/Math.PI));
		rateSprite.setRotation(angle + 90);
	}
	
	public void draw(SpriteBatch batch)
	{
		rateSprite.draw(batch);
	}
}
