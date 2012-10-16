package com.whateversoft.colorshafted.game.factories;

import android.util.Log;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.game.ErrorExplosion;
import com.whateversoft.colorshafted.screens.GameScr;

public class ErrorExplosionFactory extends ObjectFactory<ErrorExplosion>
{
	GameScr screen;

	public ErrorExplosionFactory(GameScr s)
	{
		screen = s;
	}
	
	@Override
	public ErrorExplosion newObject()
	{	
		return new ErrorExplosion(-1000, -1000, screen);
	}

	@Override
	public void throwInPool(ErrorExplosion obj)
	{
		obj.visible   = false;
		obj.destroyed = true;
		obj.x = -1000;
		obj.y = -1000;
		
		objectPool.add(obj);
	}
	
	public ErrorExplosion getFactoryObject(int x, int y)
	{
		ErrorExplosion returnObj = retrieveInstance();
		returnObj.x = x;
		returnObj.y = y;
		returnObj.destroyed = false;
		returnObj.visible = true;
		
		if(returnObj.anims.get(0) != null)
		{
			returnObj.resetAnim();				//allow the explosion to animate again
			returnObj.animate(0);
		}
		return returnObj;
	}
}