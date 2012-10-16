package com.whateversoft.colorshafted.game;

import com.whateversoft.android.framework.impl.android.SystemInfo;
import com.whateversoft.colorshafted.ColorShafted;
import com.whateversoft.colorshafted.ColorShaftedPrompter;
import com.whateversoft.colorshafted.constants.CSSettings;
import com.whateversoft.colorshafted.screens.GameScr;
import com.whateversoft.colorshafted.screens.GameScrAssets;

public class TutorialSequencer
{
	/** constants wich represent the state of each step of the game. They are as follows:<br><br>
	 *  <b>S_WELCOME</b> - Player is welcomed to the game<br>
	 *  <b>S_MATCHCOLOR_DEMO</b> - An example is demonstrated of an incoming block and a player destroying it<br>
	 *  <b>S_MOVE_DEMO</b> - An example is demonstrated of moving the control block along the grid positions
	 *  					(in 8 directions on touchscreen directions and 4 on non-touchscreen)<br>
	 *  <b>S_MOVE_PLAY_PROMPT</b> - Player is prompted to move in either all 8 or all 4 directions</b><br>
	 *  <b>S_MOVE_PLAY_REPROMPT</b> - Player is reprompted if he fails to move in all directions</b><br>
	 *  <b>S_MOVE_PLAY</b> - Player is tasked with moving around in 8 directions
	 *  <b>S_ROTATE_DEMO</b> - An example is demonstrated of a player rotating left and right<br>
	 *  <b>S_WELCOME</b> - Player is welcomed to the game<br>
	 *  <b>S_WELCOME</b> - Player is welcomed to the game<br> */
	public static final int S_WELCOME 	  		   = 0,
							S_HUD_COMPONENTS       = 1,
							S_MATCHCOLOR_DEMO      = 2,
							S_MOVE_DEMO			   = 3,
							S_MOVE_PLAY_PROMPT     = 4,
							S_MOVE_PLAY_REPROMPT   = 5,
							S_MOVE_PLAY			   = 6,
							S_ROTATE_DEMO		   = 7,
							S_ROTATE_PLAY_PROMPT   = 8,
							S_ROTATE_PLAY_REPROMPT = 9,
							S_BOMB_DEMO			   = 10,
							S_BOMB_PLAY_PROMPT	   = 11,
							S_BOMB_PLAY_REPROMPT   = 12,
							S_FREEPLAY			   = 13;
	
	GameScr					screen;
	
	public int				currentStep   = -1;
	public boolean			hasStarted    = false;
	public int				sequenceTimer = 0;
	
	public int				moveDirCount;
	public boolean			trackingPlayerInput = false;
	
	public boolean[]		movedInDirections = new boolean[8];
	public int				movedInDirectionsCount = 0;
	public int				rotatedCount = 0;
	public int				bombedCount  = 0;
	
	public TutorialSequencer(GameScr s)
	{
		screen = s;
		currentStep = S_WELCOME;
		
		moveDirCount = (SystemInfo.isGoogleTV() || !SystemInfo.hasTouchInput()) ? 4 : 8;
		
		for(boolean d : movedInDirections)	//initially we have not moved in an directions
			d = false;
		
		//disable player input at the start of tutorial mode
		screen.playerController.playerInputEnabled(false);	
	}
	
