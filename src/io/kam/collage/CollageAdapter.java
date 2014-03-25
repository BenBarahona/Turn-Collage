package io.kam.collage;

import java.util.List;

import io.kam.collage.models.PictureLayout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class CollageAdapter extends ArrayAdapter<PictureLayout>{
	
	private final int resource;
	private final List<PictureLayout> items;
	
	public CollageAdapter(Context context, int textViewResourceId, List<PictureLayout>items) {
		super(context, textViewResourceId);
		this.items = items;
		this.resource = textViewResourceId;
	}
	
	
	@Override
	public int getCount(){
		return items.size();
	}
	
	@Override
	public PictureLayout getItem(int position) {
		return items.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View returnView = convertView;
		if(returnView == null)
		{
			returnView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
		}
		
		PictureLayout layout = getItem(position);
		ImageView thumbnail = (ImageView)returnView.findViewById(R.id.collage_thumbnail_id);
		thumbnail.setImageResource(layout.thumbnailResource);
		
		return returnView;
	}
}
