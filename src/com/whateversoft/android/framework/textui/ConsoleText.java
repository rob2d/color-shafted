package com.whateversoft.android.framework.textui;

import java.util.ArrayList;

public class ConsoleText extends ArrayList<String>
{
	public String[] getRecentLines(int lineCount)
	{
		String[] returnLines;
		if(lineCount <= size())
			returnLines = new String[lineCount];  //create array to return
		else
			returnLines = new String[size()];
			
		for(int i = 0; i < returnLines.length; i++)			   //build the array with the most recent lines
		{
			returnLines[i] = get(size() - returnLines.length + i);
		} 
		return returnLines;
	}
	public void addStringLineByLine(String str)
	{
		char scanChar;
		boolean scanning = true;
		int stringStart = 0;
		int lineCount = 0;
			for(int i = 0; i < str.length(); i++)
			{
				if(str.charAt(i) == '\n' || i== (str.length() - 1) || (i - stringStart > 52))
				{
					//create a new string of characters to add
					char[] addedStringData = new char[i - stringStart + 1];
					str.getChars(stringStart, i + 1, addedStringData, 0);	//copy them from what is necessary
					
					if(lineCount != 0)
					add("   " + String.valueOf(addedStringData));	//add these to our list of console text strings
					else add(">> " + String.valueOf(addedStringData));
					lineCount++;
					stringStart = i + 1;	//starting point of next is set
				}
			}
	}
}
