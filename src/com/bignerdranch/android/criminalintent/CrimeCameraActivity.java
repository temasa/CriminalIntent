package com.bignerdranch.android.criminalintent;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public Fragment createFragment()
	{
		return new CrimeCameraFragment();
	}
}
