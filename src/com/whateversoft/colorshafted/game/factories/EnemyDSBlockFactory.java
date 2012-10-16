package com.whateversoft.colorshafted.game.factories;

import android.util.Log;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.game.ColorBlockE;
import com.whateversoft.colorshafted.game.ColorBlockEDS;
import com.whateversoft.colorshafted.screens.GameScr;

public class EnemyDSBlockFactory extends ObjectFactory<ColorBlockEDS>
{
	GameScr screen;
	public int layer;
	
	public EnemyDSBlockFactory(GameScr s, int l)
	{
		screen = s;
		layer  = l;
	}
	
	@Override
	public ColorBlockEDS newObject()
	{
		return new ColorBlockEDS(-1, -1, -1, -100, -100, layer, screen);
	}
	
	public ColorBlockE getFactoryObject(int color, int initialDirection, int initialShaft)
	{
		Log.d("COLORSHAFTED", "just retrieved a factory obect from the desaturation block factory");
		//return instance from factory
		ColorBlockE returnObj = retrieveInstance();					  
		
		//set its initial parameters
		returnObj.destroyed = false;				//re-allow collisions
		returnObj.visible   = true;
		returnObj.instantiateAsEnemy(initialDirection, initialShaft, color); 
		
		//return this new object with the correct parameters
		return returnObj;
	}
	
	public ColorBlockE getFactoryObject(int shaftIndex, int color)
	{
		Log.d("COLORSHAFTED", "just retrieved a factory obect from the desaturation block factory");
		//return instance from factory
		ColorBlockEDS returnObj = retrieveInstance();
		
		//set its initial parameters
		returnObj.destroyed = false;
		returnObj.visible = true;
		returnObj.instantiateAsEnemy(shaftIndex, color);
		returnObj.setColorAndGraphic(color);
		
		//return this object with the defined parameters
		return returnObj;
	}

	@Override
	public void throwInPool(ColorBlockEDS obj)
	{
		obj.visible = false;
		obj.x = -100;
		obj.y = -100;
		obj.destroyed = true;	//to prevent collision
		screen.eBlocks.remove(obj);
		objectPool.add(obj);
	}
}
