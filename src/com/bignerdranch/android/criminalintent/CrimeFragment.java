package com.bignerdranch.android.criminalintent;


import android.os.Bundle;
import android.os.Build;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.text.TextWatcher;
import android.text.Editable;
import java.lang.CharSequence;
import android.text.format.DateFormat;
import java.util.UUID;
import android.app.Activity;
import java.util.Date;
import android.content.Intent;
import android.annotation.TargetApi;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.widget.Toast;

public class CrimeFragment extends Fragment
{
	public static final String EXTRA_CRIME_ID =
		"com.bignerdranch.android.criminalintent.crime_id";
		
	public static final String DIALOG_DATE = "date";
	public static final int REQUEST_DATE = 0;
		
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	private ImageButton mPhotoButton;
	
	public static CrimeFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		
		CrimeFragment fragment = new CrimeFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		//mCrime = new Crime();
		
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case android.R.id.home:
				if (NavUtils.getParentActivityName(getActivity()) != null) {
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragmet_crime, container, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		mTitleField = (EditText) v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(
			new TextWatcher() {
				@Override
				public void afterTextChanged(Editable s) {
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					
				}
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					mCrime.setTitle(s.toString());
				}
			}
		);
		
		//java.text.DateFormat tf = DateFormat.getTimeFormat(container.getContext());
		//java.text.DateFormat df = DateFormat.getDateFormat(container.getContext());
		mDateButton = (Button) v.findViewById(R.id.crime_date);
		//mDateButton.setText(mCrime.getDate().toString());
		//mDateButton.setText(df.format(mCrime.getDate()) + " " + tf.format(mCrime.getDate()));
		//mDateButton.setText(Util.dateToString(mCrime.getDate()));
		updateDate();
		//mDateButton.setEnabled(false);
		mDateButton.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					FragmentManager fm = getActivity().getSupportFragmentManager();
					DatePickerFragment dialog = DatePickerFragment.newIntance(mCrime.getDate());
					dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
					dialog.show(fm, DIALOG_DATE);
				}
				
			}
		);
		
		mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(
			new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					mCrime.setSolved(isChecked);
				}
			}
		);
		
		mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
					startActivity(i);
					//Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
				}
			}
		);
		
		PackageManager pm = getActivity().getPackageManager();
		boolean hasCamera =
			pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
			pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
			(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras() > 0);
		if (!hasCamera) {
			mPhotoButton.setEnabled(false);
		}
		
		
		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (Activity.RESULT_OK != resultCode ) return;
		 Logger.d(this.getClass(), "onActivityResult", "Entered");
		if (REQUEST_DATE == requestCode) {
			Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			updateDate();
		}
	}
	
	private void updateDate() {
		mDateButton.setText(Util.dateToString(mCrime.getDate()));
	}

}
