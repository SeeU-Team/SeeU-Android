package com.seeu.common;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class AbstractService {

	protected Context context;
	protected RequestQueue queue;
	protected final String BASE_URL;

	protected AbstractService(Context context, String rootEndPointUrl) {
		this.context = context;
		this.queue = Volley.newRequestQueue(context);
		this.BASE_URL = Constants.SEEU_API_URL + rootEndPointUrl;
	}
}
