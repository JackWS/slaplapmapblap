package me.cexi.blisd;

import java.util.*;

import me.cexi.blisd.adapter.*;
import me.cexi.blisd.common.*;
import me.cexi.blisd.providers.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;

/**
 * Class for implementing Location List screen
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class LocationListActivity extends Activity {
	
	/**
	 * Member Variable for saving "Back" button object
	 */
	private Button backButton = null;
	
	/**
	 * Member Variable for saving Location ListView object
	 */
	private ListView listView = null;
	
	/**
	 * Member Variable for saving items in Location ListView
	 */
	private ArrayList<Object> items = null;

	/**
	 * Member Variable for saving Adapter object for Location ListView
	 */
	private LocationAdapter adapter = null;

	/**
	 * Member Variable for saving ProgressDialog object
	 */
	private ProgressDialog progressDialog = null;

	/** 
	 * Method to call when the activity is first created. 
	 * 
	 **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_list);
		
		/** Show "Back" button */
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBack();
			}
		});
		
		/** Show Location ListView */
		listView = (ListView) findViewById(R.id.listView);
		items = new ArrayList<Object>();
		adapter = new LocationAdapter(this, getApplicationContext(), R.layout.location_row, items);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				if (items.get(position) instanceof BLocation) {
					Constants.selectedLocation = (BLocation) items.get(position);
					Intent intent = new Intent(LocationListActivity.this, LocationDetailActivity.class);
					intent.putExtra("Back", "Back");
					startActivity(intent);
				}
			}
		});
		
		init();
	}

	/**
	 * Method to initialize the Location List screen
	 * 1) Show all location  items from local database
	 * 2) Show Group table per type(Attraction/Dining/Shopping etc)
	 */
	private void init() {
		progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.Please_waiting_), true);
		progressDialog.setCancelable(true);
		
		new Thread() {
			public void run() {				
				ArrayList<Object > newItems = Utilities.getLocationList(getApplicationContext());
				for (int i = 0;i < newItems.size();i++) {
					items.add(newItems.get(i));
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (progressDialog != null && progressDialog.isShowing()) {
							adapter.setItems(items);
							adapter.notifyDataSetChanged();
							progressDialog.dismiss();
						}
					}
				});
			}
		}.start();
	}

	/**
	 * Method to call when pressing "Back" button
	 */
	protected void onBack() {
		finish();
	}

}
