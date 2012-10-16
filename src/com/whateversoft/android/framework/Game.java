package com.whateversoft.android.framework;

import android.view.SurfaceView;

import com.whateversoft.android.framework.impl.android.AndroidFastRenderView;
import com.whateversoft.android.framework.impl.android.AndroidGameKeyboard;
import com.whateversoft.android.framework.textui.Prompter;

/** Copyright 2011-2012 Robert Concepcion III */
public interface Game
{
	public Input getInput();
	public GamePreferences getPreferences();
	public Graphics getGraphics();
	public Audio getAudio();
	public FileIO getFileIO();
	public void setScreen(Screen screen);
	public Screen getCurrentScreen();
	public Screen getStartScreen();
	public SurfaceView getRenderView();
	public MusicJukebox getMusic();
	public boolean isMainRunning();
	public boolean isPaused();
	public void launchWebsite(String url);
	public void startLoadingDialog(String msg, String title, boolean cancellable);
	public void stopLoadingDialog();
	public Prompter getPrompter();
	/** callback method to the game to handle changes in options */
	public void onSettingChanged(String key);
	public void setScreenRes(int w, int h);
	public void launchSettings();
	public void pauseGame();
	public void resumeGame();
	AndroidGameKeyboard getGameKeyboard();
}
