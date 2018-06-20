package com.seeu.common;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;

/**
 * Created by thomasfouan on 20/03/2018.
 *
 * Shader Factory for the gender gradient. It generates a gradient based on a male proportion.
 *
 * It has fixed colors for the start, middle and end.
 * It also has fixed positions for the start and end but has a variable middle based on the male proportion.
 */
public class GenderShaderFactory extends ShapeDrawable.ShaderFactory {

	private final int[] colors;
	private final float[] positions;

	public GenderShaderFactory(float maleProportion) {
		colors = new int[] {
				Color.parseColor("#1EB7D3"), // start
				Color.parseColor("#886EDF"), // center
				Color.parseColor("#CF2FA8") // end
		};

		positions = new float[] {
				0, // start
				maleProportion, // center
				1 // end
		};
	}

	@Override
	public Shader resize(int width, int height) {
		return new LinearGradient(width/2, 0, width/2, height,
				colors,
				positions,
				Shader.TileMode.CLAMP);
	}
}
