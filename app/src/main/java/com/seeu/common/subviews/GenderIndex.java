package com.seeu.common.subviews;

import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.View;

import com.seeu.common.GenderShaderFactory;

/**
 * Created by thomasfouan on 07/05/2018.
 */

public class GenderIndex {

	private View indexView;

	public GenderIndex(View indexView) {
		this.indexView = indexView;
	}

	public void setMaleProportion(float maleProportion) {
		/*	IMPORTANT, this doesn't work :
				GradientDrawable drawable = (GradientDrawable) layoutPicture.getResources().getDrawable(R.drawable.measure_gender);
				drawable.mutate();
				drawable.setGradientCenter((float) 0.5, maleProportion);

			The current version of the API doesn't allow to change dynamically the center color position of a linear gradient
		*/

		GenderShaderFactory genderShaderFactory = new GenderShaderFactory(maleProportion);
		PaintDrawable pd = new PaintDrawable();
		pd.setShape(new RoundRectShape(new float[]{100, 100, 100, 100, 100, 100, 100, 100}, null, null));
		pd.setShaderFactory(genderShaderFactory);

		indexView.setBackground(pd);
	}
}
