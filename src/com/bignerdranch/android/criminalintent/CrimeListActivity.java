package com.bignerdranch.android.criminalintent;


//import android.app.Fragment;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity
{
	@Override
	protected Fragment createFragment() {
		return new CrimeListFragment();
	}
}
