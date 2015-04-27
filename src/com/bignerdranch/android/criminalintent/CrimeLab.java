package com.bignerdranch.android.criminalintent;


import java.util.UUID;
import java.util.ArrayList;
import android.content.Context;

public class CrimeLab
{
	private static final String FILENAME = "crimes.json";
	
	private ArrayList<Crime> mCrimes;
	private CriminalIntentJSONSerializer mSerializer;
	
	private static CrimeLab sCrimeLab;
	private Context mAppContext;
	
	private CrimeLab(Context appContext) {
		mAppContext = appContext;
		//mCrimes = new ArrayList<Crime>();
		
		mSerializer = new CriminalIntentJSONSerializer(appContext, FILENAME);
		try {
			mCrimes = mSerializer.loadCrimes();
		}
		catch (Exception e) {
			mCrimes = new ArrayList<Crime>();
			Logger.e(this.getClass(), "CrimeLab.ctor", e.toString());
		}
	}
	
	public static CrimeLab get(Context c) {
		if (null == sCrimeLab) {
			sCrimeLab = new CrimeLab(c.getApplicationContext());
		}
		
		return sCrimeLab;
	}
	
	public void addCrime(Crime c) {
		mCrimes.add(c);
	}
	
	public boolean saveCrimes() {
		try {
			mSerializer.saveCrimes(mCrimes);
			Logger.d(this.getClass(), "saveCrimes", "crimes saved to file.");
		}
		catch (Exception e) {
			Logger.e(this.getClass(), "SaveCrime", e.toString());
			return false;
		}
		
		return true;
	}
	
	public ArrayList<Crime> getCrimes() {
		return mCrimes;
	}
	
	public Crime getCrime(UUID id) {
		for (Crime c : mCrimes) {
			if (c.getId().equals(id)) {
				return c;
			}
		}
		
		return null;
	}
	
}
