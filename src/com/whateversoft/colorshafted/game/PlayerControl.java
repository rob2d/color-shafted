package com.whateversoft.colorshafted.game;

import android.graphics.Point;
import android.view.KeyEvent;

import com.whateversoft.android.framework.Input;
import com.whateversoft.android.framework.ScreenInfo;
import com.whateversoft.android.framework.impl.android.CanvasGame;
import com.whateversoft.android.framework.impl.android.SystemInfo;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.screens.GameScr;
import com.whateversoft.math.MotionMath;

/** the player control class is used to detect gestures made with the touch screen that represent the player
 *  giving input to the game during a game sequence 
 *  Copyright 2011 Robert Concepcion III*/
public class PlayerControl
{
	public final static int SWIPE_UP     		= 0,
							SWIPE_UP_RIGHT		= 1,
							SWIPE_RIGHT  		= 2,
							SWIPE_RIGHT_DOWN  	= 3,
							SWIPE_DOWN   		= 4,
							SWIPE_DOWN_LEFT 	= 5,
							SWIPE_LEFT   		= 6,
							SWIPE_LEFT_UP   	= 7,
							ROTATE_LEFT  		= 8,
							ROTATE_RIGHT 		= 9,
							BOMB_SCREEN			= 10;
	
	public final int		FINGER1			= 0,
							FINGER2			= 1;
	
	public final int		LR_SHAKE		= 1,
							RL_SHAKE		= 2;
	
	/** how far along the axis being checked for shake gestures a user has to tilt */
	public final float		shakeDelta;
	
	//for the accelerometer
	public double[] accelQueue = new double[10];
	public int accelShakeCount = 0;
	public int accelShakeDir   = 0;
	public int accelShakeTimer = 0;
	private boolean playerInputEnabled = true;
	
	public CanvasGame			game;
	public GameScr gameScreen;
	public Input 	  	input;
	
	//VALUES FOR TRACKING POINTS TOUCHED
	/** the number of touch events saved in touchPoints */
	public final int POINTER_1_OFFSET = 15;
	/** points where the user has touched the screen(index 0-11 are first finger and index 12-23 are second) */
	Point[] touchPoints = new Point[30];
	
	public PlayerControl(GameScr scr)
	{
		gameScreen = scr;
		game	   = (CanvasGame)scr.game;
		input = scr.game.getInput();
		
		for(int i = 0; i < 30; i++)
			touchPoints[i] = new Point();
		
		shakeDelta = 2.5F - (game.getPreferences().getPref(CSSettings.KEY_SHAKE_SENSITIVITY, CSSettings.DEFAULT_SHAKE_SENSITIVITY)/50.0F);
	}
	
