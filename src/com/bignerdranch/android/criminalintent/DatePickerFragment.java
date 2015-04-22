package com.bignerdranch.android.criminalintent;


import android.support.v4.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import android.widget.DatePicker;
import android.content.Intent;
import android.content.DialogInterface;
import android.app.Activity;

public class DatePickerFragment 
	extends DialogFragment
{
	
	public static final String EXTRA_DATE =
		"com.bignerdranch.android.criminalintent.date";
		
	private Date mDate;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Logger.d(this.getClass(), "OnCreateDialog", "Entered");
		mDate = (Date) getArguments().getSerializable(EXTRA_DATE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(mDate);
		int year = calendar.get(calendar.YEAR);
		int month = calendar.get(calendar.MONTH);
		int day = calendar.get(calendar.DATE);
		
		View view = getActivity()
			.getLayoutInflater()
			.inflate(R.layout.dialog_date, null);
			
		DatePicker datePicker = 
			(DatePicker) view.findViewById(R.id.dialog_date_datePicker);
		Logger.d(this.getClass(), "onCreateDialog", "Get DatePicker control");
		datePicker.init(year, month, day,
			new DatePicker.OnDateChangedListener() {
				@Override
				public void onDateChanged(DatePicker view, int year, int month, int day) {
					Logger.d(DatePickerFragment.this.getClass(), "onDateChanged", "Entered");
					mDate = new GregorianCalendar(year, month, day).getTime();
					getArguments().putSerializable(EXTRA_DATE, mDate);
					Logger.d(DatePickerFragment.this.getClass(), "onDateChanged", Util.dateToString(mDate));
				}
			}
		);
		
		Logger.d(this.getClass(), "onCreayeDialog", "DatePicker control initialized");
			
		return new AlertDialog.Builder(getActivity())
			.setView(view)
			.setTitle(R.string.date_picker_title)
			.setPositiveButton(android.R.string.ok, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK);
					}
				})
			.create();
	}
	
	public static DatePickerFragment newIntance(Date date) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	private void sendResult(int resultCode) {
		if (null == getTargetFragment()) {
			return;
		}
		
		Intent i = new Intent();
		i.putExtra(EXTRA_DATE,mDate);
		
		getTargetFragment().onActivityResult(
			getTargetRequestCode(), resultCode, i);
	}
}
