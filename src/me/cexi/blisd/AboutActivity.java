package me.cexi.blisd;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

/**
 * Class for implementing About screen (Empty screen)
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class AboutActivity extends Activity {
	
	/**
	 * Member Variable for saving "Back" button object
	 */
	private Button backButton = null;
	
	/** 
	 * Method to call when the activity is first created. 
	 * 
	 **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBack();
			}
		});
	}

	/**
	 * Method to call when pressing "Back" button
	 * 
	 */
	protected void onBack() {
		finish();
	}

}
