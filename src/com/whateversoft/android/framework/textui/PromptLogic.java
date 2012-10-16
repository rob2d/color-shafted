package com.whateversoft.android.framework.textui;

/** Helper class made for menus to prompt for certain types of responses. */
public abstract class PromptLogic 
{
	/** evaluates whether "yes" or "no" has been inputted. will return NULL if neither because
	 * we are using a wrapper class for the answer
	 * @param evaluateStr
	 * @return TRUE if Yes, FALSE if No, and NULL if neither. This will throw a "NullPointerException" from the 
	 * calling code if we try to assign it to a primitive boolean(HANDY FOR US! :D)*/
	public static Boolean yesNoQuestion(String evaluateStr)
	{
		Boolean returnBoolean = null;
		evaluateStr = evaluateStr.trim();	//cut off blank spaces and make one case for easy analyzing
		if(evaluateStr.equalsIgnoreCase("yes"))
			return true;
		else if(evaluateStr.equalsIgnoreCase("no"))
			return false;
		else return null;
	}
	
	/** evaluates a String input for questions from a text field answer from a user. 
	 * Compares against any array of possible answers to match and returns the number of the choice,
	 * -1 if nothing works
	 * @param evaluateStr - String the user gives us when prompted
	 * @param options - the list of options to compare against
	 * @return the number of the option made if valid, otherwise -1.
	 *  EXCEPTION: If cancel is selected, -2 is returned! */
	public static int optionSelection(String evaluateStr, String... options) throws NullPointerException
	{
		if(evaluateStr != null)
		{
			int selection = -1;	//if no valid entry is found, we return "-1".
			evaluateStr = evaluateStr.trim();	//cut off blank spaces and make one case for easy analyzing
			for(int i = 0; i < options.length; i++)		//check whether our evaluation string is equal 
				if(evaluateStr.equalsIgnoreCase(options[i]))
					selection = i;
			if(evaluateStr.equalsIgnoreCase("EXIT"))
				return -2;
			return selection;	//return selection
		}
		else return -2;
	}
}
