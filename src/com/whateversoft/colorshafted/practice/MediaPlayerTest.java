package com.whateversoft.colorshafted.practice;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.TextView;

public class MediaPlayerTest extends Activity
{
	MediaPlayer mediaPlayer;
	PowerManager powerManager;
	WakeLock wakeLock;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Media Player");
		wakeLock.acquire();
		
		TextView textView = new TextView(this);
		setContentView(textView);
		//set the volume before we start to mess with the media player
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mediaPlayer = new MediaPlayer();	//instantiate the media player into memory
		try
		{
			AssetManager assetManager = getAssets();	//asset manager used to store our music file
			AssetFileDescriptor descriptor = assetManager.openFd("music.mp3");	//load a file descriptor from a song using the asset manager
			mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());	//load the file descriptor into the media player w the required parameters
			mediaPlayer.prepare();	//prepare to play
			mediaPlayer.setLooping(true);	//set looping to true
		}
		catch(IOException e)
		{
			textView.setText("Couldn't load music file, " + e.getMessage());
			mediaPlayer = null;
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		wakeLock.acquire();
		if(mediaPlayer != null)
		{
			mediaPlayer.start();	//play the file after the screen has been created
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		wakeLock.release();
		if(mediaPlayer != null)
		{
			mediaPlayer.pause();		//pause it
			if(isFinishing())
			{
				mediaPlayer.stop();		//if exiting free the memory
				mediaPlayer.release();
			}
		}
	}
}
