package io.kam.collage;

import io.kam.collage.models.Photo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

public class PhotoPickerActivity extends Activity {
	
	public List<Photo> photoList;
	PhotoAdapter adapter;
	GridView gridView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.photo_picker_layout);
	    
	    photoList = new ArrayList<Photo>();
	    
	    adapter = new PhotoAdapter(this, R.layout.photo_picker_cell_layout, photoList);
	    gridView = (GridView)findViewById(R.id.photo_picker_grid);
	    gridView.setAdapter(adapter);
	    
	}
	
	private class PhotoAdapter extends ArrayAdapter<Photo> {

		public PhotoAdapter(Context context, int textViewResourceId, List<Photo> objects) {
			super(context, textViewResourceId, objects);
			
		}
		
	}

}
