package com.seeu.utils.network;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.Map;

public interface CustomResponseListener<T> extends Listener<T>, ErrorListener {

	void onHeadersResponse(Map<String, String> headers);
}
