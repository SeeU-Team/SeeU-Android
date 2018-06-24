package com.seeu.utils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.seeu.R;

import java.lang.ref.WeakReference;

/**
 * Created by thomasfouan on 08/05/2018.
 *
 * Async task for getting images from URI and set it on an ImageView.
 */
public class GetAndShowImageFromUriAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

	private ContentResolver contentResolver;
	private WeakReference<ImageView> imageViewWeakReference;

	public GetAndShowImageFromUriAsyncTask(ContentResolver contentResolver, ImageView imageView) {
		this.contentResolver = contentResolver;
		this.imageViewWeakReference = new WeakReference<>(imageView);
	}

	@Override
	protected Bitmap doInBackground(Uri... uris) {
		Bitmap bitmap = null;

		try {
			if (null != contentResolver) {
				bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uris[0]);
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
			bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_broken_image_black);
		} finally {
			ImageView imageView = imageViewWeakReference.get();

			if (null != imageView && null != bitmap) {
				bitmap = ImageUtils.resizeBitmapFitXY(bitmap, imageView.getWidth(), imageView.getHeight());
			}
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		ImageView pictureChosen = imageViewWeakReference.get();

		if (null != bitmap
				&& null != pictureChosen) {
			pictureChosen.setImageBitmap(bitmap);
		}
	}
}
