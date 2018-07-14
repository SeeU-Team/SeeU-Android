package com.seeu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Base64;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.ByteArrayOutputStream;

/**
 * Created by thomasfouan on 19/03/2018.
 *
 * Utils for images.
 */
public class ImageUtils {

	private ImageUtils() {
	}

	/**
	 * Resize the Bitmap to fit the given width and height.
	 * Zoom in the image if needed.
	 *
	 * @param bitmap the bitmap to resize
	 * @param width the expected width
	 * @param height the expected height
	 * @return the resized bitmap
	 */
	public static Bitmap resizeBitmapFitXY(Bitmap bitmap, int width, int height) {
		Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		float originalWidth = bitmap.getWidth();
		float originalHeight = bitmap.getHeight();
		Canvas canvas = new Canvas(background);
		float scale, xTranslation = 0.0f, yTranslation = 0.0f;

		if (originalWidth > originalHeight) {
			scale = height / originalHeight;
			xTranslation = (width - originalWidth * scale) / 2.0f;
		} else {
			scale = width / originalWidth;
			yTranslation = (height - originalHeight * scale) / 2.0f;
		}

		Matrix transformation = new Matrix();
		transformation.postTranslate(xTranslation, yTranslation);
		transformation.preScale(scale, scale);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		canvas.drawBitmap(bitmap, transformation, paint);
		return background;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	/**
	 * Get the image data from a Bitmap to a Base64-encoded String.
	 * @param bmp the image
	 * @return the image data as Base64-encoded String
	 */
	public static String getStringImage(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 75, baos);

		byte[] imageBytes = baos.toByteArray();
		return Base64.encodeToString(imageBytes, Base64.NO_WRAP | Base64.URL_SAFE);
	}

	public static void runJustBeforeBeingDrawn(final View view, final Runnable runnable) {
		final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				view.getViewTreeObserver().removeOnPreDrawListener(this);
				runnable.run();
				return true;
			}
		};
		view.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
	}

	public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
		if (maxHeight > 0 && maxWidth > 0) {
			int width = image.getWidth();
			int height = image.getHeight();
			float ratioBitmap = (float) width / (float) height;
			float ratioMax = (float) maxWidth / (float) maxHeight;

			int finalWidth = maxWidth;
			int finalHeight = maxHeight;
			if (ratioMax > 1) {
				finalWidth = (int) ((float)maxHeight * ratioBitmap);
			} else {
				finalHeight = (int) ((float)maxWidth / ratioBitmap);
			}
			image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
		}
		return image;
	}
}
