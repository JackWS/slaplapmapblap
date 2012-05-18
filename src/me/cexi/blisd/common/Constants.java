package me.cexi.blisd.common;

import java.util.*;

import me.cexi.blisd.providers.*;

/**
 * Class for implementing Constants used on the app 
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class Constants {

	/**
	 * Constant for saving HTTP Connection Timeout
	 */
	public static final int TIMEOUT = 10000;
	
	/**
	 * Constant for saving All type
	 */
	public static final String TYPE_ALL = "All";

	/**
	 * Constant for saving Dining type
	 */
	public static final String TYPE_DINING = "Dining";

	/**
	 * Constant for saving Attraction type
	 */
	public static final String TYPE_ATTRACTION = "Attraction";

	/**
	 * Constant for saving Shopping type
	 */
	public static final String TYPE_SHOPPING = "Shopping";

	/**
	 * Constant for saving the preference name used for the app
	 */
	public static final String PREFS_NAME = "blisd";

	/**
	 * Constant for saving the location arrays used for the app
	 */
	public static ArrayList<BLocation> locations = null;

	/**
	 * Constant for saving the selected location used for the app
	 */
	public static BLocation selectedLocation = null;

}
