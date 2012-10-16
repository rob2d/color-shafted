package com.whateversoft.android.framework.textui;

public interface Prompter
{
	public void showMsg(StringBuilder promptMsg);
	
	public String showInputPrompt(StringBuilder promptMsg);
	
	int showTripleOption(StringBuilder promptMsg, 
							StringBuilder b1t, StringBuilder b2t, StringBuilder b3t);
	
	int showDualOption(StringBuilder promptMsg,  
							StringBuilder b1t, StringBuilder b2t);
}
