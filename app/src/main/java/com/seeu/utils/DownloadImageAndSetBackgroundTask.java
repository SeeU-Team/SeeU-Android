package com.seeu.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.seeu.R;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Created by thomasfouan on 19/03/2018.
 *
 * Async task for downloading images and set it on a view.
 */
public class DownloadImageAndSetBackgroundTask extends AsyncTask<String, Void, RoundedBitmapDrawable> {
	private WeakReference<View> viewWeakReference;
	private float cornerRadius;
	private int width;
	private int height;
	private boolean blurEffect;

	public DownloadImageAndSetBackgroundTask(@NonNull View view, float cornerRadius) {
		this(view, cornerRadius, view.getWidth(), view.getHeight());
	}

	public DownloadImageAndSetBackgroundTask(@NonNull View view, float cornerRadius, boolean blurEffect) {
		this(view, cornerRadius, view.getWidth(), view.getHeight(), blurEffect);
	}

	public DownloadImageAndSetBackgroundTask(@NonNull View view, float cornerRadius, int width, int height) {
		this(view, cornerRadius, width, height, false);
	}

	public DownloadImageAndSetBackgroundTask(@NonNull View view, float cornerRadius, int width, int height, boolean blurEffect) {
		this.viewWeakReference = new WeakReference<>(view);
		this.cornerRadius = cornerRadius;
		this.width = width;
		this.height = height;
		this.blurEffect = blurEffect;
	}

	@Override
	protected RoundedBitmapDrawable doInBackground(String... urls) {
		Bitmap bitmap = null;
		RoundedBitmapDrawable result;

		try {
			String url = urls[0];
			InputStream in = new java.net.URL(url).openStream();
			bitmap = BitmapFactory.decodeStream(in);
			bitmap = ImageUtils.resizeBitmapFitXY(bitmap, width, height);

			View view = viewWeakReference.get();
			if (blurEffect && null != view) {
				bitmap = BlurBuilder.blur(view.getContext(), bitmap);
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
			bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_broken_image_black);
		} finally {
			// BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), result);
			result = RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap);
			result.setCornerRadius(cornerRadius);
		}

		return result;
	}

	@Override
	protected void onPostExecute(final RoundedBitmapDrawable drawable) {
		View view = viewWeakReference.get();
		if (null == view) {
			return;
		}

		if (view instanceof ImageView) {
			((ImageView) view).setImageDrawable(drawable);
		} else {
			view.setBackground(drawable);
		}
	}
}
