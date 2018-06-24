package com.seeu.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

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