	/** take the necessary actions to process the next step */
	public void processStep()
	{
		if(currentStep != 0 && 
				screen.game.getPreferences().getPref(CSSettings.KEY_ENABLE_SFX, CSSettings.DEFAULT_ENABLE_SFX))
			screen.assets.getSound(GameScrAssets.SND_ITEM).play(1f);
			sequenceTimer = 0;
		switch(currentStep)
		{
			case S_WELCOME:
				GameStats.newGameStats((ColorShafted)screen.game);
				GameStats.difficulty = 0;
				GameStats.bombs = 3;
				GameStats.score = 0;
				showPrompt( new StringBuilder("Welcome to Tutorial Mode"), 
							new StringBuilder("The energy grid which controls the galaxy has gone out of balance! Thank you for taking the time to refresh your skills in controlling" +
									" the new technology that will save us all. We will now go over the fundamentals." +
									" "), new StringBuilder("NEXT>>"));
				currentStep++;
				break;
			case S_HUD_COMPONENTS:
				showPrompt( new StringBuilder("Heads Up Display(HUD)"), 
						new StringBuilder("During the game, you will see several statistics listed that will help you see what is going on. \n\nOn the top left, we have " +
								"\"SCORE\", \"LIVES\" and \"DIFFICULTY\". The SCORE tells you how well you are doing. You get a better score by getting succesful color matches, hitting " +
								"them in succession with no misses and surviving through higher difficulties. LIVES tells you how many times you can afford to miss hitting colors together. " +
								"DIFFICULTY will tell you what level you are currently at. As you progress, the game gets faster and more difficult!\n\nOn the top right of the screen you will " +
								"see \"BOMBS\" which tells you how many bombs you have remaining to use in tight situations."), new StringBuilder("NEXT>>"));
				currentStep++;
				break;
			case S_MATCHCOLOR_DEMO:
				showPrompt( new StringBuilder("MATCHING COLORS"),
						new StringBuilder("The aim of the game is to make your control block's color blocks to match up with" +
								" incoming blocks. This will add to your score and keep the galaxy intact"), 
								new StringBuilder("DEMO>>"));
				screen.playerController.playerInputEnabled(false);			//disable player input
				break;
			case S_MOVE_DEMO:
				showPrompt( new StringBuilder("MOVING IN THE GRID"),
						new StringBuilder("In order to avoid and position our control block, we are able to move in " + moveDirCount +
								" directions. "), 
								new StringBuilder("DEMO>>"));
				break;
			case S_MOVE_PLAY_PROMPT:
				movedInDirectionsCount = 0;
				showPrompt(new StringBuilder("MOVING IN THE GRID"),
						new StringBuilder("Now you try it! " + ((moveDirCount == 8) ? 
							("Slide your finger accross the screen quickly in a direction to move the control block. " +
							 "Successfully move in a few directions to continue. ") : 
							("Press the DPAD Up, Down, Left and Right or W, A, D, X keys in order to move in the 4 directions. " +
							 "Successfully move in all 4 directions to continue"))),
							 new StringBuilder("TRY IT"));
				screen.playerController.playerInputEnabled(true);
				trackingPlayerInput = true;
				for(boolean b: movedInDirections)
					b = false;
				break;
			case S_MOVE_PLAY_REPROMPT:
				movedInDirectionsCount = 0;
				showPrompt(new StringBuilder("MOVING IN THE GRID"),
						new StringBuilder("Having trouble? " + ((moveDirCount == 8) ? 
							("The key is to tap slightly down and then let go fast. " +
							 "Successfully move in a few different directions to continue. ") : 
							("Press the DPAD Up, Down, Left and Right or W, A, D, X keys in order to move in the 4 directions. " +
							 "Successfully move in all 4 directions to continue"))),
							 new StringBuilder("RETRY"));
				screen.playerController.playerInputEnabled(true);
				trackingPlayerInput = true;
				for(boolean b: movedInDirections)
					b = false;
				break;
			case S_ROTATE_DEMO:
				showPrompt(new StringBuilder("ROTATING"),
						new StringBuilder("Alright! Nice Job! Now, aside from moving, you can also rotate the control block to quickly get ready for " +
								"incoming blocks at different angles!"),
							 new StringBuilder("DEMO>>"));
				screen.playerController.playerInputEnabled(false);
				trackingPlayerInput = false;
				break;
			case S_ROTATE_PLAY_PROMPT:
				showPrompt(new StringBuilder("ROTATING"),
						new StringBuilder("Now you try it! " + ((moveDirCount == 4) ? "Press the DPAD Center or K to rotate Left, " +
								"and L (or tap the mouse button if using Sony Google TV) to rotate right.)" : 
							"Tap the left side of the screen to rotate counter clockwise or the right side to rotate clockwise." ) + 
										  " Successfully rotate 4 times in order to continue to free practice"),
							 new StringBuilder("TRY IT"));
				screen.playerController.playerInputEnabled(true);
				trackingPlayerInput = true;
				rotatedCount = 0;
				break;
			case S_ROTATE_PLAY_REPROMPT:
				showPrompt(new StringBuilder("ROTATING"),
						new StringBuilder("Having a hard time? " + ((moveDirCount == 4) ? "Press the DPAD Center or K to rotate Left, " +
								"and L (or tap the mouse button if using Sony Google TV) to rotate right. Make sure you are only tapping and not holding the rotate buttons!" : 
							"Tap the left side of the screen to rotate counter clockwise or the right side to rotate clockwise. " +
							"Make sure that you are tapping the screen very quickly and not holding it!" ) + 
										  " Successfully rotate 4 times in order to continue to free practice"),
							 new StringBuilder("RETRY"));
				screen.playerController.playerInputEnabled(true);
				trackingPlayerInput = true;
				rotatedCount = 0;
				break;			
			case S_BOMB_DEMO:
				GameStats.bombs = 3;
				showPrompt(new StringBuilder("BOMBING!"),
							new StringBuilder("Cool! So now we have moving and rotating down. " +
									"When things become overwhelming, you are also going to be able to destroy all enemies swiftly with the use" +
									" of a bomb trigger."),
								 new StringBuilder("DEMO>>"));
					screen.playerController.playerInputEnabled(false);
					trackingPlayerInput = false;
					break;
			case S_BOMB_PLAY_PROMPT:
				GameStats.bombs = 3;
				bombedCount = 0;
				showPrompt(new StringBuilder("BOMBING!"),
						new StringBuilder("Alright! So in order to do that, you have to tap the red bomb trigger button" +
								" on the top right of the screen or if using a keypad press the space key. Now lets see if you can get this. Try to successfully launch 3 bombs."),
							 new StringBuilder("TRY IT"));
				trackingPlayerInput = true;
				screen.playerController.playerInputEnabled(true);
				break;
			case S_BOMB_PLAY_REPROMPT:
				GameStats.bombs = 3;
				showPrompt(new StringBuilder("BOMBING!"),
						new StringBuilder("Having trouble? tap the red bomb trigger button" +
								" on the top right of the screen or if using a keypad press the space key. Now lets see if you can get it this time. Try to successfully launch 3 bombs."),
							 new StringBuilder("RETRY"));
				trackingPlayerInput = true;
				screen.playerController.playerInputEnabled(true);
				bombedCount = 0;
				break;
			case S_FREEPLAY:
				GameStats.difficulty = 0;
				showPrompt(new StringBuilder("PRACTICE MODE"),
						new StringBuilder("Now we will enter practice mode until you begin. This will allow you to sharpen your skills " +
								"as we take things a little slower before trying your hand at arcade or survival mode. During practice mode, " +
								"press your device's \"BACK\" key to return to the title screen"),
							 new StringBuilder("START"));
				screen.playerController.playerInputEnabled(true);
				break;
		}
	}
	
