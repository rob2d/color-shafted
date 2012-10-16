package com.whateversoft.android.framework.impl.android.network;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

//Author Timothy Mugayi 
//more codes and tutorials as http://javacupaday.blogspot.com/

public class HTMLParser {
        
    /**
     * Creates a new instance of <code>HTMLParser</code> .
     */
    public HTMLParser() 
    {
    }
    
    /** clears HTML tags from a string using regex */
    public String clearHTMLTags(String strHTML)
    {
    	Pattern pattern = null;
	    Matcher matcher = null;
	   	String regex;
	   	String strTagLess = null; 
    	strTagLess = strHTML; 
    	
  			regex = "<[^>]*>";
  			//removes all html tags
			pattern = pattern.compile(regex);
			strTagLess = pattern.matcher(strTagLess).replaceAll(" "); 
		return strTagLess; 
    }
}