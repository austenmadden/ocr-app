package com.dynamsoft.tessocr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class OCRActivity extends Activity implements OnClickListener {
	private TessOCR mTessOCR;
	private TextView mResult;
	private ProgressDialog mProgressDialog;
	private ImageView mImage;
	private Button mButtonGallery, mButtonCamera,mButtonDatabase;
	private String mCurrentPhotoPath;
	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final int REQUEST_PICK_PHOTO = 2;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mResult = (TextView) findViewById(R.id.tv_result);
		mImage = (ImageView) findViewById(R.id.image);
		mButtonGallery = (Button) findViewById(R.id.bt_gallery);
		mButtonGallery.setOnClickListener(this);
		mButtonCamera = (Button) findViewById(R.id.bt_camera);
		mButtonCamera.setOnClickListener(this);
		mButtonDatabase = (Button) findViewById(R.id.bt_db);
		mButtonDatabase.setOnClickListener(this);
		mTessOCR = new TessOCR();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
		copyAssets();
	}

	private void uriOCR(Uri uri) {
		if (uri != null) {
			InputStream is = null;
			try {
				is = getContentResolver().openInputStream(uri);
				Bitmap bitmap = BitmapFactory.decodeStream(is);
				mImage.setImageBitmap(bitmap);
				doOCR(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Intent intent = getIntent();
		if (Intent.ACTION_SEND.equals(intent.getAction())) {
			Uri uri = (Uri) intent
					.getParcelableExtra(Intent.EXTRA_STREAM);
			uriOCR(uri);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		mTessOCR.onDestroy();
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File

			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	/**
	 * http://developer.android.com/training/camera/photobasics.html
	 */
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		String storageDir = Environment.getExternalStorageDirectory()
				+ "/TessOCR";
		File dir = new File(storageDir);
		if (!dir.exists())
			dir.mkdir();

		File image = new File(storageDir + "/" + imageFileName + ".jpg");

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_TAKE_PHOTO
				&& resultCode == Activity.RESULT_OK) {
			setPic();
		} else if (requestCode == REQUEST_PICK_PHOTO
				&& resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			if (uri != null) {
				uriOCR(uri);
			}
		}
	}

	private void setPic() {
		// Get the dimensions of the View
		int targetW = mImage.getWidth();
		int targetH = mImage.getHeight();

		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;

		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor << 1;
		bmOptions.inPurgeable = true;

		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		mImage.setImageBitmap(bitmap);
		doOCR(bitmap);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
			case R.id.bt_gallery:
				pickPhoto();
				break;
			case R.id.bt_camera:
				takePhoto();
				break;
			case R.id.bt_db:
				showDatabase();
				break;

		}
	}

	private void showDatabase() {
		Intent intent = new Intent(this,ShowDatabaseActivity.class);
		startActivity(intent);

	}

	private void pickPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, REQUEST_PICK_PHOTO);
	}

	private void takePhoto() {
		dispatchTakePictureIntent();
	}

	private void doOCR(final Bitmap bitmap) {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this, "Processing",
					"Doing OCR...", true);
		} else {
			mProgressDialog.show();
		}

		new Thread(new Runnable() {
			public void run() {

				final String result = mTessOCR.getOCRResult(bitmap);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (result != null && !result.equals("")) {
							mResult.setText(result);

						}

						mProgressDialog.dismiss();
					}

				});

			}

			;
		}).start();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"OCR Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.dynamsoft.tessocr/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"OCR Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.dynamsoft.tessocr/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}

	private void copyAssets() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		if (files != null) for (String filename : files) {
			if(filename.startsWith("eng.")) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = assetManager.open(filename);
					String path = Environment.getExternalStorageDirectory() + "/tesseract/tessdata/";
					File outFile = new File(path, filename);
					out = new FileOutputStream(outFile);
					copyFile(in, out);
					Log.d("AssetCopier", path + filename);
				} catch (IOException e) {
					Log.e("tag", "Failed to copy asset file: " + filename, e);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							// NOOP
						}
					}
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							// NOOP
						}
					}
				}
			}
		}
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}
}
