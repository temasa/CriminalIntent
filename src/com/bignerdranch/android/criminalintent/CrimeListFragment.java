package com.bignerdranch.android.criminalintent;


//import android.app.ListFragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.app.Activity;
import android.content.Intent;

public class CrimeListFragment extends ListFragment
{
	private ArrayList<Crime> mCrimes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		//ArrayAdapter<Crime> adapter = 
		//	new ArrayAdapter<Crime> (getActivity(), android.R.layout.simple_list_item_1, mCrimes);
		CrimeAdapter adapter= new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
		//Logger.d(this.getClass(), "onListItemClick", c.getTitle() + " was clicked");
		
		//Intent i = new Intent(getActivity(), CrimeActivity.class);
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i);
	}
	
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = getActivity()
					.getLayoutInflater()
					.inflate(R.layout.list_item_crime, null);
			}
			
			Crime c = getItem(position);
			
			TextView titleTextView = 
				(TextView) convertView
				.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			
			TextView dateTextView = 
				(TextView) convertView
				.findViewById(R.id.crime_list_item_dateTextView);
			dateTextView.setText(Util.dateToString(c.getDate()));
			
			CheckBox solvedCheckBox =
				(CheckBox) convertView
				.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			//solvedCheckBox.setEnabled(false);
				
			return convertView;
		}
		
	}
}
