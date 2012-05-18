package me.cexi.blisd.communication;

import java.io.*;
import java.net.*;

import me.cexi.blisd.*;
import me.cexi.blisd.common.*;
import me.cexi.blisd.providers.BLocation.*;

import org.json.*;

import android.content.*;
import android.util.*;

/**
 * Class for implementing the communication module for Web API
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class GetLocations {
	
	private static final String TAG = "GetClassList";
	
	/**
	 * Member Variable for saving the Context object
	 */
	private Context context = null;
	
	/**
	 * Constructor 
	 * 
	 * @param context	The Context object
	 */
	public GetLocations(Context context) {
		this.context = context;
	}
	
	/**
	 * Method to get location information from Web API
	 * (Parsing JSON data)
	 * 
	 * @param url	the Web API URL
	 */
    public void request(String url) {
		SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME, 0);
    	
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
    	
		String response = connect(url);
		if (response == null || response.equals("")) {
			response = connect();
		}
		
		if (response == null || response.equals("")) {
			return;
		}
		
		JSONObject message = null;
		JSONArray items = null;
		JSONObject item = null;
		JSONObject extra = null;
		
		try {
			message = new JSONObject(response);
			
				items = message.getJSONArray("results");
				for (int i = 0;i < items.length();i++) {
					item = items.getJSONObject(i);
					extra = item.getJSONObject("cexi-extras");
					
					values.clear();
					
					values.put(BLocations.LOCATIONNAME, item.getString("name"));
					values.put(BLocations.ADDRESS, item.getString("formatted_address"));
					values.put(BLocations.CITY, "");
					values.put(BLocations.LOCATIONSTATE, "");
					values.put(BLocations.ZIP, "");
					values.put(BLocations.TAGLINE, extra.getString("tagline"));
					values.put(BLocations.LOCATIONDESCRIPTION, extra.getString("locationdescription"));
					values.put(BLocations.PHONE, extra.getString("phone"));
					values.put(BLocations.WEBSITE, extra.getString("website"));
					values.put(BLocations.DATESTART, extra.getString("datestart"));
					values.put(BLocations.DATEEND, extra.getString("dateend"));
					values.put(BLocations.TIMESTART, extra.getString("timestart"));
					values.put(BLocations.TIMEEND, extra.getString("timeend"));
					values.put(BLocations.LOCATIONTYPE, extra.getString("locationtype"));
					values.put(BLocations.LOCATIONTYPEGRAPHIC, extra.getInt("locationtypegraphic"));
					values.put(BLocations.DISPLAYCUSTOMERICON, extra.getInt("displaycustomericon"));
					values.put(BLocations.CUSTOMERICONURL, extra.getString("customericonURL"));
					values.put(BLocations.ALLOWMOREINFO, extra.getInt("allowmoreinfo"));
					values.put(BLocations.DISPLAYSUBVIEW, extra.getInt("allowmoreinfo"));
					values.put(BLocations.CALLFROMANNOTATION, extra.getInt("allowmoreinfo"));
					values.put(BLocations.LATITUDE, item.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
					values.put(BLocations.LONGITUDE, item.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
					values.put(BLocations.BUY, -1/*extra.getInt("buy")*/);
					values.put(BLocations.BUYITEMS, ""/*extra.getString("buyitems")*/);
					values.put(BLocations.GET, ""/*extra.getString("get")*/);
					
					cr.insert(BLocations.CONTENT_URI, values);
				}
				
				SharedPreferences.Editor editor = settings.edit();
				editor.putLong("last_update", System.currentTimeMillis());
				editor.commit();
			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
	}
	
    /**
     * Method to get response from local cash
     * 
     * @return		The response data
     */
	private String connect() {
		StringBuffer result = new StringBuffer();
		
		try {
			InputStream in = context.getResources().openRawResource(R.raw.locationresults);
			BufferedInputStream bis = new BufferedInputStream(in);
			
			int len = 0;
			byte[] buf = new byte[102400];
			while ((len = bis.read(buf)) != -1)
			{
				result.append(new String(buf, 0, len));
			}
			bis.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
    /**
     * Method to get response from remote path
     * 
     * @return		The response data
     */
	private String connect(String path) {
		URL url = null;
		HttpURLConnection http_connection = null;
		InputStream inputstream = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		String line = null;
		int result = -1;

		try {
			url = new URL(path);

			http_connection = (HttpURLConnection) url.openConnection();

			http_connection.setConnectTimeout(Constants.TIMEOUT);
			
			result = http_connection.getResponseCode();

			if (result == HttpURLConnection.HTTP_OK) {

				inputstream = http_connection.getInputStream();

				reader = new BufferedReader(new InputStreamReader(inputstream));

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			}
			else {
				Log.d("HTTPConnection Fail", "" + result);
			}

		} catch (Exception e) {
			Log.e(TAG, e.toString());
		} finally {
			try {
				reader.close();
				reader = null;
				inputstream.close();
				inputstream = null;
				http_connection.disconnect();
				http_connection = null;
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
		}

		return sb.toString();
	}

}
