package com.seeu.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by thomasfouan on 08/05/2018.
 */

public class GetAndShowImageFromUriAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

	private ContentResolver contentResolver;
	private ImageView pictureChosen;
	private View pictureChooser;

	public GetAndShowImageFromUriAsyncTask(ContentResolver contentResolver, ImageView pictureChosen, View pictureChooser) {
		this.contentResolver = contentResolver;
		this.pictureChosen = pictureChosen;
		this.pictureChooser = pictureChooser;
	}

	@Override
	protected Bitmap doInBackground(Uri... uris) {
		Bitmap bitmap = null;

		try {
			bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uris[0]);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
			// TODO: set default/broken image
		}

		return ImageUtils.resizeBitmapFitXY(bitmap, pictureChosen.getWidth(), pictureChosen.getHeight());
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (null != bitmap) {
			pictureChosen.setImageBitmap(bitmap);

			pictureChosen.setVisibility(View.VISIBLE);
			pictureChooser.setVisibility(View.GONE);
		}
	}
}
