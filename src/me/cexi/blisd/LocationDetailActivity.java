package me.cexi.blisd;

import java.util.*;

import me.cexi.blisd.common.*;
import me.cexi.blisd.map.*;
import me.cexi.blisd.providers.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.google.android.maps.*;

/**
 * Class for implementing Location Detail screen
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class LocationDetailActivity extends MapActivity {
	
	/**
	 * Member Variable for saving "Back" button object
	 */
	private Button backButton = null;
	
	/**
	 * Member Variable for saving MapView object
	 */
	private MapView mapView = null;

	/**
	 * Member Variable for saving Location Name
	 */
	private TextView nameTextView = null;

	/**
	 * Member Variable for saving Location Description
	 */
	private TextView descriptionTextView = null;

	/**
	 * Member Variable for saving Address
	 */
	private TextView addressTextView = null;

	/**
	 * Member Variable for saving Phone Number
	 */
	private TextView phoneTextView = null;

	/**
	 * Member Variable for saving "Get Direction" button object
	 */
	private Button directionButton = null;

	/**
	 * Member Variable for saving list of pins on MapView
	 */
	private List<Overlay> overlays = null;

	/** 
	 * Method to call when the activity is first created. 
	 * 
	 **/
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.location_detail);
		
		Bundle bundle = getIntent().getExtras();
		
		/** Back button */
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setText(bundle.getString("Back"));
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBack();
			}
		});
		
		/** MapView */
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.getController().setZoom(16);
		mapView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					for (Overlay overlay : overlays) {
						if (overlay instanceof BalloonItemizedOverlay<?>) {
							((BalloonItemizedOverlay<?>) overlay).hideBalloon();
						}
					}
				}
				return false;
			}
		});
		
		/**
		 * Overlays(=pins) on MapView
		 */
		overlays = mapView.getOverlays();

		// Map Code
		for (Overlay overlay : overlays) {
			if (overlay instanceof BalloonItemizedOverlay<?>) {
				((BalloonItemizedOverlay<?>) overlay).hideBalloon();
			}
		}
		overlays.clear();		
		BLocation location = null;
		OverlayItem overlayItem = null;
		
		String type = null;
		int graphic = -1;
		
		/**
		 *  add the target location overlay 
		 * */
		/** Default pin for Dining */
		MyItemizedOverlay diningItemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.pin_red), mapView);
		/** Custom pin for Dining */
		MyItemizedOverlay diningItemizedOverlay1 = new MyItemizedOverlay(getResources().getDrawable(R.drawable.dining), mapView);
		/** Default pin for Attraction */
		MyItemizedOverlay attractionItemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.pin_green), mapView);
		/** Custom pin for Attraction */
		MyItemizedOverlay attractionItemizedOverlay1 = new MyItemizedOverlay(getResources().getDrawable(R.drawable.events), mapView);
		/** Default pin for Shopping */
		MyItemizedOverlay shoppingItemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.pin_pink), mapView);
		/** Custom pin for Shopping */
		MyItemizedOverlay shoppingItemizedOverlay1 = new MyItemizedOverlay(getResources().getDrawable(R.drawable.shopping), mapView);
		
		/**
		 * Determine which type(=pin) the selected location is 
		 */
		location = Constants.selectedLocation;
		overlayItem = new OverlayItem(new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6)), location.getLocationname(), location.getTagline());
		type = location.getLocationtype();
		graphic = location.getLocationtypegraphic();
		if (type.equals(Constants.TYPE_DINING)) {	// IF Type = Dining THEN
			if (graphic == 0) {	// Default Dining pin
				diningItemizedOverlay.addOverlay(overlayItem);
			}
			else {				// Custom Dining pin
				diningItemizedOverlay1.addOverlay(overlayItem);
			}
		}
		else if (type.equals(Constants.TYPE_ATTRACTION)) {	// IF Type = Attraction THEN
			if (graphic == 0) {	// Default Attraction pin
				attractionItemizedOverlay.addOverlay(overlayItem);
			}
			else {				// Custom Attraction pin
				attractionItemizedOverlay1.addOverlay(overlayItem);
			}
		}
		else if (type.equals(Constants.TYPE_SHOPPING)) {	// IF Type = Shopping THEN
			if (graphic == 0) {	// Default Shopping pin
				shoppingItemizedOverlay.addOverlay(overlayItem);
			}
			else {				// Custom Shopping pin
				shoppingItemizedOverlay1.addOverlay(overlayItem);
			}
		}
		
		/**
		 * Add a pin(Dining/Attraction/Shopping) into pin list
		 */
		if (diningItemizedOverlay.size() > 0) {
			overlays.add(diningItemizedOverlay);
		}
		else if (diningItemizedOverlay1.size() > 0) {
			overlays.add(diningItemizedOverlay1);
		}
		else if (shoppingItemizedOverlay.size() > 0) {
			overlays.add(shoppingItemizedOverlay);
		}
		else if (shoppingItemizedOverlay1.size() > 0) {
			overlays.add(shoppingItemizedOverlay1);
		}
		else if (attractionItemizedOverlay.size() > 0) {
			overlays.add(attractionItemizedOverlay);							
		}
		if (attractionItemizedOverlay1.size() > 0) {
			overlays.add(attractionItemizedOverlay1);							
		}
		
		/** Center the current location */
		if (location != null) {
			mapView.getController().setCenter(new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6)));
		}
		
		/** Redraw Map View */
		mapView.invalidate();
		
		/** Show the location name */
		nameTextView = (TextView) findViewById(R.id.nameTextView);
		nameTextView.setText(Constants.selectedLocation.getLocationname());
		
		/** Show the location description */
		descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		descriptionTextView.setText(Constants.selectedLocation.getLocationdescription());
		
		/** Show the address */
		addressTextView = (TextView) findViewById(R.id.addressTextView);
		addressTextView.setText(Constants.selectedLocation.getAddress());
		
		/** Show the phone number */
		phoneTextView = (TextView) findViewById(R.id.phoneTextView);
		phoneTextView.setText(Constants.selectedLocation.getPhone());
		
		/** Show the "Get Direction" button */
		directionButton = (Button) findViewById(R.id.directionButton);
		directionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDirection();
			}
		});
		
	}

	/**
	 * Method to call when pressing "Back" button
	 */
	protected void onBack() {
		finish();
	}

	/**
	 * Method to call when pressing "Get Direction" button
	 * Show the direction from the current location to the destination location
	 * 
	 */
	protected void onDirection() {
		String uri = String.format("http://maps.google.com/maps?saddr=%f,%f&daddr=%s", 
				MainActivity.getInstance().getLocation().getLatitude(),
				MainActivity.getInstance().getLocation().getLongitude(), 
				addressTextView.getText().toString());
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		startActivity(intent);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
