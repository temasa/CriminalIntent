package com.bignerdranch.android.criminalintent;


import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.widget.Button;
import android.os.Bundle;
import android.hardware.Camera;
import android.annotation.TargetApi;
import android.os.Build;
import java.io.IOException;
import java.util.List;

public class CrimeCameraFragment extends Fragment
{

	private Camera mCamera;
	private SurfaceView mSurfaceView;
	
	@TargetApi(9)
	@Override
	public void onResume() {
		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
		}
		else {
			mCamera = Camera.open();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_crime_camera, parent, false);
		
		Button takePictureButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(
			new View.OnClickListener() {
				public void onClick(View view) {
					getActivity().finish();
				}
			}
		);
		
		mSurfaceView = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(
			new SurfaceHolder.Callback() {
				public void surfaceCreated(SurfaceHolder holder) {
					try {
						if (null != mCamera) {
							mCamera.setPreviewDisplay(holder);
						}
					}
					catch (IOException e) {
						Logger.e(CrimeCameraFragment.this.getClass(), "surfaceCreated", e.toString());
					}
				}
				
				public void surfaceDestroyed(SurfaceHolder holder) {
					if (null != mCamera) {
						mCamera.stopPreview();
					}
				}
				
				public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
					if (mCamera == null ) return;
					
					Camera.Parameters parameters = mCamera.getParameters();
					Camera.Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), w, h);
					parameters.setPreviewSize(s.width, s.height);
					try {
						mCamera.startPreview();
					}
					catch (Exception e) {
						Logger.e(CrimeCameraFragment.this.getClass(), "surfaceChaned", e.toString());
						mCamera.release();
						mCamera = null;
					}
				}
			}
		);
		
		return v;
	}
	
	private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
		Camera.Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Camera.Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				bestSize = s;
				largestArea = area;
			}
		}
		
		return bestSize;
		
		
	}
}
