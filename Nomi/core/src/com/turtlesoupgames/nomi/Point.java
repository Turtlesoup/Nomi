package com.turtlesoupgames.nomi;

public class Point
{
	public float x;
	public float y;
	
	public Point()
	{
		this.x = 0;
		this.y = 0;
	}
	
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public static double distance(Point p1, Point p2)
	{
		return Math.sqrt( Math.pow((p2.x - p1.x) , 2) + Math.pow((p2.y - p1.y) , 2));
	}
}
