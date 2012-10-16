package com.whateversoft.colorshafted.game.factories;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.game.ShadowColor;
import com.whateversoft.colorshafted.screens.GameScr;

public class ShadowColorFactory extends ObjectFactory<ShadowColor>
{
	GameScr screen;
	public int layer;
	
	public ShadowColorFactory(GameScr s)
	{
		screen = s;
	}
	
	@Override
	public ShadowColor newObject()
	{
		return new ShadowColor(screen);
	}
	
	public ShadowColor getFactoryObject(int c, float xOrig, float yOrig, int aOrig, int aIncrement, boolean rotateDir, boolean isRotationObject)
	{
		//return instance from factory
		ShadowColor returnObj = retrieveInstance();					  
		
		//set its initial parameters
		returnObj.destroyed = false;		//re-allow collisions
		returnObj.initializeObject(c, xOrig, yOrig, aOrig, aIncrement, rotateDir, isRotationObject);
		
		//return this new object with the correct parameters
		return returnObj;
	}
	
	/** for returning a shadow block that was an enemy block coming in's shadow */
	public ShadowColor getFactoryObject(int c, float xOrig, float yOrig)
	{
		//return instance from factory
		ShadowColor returnObj = retrieveInstance();					  
		
		//set its initial parameters
		returnObj.destroyed = false;		//re-allow collisions
		returnObj.initializeObject(c, xOrig, yOrig);
		
		//return this new object with the correct parameters
		return returnObj;
	}

	@Override
	public void throwInPool(ShadowColor obj)
	{
		obj.visible = false;
		obj.destroyed = true;	//to prevent collision
		
		obj.x = -100;
		obj.y = -100;
	
		objectPool.add(obj);
	}
}
