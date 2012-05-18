package me.cexi.blisd.imagerepository;

import java.io.*;
import java.net.*;

import android.graphics.*;
import android.util.*;

/**
 * Class for implementing ImageRepository
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class ImageRepository {
	
	private static final String TAG = "ImageRepository";

	/**
	 * Member Variable for saving the single ImageReposigory object (Reference Singleton Pattern)
	 */
	private static ImageRepository instance = null;
	
	private ImageRepository() {
	}
	
	/**
	 * Method to get the single ImageRepository object
	 * @return
	 */
	public static ImageRepository getInstance() {
		if (instance == null) {
			instance = new ImageRepository();
		}
		return instance;
	}
	
	/**
	 * Method to get the Bitmap object from the remote path
	 * 
	 * @param image_url		The remote image url
	 * @return				The image bitmap object
	 */
	public Bitmap getRemoteImage(String image_url) {
		Bitmap bm = null;
		try {
			URL imageURL = new URL(image_url);
			HttpURLConnection conn = (HttpURLConnection)imageURL.openConnection();            
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			bm = BitmapFactory.decodeStream(bis);
			bis.close();
			conn.disconnect();
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
			bm = null;
		}
		return bm;
	}
}
