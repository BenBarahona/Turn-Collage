package io.kam.collage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CollageActivity extends Activity {

	private final String TAG = "Collage-Tag";

	public static final int MAIN_PICTURE_REQUEST_CODE = 100;

	private ImageView mainPicture;
	private Button takePicture1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_layout);

	    mainPicture = (ImageView)findViewById(R.id.main_imageview);
	    takePicture1 = (Button)findViewById(R.id.main_takepicture_btn);

	    takePicture1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startCollagePickerActivity();
			}
		});
	}
	
	private void startCollagePickerActivity()
	{
		Intent intent = new Intent(CollageActivity.this, ChooseLayoutActivity.class);
		
		startActivityForResult(intent, MAIN_PICTURE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK)
		{
			switch(requestCode)
			{
				case MAIN_PICTURE_REQUEST_CODE:
					Log.i(TAG, "Got image result");
					mainPicture.setImageURI(data.getData());
				break;

			}
		}
	}

}