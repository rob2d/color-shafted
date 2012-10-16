package com.whateversoft.colorshafted.game.factories;

import android.util.Log;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.game.BlackHole;
import com.whateversoft.colorshafted.screens.GameScr;

public class BlackHoleFactory extends ObjectFactory<BlackHole>
{
	GameScr screen;
	public int layer;
	
	public BlackHoleFactory(GameScr s)
	{
		screen = s;
	}
	
	@Override
	public BlackHole newObject()
	{
		return new BlackHole(screen);
	}
	
	public BlackHole getFactoryObject(int orientation)
	{
		//return instance from factory
		BlackHole returnObj = retrieveInstance();					  
		
		//set its initial parameters
		returnObj.destroyed = false;				//re-allow collisions
		returnObj.visible   = true;
		returnObj.initializeObject(orientation);	
		
		//return this new object with the correct parameters
		return returnObj;
	}

	@Override
	public void throwInPool(BlackHole obj)
	{
		Log.d("COLORSHAFTED", "Threw an object in the object pool");
		obj.visible = false;
		obj.destroyed = true;	//to prevent collision
		
		obj.x = -100;
		obj.y = -100;
		obj.dx = 0;
		obj.dy = 0;
		screen.blackHoles.remove(obj);
		objectPool.add(obj);
	}
}
