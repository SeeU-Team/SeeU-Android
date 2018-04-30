package com.seeu.common.subviews;

import android.widget.TextView;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class Mark {

	private static final int MAX_MARK = 5;

	private TextView markView;

	public Mark(TextView markView) {
		this.markView = markView;
	}

	public void setMark(int mark) {
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < MAX_MARK; i++) {
			if (i < mark) {
				builder.append("★");
			} else {
				builder.append("☆");
			}
		}

		markView.setText(builder.toString());
	}
}