	/** called during the game screen's timedLogic() to help encapsulate the tutorial logic. helps us by avoiding recursion. 
	 * <br>[fudge j00 recursion >=o] */
	public void update()
	{
		if(currentStep == S_WELCOME && !hasStarted)
		{
			hasStarted = true;
			processStep();				//show welcome prompt
			processStep();				//after returning to screen, show the HUD components
			processStep();				//now show the matching colors prompt
		}
		
		switch(currentStep)
		{
			case S_MATCHCOLOR_DEMO:
			{
				switch(sequenceTimer)
				{
					case 50:
						screen.envObjCreator.spawnColorBlock(ColorBlock.BLUE, 0, 1);		//create the incoming enemy block
						break;
					case 100:
						screen.envObjCreator.spawnColorBlock(ColorBlock.GREEN, 3, 2);
						break;
					case 150:
						screen.envObjCreator.spawnColorBlock(ColorBlock.RED, 1, 2);
						break;
					case 200:
						screen.envObjCreator.spawnColorBlock(ColorBlock.YELLOW, 2, 1);
						break;
					//show the user a demo of not correctly matching colors
					case 300:
						showPrompt( new StringBuilder("MATCHING COLORS"),
								new StringBuilder("Be careful though! If you don't match the colors, you will lose a life or shield" +
										" and the galaxy will be closer to being destroyed! When you lose all of your lives or shield, it is game over.\n\n" +
										"For a split second after an unsuccesful collision, you will be invincible and destroy and block that you come in contact" +
										" with so be sure to remember this when things get crazy!"), 
										new StringBuilder("DEMO>>"));
						break;
					case 350:
						screen.envObjCreator.spawnColorBlock(ColorBlock.RED, 1, 1);
						break;
					case 450:
						screen.envObjCreator.spawnColorBlock(ColorBlock.BLUE, 0, 2);
						break;
					//now advance to the control block swipe movement sequence
					case 550:
						currentStep++;
						processStep();
						break;
				}
				break;
			}
			case S_MOVE_DEMO:
			{
				if(moveDirCount == 4)
				switch(sequenceTimer)
				{
					case 50: 
						screen.playerControlEvent(PlayerControl.SWIPE_UP);
						break;
					case 75: 
						screen.playerControlEvent(PlayerControl.SWIPE_RIGHT);
						break;
					case 100:
						screen.playerControlEvent(PlayerControl.SWIPE_DOWN);
						break;
					case 125:
						screen.playerControlEvent(PlayerControl.SWIPE_LEFT);
						break;
					case 150:
						screen.playerControlEvent(PlayerControl.SWIPE_DOWN);
						break;
					case 175:
						screen.playerControlEvent(PlayerControl.SWIPE_UP);
						break;
					case 200:
						screen.playerControlEvent(PlayerControl.SWIPE_LEFT);
						break;
					case 225:
						screen.playerControlEvent(PlayerControl.SWIPE_RIGHT);
						break;
					case 250:
						//ask the player to try
						currentStep++;
						processStep();
						break;
				}
				else	//if 8 direction input is allowed...
				{
					switch(sequenceTimer)
					{
						case 50: 
							screen.playerControlEvent(PlayerControl.SWIPE_UP);
							break;
						case 75: 
							screen.playerControlEvent(PlayerControl.SWIPE_RIGHT);
							break;
						case 100:
							screen.playerControlEvent(PlayerControl.SWIPE_DOWN);
							break;
						case 125:
							screen.playerControlEvent(PlayerControl.SWIPE_LEFT);
							break;
						case 150:
							screen.playerControlEvent(PlayerControl.SWIPE_UP_RIGHT);
							break;
						case 175:
							screen.playerControlEvent(PlayerControl.SWIPE_DOWN_LEFT);
							break;
						case 200:
							screen.playerControlEvent(PlayerControl.SWIPE_LEFT_UP);
							break;
						case 225:
							screen.playerControlEvent(PlayerControl.SWIPE_RIGHT_DOWN);
							break;
						case 250:
							screen.playerControlEvent(PlayerControl.SWIPE_DOWN_LEFT);
							break;
						case 275:
							screen.playerControlEvent(PlayerControl.SWIPE_UP_RIGHT);
							break;
						case 300:
							screen.playerControlEvent(PlayerControl.SWIPE_RIGHT_DOWN);
							break;
						case 325:
							screen.playerControlEvent(PlayerControl.SWIPE_LEFT_UP);
							break;
						case 350:
							//ask the player to try
							currentStep++;
							processStep();
							break;
					}
				}
				break;
			}
			case S_MOVE_PLAY_REPROMPT:
			case S_MOVE_PLAY_PROMPT:
			{
				if(movedInDirectionsCount >= 4)
				{
					currentStep = S_ROTATE_DEMO;
					processStep();
				}
			}
			
			if(sequenceTimer == 750)
			{
				currentStep = S_MOVE_PLAY_REPROMPT;
				processStep();
			}
			break;
			case S_ROTATE_DEMO:
			{
				switch(sequenceTimer)
				{
					case 10:
					case 20:
					case 30:
					case 40:
						if(screen.controlBlock.rotationPos != 0)
							screen.playerControlEvent(PlayerControl.ROTATE_RIGHT);
						if(screen.controlBlock.gridPosX > 1)
							screen.controlBlock.gridPosX--;
						else if(screen.controlBlock.gridPosX < 1)
							screen.controlBlock.gridPosX++;
						if(screen.controlBlock.gridPosY > 1)
							screen.controlBlock.gridPosY--;
						else if(screen.controlBlock.gridPosY < 1)
							screen.controlBlock.gridPosY++;
						break;
					case 50:
						screen.envObjCreator.spawnColorBlock(ColorBlock.RED, 1, 1);
						break;
					case 65:
						screen.playerControlEvent(PlayerControl.ROTATE_RIGHT);
						break;
					case 100:
						screen.envObjCreator.spawnColorBlock(ColorBlock.GREEN, 3, 2);
						break;
					case 115:
						screen.playerControlEvent(PlayerControl.SWIPE_DOWN);
						break;
					case 118:
					case 122:
						screen.playerControlEvent(PlayerControl.ROTATE_LEFT);
						break;
					case 165:
						screen.envObjCreator.spawnColorBlock(ColorBlock.YELLOW, 2, 2);
						break;
					case 180:
						screen.playerControlEvent(PlayerControl.ROTATE_RIGHT);
						break;
					case 185:
						screen.playerControlEvent(PlayerControl.SWIPE_LEFT);
						break;
					case 240:
						screen.playerControlEvent(PlayerControl.SWIPE_UP);
						break;
					case 250:
						screen.playerControlEvent(PlayerControl.SWIPE_RIGHT);
						break;
					case 280:
						currentStep = S_ROTATE_PLAY_PROMPT;
						processStep();
						break;
				}
			}
				break;
			case S_ROTATE_PLAY_REPROMPT:
			case S_ROTATE_PLAY_PROMPT:
				if(sequenceTimer == 500)
				{
					currentStep = S_ROTATE_PLAY_REPROMPT;
					processStep();
				}
				if(rotatedCount >= 4)
				{
					currentStep = S_BOMB_DEMO;
					processStep();
				}
				break;
			case S_BOMB_DEMO:
				switch(sequenceTimer)
				{
					case 0: 
					case 15:
					case 30:
					case 45:
					case 60:
					case 150:
					case 165:				GameStats.difficulty = 0;
					case 180:
					case 195:
					case 210:
						screen.envObjCreator.spawnColorBlock();
						break;
					case 80:
					case 215:
						screen.playerControlEvent(PlayerControl.BOMB_SCREEN);
						break;
					case 300:
						currentStep = S_BOMB_PLAY_PROMPT;
						processStep();
						break;
				}
				break;
			case S_BOMB_PLAY_PROMPT:
			case S_BOMB_PLAY_REPROMPT:
			{
				switch(sequenceTimer)
				{
					case 1000:
						currentStep = S_BOMB_PLAY_REPROMPT;
						processStep();
						break;
				}
				
				if(bombedCount >= 3)
				{
					currentStep = S_FREEPLAY;
					processStep();
				}
				break;
			}
			case S_FREEPLAY:
				GameStats.bombs = 5;
				GameStats.difficulty = 0;
				GameStats.score = 0;
				GameStats.lives = 5;
				break;
		}
		sequenceTimer++;
	}
	
