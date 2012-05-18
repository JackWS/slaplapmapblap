package me.cexi.blisd.view;

import me.cexi.blisd.*;
import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

/**
 * Class for implementing "Other Window" tab view in Main screen
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class OtherWindowTabView extends LinearLayout {
	
	/**
	 * Member Variable for saving the View object
	 */
	private View view = null;

	/**
	 * Constructor for showing "Other Window" tab view
	 * 
	 * @param context
	 * @param attrs
	 */
	public OtherWindowTabView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = layoutInflater.inflate(R.layout.other_tab_view, this);

	}

}