	/** detect gestures made by the player which should trigger the game to react to player actions */
	public void detectPlayerInputEvents()
	{
		int[] touchTimer = gameScreen.touchTimer;
		int[] touchLength = gameScreen.touchLength;
		
		//-------------------------------------------------------------------------------------//
		//   DETECT IF USED HAS PRESSEd A DPAD KEY AND SEND PLAYER MOVE EVENT				   //
		//-------------------------------------------------------------------------------------//
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_UP) == 1 || game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_W) == 1)
			gameScreen.playerControlEvent(SWIPE_UP);
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_DOWN) == 1 || game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_S) == 1)
			gameScreen.playerControlEvent(SWIPE_DOWN);
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_LEFT) == 1 || game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_A) == 1)
			gameScreen.playerControlEvent(SWIPE_LEFT);
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_RIGHT) == 1 || game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_D) == 1)
			gameScreen.playerControlEvent(SWIPE_RIGHT);
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_DPAD_CENTER)== 1 || game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_K) == 1)
			gameScreen.playerControlEvent(ROTATE_LEFT);
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_L) == 1)
			gameScreen.playerControlEvent(ROTATE_RIGHT);
		if(game.getGameKeyboard().getKeyStatus(KeyEvent.KEYCODE_SPACE) == 1)
			gameScreen.playerControlEvent(BOMB_SCREEN);

		//-------------------------------------------------------------------------------------//
		//   DETECT WHEN SCREEN HAS BEEN TOUCHED AND/OR GESTURED BY FIRST FINGER(POINTER 0)	   //
		//-------------------------------------------------------------------------------------//
		if(touchTimer[FINGER1] > 0 && touchTimer[FINGER1] <= 12)//store the touch points of second finger
		{
			touchPoints[touchTimer[FINGER1] - 1].x = game.getInput().getTouchX(0); 
			touchPoints[touchTimer[FINGER1] - 1].y = game.getInput().getTouchY(0);
		}
		if(touchTimer[FINGER1] == 0 && touchLength[FINGER1] >= 1 && touchLength[FINGER1] <= 10)
		{
			

			double angleSwiped = MotionMath.coordsToAngle((touchPoints[touchLength[FINGER1] - 1].x - touchPoints[0].x),
						  		(touchPoints[touchLength[FINGER1] - 1].y - touchPoints[0].y));
			
			//IF WE PRESS THE BOMB BUTTON :b
			if(game.getPreferences().getPref(CSSettings.KEY_BOMB_BUTTON, CSSettings.DEFAULT_BOMB_BUTTON) && 
					touchPoints[0].x > ScreenInfo.virtualWidth - 132 && touchPoints[0].y < 132)
			{
				gameScreen.playerControlEvent(BOMB_SCREEN);
			}
			//detect an upward swipe gesture
			else if((angleSwiped < 112.5 && angleSwiped >= 67.5) && touchPoints[touchLength[FINGER1] - 1].y < touchPoints[0].y - 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_UP);
			//detect an upward right swipe gesture
			else if((angleSwiped < 67.5 && angleSwiped >= 22.5) && touchPoints[touchLength[FINGER1] - 1].y < touchPoints[0].y - 8 && 
															 touchPoints[touchLength[FINGER1] - 1].x > touchPoints[0].x + 8)
			{
				gameScreen.playerControlEvent(PlayerControl.SWIPE_UP_RIGHT);
			}
			//detect a right swipe gesture
			else if((angleSwiped < 22.5 || angleSwiped >= 337.5) && touchPoints[touchLength[FINGER1] - 1].x > touchPoints[0].x + 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_RIGHT);
			//detect a right downward swipe gesture
			else if((angleSwiped < 337.5 || angleSwiped >= 292.5) && touchPoints[touchLength[FINGER1] - 1].x > touchPoints[0].x + 8 &&
																   touchPoints[touchLength[FINGER1] - 1].y > touchPoints[0].y + 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_RIGHT_DOWN);
			//detect a downward swipe gesture
			else if((angleSwiped < 292.5 && angleSwiped >= 247.5) && touchPoints[touchLength[FINGER1] - 1].y > touchPoints[0].y + 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_DOWN);
			
			//detect a downwward left swipe gesture
			else if((angleSwiped < 247.5 && angleSwiped >= 202.5) && touchPoints[touchLength[FINGER1] - 1].y > touchPoints[0].y + 8 &&
																	touchPoints[touchLength[FINGER1] - 1].x < touchPoints[0].x - 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_DOWN_LEFT);
			//detect a left swipe gesture
			else if(angleSwiped < 202.5 && angleSwiped >= 157.5 && touchPoints[touchLength[FINGER1] - 1].x < touchPoints[0].x - 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_LEFT);
			//detect a left upward swipe gesture
			else if(angleSwiped >= 112.5 && angleSwiped < 157.5 && touchPoints[touchLength[FINGER1] - 1].x < touchPoints[0].x - 8 &&
																   touchPoints[touchLength[FINGER1] - 1].y < touchPoints[0].y - 8 )
				gameScreen.playerControlEvent(PlayerControl.SWIPE_LEFT_UP);
			else
			{
				if(!SystemInfo.isGoogleTV())
				{
					if(touchPoints[touchLength[FINGER1] - 1].x < game.getGraphics().getWidth() / 2)
						gameScreen.playerControlEvent(PlayerControl.ROTATE_LEFT);
					else
						gameScreen.playerControlEvent(PlayerControl.ROTATE_RIGHT);
				}
				else
						gameScreen.playerControlEvent(PlayerControl.ROTATE_RIGHT);
			}
			}
		//-------------------------------------------------------------------------------------//
		//   DETECT WHEN SCREEN HAS BEEN TAPPED BY A SECOND FINGER TO ROTATE				   //
		//-------------------------------------------------------------------------------------//
		if(touchTimer[FINGER2] > 0 && touchTimer[FINGER2] <= 12)	//store the touch points of second finger
		{
			touchPoints[touchTimer[FINGER2] - 1 + POINTER_1_OFFSET].x = game.getInput().getTouchX(1);
			touchPoints[touchTimer[FINGER2] - 1 + POINTER_1_OFFSET].y = game.getInput().getTouchY(1);
		}
		if(touchTimer[FINGER2] == 0 && touchLength[FINGER2] >= 1 && touchLength[FINGER2] <= 10)
		{
			{	//rotate the control block depending on the first pointer is
				if(touchPoints[touchLength[FINGER2] - 1 + POINTER_1_OFFSET].x < game.getGraphics().getWidth() / 2)
					gameScreen.playerControlEvent(PlayerControl.ROTATE_LEFT);
				else
					gameScreen.playerControlEvent(PlayerControl.ROTATE_RIGHT);
			}
		}
		
		if(touchTimer[FINGER1] == 0 && touchLength[FINGER1] > 10 && touchLength[FINGER1] < 25)
		{
			//DETECT ANGLE SWIPES
			double angleSwiped = MotionMath.coordsToAngle((touchPoints[touchLength[FINGER1] - 1].x - touchPoints[0].x),
														  (touchPoints[touchLength[FINGER1] - 1].y - touchPoints[0].y));
			
			//detect an upward swipe gesture
			if(touchLength[FINGER1] <= 15 && (angleSwiped < 112.5 && angleSwiped >= 67.5) && touchPoints[touchLength[FINGER1] - 1].y < touchPoints[0].y - 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_UP);
			//detect an upward right swipe gesture
			else if(touchLength[FINGER1] <= 15 && (angleSwiped < 67.5 && angleSwiped >= 22.5) && touchPoints[touchLength[FINGER1] - 1].y < touchPoints[0].y - 8 && 
															 touchPoints[touchLength[FINGER1] - 1].x > touchPoints[0].x + 8)
			{
				gameScreen.playerControlEvent(PlayerControl.SWIPE_UP_RIGHT);
			}
			//detect a right swipe gesture
			else if(touchLength[FINGER1] <= 15 && (angleSwiped < 22.5 || angleSwiped >= 337.5) && touchPoints[touchLength[FINGER1] - 1].x > touchPoints[0].x + 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_RIGHT);
			//detect a right downward swipe gesture
			else if(touchLength[FINGER1] <= 15 && (angleSwiped < 337.5 || angleSwiped >= 292.5) && touchPoints[touchLength[FINGER1] - 1].x > touchPoints[0].x + 8 &&
																   touchPoints[touchLength[FINGER1] - 1].y > touchPoints[0].y + 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_RIGHT_DOWN);
			//detect a downward swipe gesture
			else if(touchLength[FINGER1] <= 15 && (angleSwiped < 292.5 && angleSwiped >= 247.5) && touchPoints[touchLength[FINGER1] - 1].y > touchPoints[0].y + 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_DOWN);
			
			//detect a downwward left swipe gesture
			else if(touchLength[FINGER1] <= 15 && (angleSwiped < 247.5 && angleSwiped >= 202.5) && touchPoints[touchLength[FINGER1] - 1].y > touchPoints[0].y + 8 &&
																	touchPoints[touchLength[FINGER1] - 1].x < touchPoints[0].x - 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_DOWN_LEFT);
			//detect a left swipe gesture
			else if(touchLength[FINGER1] <= 15 && angleSwiped < 202.5 && angleSwiped >= 157.5 && touchPoints[touchLength[FINGER1] - 1].x < touchPoints[0].x - 8)
				gameScreen.playerControlEvent(PlayerControl.SWIPE_LEFT);
			//detect a left upward swipe gesture
			else if(touchLength[FINGER1] <= 15 && angleSwiped >= 112.5 && angleSwiped < 157.5 && touchPoints[touchLength[FINGER1] - 1].x < touchPoints[0].x - 8 &&
																   touchPoints[touchLength[FINGER1] - 1].y < touchPoints[0].y - 8 )
				gameScreen.playerControlEvent(PlayerControl.SWIPE_LEFT_UP);
			else 
			{
				if(game.getPreferences().getPref(CSSettings.KEY_BOMB_LONGTAP, CSSettings.DEFAULT_BOMB_LONGTAP))
					gameScreen.playerControlEvent(BOMB_SCREEN);
			}
		}
	}
	
	/** detect a basic screen shake gesture */
	public void detectScreenShake()
	{
		if(game.getPreferences().getPref(CSSettings.KEY_BOMB_SHAKE_GESTURE, 	//for verification of setting
										 CSSettings.DEFAULT_BOMB_SHAKE_GESTURE))//(some glitch overlooked..)
		{
			
			if(accelShakeTimer > 0)
				accelShakeTimer -= 1;
			
			for(int i = 1; i < 3; i++)
				accelQueue[i] = accelQueue[i - 1];
			
			if(game.getPreferences().getPref(CSSettings.KEY_BOMB_SHAKE_ON_X, true))
				accelQueue[0] = game.getInput().getAccelX();
			else
				accelQueue[0] = game.getInput().getAccelY();
			
			if(accelShakeCount == 0 && 
					accelQueue[0] <= accelQueue[1] - shakeDelta
					&& accelShakeTimer == 0)
			{
				accelShakeDir	= LR_SHAKE;
				accelShakeCount = 1;
				accelShakeTimer = 10;
			}
			
			if(accelShakeCount == 1 && accelQueue[0] >= accelQueue[1] + shakeDelta && 
			   accelShakeTimer > 0 && accelShakeDir == LR_SHAKE)
			{
				accelShakeDir	= 0;
				accelShakeCount = 2;
				accelShakeTimer = 50;
				gameScreen.playerControlEvent(BOMB_SCREEN);
			}
			
			//if we are in any engaged state and the shake timer goes down to 0, we reset the direction and state
			if(accelShakeCount > 0 && accelShakeTimer == 0)
			{
				accelShakeCount = 0;
				accelShakeDir	= 0;
			}
			
			if(accelShakeCount == 0 && accelQueue[0] >= accelQueue[1] + 1.20 && accelShakeTimer == 0)
			{
				accelShakeDir	= RL_SHAKE;
				accelShakeCount = 1;
				accelShakeTimer = 10;
			}
			
			if(accelShakeCount == 1 && accelQueue[0] <= accelQueue[1] - 1.20 && 
					   accelShakeTimer > 0 && accelShakeDir == RL_SHAKE)
					{
						accelShakeDir	= 0;
						accelShakeCount = 2;
						accelShakeTimer = 50;
						gameScreen.playerControlEvent(BOMB_SCREEN);
					}
		}
	}
	
	/** set whether an actual physical person can give input controls */
	public void playerInputEnabled(boolean enableState)
	{
		playerInputEnabled = enableState;
	}
	
	/** detect whether the player can interact with the game */
	public boolean isPlayerInputEnabled()
	{
		return playerInputEnabled;
	}
}