	/** display a tutorial dialog(used for convenient reference) */
	public void showPrompt(StringBuilder header, StringBuilder content, StringBuilder btnTxt)
	{
		((ColorShaftedPrompter)screen.game.getPrompter()).showTutorialDialog(header, content, btnTxt);
	}
	
	public void trackPlayerInput(int playerAction)
	{
			switch(playerAction)
			{
				case PlayerControl.SWIPE_UP:
					if(!movedInDirections[0])
					{
						movedInDirections[0] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.SWIPE_DOWN:
					if(!movedInDirections[1])
					{
						movedInDirections[1] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.SWIPE_LEFT:
					if(!movedInDirections[2])
					{
						movedInDirections[2] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.SWIPE_RIGHT:
					if(!movedInDirections[3])
					{
						movedInDirections[3] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.SWIPE_UP_RIGHT:
					if(!movedInDirections[4])
					{
						movedInDirections[4] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.SWIPE_RIGHT_DOWN:
					if(!movedInDirections[5])
					{
						movedInDirections[5] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.SWIPE_DOWN_LEFT:
					if(!movedInDirections[6])
					{
						movedInDirections[6] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.SWIPE_LEFT_UP:
					if(!movedInDirections[7])
					{
						movedInDirections[7] = true;
						movedInDirectionsCount++;
					}
					break;
				case PlayerControl.ROTATE_LEFT:
					rotatedCount++;
					break;
				case PlayerControl.ROTATE_RIGHT:
					rotatedCount++;
					break;
				case PlayerControl.BOMB_SCREEN:
					bombedCount++;
					break;
		}			
	}
}
