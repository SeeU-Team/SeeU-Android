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
import java.net.URL;

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
	private BitmapFactory.Options options;

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

		long time = System.currentTimeMillis();

		try {
			String url = urls[0];
			InputStream in = new URL(url).openStream();

			// First decode with inJustDecodeBounds=true to check dimensions
			options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();

			// Calculate inSampleSize
			options.inSampleSize = ImageUtils.calculateInSampleSize(options, width, height);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			in = new URL(url).openStream();

			if (isCancelled()) {
				return null;
			}

			// The following line is very slow to execute
			bitmap = BitmapFactory.decodeStream(in, null, options);
			in.close();

			if (isCancelled()) {
				return null;
			}

			time = System.currentTimeMillis() - time;
			System.out.println("Time to process downloading image : " + time/1000 + "." + time%1000);
			time = System.currentTimeMillis();

			bitmap = ImageUtils.resizeBitmapFitXY(bitmap, width, height);

			View view = viewWeakReference.get();
			if (blurEffect && null != view) {
				bitmap = BlurBuilder.blur(view.getContext(), bitmap);
			}
		} catch (Exception e) {
			Log.e(this.getClass().getName() + " : Error : ", "message : " + e.getMessage());
			bitmap = BitmapFactory.decodeResource(viewWeakReference.get().getResources(), R.drawable.ic_broken_image_black);
		} finally {
			// BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), result);
			result = RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap);
			result.setCornerRadius(cornerRadius);
		}

		time = System.currentTimeMillis() - time;
		System.out.println("Time to process resize/blur/round image : " + time/1000 + "." + time%1000);

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

	public void cancelDownload() {
		super.cancel(true);
		if (null != options) {
			options.requestCancelDecode();
		}
	}
}
