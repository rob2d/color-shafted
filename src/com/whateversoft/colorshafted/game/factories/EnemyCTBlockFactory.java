package com.whateversoft.colorshafted.game.factories;

import android.util.Log;

import com.whateversoft.android.framework.ObjectFactory;
import com.whateversoft.colorshafted.game.ColorBlockE;
import com.whateversoft.colorshafted.game.ColorBlockECT;
import com.whateversoft.colorshafted.screens.GameScr;

public class EnemyCTBlockFactory extends ObjectFactory<ColorBlockECT>
{
	GameScr screen;
	public int layer;
	
	public EnemyCTBlockFactory(GameScr s, int l)
	{
		screen = s;
		layer  = l;
	}
	
	@Override
	public ColorBlockECT newObject()
	{
		return new ColorBlockECT(-1, -1, -1, -100, -100, layer, screen);
	}
	
	public ColorBlockE getFactoryObject(int color, int initialDirection, int initialShaft)
	{
		Log.d("COLORSHAFTED", "just retrieved a factory obect from the color transformation factory");
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
		Log.d("COLORSHAFTED", "just retrieved a factory obect from the color transformation factory");
		//return instance from factory
		ColorBlockECT returnObj = retrieveInstance();
		
		//set its initial parameters
		returnObj.destroyed = false;
		returnObj.visible = true;
		returnObj.instantiateAsEnemy(shaftIndex, color);
		
		//return this object with the defined parameters
		return returnObj;
	}

	@Override
	public void throwInPool(ColorBlockECT obj)
	{
		obj.visible = false;
		obj.x = -100;
		obj.y = -100;
		obj.destroyed = true;	//to prevent collision
		screen.eBlocks.remove(obj);
		objectPool.add(obj);
	}
}
