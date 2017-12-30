package com.turtlesoupgames.nomi.android;

import android.widget.Toast;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;
import com.turtlesoupgames.nomi.NomiApplication;

public class AndroidLauncher extends AndroidLiveWallpaperService
{
	@Override
	public void onCreateApplication()
	{
		super.onCreateApplication();
		  
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
		config.useWakelock = false;
		config.useAccelerometer = false;
		config.getTouchEventsForLiveWallpaper = true;
		  
		ApplicationListener listener = new NomiApplication();
		initialize(listener, config);
		  
		//display info toast
		Toast toast = Toast.makeText(getApplicationContext(), "Press and hold to clear", Toast.LENGTH_SHORT);
		toast.show();
	}
}

class NomiApplicationWallpaperListener implements AndroidWallpaperListener
{
	@Override
	public void offsetChange(float xOffset, float yOffset, float xOffsetStep,
			float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void previewStateChange(boolean isPreview) {
		// TODO Auto-generated method stub
		
	}
}