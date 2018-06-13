package com.seeu.common;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class AbstractService {

	protected Context context;
	protected RequestQueue queue;

	protected AbstractService(Context context) {
		this.context = context;
		this.queue = Volley.newRequestQueue(context);
	}
}
