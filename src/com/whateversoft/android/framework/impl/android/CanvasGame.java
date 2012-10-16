package com.whateversoft.android.framework.impl.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.whateversoft.R;
import com.whateversoft.android.framework.Audio;
import com.whateversoft.android.framework.FileIO;
import com.whateversoft.android.framework.Game;
import com.whateversoft.android.framework.GamePreferences;
import com.whateversoft.android.framework.Graphics;
import com.whateversoft.android.framework.Input;
import com.whateversoft.android.framework.Screen;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.android.framework.ScreenInfo.VirtualResolution;
import com.whateversoft.android.framework.textui.Prompter;
import com.whateversoft.android.framework.textui.PrompterAndroid;
import com.whateversoft.colorshafted.constants.CSSettings;


/** Copyright 2011 Robert Concepcion III */
public abstract class CanvasGame extends Activity implements Game
{
	public final CanvasGame game = this;
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio  audio;
	Input  input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;
	Thread mainUIThread;
	ProgressDialog progressDialog;
	AndroidGameKeyboard gameKeyboard;
	AndroidGamePreferences preferences;
	protected PrompterAndroid prompter;
	
	public Handler handler;
	public boolean mainRunning;
	public boolean paused;
	
	//change the screen resolution during the game
	@Override
	public void setScreenRes(int w, int h)
	{
		boolean isLandscape = getResources().getConfiguration().orientation ==
				Configuration.ORIENTATION_LANDSCAPE;
		
		//actual width/height will be based on screen orientation to keep things constant!
		int virtualW = (isLandscape? w : h);
		int virtualH =(isLandscape? h : w);
	
		float scaleX = (float) virtualW / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) virtualH / getWindowManager().getDefaultDisplay().getHeight();
		
