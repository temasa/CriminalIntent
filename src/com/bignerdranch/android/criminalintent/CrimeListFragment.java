package com.bignerdranch.android.criminalintent;


//import android.app.ListFragment;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.view.MenuInflater;
import android.view.ContextMenu;
import android.annotation.TargetApi;
import android.os.Build;

public class CrimeListFragment extends ListFragment
{
	private ArrayList<Crime> mCrimes;
	private boolean mSubtitleVisible;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mSubtitleVisible = false;
		setHasOptionsMenu(true);
		getActivity().setTitle(R.string.crimes_title);
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		//ArrayAdapter<Crime> adapter = 
		//	new ArrayAdapter<Crime> (getActivity(), android.R.layout.simple_list_item_1, mCrimes);
		CrimeAdapter adapter= new CrimeAdapter(mCrimes);
		setListAdapter(adapter);
	}

	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mSubtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		ListView listView = (ListView) view.findViewById(android.R.id.list);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
		}
		else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(
				new AbsListView.MultiChoiceModeListener() {
					public void onItemCheckedStateChanged(ActionMode mode, int position, long id,boolean checked) {
						
					}
					
					public boolean onCreateActionMode(ActionMode mode, Menu menu) {
						MenuInflater inflater = mode.getMenuInflater();
						inflater.inflate(R.menu.crime_list_item_context, menu);
						
						return true;
					}
					
					public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
						return false;
					}
					
					public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
						switch (item.getItemId()) {
							case R.id.menu_item_delete_crime:
								CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
								CrimeLab crimeLab = CrimeLab.get(getActivity());
								for (int i=adapter.getCount()-1; i>=0; i--) {
									if (getListView().isItemChecked(i)) {
										crimeLab.deleteCrime(adapter.getItem(i));
									}
								}
								mode.finish();
								adapter.notifyDataSetChanged();
								return true;
							default:
								return false;
						}
						
					}
					
					public void onDestroyActionMode(ActionMode mode) {
						
					}
				}
			);
		}
		
		return view;
	} 

	@Override
	public void onResume()
	{
		super.onResume();
		((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle != null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}

	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case R.id.menu_item_new_crime:
				Crime crime = new Crime();
				CrimeLab.get(getActivity()).addCrime(crime);
				Intent i= new Intent(getActivity(), CrimePagerActivity.class);
				i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
				startActivityForResult(i,0);
				return true;
			case R.id.menu_item_show_subtitle:
				if (getActivity().getActionBar().getSubtitle() == null) {
					getActivity().getActionBar().setSubtitle(R.string.subtitle);
					mSubtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
				}
				else {
					getActivity().getActionBar().setSubtitle(null);
					mSubtitleVisible = false;
					item.setTitle(R.string.show_subtitle);
				}
				
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
			case R.id.menu_item_delete_crime:
				CrimeLab.get(getActivity()).deleteCrime(crime);
				adapter.notifyDataSetChanged();
				return true;
		}
		return super.onContextItemSelected(item);
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
