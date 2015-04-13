package com.bignerdranch.android.criminalintent;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class CrimeActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if (null == fragment) {
			fragment = new CrimeFragment();
			fm.beginTransaction().add(R.id.fragmentContainer, fragment)
				.commit();
		}
		
    }
}
