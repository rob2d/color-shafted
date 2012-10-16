package com.whateversoft.android.framework.impl.android.network;

/** respresents an application that can make HTTP Requests. In an Android activity, you would
 *  typically be implemente by an <b>Activity</b> class */
public interface HttpClientApp
{
	public static int REQUEST_FAILED = -1;
	/** react to an incoming http request response. <font color="red">Be sure</font color> to check for 
	 *  serverRequestCode == <b>REQUEST_FAILED</b> if things fail */
	public void onServerResponse(HttpRequestTask httpTask, int serverRequestCode, String responseStr);
	/** calle when an HTTP Request times out */
	public void onRequestTimeout(HttpRequestTask httpTask);
	
	/** when an error communicating with the server occurs, tell the HttpClientApp that
	 *  there was a problem so it can notify the user */
	public void showServerErrorMsg();
}
