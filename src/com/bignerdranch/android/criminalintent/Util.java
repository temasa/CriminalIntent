package com.bignerdranch.android.criminalintent;


import java.util.Date;
import java.text.SimpleDateFormat;

public class Util
{
	
	private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH;mm:ss");
	
	public static String dateToString(Date date) {
		return df.format(date);
	}
}
