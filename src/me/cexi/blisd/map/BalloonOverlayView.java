/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package me.cexi.blisd.map;

import me.cexi.blisd.LocationDetailActivity;
import me.cexi.blisd.MainActivity;
import me.cexi.blisd.R;
import me.cexi.blisd.common.Constants;
import me.cexi.blisd.imagerepository.ImageRepository;
import android.content.*;
import android.graphics.Bitmap;
import android.net.*;
import android.view.*;
import android.widget.*;

import com.google.android.maps.*;

/**
 * A view representing a MapView marker information balloon.
 * <p>
 * This class has a number of Android resource dependencies:
 * <ul>
 * <li>drawable/balloon_overlay_bg_selector.xml</li>
 * <li>drawable/balloon_overlay_close.png</li>
 * <li>drawable/balloon_overlay_focused.9.png</li>
 * <li>drawable/balloon_overlay_unfocused.9.png</li>
 * <li>layout/balloon_map_overlay.xml</li>
 * </ul>
 * </p>
 * 
 * @author Jeff Gilfelt
 * 
 */
public class BalloonOverlayView<Item extends OverlayItem> extends FrameLayout {

	private Context context = null;

	private LinearLayout layout = null;
	
	private ImageView customImageView = null;
	
	private Button callButton = null;

	private TextView titleTextView = null;

	private TextView subtitleTextView = null;
	
	private Button infoButton = null;

	private int index = -1;

	/**
	 * Create a new BalloonOverlayView.
	 * 
	 * @param context - The activity context.
	 * @param balloonBottomOffset - The bottom padding (in pixels) to be applied when rendering
	 *            this view.
	 */
	public BalloonOverlayView(final Context context, int balloonBottomOffset) {

		super(context);
		
		this.context = context;

		setPadding(10, 0, 10, balloonBottomOffset);
		layout = new LinearLayout(context);
		layout.setVisibility(VISIBLE);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.balloon_overlay, layout);
		
		customImageView = (ImageView) view.findViewById(R.id.customImageView);
		
		callButton = (Button) view.findViewById(R.id.callButton);
		callButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onCall();
			}
		});

		titleTextView = (TextView) view.findViewById(R.id.titleTextView);

		subtitleTextView = (TextView) view.findViewById(R.id.subtitleTextView);
		
		infoButton = (Button) view.findViewById(R.id.infoButton);
		infoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onInfo();
			}
		});
		
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.NO_GRAVITY;

		addView(layout, params);

	}

	/**
	 * Sets the view data from a given overlay item.
	 * 
	 * @param item - The overlay item containing the relevant view data (title and snippet).
	 */
	public void setData(Item item) {
		layout.setVisibility(VISIBLE);
		titleTextView.setText(item.getTitle());
		
		try {
			index = Integer.parseInt(item.getSnippet()); // Index
		} catch (NumberFormatException e) {
			subtitleTextView.setText(item.getSnippet());
			infoButton.setVisibility(View.GONE);
			callButton.setVisibility(View.GONE);
			customImageView.setVisibility(View.GONE);
			return;
		}

		subtitleTextView.setText(Constants.locations.get(index).getTagline());
		
		if (Constants.locations.get(index).getAllowmoreinfo() == 1) {
			infoButton.setVisibility(View.VISIBLE);
		}
		else {
			infoButton.setVisibility(View.GONE);
		}

		if (Constants.locations.get(index).getCallfromannotation() == 1) {
			callButton.setVisibility(View.VISIBLE);
		}
		else {
			callButton.setVisibility(View.GONE);
		}
		if (Constants.locations.get(index).getCustomericonURL().startsWith("http://")) {
			customImageView.setVisibility(View.VISIBLE);
			showCutomIcon();
		}
		else {
			customImageView.setVisibility(View.GONE);
		}
	}

	/**
	 * Method to show the custom image from the remote image url
	 * 
	 */
	private void showCutomIcon() {
		new Thread() {
			@Override
			public void run() {
				final Bitmap bm = ImageRepository.getInstance().getRemoteImage(Constants.locations.get(index).getCustomericonURL());
				
				MainActivity.getInstance().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (bm != null) {
							customImageView.setImageBitmap(bm);
						}
					}
				});
			}
		}.start();
	}

	/**
	 * Method to call with the telephone number 
	 */
	protected void onCall() {
		if (!Constants.locations.get(index).getPhone().equals("")) {
			Intent intent = new Intent(Intent.ACTION_CALL);
			intent.setData(Uri.parse("tel:" + Constants.locations.get(index).getPhone()));
			context.startActivity(intent);
		}
	}

	/**
	 * Method to show the location detail screen
	 * 
	 */
	protected void onInfo() {
		Constants.selectedLocation = Constants.locations.get(index);
		Intent intent = new Intent(context, LocationDetailActivity.class);
		intent.putExtra("Back", "Map");
		context.startActivity(intent);
	}

}
