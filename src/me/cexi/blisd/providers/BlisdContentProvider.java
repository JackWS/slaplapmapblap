package me.cexi.blisd.providers;

import java.util.*;

import me.cexi.blisd.providers.BLocation.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.net.*;
import android.util.*;

/**
 * Class for implementing the local database for the app
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class BlisdContentProvider extends ContentProvider {

	public static final String AUTHORITY = "me.cexi.blisd.providers.BlisdContentProvider";
	
	private static final String DATABASE_NAME = "blisd.db";

	private static final int DATABASE_VERSION = 1;

	private DatabaseHelper dbHelper = null;
	
	private static final UriMatcher sUriMatcher;

	private static final String TAG = "BlisdContentProvider";
	
	/**
	 * LOCATIONS Table
	 */
	private static final String CREATE_TABLE_LOCATIONS_SQL = "CREATE TABLE locations (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , locationname VARCHAR, address VARCHAR, city VARCHAR, locationstate VARCHAR, zip VARCHAR, tagline VARCHAR, locationdescription VARCHAR, phone VARCHAR, website VARCHAR, datestart DATETIME, dateend DATETIME, timestart DATETIME, timeend DATETIME, locationtype VARCHAR, locationtypegraphic INTEGER, displaycustomericon INTEGER, customericonURL VARCHAR, allowmoreinfo INTEGER, displaysubview INTEGER, callfromannotation INTEGER, latitude FLOAT, longitude FLOAT, buy INTEGER, buyitems VARCHAR, get VARCHAR);";

	private static final String LOCATIONS_TABLE_NAME = "locations";

	private static final int LOCATIONS = 1;

	private static HashMap<String, String> locationsProjectionMap;
	
	
	public static class DatabaseHelper extends SQLiteOpenHelper {
		
		private SQLiteDatabase mDatabase = null;

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_LOCATIONS_SQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
			onCreate(db);
		}
		
		public boolean openDatabase() {
			try {
				mDatabase = SQLiteDatabase.openDatabase("/data/data/me.cexi.blisd/databases/blisd.db", null, SQLiteDatabase.OPEN_READONLY);
			} catch (Exception e) {
				Log.e("DatabaseAccess", e.getLocalizedMessage());
				return false;
			}
			if(mDatabase == null)
				return false;

			return true;
		}
		
		public Cursor query(String query) {			
			try {
				return mDatabase.rawQuery(query, null);
			}catch (android.database.sqlite.SQLiteDatabaseCorruptException e) {
				return null;
			}catch (SQLiteException exception) {
				Log.e("DatabaseAccess", exception.getLocalizedMessage());
				return null;
			}
		}
		
		@Override
		public synchronized void close() {

			if (mDatabase != null)
				mDatabase.close();

			super.close();

		}
		
		public boolean isOpen() {
			if(mDatabase == null)
				return false;
			
			if(mDatabase.isOpen())
				return true;
			
			return false;
		}

	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
			case LOCATIONS:
				count = db.delete(LOCATIONS_TABLE_NAME, where, whereArgs);
				break;
			
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case LOCATIONS:
			return BLocations.CONTENT_TYPE;
		
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {

		switch (sUriMatcher.match(uri)) {
		case LOCATIONS:
			return insert(LOCATIONS_TABLE_NAME, BLocations.CONTENT_URI, initialValues);
		
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

	}

	private Uri insert(String tableName, Uri contentUri, ContentValues initialValues) {
		
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(tableName, null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(contentUri, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		else {
			Log.e(TAG, "Failed to insert row into " + contentUri);
			//throw new SQLException("Failed to insert row into " + contentUri);
			return null;
		}
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
			case LOCATIONS:
				qb.setTables(LOCATIONS_TABLE_NAME);
				qb.setProjectionMap(locationsProjectionMap);
				break;
	
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case LOCATIONS:
			count = db.update(LOCATIONS_TABLE_NAME, values, where, whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		/** Locations table */
		sUriMatcher.addURI(AUTHORITY, LOCATIONS_TABLE_NAME, LOCATIONS);

		locationsProjectionMap = new HashMap<String, String>();
		locationsProjectionMap.put(BLocations.ID, BLocations.ID);
		locationsProjectionMap.put(BLocations.LOCATIONNAME, BLocations.LOCATIONNAME);
		locationsProjectionMap.put(BLocations.ADDRESS, BLocations.ADDRESS);
		locationsProjectionMap.put(BLocations.CITY, BLocations.CITY);
		locationsProjectionMap.put(BLocations.LOCATIONSTATE, BLocations.LOCATIONSTATE);
		locationsProjectionMap.put(BLocations.ZIP, BLocations.ZIP);
		locationsProjectionMap.put(BLocations.TAGLINE, BLocations.TAGLINE);
		locationsProjectionMap.put(BLocations.LOCATIONDESCRIPTION, BLocations.LOCATIONDESCRIPTION);
		locationsProjectionMap.put(BLocations.PHONE, BLocations.PHONE);
		locationsProjectionMap.put(BLocations.WEBSITE, BLocations.WEBSITE);
		locationsProjectionMap.put(BLocations.DATESTART, BLocations.DATESTART);
		locationsProjectionMap.put(BLocations.DATEEND, BLocations.DATEEND);
		locationsProjectionMap.put(BLocations.TIMESTART, BLocations.TIMESTART);
		locationsProjectionMap.put(BLocations.TIMEEND, BLocations.TIMEEND);
		locationsProjectionMap.put(BLocations.LOCATIONTYPE, BLocations.LOCATIONTYPE);
		locationsProjectionMap.put(BLocations.LOCATIONTYPEGRAPHIC, BLocations.LOCATIONTYPEGRAPHIC);
		locationsProjectionMap.put(BLocations.DISPLAYCUSTOMERICON, BLocations.DISPLAYCUSTOMERICON);
		locationsProjectionMap.put(BLocations.CUSTOMERICONURL, BLocations.CUSTOMERICONURL);
		locationsProjectionMap.put(BLocations.ALLOWMOREINFO, BLocations.ALLOWMOREINFO);
		locationsProjectionMap.put(BLocations.DISPLAYSUBVIEW, BLocations.DISPLAYSUBVIEW);
		locationsProjectionMap.put(BLocations.CALLFROMANNOTATION, BLocations.CALLFROMANNOTATION);
		locationsProjectionMap.put(BLocations.LATITUDE, BLocations.LATITUDE);
		locationsProjectionMap.put(BLocations.LONGITUDE, BLocations.LONGITUDE);
		locationsProjectionMap.put(BLocations.BUY, BLocations.BUY);
		locationsProjectionMap.put(BLocations.BUYITEMS, BLocations.BUYITEMS);
		locationsProjectionMap.put(BLocations.GET, BLocations.GET);
		
	}

}
