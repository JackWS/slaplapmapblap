package me.cexi.blisd.common;

import java.util.*;

import me.cexi.blisd.providers.*;
import me.cexi.blisd.providers.BLocation.BLocations;
import me.cexi.blisd.providers.BlisdContentProvider.*;

import android.content.*;
import android.database.*;

/**
 * Class for implementing utilities used on the app 
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class Utilities {

	/**
	 * Method to search locations from location database per type 
	 * 
	 * @param context		The Context object
	 * @param type			The type for query
	 */
	public static void search(Context context, String type) {
		Constants.locations = new ArrayList<BLocation>();
		BLocation item = null;
        
		String sql = "SELECT id, locationname, address, city, locationstate, zip, tagline, locationdescription, phone, website, datestart, dateend, timestart, timeend, locationtype, locationtypegraphic, displaycustomericon, customericonURL, allowmoreinfo, displaysubview, callfromannotation, latitude, longitude, buy, buyitems, get FROM locations";
		
		if (type != null && !type.equals("All")) {
			sql += " WHERE locationtype like '%" + type + "%'";
		}
		
		BlisdContentProvider.DatabaseHelper dbConn = new DatabaseHelper(context);
		
		Cursor cursor = null;
		
		if (dbConn.openDatabase()) {
			cursor = dbConn.query(sql);
			if (cursor.moveToFirst()) {
				do {
					item = new BLocation();
					item.setId(cursor.getInt(0));						// id 						INTEGER
					item.setLocationname(cursor.getString(1));			// locationname				VARCHAR
					item.setAddress(cursor.getString(2));				// address 					VARCHAR
					item.setCity(cursor.getString(3));					// city						VARCHAR
					item.setLocationstate(cursor.getString(4));			// locationstate			VARCHAR
					item.setZip(cursor.getString(5));					// zip						VARCHAR
					item.setTagline(cursor.getString(6));				// tagline					VARCHAR
					item.setLocationdescription(cursor.getString(7));	// locationdescription		VARCHAR
					item.setPhone(cursor.getString(8));					// phone					VARCHAR
					item.setWebsite(cursor.getString(9));				// website 					VARCHAR
					item.setDatestart(cursor.getString(10));			// datestart			 	DATETIME
					item.setDateend(cursor.getString(11)); 				// dateend					DATETIME
					item.setTimestart(cursor.getString(12));			// timestart		 		DATETIME
					item.setTimeend(cursor.getString(13));				// timeend		 			DATETIME
					item.setLocationtype(cursor.getString(14));			// locationtype 			VARCHAR
					item.setLocationtypegraphic(cursor.getInt(15));		// locationtypegraphic 		INTEGER
					item.setDisplaycustomericon(cursor.getInt(16));		// displaycustomericon		INTEGER
					item.setCustomericonURL(cursor.getString(17));		// customericonURL 			VARCHAR
					item.setAllowmoreinfo(cursor.getInt(18));			// allowmoreinfo 			INTEGER
					item.setDisplaysubview(cursor.getInt(19));			// displaysubview 			INTEGER
					item.setCallfromannotation(cursor.getInt(20));		// callfromannotation 		INTEGER
					item.setLatitude(cursor.getFloat(21));				// latitude 				FLOAT
					item.setLongitude(cursor.getFloat(22));				// longitude 				FLOAT
					item.setBuy(cursor.getInt(23));						// buy		 				INTEGER
					item.setBuyitems(cursor.getString(24));				// buyitems 				VARCHAR
					item.setGet(cursor.getString(25));					// get 						VARCHAR
										
					Constants.locations.add(item);
				}
				while(cursor.moveToNext());
			}
			
			cursor.close();
			dbConn.close();
		}
	}
	
	/**
	 * Method to group-search from local database
	 * 
	 * @param context		The Context object
	 * @return
	 */
	public static ArrayList<Object> getLocationList(Context context) {
		ArrayList<Object> items = new ArrayList<Object>();
		ArrayList<String> types = new ArrayList<String>();
		BLocation item = null;
		
		BlisdContentProvider.DatabaseHelper dbConn = new DatabaseHelper(context);
		
		Cursor cursor = null;
		
		String sql = "SELECT locationtype FROM locations group by locationtype";
		
		if (dbConn.openDatabase()) {
			
			/** Fetch list of location type */
			cursor = dbConn.query(sql);
			if (cursor.moveToFirst()) {
				do {
					types.add(cursor.getString(0));	// locationtype	VARCHAR
				}
				while(cursor.moveToNext());
			}
			
			/** Fetch list with same location type */
			for (int i = 0;i < types.size();i++) {
				
				items.add(types.get(i));
				
				sql = "SELECT id, locationname, address, city, locationstate, zip, tagline, locationdescription, phone, website, datestart, dateend, timestart, timeend, locationtype, locationtypegraphic, displaycustomericon, customericonURL, allowmoreinfo, displaysubview, callfromannotation, latitude, longitude, buy, buyitems, get FROM locations"
					+ " WHERE locationtype like '%" + types.get(i) + "%' ORDER BY locationname ASC";
				
				// Fetch location object per type
				cursor = dbConn.query(sql);
				if (cursor.moveToFirst()) {
					do {
						item = new BLocation();
						item.setId(cursor.getInt(0));						// id 						INTEGER
						item.setLocationname(cursor.getString(1));			// locationname				VARCHAR
						item.setAddress(cursor.getString(2));				// address 					VARCHAR
						item.setCity(cursor.getString(3));					// city						VARCHAR
						item.setLocationstate(cursor.getString(4));			// locationstate			VARCHAR
						item.setZip(cursor.getString(5));					// zip						VARCHAR
						item.setTagline(cursor.getString(6));				// tagline					VARCHAR
						item.setLocationdescription(cursor.getString(7));	// locationdescription		VARCHAR
						item.setPhone(cursor.getString(8));					// phone					VARCHAR
						item.setWebsite(cursor.getString(9));				// website 					VARCHAR
						item.setDatestart(cursor.getString(10));			// datestart			 	DATETIME
						item.setDateend(cursor.getString(11)); 				// dateend					DATETIME
						item.setTimestart(cursor.getString(12));			// timestart		 		DATETIME
						item.setTimeend(cursor.getString(13));				// timeend		 			DATETIME
						item.setLocationtype(cursor.getString(14));			// locationtype 			VARCHAR
						item.setLocationtypegraphic(cursor.getInt(15));		// locationtypegraphic 		INTEGER
						item.setDisplaycustomericon(cursor.getInt(16));		// displaycustomericon		INTEGER
						item.setCustomericonURL(cursor.getString(17));		// customericonURL 			VARCHAR
						item.setAllowmoreinfo(cursor.getInt(18));			// allowmoreinfo 			INTEGER
						item.setDisplaysubview(cursor.getInt(19));			// displaysubview 			INTEGER
						item.setCallfromannotation(cursor.getInt(20));		// callfromannotation 		INTEGER
						item.setLatitude(cursor.getFloat(21));				// latitude 				FLOAT
						item.setLongitude(cursor.getFloat(22));				// longitude 				FLOAT
						item.setBuy(cursor.getInt(23));						// buy		 				INTEGER
						item.setBuyitems(cursor.getString(24));				// buyitems 				VARCHAR
						item.setGet(cursor.getString(25));					// get 						VARCHAR
											
						items.add(item);
					}
					while(cursor.moveToNext());
				}
				
			}
			
			cursor.close();
			dbConn.close();
		}
		
		return items;
	}
	
	/**
	 * Method to clear the local database
	 * 
	 * @param context		The Context object
	 */
	public static void clearDB(Context context) {
		ContentResolver cr = context.getContentResolver();
		cr.delete(BLocations.CONTENT_URI, null, null);
	}

}
