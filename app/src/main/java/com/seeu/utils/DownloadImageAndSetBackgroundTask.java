package com.seeu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by thomasfouan on 19/03/2018.
 */

public class DownloadImageAndSetBackgroundTask extends AsyncTask<String, Void, Bitmap> {
	private View view;
	private float cornerRadius;
	private int width;
	private int height;

	public DownloadImageAndSetBackgroundTask(View view, float cornerRadius) {
		this.view = view;
		this.cornerRadius = cornerRadius;
		this.width = view.getWidth();
		this.height = view.getHeight();
	}

	public DownloadImageAndSetBackgroundTask(View view, float cornerRadius, int width, int height) {
		this(view, cornerRadius);
		this.width = width;
		this.height = height;
	}

	protected Bitmap doInBackground(String... urls) {
		String url = urls[0];
		Bitmap bitmap = null;

		try {
			InputStream in = new java.net.URL(url).openStream();
			bitmap = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
			// TODO: set default/broken image
		}

		return ImageUtils.resizeBitmapFitXY(bitmap, width, height);
	}

	protected void onPostExecute(Bitmap result) {
		// BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), result);
		RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(view.getResources(), result);
		drawable.setCornerRadius(cornerRadius);

		if (view instanceof ImageView) {
			((ImageView) view).setImageDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}
}
