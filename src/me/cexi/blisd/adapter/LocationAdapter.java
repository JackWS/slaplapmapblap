package me.cexi.blisd.adapter;

import java.util.*;

import me.cexi.blisd.*;
import me.cexi.blisd.providers.*;

import android.app.*;
import android.content.*;
import android.view.*;
import android.widget.*;

/**
 * Class for implementing Adapter for Location List
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class LocationAdapter extends ArrayAdapter<Object> {

	/**
	 * Member Variable for saving parent activity object
	 */
	private Activity parent = null;
	
	/**
	 * Member Variable for saving all items in ListView
	 */
	private ArrayList<Object> items = null;

	/**
	 * Member Variable for saving any item in ListView
	 */
	private Object item = null;

	/**
	 * Member Variable for saving Type TextView object 
	 * Only showing the Type (Attraction/Dining/Shopping etc
	 */
	private TextView typeTextView = null;

	/**
	 * Member Variable for saving Location TextView object
	 * Only showing the real Location
	 */
	private TextView locationTextView = null;

	/**
	 * Constructor 
	 * 
	 * @param parent				The parent Activity object
	 * @param context				The parent Context object
	 * @param textViewResourceId	The resource layout id
	 * @param items					The items in ListView
	 */
	public LocationAdapter(Activity parent, Context context, int textViewResourceId, ArrayList<Object> items) {
		super(context, textViewResourceId, items);
		this.parent = parent;
		this.items = items;
	}
	
	/**
	 * Method to show the special row in ListView (Group Table)
	 * 
	 * The Group table has the following 2 types:
	 * 1) Type 		(Attraction/Dining/Shopping etc)
	 * 2) Location	(The location per over type)
	 *	
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		item = items.get(position);
		
		if (view == null) {
			LayoutInflater li = (LayoutInflater)this.parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = li.inflate(R.layout.location_row, null);
		}
		
		typeTextView = (TextView) view.findViewById(R.id.typeTextView);
		
		locationTextView = (TextView) view.findViewById(R.id.locationTextView);
		
		if (item instanceof String) { 			// Only showing the Type
			typeTextView.setVisibility(View.VISIBLE);
			locationTextView.setVisibility(View.GONE);
			
			typeTextView.setText(item.toString());
		}
		else if (item instanceof BLocation) {	// Only showing the Location
			typeTextView.setVisibility(View.GONE);
			locationTextView.setVisibility(View.VISIBLE);
			
			locationTextView.setText(((BLocation) item).getLocationname());
		}
		else {
			typeTextView.setVisibility(View.GONE);
			locationTextView.setVisibility(View.GONE);
		}
		
		return view;
	}
	
	/**
	 * Method to replace original items into new items
	 * 
	 * @param items		The new items to replace
	 */
	public void setItems(ArrayList<Object> items) {
		this.items = items;
	}

}
