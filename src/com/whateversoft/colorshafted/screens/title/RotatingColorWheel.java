package com.whateversoft.colorshafted.screens.title;

import com.whateversoft.android.framework.ImageEntity;
import com.whateversoft.android.framework.Input;
import com.whateversoft.colorshafted.screens.TitleScr;
import com.whateversoft.colorshafted.screens.TitleScrAssets;

public class RotatingColorWheel extends ImageEntity
{
	public Input input;
	public double rotationSpeed = 0;
	public RotatingColorWheel(float x, float y, TitleScr s)
	{
		super(x, y, s.assets.getImage(TitleScrAssets.IMG_BUTTON_WHEEL), 1, s);
		input = s.game.getInput();
		rotationSpeed = 1;
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		if(input.getAccelY() <  0 && rotationSpeed > input.getAccelY() * 2)
		{
			if(Math.abs(input.getAccelY()) > 2 || Math.abs(rotationSpeed) > 2)
				rotationSpeed -= 0.2 + Math.floor(Math.abs(input.getAccelY()) * 0.1);
		}
		else if(input.getAccelY() > 0 && rotationSpeed < input.getAccelY() * 2)
		{
			if(Math.abs(input.getAccelY()) > 2 || Math.abs(rotationSpeed) > 2)
				rotationSpeed += 0.2 + Math.floor(Math.abs(input.getAccelY()) * 0.1);;
		}
		rotation -= rotationSpeed %= 360;
	}
}
