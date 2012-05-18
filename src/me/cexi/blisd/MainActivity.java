package me.cexi.blisd;


import me.cexi.blisd.common.*;
import me.cexi.blisd.communication.*;
import me.cexi.blisd.view.*;

import com.google.android.maps.*;

import android.app.*;
import android.content.*;
import android.location.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

/**
 * Class for implementing Main screen
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class MainActivity extends MapActivity implements LocationListener {
	
	/**
	 * Member Variable for saving "Other Window" tab button object
	 */
	private Button otherButton = null;
	
	/**
	 * Member Variable for saving "Other Window" tab content object
	 */
	private OtherWindowTabView otherWindowTabView = null;

	/**
	 * Member Variable for saving "Map" tab button object
	 */
	private Button mapButton = null;

	/**
	 * Member Variable for saving "Map" tab content object
	 */
	private MapTabView mapTabView = null;

	/**
	 * Member Variable for saving ProgressDialog object
	 */
	private ProgressDialog progressDialog = null;

	/**
	 * Member Variable for saving LocationManager object
	 */
	private LocationManager locationManager = null;
	
	/**
	 * Member Variable for saving Location object
	 */
	private Location location = null;
	
	/**
	 * Member Variable for saving Main screen object (Reference Singleton Pattern)
	 */
	private static MainActivity instance = null;

	/**
	 * Method to return the Object for current Main screen
	 * 
	 * @return	Object for Main screen
	 */
	public static MainActivity getInstance() {
		return instance;
	}

	/** 
	 * Method to call when the activity is first created. 
	 * 
	 **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        instance = this;
        
        /** Other Window */
        otherButton = (Button) findViewById(R.id.otherButton);
        otherButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onOtherWindowTab();
			}
		});
        
        otherWindowTabView = (OtherWindowTabView) findViewById(R.id.otherWindowTabView);
        
        /** Map */
        mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onMapTab();
			}
		});
        
        mapTabView = (MapTabView) findViewById(R.id.mapTabView);
        
        /** Get the current location in start-up */
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this );
        location  = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        
        init();
    }

    /**
     * Method to initialize on Main screen
     * 
     */
	private void init() {
		SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		long last_update = settings.getLong("last_update", 0);
		int readFrequence = Integer.parseInt(getResources().getString(R.string.ReadFrequency));
		
		switch (readFrequence) {
		case 0: // EveryTime the app is loaded.
			update();
			break;
		case 1: // Once a Week(Since Last Use)
			if (last_update == 0 || (System.currentTimeMillis() - last_update) > (3600000 * 24 * 7)) {
				update();
			}
			break;
		case 2: // Once A Month
			if (last_update == 0 || (System.currentTimeMillis() - last_update) > (3600000 * 24 * 30)) {
				update();
			}
			break;

		default:
			break;
		}
		
	}

	/**
	 * Method to update location information( with JSON format) from Web API
	 * 
	 */
	private void update() {
		
		/**
		 * Clear the original database
		 */
		Utilities.clearDB(this);
		
		progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.Please_waiting_), true);
		progressDialog.setCancelable(true);
		
		new Thread() {
			public void run() {
				
				/**
				 * Download the location information from Web API and insert into local database
				 */
				GetLocations service = new GetLocations(getApplicationContext());
				service.request(getResources().getString(R.string.JSONFeedURL));
				
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}
				});
			}
		}.start();
	}

	/**
	 * Method to show "Other Window" tab when pressing "Other Window" tab button
	 * 
	 */
	private void onOtherWindowTab() {
		otherButton.setBackgroundResource(R.drawable.tab_other_pressed);
		otherButton.setTextColor(getResources().getColor(R.color.WHITE_TEXTCOLOR));
		mapButton.setBackgroundResource(R.drawable.tab_map_normal);
		mapButton.setTextColor(getResources().getColor(R.color.GRAY_TEXTCOLOR));
		
		otherWindowTabView.setVisibility(View.VISIBLE);
		mapTabView.hide();
	}
	
	/**
	 * Method to show "Map" tab when pressing "Map" tab button
	 * 
	 */
	private void onMapTab() {
		otherButton.setBackgroundResource(R.drawable.tab_other_normal);
		otherButton.setTextColor(getResources().getColor(R.color.GRAY_TEXTCOLOR));
		mapButton.setBackgroundResource(R.drawable.tab_map_pressed);
		mapButton.setTextColor(getResources().getColor(R.color.WHITE_TEXTCOLOR));
		
		otherWindowTabView.setVisibility(View.GONE);
		mapTabView.show(this);
	}
    
	/**
	 * Method to get the current location 
	 * 
	 * @return	the location object
	 */
    public Location getLocation() {
		return location;
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Method to change the location object when moving the current location
	 * 
	 */
	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		mapTabView.centerLocation();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}