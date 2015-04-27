package com.bignerdranch.android.criminalintent;


import android.content.Context;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

public class CriminalIntentJSONSerializer
{
	private Context mContext;
	private String mFilename;
	
	public CriminalIntentJSONSerializer(Context context, String filename) {
		mContext = context;
		mFilename = filename;
	}
	
	public ArrayList<Crime> loadCrimes()
		throws JSONException, IOException {
		
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for (int i=0; i<array.length(); i++) {
				crimes.add(new Crime(array.getJSONObject(i)));
			}
		}
		catch (FileNotFoundException e) {
			if (null != reader) {
				reader.close();
			}
		}
		
		return crimes;
	}
	
	public void saveCrimes(ArrayList<Crime> crimes) 
		throws JSONException, IOException {
		
		JSONArray array = new JSONArray();
		for (Crime c: crimes) {
			array.put(c.toJSON());
		}
		
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		}
		finally {
			if (null != writer) {
				writer.close();
			}
		}
	}
	
}
