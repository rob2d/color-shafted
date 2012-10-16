package com.whateversoft.android.framework.impl.android;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.Sound;

/** Copyright 2011 Robert Concepcion III */
public class AndroidAudio implements Audio
{
	AssetManager assets;
	SoundPool soundPool;
	Game game;
	
	public AndroidAudio(Game g, Activity activity)
	{
		game = g;
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		assets = activity.getAssets();
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}
	
	@Override
	public AssetFileDescriptor newMusic(String fileName)
	{
		try
		{
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			return assetDescriptor;
		}
		catch(IOException e)
		{
			throw new RuntimeException("Couldn't load music " + fileName + "'");
		}
	}
	
	@Override
	public Sound newSound(String fileName)
	{
		try
		{
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		}
		catch(IOException e)
		{
			throw new RuntimeException("Couldn't load sound " + fileName + "'");
		}
	}
}
