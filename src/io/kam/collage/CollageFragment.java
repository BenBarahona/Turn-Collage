package io.kam.collage;

import java.io.File;
import java.util.UUID;

import io.kam.collage.PhotoSourceAdapter;
import io.kam.collage.models.PhotoArea;
import io.kam.collage.models.PictureLayout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class CollageFragment extends Fragment {
	
	public static final String POSITION_KEY = "kPosition";
	public static final String TAG = "COLLAGE_FRAG";
	public PictureLayout collageLayout;
	public int layoutWidth;
	public int layoutHeight;

	private static final int PHOTO_PICKER_CODE = 100;
	private static final int CAMERA_PICTURE_REQUEST_CODE = 100;
	private RelativeLayout collageRelativeLayout;
	
	private ImageView background;

	private ImageButton button1;
	private ImageButton button2;
	private ImageButton button3;
	private ImageButton button4;

	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private ImageView image4;
	
	private View.OnClickListener buttonListener;

	public CollageFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Log.i(TAG, "" + getArguments().getInt(POSITION_KEY));
		} catch (Exception e) {
			Log.i(TAG, "No args");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View returnView = inflater.inflate(R.layout.collage_layout, container,
				false);
		collageRelativeLayout = (RelativeLayout) returnView
				.findViewById(R.id.collage_container);
		background = (ImageView) returnView.findViewById(R.id.collage_background);
		if(collageLayout != null) {
			background.setImageResource(collageLayout.backgroundResource);
		}
		constructFrameLayout();
		
		return returnView;
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == Activity.RESULT_OK)
		{
			if(requestCode == PHOTO_PICKER_CODE)
			{
				
			}
			else if(requestCode == CAMERA_PICTURE_REQUEST_CODE)
			{
				
			}
		}
	}

	public void constructFrameLayout() {
		buttonListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, ""+v.getId());
				Intent intent = new Intent(getActivity().getApplicationContext(), PhotoPickerActivity.class);
				startActivityForResult(intent, PHOTO_PICKER_CODE);
			}
		};
		RelativeLayout.LayoutParams layoutParams = null;

		for (PhotoArea photo : collageLayout.photoAreas) {
			int index = photo.areaID;
			int buttonWidth = (int) (this.layoutWidth * photo.width);
			int buttonHeight = (int) (this.layoutHeight * photo.height);
			int offsetX = this.layoutWidth / 2 - buttonWidth / 2;
			int offsetY =  this.layoutHeight / 2 - buttonHeight / 2;
			int buttonImage = R.drawable.collage_image_filler;

			layoutParams = new RelativeLayout.LayoutParams(buttonWidth,
					buttonHeight);
			layoutParams
					.setMargins(
							(int) (this.layoutWidth * photo.offsetX + offsetX),
							(int) (this.layoutHeight * photo.offsetY + offsetY),
							0, 0);

			switch (index) {
			case 1:
				button1 = new ImageButton(getActivity());
				button1.setId(index);
				button1.setLayoutParams(layoutParams);
				button1.setImageResource(buttonImage);
				button1.setOnClickListener(buttonListener);

				image1 = new ImageView(getActivity());
				image1.setLayoutParams(layoutParams);
				image1.setScaleType(ScaleType.FIT_XY);

				collageRelativeLayout.addView(button1);
				collageRelativeLayout.addView(image1);
				break;
			case 2:
				button2 = new ImageButton(getActivity());
				button2.setId(index);
				button2.setLayoutParams(layoutParams);
				button2.setImageResource(buttonImage);
				button2.setOnClickListener(buttonListener);

				image2 = new ImageView(getActivity());
				image2.setLayoutParams(layoutParams);
				image2.setScaleType(ScaleType.FIT_XY);
				
				collageRelativeLayout.addView(button2);
				collageRelativeLayout.addView(image2);
				break;
			case 3:
				button3 = new ImageButton(getActivity());
				button3.setLayoutParams(layoutParams);
				button3.setImageResource(buttonImage);
				button3.setId(index);
				button3.setOnClickListener(buttonListener);
				
				image3 = new ImageView(getActivity());
				image3.setLayoutParams(layoutParams);
				image3.setScaleType(ScaleType.FIT_XY);
				
				collageRelativeLayout.addView(button3);
				collageRelativeLayout.addView(image3);
				break;

			case 4:
				button4 = new ImageButton(getActivity());
				button4.setLayoutParams(layoutParams);
				button4.setImageResource(buttonImage);
				button4.setId(index);
				button4.setOnClickListener(buttonListener);

				image4 = new ImageView(getActivity());
				image4.setLayoutParams(layoutParams);
				
				collageRelativeLayout.addView(button4);
				collageRelativeLayout.addView(image4);
				break;
			}

		}

	}
	
	public static File buildFile() {
		String path = Environment.getExternalStorageDirectory().toString();
        String directoryString = "/Turn";

        //Check if app directory exists
        File directory = new File(path, directoryString);
        if (!directory.exists()) {
        	//Create the app directory
        	directory.mkdirs();
        }
        
        //If the app directory exists use the app directory if not 
        //keep on the root of the external storage
        if (directory.exists() && directory.isDirectory()) {
        	path = path + directoryString;
        }
        
        File file = new File(path, buildFilename());
        
        return file;
	}
	public static String buildFilename() {
		UUID uuid = UUID.randomUUID();
		String name = uuid.toString() + ".jpg";
		return name;
	}

	public void showSourceMenu()
	{
		Log.i(TAG, "Select a source");
		new AlertDialog.Builder(getActivity().getApplicationContext())
		.setTitle("Choose a photo source")
		.setAdapter(new PhotoSourceAdapter(getActivity().getApplicationContext(), R.layout.source_picker_layout), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which)
				{
					case 0:
						
						break;
					case 1:
						pickFromGallery();
						break;
					case 2:
						useDeviceCamera();
						break;
					default:
						break;
				}
			}
		}).show();
	}

	Uri fileUri;
	private void useDeviceCamera()
	{
		Log.i(TAG, "Use camera");
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = Uri.fromFile(buildFile());
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		startActivityForResult(cameraIntent, CAMERA_PICTURE_REQUEST_CODE);
	}
	
	private void pickFromGallery()
	{
		Log.i(TAG, "Pick from gallery");
	}
}
