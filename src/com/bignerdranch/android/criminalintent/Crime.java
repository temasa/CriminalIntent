package com.bignerdranch.android.criminalintent;


import java.util.UUID;
import java.util.Date;
import org.json.JSONObject;
import org.json.JSONException;


import org.json.*;public class Crime
{
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO = "photo";
	
	private UUID mId;
	private String mTitle;
	private Date mDate;
	private boolean mSolved;
	private Photo mPhoto;
	
	public Crime()
	{
		mId = UUID.randomUUID();
		mDate = new Date();
	}
	
	public Crime(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_DATE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		if (json.has(JSON_PHOTO)) {
			mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
		}
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		if (null != mPhoto) {
			json.put(JSON_PHOTO, mPhoto.toJSON());
		}
		
		return json;
	}
	
	public UUID getId()
	{
		return mId;
	}
	
	public String getTitle()
	{
		return mTitle;
	}
	public void setTitle(String title)
	{
		mTitle = title;
	}
	
	public Date getDate() {
		return mDate;
	}
	public void setDate(Date date) {
		mDate = date;
	}
	
	public Photo getPhoto() {
		return mPhoto;
	}
	public void setPhoto(Photo photo) {
		mPhoto = photo;
	}
	
	public boolean isSolved() {
		return mSolved;
	}
	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	@Override
	public String toString()
	{
		return mTitle;
	}
	
}
