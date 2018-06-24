package com.seeu.utils.network;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.Map;

/**
 * Created by thomasfouan on 16/05/2018.
 *
 * Callback interface for delivering custom http responses, using json format.
 *
 * @param <T> the type of object to parse and return when the request is complete
 */
public interface CustomResponseListener<T> extends Listener<T>, ErrorListener {

	/**
	 * Callback method to retrieve headers in the response.
	 * @param headers the response's headers
	 */
	void onHeadersResponse(Map<String, String> headers);
}
