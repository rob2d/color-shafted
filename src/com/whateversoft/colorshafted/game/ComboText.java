package com.whateversoft.colorshafted.game;

import static com.whateversoft.colorshafted.screens.GameScrAssets.FONT_SFTELEVISED;
import android.graphics.Color;
import android.graphics.Paint;

import com.whateversoft.android.framework.TextEntity;
import com.whateversoft.colorshafted.screens.GameScr;

public class ComboText extends TextEntity
{
	public float sizeFloat;
	public int 	 colorType;
	
	public static final int HUD_DOWN = 4,
							HUD_UP   = 5;

	public ComboText(int x, int y, StringBuffer comboStrB, int expColor, GameScr scr)
	{
		super(x, y, comboStrB, 
				Color.WHITE, scr.assets.getFont(FONT_SFTELEVISED), 20, Paint.Align.CENTER, 
				GameScr.LAYER_HUD, scr);
		
		//reset to original size/alpha/color type(RED, BLUE, GREEN, YELLOW)
		initialValues(expColor);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
		if(colorType != -1)	//if colorType == -1, it wasn't initialized yet	
		{
			//set color as it fades out
			switch(colorType)
			{
				case Color.BLUE:   color = Color.argb(alpha, alpha, alpha, 255);   break;
				case Color.GREEN:  color = Color.argb(alpha, alpha, 255, alpha);   break;
				case Color.YELLOW: color = Color.argb(alpha, 255, 255, alpha);     break;
				case Color.RED:    color = Color.argb(alpha, 255, alpha, alpha);   break;
				case HUD_DOWN:   	   color = Color.argb(alpha, 255, 255, 255);   break;
				case HUD_UP:	   color = Color.argb(255 - alpha, 255, 255, 255); break;
			}
		
		//increase size
		if(colorType != HUD_DOWN && colorType != HUD_UP)
			sizeFloat += 1.3;
		else
		{
			sizeFloat = 18;
			x+=	1;
		}
		
		size = Math.round(sizeFloat);
		
		//fade out alpha & destroy object as necessary
		if(alpha > 10) alpha -= colorType != HUD_DOWN ? 10 : 20;
		else ((GameScr)screen).cTxtFactory.throwInPool(this);
		}	
	}
	
	public void initialValues(int expColor)
	{
		sizeFloat = 2;
		alpha = 255;
		
		switch(expColor)
		{
			case ColorBlock.BLUE:   colorType = Color.BLUE;   break;
			case ColorBlock.GREEN:  colorType = Color.GREEN;  break;
			case ColorBlock.RED:    colorType = Color.RED;    break;
			case ColorBlock.YELLOW: colorType = Color.YELLOW; break;
			case HUD_DOWN:			colorType = HUD_DOWN;    break;
			case HUD_UP:			colorType = HUD_UP;      break;
		}
	}
}