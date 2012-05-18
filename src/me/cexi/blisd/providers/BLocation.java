package me.cexi.blisd.providers;

import android.net.*;
import android.provider.*;

/**
 * Class for implementing Location structure
 * 
 * @author 		John Stack
 * @since		2011/10/31
 * @version		1.0
 *
 */
public class BLocation {

	private int id = -1;

	private String locationname = null;

	private String address = null;

	private String city = null;

	private String locationstate = null;

	private String zip = null;

	private String tagline = null;

	private String locationdescription = null;

	private String phone = null;

	private String website = null;

	private String datestart = null;

	private String dateend = null;

	private String timestart = null;

	private String timeend = null;

	private String locationtype = null;

	private int locationtypegraphic = -1;

	private int displaycustomericon = -1;

	private String customericonURL = null;

	private int allowmoreinfo = -1;

	private int displaysubview = -1;

	private int callfromannotation = -1;

	private float latitude = -1;

	private float longitude = -1;

	private int buy = -1;

	private String buyitems = null;

	private String get = null;

	public BLocation() {
		// TODO Auto-generated constructor stub
	}

	public BLocation(int id, String locationname, String address, String city,
			String locationstate, String zip, String tagline,
			String locationdescription, String phone, String website,
			String datestart, String dateend, String timestart, String timeend,
			String locationtype, int locationtypegraphic,
			int displaycustomericon, String customericonURL, int allowmoreinfo,
			int displaysubview, int callfromannotation, float latitude,
			float longitude, int buy, String buyitems, String get) {
		this.id = id;
		this.locationname = locationname;
		this.address = address;
		this.city = city;
		this.locationstate = locationstate;
		this.zip = zip;
		this.tagline = tagline;
		this.locationdescription = locationdescription;
		this.phone = phone;
		this.website = website;
		this.datestart = datestart;
		this.dateend = dateend;
		this.timestart = timestart;
		this.timeend = timeend;
		this.locationtype = locationtype;
		this.locationtypegraphic = locationtypegraphic;
		this.displaycustomericon = displaycustomericon;
		this.customericonURL = customericonURL;
		this.allowmoreinfo = allowmoreinfo;
		this.displaysubview = displaysubview;
		this.callfromannotation = callfromannotation;
		this.latitude = latitude;
		this.longitude = longitude;
		this.buy = buy;
		this.buyitems = buyitems;
		this.get = get;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocationname() {
		return locationname;
	}

	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocationstate() {
		return locationstate;
	}

	public void setLocationstate(String locationstate) {
		this.locationstate = locationstate;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public String getLocationdescription() {
		return locationdescription;
	}

	public void setLocationdescription(String locationdescription) {
		this.locationdescription = locationdescription;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDatestart() {
		return datestart;
	}

	public void setDatestart(String datestart) {
		this.datestart = datestart;
	}

	public String getDateend() {
		return dateend;
	}

	public void setDateend(String dateend) {
		this.dateend = dateend;
	}

	public String getTimestart() {
		return timestart;
	}

	public void setTimestart(String timestart) {
		this.timestart = timestart;
	}

	public String getTimeend() {
		return timeend;
	}

	public void setTimeend(String timeend) {
		this.timeend = timeend;
	}

	public String getLocationtype() {
		return locationtype;
	}

	public void setLocationtype(String locationtype) {
		this.locationtype = locationtype;
	}

	public int getLocationtypegraphic() {
		return locationtypegraphic;
	}

	public void setLocationtypegraphic(int locationtypegraphic) {
		this.locationtypegraphic = locationtypegraphic;
	}

	public int getDisplaycustomericon() {
		return displaycustomericon;
	}

	public void setDisplaycustomericon(int displaycustomericon) {
		this.displaycustomericon = displaycustomericon;
	}

	public String getCustomericonURL() {
		return customericonURL;
	}

	public void setCustomericonURL(String customericonURL) {
		this.customericonURL = customericonURL;
	}

	public int getAllowmoreinfo() {
		return allowmoreinfo;
	}

	public void setAllowmoreinfo(int allowmoreinfo) {
		this.allowmoreinfo = allowmoreinfo;
	}

	public int getDisplaysubview() {
		return displaysubview;
	}

	public void setDisplaysubview(int displaysubview) {
		this.displaysubview = displaysubview;
	}

	public int getCallfromannotation() {
		return callfromannotation;
	}

	public void setCallfromannotation(int callfromannotation) {
		this.callfromannotation = callfromannotation;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getBuy() {
		return buy;
	}

	public void setBuy(int buy) {
		this.buy = buy;
	}

	public String getBuyitems() {
		return buyitems;
	}

	public void setBuyitems(String buyitems) {
		this.buyitems = buyitems;
	}

	public String getGet() {
		return get;
	}

	public void setGet(String get) {
		this.get = get;
	}

	public static final class BLocations implements BaseColumns {
		public BLocations() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ BlisdContentProvider.AUTHORITY + "/locations");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.blisd.locations";

		public static final String ID = "id";

		public static final String LOCATIONNAME = "locationname";

		public static final String ADDRESS = "address";

		public static final String CITY = "city";

		public static final String LOCATIONSTATE = "locationstate";

		public static final String ZIP = "zip";

		public static final String TAGLINE = "tagline";

		public static final String LOCATIONDESCRIPTION = "locationdescription";

		public static final String PHONE = "phone";

		public static final String WEBSITE = "website";

		public static final String DATESTART = "datestart";

		public static final String DATEEND = "dateend";

		public static final String TIMESTART = "timestart";

		public static final String TIMEEND = "timeend";

		public static final String LOCATIONTYPE = "locationtype";

		public static final String LOCATIONTYPEGRAPHIC = "locationtypegraphic";

		public static final String DISPLAYCUSTOMERICON = "displaycustomericon";

		public static final String CUSTOMERICONURL = "customericonURL";

		public static final String ALLOWMOREINFO = "allowmoreinfo";

		public static final String DISPLAYSUBVIEW = "displaysubview";

		public static final String CALLFROMANNOTATION = "callfromannotation";

		public static final String LATITUDE = "latitude";

		public static final String LONGITUDE = "longitude";

		public static final String BUY = "buy";

		public static final String BUYITEMS = "buyitems";

		public static final String GET = "get";
	}

}
