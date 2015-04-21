package com.bignerdranch.android.criminalintent;


import android.os.Bundle;
import android.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.text.TextWatcher;
import android.text.Editable;
import java.lang.CharSequence;
import android.text.format.DateFormat;
import java.util.UUID;

public class CrimeFragment extends Fragment
{
	public static final String EXTRA_CRIME_ID =
		"com.bignerdranch.android.criminalintent.crime_id";
		
	private Crime mCrime;
	private EditText mTitleField;
	private Button mDateButton;
	private CheckBox mSolvedCheckBox;
	
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
		//mCrime = new Crime();
		
		UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragmet_crime, container, false);
		
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
		mDateButton.setText(Util.dateToString(mCrime.getDate()));
		mDateButton.setEnabled(false);
		
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
		
		return v;
	}

}
