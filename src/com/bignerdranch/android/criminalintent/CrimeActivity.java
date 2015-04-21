package com.bignerdranch.android.criminalintent;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class CrimeActivity extends SingleFragmentActivity
{
	@Override
	protected Fragment createFragment()
	{
		return new CrimeFragment();
	}
}