		Bitmap frameBuffer = Bitmap.createBitmap(virtualW,virtualH, Config.RGB_565);
		
		
		renderView.setFrameBuffer(frameBuffer);
		graphics.setBitmapBuffer(frameBuffer);
		input.setTouchScale(scaleX, scaleY);
	}
	
	// THE MAIN ACTIVITY RECEIVES INITIAL INPUT, RUN INITIALIZATION SEQUENCE
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//GET ALL INFORMATION REGARDING RUNTIME ENV & DEVICE FOR INPUT SETTINGS
		SystemInfo.initialize(this);
		
		//WINDOW INITIALIZATION SEQUENCE
		//disable window title, go into fullscreen, record whether we are in the desired landscape mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		boolean isLandscape = getResources().getConfiguration().orientation ==
			Configuration.ORIENTATION_LANDSCAPE;
		
		//-----------------------------------------------------//
		//DETERMINE THE ASPECT RATIO TO USE FOR THE PHONE
		//-----------------------------------------------------//
		//first, lets calculate the phone's aspect ratio(resolution is not important since it is scalable)
		double screenAspectRatio = 		(double)getWindowManager().getDefaultDisplay().getWidth() /
										(double)getWindowManager().getDefaultDisplay().getHeight();
		
		
		//next, we determine the nearest virtual resolution to use
		if(screenAspectRatio >= 1.535)
			ScreenInfo.virtualRes = VirtualResolution.R800X480;		//800 x 480
		else if(screenAspectRatio <= 1.416)
			ScreenInfo.virtualRes = VirtualResolution.R680X480;		//680 x 480
		else
			ScreenInfo.virtualRes = VirtualResolution.R720X480;		//720 x 480
		
		int screenWidth = 0, screenHeight = 0;
		switch(ScreenInfo.virtualRes)
		{
			case R800X480: 
				screenWidth  = 800;
				screenHeight = 480;
				break;
			case R720X480:
				screenWidth  = 720;
				screenHeight = 480;
				break;
			case R680X480:
				screenWidth  = 680;
				screenHeight = 480;
				break;
		}
		
		ScreenInfo.virtualWidth  = screenWidth;
		ScreenInfo.virtualHeight = screenHeight;
		
		//determine the virtual size of the game screen depending on the screen orientation
		int frameBufferWidth = isLandscape ? screenWidth : screenHeight;
		int frameBufferHeight = isLandscape ? screenHeight : screenWidth;
		
		//create a frame buffer to draw on
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);

		//initialize the important feature handlers to use in-game
		PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "GLGame");
		renderView = new AndroidFastRenderView(this, frameBuffer);
		audio = new AndroidAudio(this, this);
		preferences = new AndroidGamePreferences(this);
		graphics = new AndroidGraphics(getAssets(), frameBuffer, game.getPreferences().getPref(CSSettings.KEY_ENABLE_GFX_ANTIALIAS, CSSettings.DEFAULT_ENABLE_GFX_ANTIALIAS));
		
		//determine the scaling of the phone's pixels for translated virtual touch positions
		float scaleX = (float) frameBufferWidth / getWindowManager().getDefaultDisplay().getWidth();
		float scaleY = (float) frameBufferHeight / getWindowManager().getDefaultDisplay().getHeight();
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		
		gameKeyboard = new AndroidGameKeyboard(this, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_DPAD_DOWN, 
													 KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT, 
													 KeyEvent.KEYCODE_DPAD_CENTER,
													 KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_S, KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_D,
													 KeyEvent.KEYCODE_K, KeyEvent.KEYCODE_L, KeyEvent.KEYCODE_SPACE);
		
		fileIO = new AndroidFileIO(getAssets());
		
		//set default preferences only if they've never been loaded in the past!
		PreferenceManager.setDefaultValues(this, R.layout.settings, false);
		
		screen = getStartScreen();
		setContentView(renderView);

		// Create the Handler. It will implicitly bind to the Looper
	    // that is internally created for this thread (since it is the UI thread)
	    handler = new Handler();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();	
		if(getMusic() != null)
			getMusic().activityPauseCycle(false);
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		this.getCurrentFocus().requestFocus();
		
		if(getMusic() != null)
			getMusic().activityPauseCycle(true);
		
		if(((PrompterAndroid)getPrompter()).getDisplayedDialog() != null)
		{ ((PrompterAndroid)getPrompter()).getDisplayedDialog().hide(); }
			
		if(isFinishing())
		{
			screen.dispose();
			getMusic().releaseMusic();
		}
	}
	
	@Override
	public Input getInput()
	{
		return input;
	}
	
	public FileIO getFileIO()
	{
		return fileIO;
	}

	@Override
	public GamePreferences getPreferences()
	{
		return preferences;
	}
	
	@Override
	public Graphics getGraphics()
	{
		return graphics;
	}
	
	@Override
	public Audio getAudio()
	{
		return audio;
	}
	
	@Override
	public void setScreen(Screen screen)
	{
		if(screen == null)
			throw new IllegalArgumentException("Screen must not be null");
		
		//free asset resources
		if(this.screen.assets != null)
		{
			this.screen.assets.freeAssets();
			this.screen.assets = null;
		}
		
		//clear the current screen
			this.screen.pause();
			this.screen.dispose();
			this.screen = null;
		
		screen.resume();
		screen.update(0);	//update the new screen w no delay before we set the current screen
		this.screen = screen;
	}
	
	@Override
	public AndroidFastRenderView getRenderView()
	{
		return renderView;
	}
	
	@Override
	public Screen getCurrentScreen()
	{
		return screen;
	}
	
	/* check whether the game sequence is running or not */
	@Override
	public boolean isMainRunning()
	{
		return mainRunning;
	}
	/* check whether the game is paused or not */
	public boolean isPaused()
	{
		return paused;
	}
	
	@Override
	public void launchWebsite(String url)
	{
		Intent i = new Intent(Intent.ACTION_VIEW);  
		i.setData(Uri.parse(url));  
		this.startActivityFromChild(this, i, 0);
	}
	
	@Override
	public void startLoadingDialog(final String msg, final String title, final boolean cancelable)
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if(!cancelable)
					progressDialog= new ProgressDialog(game);
				else
					progressDialog = new ProgressDialog(game)
				{
					@Override
					public void onBackPressed()
					{
						getCurrentScreen().onLoadingDialogCanceled();
					}
				};
				progressDialog.setIndeterminate(true);
				if(!cancelable)
					progressDialog.setCancelable(false);
				else
					progressDialog.setCancelable(true);
				progressDialog.setTitle(title);
				progressDialog.setMessage(msg);
				progressDialog.show();
			}
			
		});
	}
	
	@Override
	public void stopLoadingDialog()
	{
		if(progressDialog != null)
			progressDialog.dismiss();
	}
	
	@Override
	public Prompter getPrompter()
	{
		return prompter;
	}
	
	@Override
	public AndroidGameKeyboard getGameKeyboard()
	{
		return gameKeyboard;
	}
	
	@Override
	public void pauseGame()
	{
		getRenderView().pauseGameWhileRunning();
	}
	
	@Override
	public void resumeGame()
	{
		getRenderView().resumeGameWhileRunning();
	}
}	
