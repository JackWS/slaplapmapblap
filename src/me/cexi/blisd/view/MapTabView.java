package me.cexi.blisd.view;

import java.util.*;

import com.google.android.maps.*;

import me.cexi.blisd.*;
import me.cexi.blisd.common.*;
import me.cexi.blisd.map.*;
import me.cexi.blisd.providers.*;
import android.app.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

/**
 * Class for implementing "Map" tab view in Main screen
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class MapTabView extends LinearLayout {

	/**
	 * Member Variable for saving the Context object
	 */
	private Context context = null;
	
	/**
	 * Member Variable for saving the View object
	 */
	private View view = null;
	
	/**
	 * Member Variable for saving the ProgressDialog object
	 */
	private ProgressDialog progressDialog = null;

	/**
	 * Member Variable for saving the Main Screen object
	 */
	private MainActivity parent = null;

	/**
	 * Member Variable for saving MapView object
	 */
	private MapView mapView = null;
	
	/**
	 * Member Variable for saving the current location overlay on MapView
	 */
	private MyLocationOverlay myLocationOverlay = null;

	/**
	 * Member Variable for saving the overlay(=pin) list on MapView
	 */
	private List<Overlay> overlays = null;

	/**
	 * Member Variable for saving the "Dining" button in the bottom
	 */
	private Button dinningButton = null;

	/**
	 * Member Variable for saving the "Attraction" button in the bottom
	 */
	private Button attractionsButton = null;

	/**
	 * Member Variable for saving the "Shopping" button in the bottom
	 */
	private Button shoppingButton = null;

	/**
	 * Member Variable for saving the "ListView" button in the bottom
	 */
	private Button listviewButton = null;

	/**
	 * Member Variable for saving the "About" button in the bottom
	 */
	private Button aboutButton = null;

	/**
	 * Constructor for showing "Map" tab view
	 * 
	 * @param context
	 * @param attrs
	 */
	public MapTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;

		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.map_tab_view, this);
		
		/** Map View */
		mapView = (MapView) view.findViewById(R.id.mapView);
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
		
		/** Dinning button */
		dinningButton = (Button) view.findViewById(R.id.dinningButton);
		dinningButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onDinning();
			}
		});
		
		/** Attractions button */
		attractionsButton = (Button) view.findViewById(R.id.attractionsButton);
		attractionsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onAttractions();
			}
		});
		
		/** Shopping button */
		shoppingButton = (Button) view.findViewById(R.id.shoppingButton);
		shoppingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onShopping();
			}
		});
		
		/** ListView button */
		listviewButton = (Button) view.findViewById(R.id.listviewButton);
		listviewButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onListView();
			}
		});
		
		/** About button */
		aboutButton = (Button) view.findViewById(R.id.aboutButton);
		aboutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onAbout();
			}
		});

	}

	/**
	 * Method to show "Map" tab view
	 * 
	 * @param parent
	 */
	public void show(MainActivity parent) {
		if (this.parent == null) {
			this.parent = parent;
			init();
		}
		
		this.setVisibility(View.VISIBLE);
	}

	/**
	 * Method to hide "Map" tab view
	 */
	public void hide() {
		this.setVisibility(View.GONE);
	}

	/**
	 * Method to initialize "Map" tab view
	 */
	private void init() {
		search(Constants.TYPE_ALL);
	}

	/**
	 * Method to show overlays(=pins) per type 
	 * 
	 * @param type			The type (Dining/Attraction/Shopping)
	 */
	private void search(final String type) {
		progressDialog = ProgressDialog.show(context, "", getResources().getString(R.string.Please_waiting_), true);
		progressDialog.setCancelable(true);
		
		new Thread() {
			public void run() {				
				Utilities.search(context, type);
				overlays = mapView.getOverlays();
				
				parent.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (progressDialog != null && progressDialog.isShowing()) {
							
							// Map Code
							for (Overlay overlay : overlays) {
								if (overlay instanceof BalloonItemizedOverlay<?>) {
									((BalloonItemizedOverlay<?>) overlay).hideBalloon();
								}
							}
							overlays.clear();
							
							/** add the MyLocationOverlay on map view */
							myLocationOverlay = new MyLocationOverlay(MapTabView.this.getContext(), mapView);
							myLocationOverlay.enableMyLocation();
							overlays.add(myLocationOverlay);
							
							BLocation location = null;
							OverlayItem overlayItem = null;
							
							String type = null;
							int graphic = -1;
							
							/** add the target location overlay */
							MyItemizedOverlay diningItemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.pin_red), mapView);
							MyItemizedOverlay diningItemizedOverlay1 = new MyItemizedOverlay(getResources().getDrawable(R.drawable.dining), mapView);
							MyItemizedOverlay attractionItemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.pin_green), mapView);
							MyItemizedOverlay attractionItemizedOverlay1 = new MyItemizedOverlay(getResources().getDrawable(R.drawable.events), mapView);
							MyItemizedOverlay shoppingItemizedOverlay = new MyItemizedOverlay(getResources().getDrawable(R.drawable.pin_pink), mapView);
							MyItemizedOverlay shoppingItemizedOverlay1 = new MyItemizedOverlay(getResources().getDrawable(R.drawable.shopping), mapView);
							
							for (int index = 0;index < Constants.locations.size();index++) {
								location = Constants.locations.get(index);
								overlayItem = new OverlayItem(new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6)), location.getLocationname(), String.valueOf(index));
								type = location.getLocationtype();
								graphic = location.getLocationtypegraphic();
								if (type.equals(Constants.TYPE_DINING)) {
									if (graphic == 0) {
										diningItemizedOverlay.addOverlay(overlayItem);
									}
									else {
										diningItemizedOverlay1.addOverlay(overlayItem);
									}
								}
								else if (type.equals(Constants.TYPE_ATTRACTION)) {
									if (graphic == 0) {
										attractionItemizedOverlay.addOverlay(overlayItem);
									}
									else {
										attractionItemizedOverlay1.addOverlay(overlayItem);
									}
								}
								else if (type.equals(Constants.TYPE_SHOPPING)) {
									if (graphic == 0) {
										shoppingItemizedOverlay.addOverlay(overlayItem);
									}
									else {
										shoppingItemizedOverlay1.addOverlay(overlayItem);
									}
								}
							}

							if (diningItemizedOverlay.size() > 0) {
								overlays.add(diningItemizedOverlay);
							}
							if (diningItemizedOverlay1.size() > 0) {
								overlays.add(diningItemizedOverlay1);
							}
							if (shoppingItemizedOverlay.size() > 0) {
								overlays.add(shoppingItemizedOverlay);
							}
							if (shoppingItemizedOverlay1.size() > 0) {
								overlays.add(shoppingItemizedOverlay1);
							}
							if (attractionItemizedOverlay.size() > 0) {
								overlays.add(attractionItemizedOverlay);							
							}
							if (attractionItemizedOverlay1.size() > 0) {
								overlays.add(attractionItemizedOverlay1);							
							}
							
							if (parent.getLocation() != null) {
								mapView.getController().setCenter(new GeoPoint((int) (parent.getLocation().getLatitude() * 1E6), (int) (parent.getLocation().getLongitude() * 1E6)));
							}
							
							mapView.invalidate();
							
							progressDialog.dismiss();
						}
					}
				});
			}
		}.start();
	}

	/**
	 * Method to call when pressing "Dining" button in the bottom
	 */
	protected void onDinning() {
		search(Constants.TYPE_DINING);
	}

	/**
	 * Method to call when pressing "Attractions" button in the bottom
	 */
	protected void onAttractions() {
		search(Constants.TYPE_ATTRACTION);
	}

	/**
	 * Method to call when pressing "Shopping" button in the bottom
	 */
	protected void onShopping() {
		search(Constants.TYPE_SHOPPING);
	}

	/**
	 * Method to call when pressing "ListView" button in the bottom
	 */
	protected void onListView() {
		Intent intent = new Intent(context, LocationListActivity.class);
		context.startActivity(intent);
	}

	/**
	 * Method to call when pressing "About" button in the bottom
	 */
	protected void onAbout() {
		Intent intent = new Intent(context, AboutActivity.class);
		context.startActivity(intent);
	}

	/**
	 * Method to center the current location
	 */
	public void centerLocation() {
		if (parent.getLocation() != null) {
			mapView.getController().setCenter(new GeoPoint((int) (parent.getLocation().getLatitude() * 1E6), (int) (parent.getLocation().getLongitude() * 1E6)));
		}
	}

}
