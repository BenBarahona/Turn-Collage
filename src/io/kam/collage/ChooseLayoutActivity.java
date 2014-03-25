package io.kam.collage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.kam.collage.models.PhotoArea;
import io.kam.collage.models.PictureLayout;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;
import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChooseLayoutActivity extends Activity {
	
	public final String TAG = "CHOOSE_LAYOUT";	
	HListView layoutList;
	CollageAdapter adapter;
	List<PictureLayout> localLayouts;
	DisplayMetrics outMetrics;
	RelativeLayout collageContainer;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.collage_picker_layout);
	    
	    ActionBar actionbar = getActionBar();
	    actionbar.setDisplayUseLogoEnabled(true);
	    
	    Display display = getWindowManager().getDefaultDisplay();
	    outMetrics = new DisplayMetrics();
	    display.getMetrics(outMetrics);
	    
	    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(outMetrics.widthPixels, (int)(outMetrics.widthPixels / 0.75));
	    
	    collageContainer = (RelativeLayout)findViewById(R.id.collage_frame);
	    collageContainer.setLayoutParams(params);
	    
	    localLayouts = getHardcodedLayouts();
	    layoutList = (HListView)findViewById(R.id.collage_list);
	    adapter = new CollageAdapter(this, R.layout.collage_picker_cell_layout, localLayouts);
	    layoutList.setAdapter(adapter);
	    layoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(
					it.sephiroth.android.library.widget.AdapterView<?> parent,
					View view, int position, long id) {
				Log.i(TAG, "Selected Item: " + position);
				
				//Change the layout on the fragment
				CollageFragment newFrag = new CollageFragment();
				newFrag.collageLayout = localLayouts.get(position);
				newFrag.layoutWidth = outMetrics.widthPixels;
				newFrag.layoutHeight = (int)(outMetrics.widthPixels / 0.75);
			    
				FragmentTransaction fragTrans = getFragmentManager().beginTransaction();
				fragTrans.replace(R.id.collage_frame, newFrag);
				fragTrans.commit();
			}
	    });
	    
	    Bundle args = new Bundle();
	    args.putInt(CollageFragment.POSITION_KEY, 0);
	    CollageFragment fragment = new CollageFragment();
	    fragment.setArguments(args);
	    fragment.collageLayout = localLayouts.get(0);
	    fragment.layoutWidth = outMetrics.widthPixels;
	    fragment.layoutHeight = (int)(outMetrics.widthPixels / 0.75);
	    
	    FragmentManager manager = getFragmentManager();
	    FragmentTransaction transaction = manager.beginTransaction();
	    transaction.add(R.id.collage_frame, fragment, TAG);
	    transaction.commit();
	    
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.collage_menu, menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == R.id.collage_save)
		{
			View container = collageContainer;
			Bitmap bmp = Bitmap.createBitmap(outMetrics.widthPixels, (int)(outMetrics.widthPixels / 0.75), Bitmap.Config.ARGB_8888);
			container.layout(0, 0, outMetrics.widthPixels, outMetrics.heightPixels);
			container.draw(new Canvas(bmp));
			
			Log.i(TAG, "Width: " + outMetrics.widthPixels + " HEIGHT: " + (int)(outMetrics.widthPixels / 0.75));
			if(outMetrics.widthPixels > 640)
			{
				Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 480, 640, false);
				if(scaledBmp != null && scaledBmp != bmp)
				{
					bmp.recycle();
					bmp = scaledBmp;
				}
			}
			
			
			String filePath = saveToFile(this, bmp, buildFilename(), true, true);
			Log.i(TAG, filePath);
			Toast.makeText(this, "Saved image", Toast.LENGTH_SHORT).show();
			
			if(filePath != null)
			{
				Intent intent = new Intent().setData(Uri.fromFile(new File(filePath)));
				setResult(RESULT_OK, intent);
			}
			else
			{
				setResult(RESULT_CANCELED);
			}
			
			super.finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void finish(Bitmap bitmap) {
	    try {
	        File folder = new File(Environment.getExternalStorageDirectory() + "/Icon Select/");
	        if(!folder.exists()) {
	            folder.mkdirs();
	        }
	        File nomediaFile = new File(folder, ".nomedia");
	        if(!nomediaFile.exists()) {
	            nomediaFile.createNewFile();
	        }

	        FileOutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Icon Select/latest.png");
	        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
	        File bitmapFile = new File(Environment.getExternalStorageDirectory() + "/Icon Select/latest.png");

	        if(bitmapFile.exists()) {
	            Intent localIntent = new Intent().setData(Uri.fromFile(bitmapFile));
	            setResult(RESULT_OK, localIntent);                
	        } else {
	            setResult(RESULT_CANCELED);
	        }
	        super.finish();

	    } catch (Exception e) {
	        e.printStackTrace();
	        Log.d("beewhale", "Error writing data");
	    }
	}
	
	public static String buildFilename() {
		UUID uuid = UUID.randomUUID();
		String name = uuid.toString() + ".jpg";
		return name;
	}
	
	public static String saveToFile(Activity activity, Bitmap bitmap, final String filename, boolean registerFile, boolean recycle) {
	    try {
	    	//TODO: Validate if saving is possible
            String path = Environment.getExternalStorageDirectory().toString();
            String app_directory = "/Turn";
            
            //Check if app directory exists
            File directory = new File(path, app_directory);
            if (!directory.exists()) {
            	//Create the app directory
            	directory.mkdirs();
            }
            
            //If the app directory exists use the app directory if not 
            //keep on the root of the external storage
            if (directory.exists() && directory.isDirectory()) {
            	path = path + app_directory;
            }
            
            OutputStream fOut = null;
            final File file = new File(path,filename);
            if (file.exists()) {
            	file.delete();
            }
            
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);

            if (registerFile) {
            	Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
            	activity.getBaseContext().sendBroadcast(intent);
            }
            
            fOut.flush();
            fOut.close();
            fOut = null;
            if (recycle) bitmap.recycle();
            
            return file.getPath();
	    } catch (Exception e) {
           e.printStackTrace();
           return null;
	    }
	}
	
	private List<PictureLayout> getHardcodedLayouts()
	{
		List<PhotoArea>areas = new ArrayList<PhotoArea>();
		
		PhotoArea area1 = new PhotoArea();
		area1.areaID = 1;
		area1.offsetX = 0.25f;
		area1.offsetY = 0.25f;
		area1.width = 0.5f;
		area1.height = 0.5f;
		
		PhotoArea area2 = new PhotoArea();
		area2.areaID = 2;
		area2.offsetX = -0.25f;
		area2.offsetY = -0.25f;
		area2.width = 0.5f;
		area2.height = 0.5f;
		
		PhotoArea area3 = new PhotoArea();
		area3.areaID = 3;
		area3.offsetX = -0.25f;
		area3.offsetY = 0.25f;
		area3.width = 0.5f;
		area3.height = 0.5f;
		
		PhotoArea area4 = new PhotoArea();
		area4.areaID = 4;
		area4.offsetX = 0.25f;
		area4.offsetY = -0.25f;
		area4.width = 0.5f;
		area4.height = 0.5f;
		
		areas.add(area1);
		areas.add(area2);
		areas.add(area3);
		areas.add(area4);
		
		PictureLayout layout1 = new PictureLayout();
		layout1.id = 1;
		layout1.thumbnailResource = R.drawable.temp_thumbnail;
		layout1.backgroundResource = R.drawable.sample_layout_background;
		layout1.photoAreas = areas;
		
		List<PhotoArea>areas2 = new ArrayList<PhotoArea>();
		PhotoArea area1b = new PhotoArea();
		area1b.areaID = 1;
		area1b.offsetX = -0.25f;
		area1b.offsetY = -0.25f;
		area1b.width = 0.5f;
		area1b.height = 0.33f;
		
		PhotoArea area2b = new PhotoArea();
		area2b.areaID = 2;
		area2b.offsetX = -0.25f;
		area2b.offsetY = 0.25f;
		area2b.width = 0.5f;
		area2b.height = 0.33f;
		
		PhotoArea area3b = new PhotoArea();
		area3b.areaID = 3;
		area3b.offsetX = 0.25f;
		area3b.offsetY = 0.0f;
		area3b.width = 0.5f;
		area3b.height = 0.33f;
		
		areas2.add(area1b);
		areas2.add(area2b);
		areas2.add(area3b);
		
		PictureLayout layout2 = new PictureLayout();
		layout2.id = 2;
		layout2.thumbnailResource = R.drawable.temp_thumbnail2;
		layout2.backgroundResource = R.drawable.sample_layout_background;
		layout2.photoAreas = areas2;
		
		List<PhotoArea>areas3 = new ArrayList<PhotoArea>();
		PhotoArea area1c = new PhotoArea();
		area1c.areaID = 1;
		area1c.offsetX = 0.0f;
		area1c.offsetY = -0.25f;
		area1c.width = 1.0f;
		area1c.height = 0.5f;
		
		PhotoArea area2c = new PhotoArea();
		area2c.areaID = 1;
		area2c.offsetX = -0.25f;
		area2c.offsetY = 0.25f;
		area2c.width = 0.5f;
		area2c.height = 0.5f;
		
		PhotoArea area3c = new PhotoArea();
		area3c.areaID = 1;
		area3c.offsetX = 0.25f;
		area3c.offsetY = 0.25f;
		area3c.width = 0.5f;
		area3c.height = 0.5f;
		
		areas3.add(area1c);
		areas3.add(area2c);
		areas3.add(area3c);
		
		PictureLayout layout3 = new PictureLayout();
		layout3.id = 3;
		layout3.thumbnailResource = R.drawable.temp_layout_image2;
		layout3.backgroundResource = R.drawable.temp_layout_image2;
		layout3.photoAreas = areas3;
		
		List<PictureLayout> answer = new ArrayList<PictureLayout>();
		answer.add(layout1);
		answer.add(layout2);
		answer.add(layout3);
		
		return answer;
	}
}
