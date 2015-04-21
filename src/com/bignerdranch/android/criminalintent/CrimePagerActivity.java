package com.bignerdranch.android.criminalintent;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.UUID;

public class CrimePagerActivity
	extends FragmentActivity
{
	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mCrimes = CrimeLab.get(this).getCrimes();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(
			new FragmentStatePagerAdapter(fm) {
				@Override
				public int getCount() {
					return mCrimes.size();
				}
				
				@Override
				public Fragment getItem(int position) {
					Crime c = mCrimes.get(position);
					return CrimeFragment.newInstance(c.getId());
				}
			}
		);
		mViewPager.setOnPageChangeListener(
			new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					Crime c = mCrimes.get(position);
					if (null != c.getTitle()) {
						setTitle(c.getTitle());
					}
				}
				
				@Override
				public void onPageScrolled(int position, float posOffset, int posOffsetPixels) {
					
				}
				
				@Override
				public void onPageScrollStateChanged(int position) {
					
				}
			}
		);
		
		UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i=0; i<mCrimes.size(); i++) {
			if (crimeId.equals(mCrimes.get(i).getId())) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}

}
