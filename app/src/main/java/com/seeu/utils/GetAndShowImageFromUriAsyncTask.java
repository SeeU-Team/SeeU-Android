package com.seeu.utils;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.seeu.R;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by thomasfouan on 08/05/2018.
 */

public class GetAndShowImageFromUriAsyncTask extends AsyncTask<Uri, Void, Bitmap> {

	private ContentResolver contentResolver;
	private WeakReference<ImageView> pictureChosenWeakReference;
	private WeakReference<View> pictureChooserWeakReference;

	public GetAndShowImageFromUriAsyncTask(ContentResolver contentResolver, ImageView pictureChosen, View pictureChooser) {
		this.contentResolver = contentResolver;
		this.pictureChosenWeakReference = new WeakReference<>(pictureChosen);
		this.pictureChooserWeakReference = new WeakReference<>(pictureChooser);
	}

	@Override
	protected Bitmap doInBackground(Uri... uris) {
		Bitmap bitmap = null;

		try {
			bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uris[0]);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
			bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_broken_image_black);
		} finally {
			ImageView imageView = pictureChosenWeakReference.get();

			if (null != imageView && null != bitmap) {
				bitmap = ImageUtils.resizeBitmapFitXY(bitmap, imageView.getWidth(), imageView.getHeight());
			}
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		ImageView pictureChosen = pictureChosenWeakReference.get();
		View pictureChooser = pictureChooserWeakReference.get();

		if (null != bitmap
				&& null != pictureChosen
				&& null != pictureChooser) {
			pictureChosen.setImageBitmap(bitmap);

			pictureChosen.setVisibility(View.VISIBLE);
			pictureChooser.setVisibility(View.GONE);
		}
	}
}
