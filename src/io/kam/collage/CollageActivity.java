package io.kam.collage;

import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CollageActivity extends Activity {

	private final String TAG = "Collage-Tag";
	
	private static final int CAMERA_PICTURE_REQUEST_CODE = 100;
	
	private Button takePicture1;
	private Button takePicture2;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.collage_view);
	    
	    takePicture1 = (Button)findViewById(R.id.button1);
	    takePicture2 = (Button)findViewById(R.id.button2);
	    
	    takePicture1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showSourceMenu();
			}
		});
	    
	    takePicture2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSourceMenu();
			}
		});
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
		new AlertDialog.Builder(this)
		.setTitle("Choose a photo source")
		.setAdapter(new SourceAdapter(this, R.layout.source_picker_layout), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which)
				{
					case 0:
						useDeviceCamera();
						break;
					case 1:
						pickFromGallery();
						break;
					case 2:
						break;
					default:
						break;
				}
			}
		}).show();
	}
	
	Uri fileUri;
	public void useDeviceCamera()
	{
		Log.i(TAG, "Use camera");
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		fileUri = Uri.fromFile(buildFile());
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		
		startActivityForResult(cameraIntent, CAMERA_PICTURE_REQUEST_CODE);
	}
	
	public void pickFromGallery()
	{
		Log.i(TAG, "Pick from gallery");
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == RESULT_OK)
		{
			switch(requestCode)
			{
				case CAMERA_PICTURE_REQUEST_CODE:
				break;
				
			}
		}
	}
	
	
	private class SourceAdapter extends ArrayAdapter<String>
	{
		public SourceAdapter(Context context, int resource) {
			super(context, resource);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View answer = convertView;
			
			if(answer == null)
			{
				answer = getLayoutInflater().inflate(R.layout.source_picker_layout, parent, false);
			}
			
			ImageView icon = (ImageView)answer.findViewById(R.id.source_icon);
			TextView text = (TextView)answer.findViewById(R.id.source_text);
			
			switch(position)
			{
				case 0:
					text.setText("Use Camera");
					break;
				case 1:
					text.setText("Gallery");
					break;
				case 2:
					text.setText("Facebook");
					break;
				case 3:
					text.setText("Instagram");
					break;
				default:
					break;
			}
			
			return answer;
		}
		
		@Override
		public int getCount() {
			return 4;
		}
	}

}
