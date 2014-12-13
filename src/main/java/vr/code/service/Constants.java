package vr.code.service;

public class Constants {
	public static int MAX_QUEUE_SIZE=100;
	
	//Timeouts
	public static int POLL_TIMEOUT = 10;
	public static int VIEW_CHANGE_TIMEOUT = 1500;
	public static int VIEW_CHANGE_TIMEOUT1 = 5000;
	
	
	public static int PREPARE_TIMEOUT = 3000;
	public static int GET_STATE_TIMEOUT = 3000;
	public static int START_VIEW_CHANGE_TIMEOUT = 10000;
	public static int DO_VIEW_CHANGE_TIMEOUT = 10000;
	public static int START_VIEW_TIMEOUT = 10000;
	
	
	
	
	//Retries
	public static int PREPARE_MAX_RETRIES = 5;
	public static int GET_STATE_MAX_RETRIES = 5;
	
	
}
