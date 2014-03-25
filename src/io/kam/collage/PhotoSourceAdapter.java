package io.kam.collage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoSourceAdapter extends ArrayAdapter<String>
{
	public PhotoSourceAdapter(Context context, int resource) {
		super(context, resource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View answer = convertView;

		if(answer == null)
		{
			LayoutInflater inflater = LayoutInflater.from(getContext());
			answer = inflater.inflate(R.layout.source_picker_layout, parent, false);
		}

		ImageView icon = (ImageView)answer.findViewById(R.id.source_icon);
		TextView text = (TextView)answer.findViewById(R.id.source_text);

		switch(position)
		{
			case 0:
				text.setText("Me Gallery");
				break;
			case 1:
				text.setText("Device Photos");
				icon.setImageResource(R.drawable.loadfromgallery_icon);
				break;
			case 2:
				text.setText("Use Camera");
				icon.setImageResource(R.drawable.takephoto_icon);
				break;
			default:
				break;
		}

		return answer;
	}

	@Override
	public int getCount() {
		return 3;
	}
}
