package com.seeu.common;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.seeu.utils.SharedPreferencesManager;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by thomasfouan on 10/06/2018.
 *
 * Abstract class that provide template for the services that communicate with the API.
 */
public abstract class AbstractService {

	protected WeakReference<Context> weakRefContext;
	protected RequestQueue queue;
	protected final String BASE_URL;

	protected AbstractService(Context context, String rootEndPointUrl) {
		this.weakRefContext = new WeakReference<>(context);
		this.queue = Volley.newRequestQueue(context);
		this.BASE_URL = Constants.SEEU_API_URL + rootEndPointUrl;
	}

	protected String getToken() {
		Context context = weakRefContext.get();
		return null != context
				? SharedPreferencesManager.getToken(context)
				: null;
	}

	/**
	 * Get the full url of GET request.
	 * Concat all params from a Map with the url.
	 * @param params the params to append to the url
	 * @return the full url
	 */
	protected String getFullGETUrl(String url, Map<String, String> params) {
		if(params != null) {
			StringBuilder stringBuilder = new StringBuilder(url);
			Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();

			for (int i = 0; iterator.hasNext(); i++) {
				Map.Entry<String, String> entry = iterator.next();

				stringBuilder.append((i == 0) ? "?" : "&");
				stringBuilder.append(entry.getKey() + "=" + entry.getValue());

				iterator.remove(); // avoids a ConcurrentModificationException
			}

			url = stringBuilder.toString();
		}

		return url;
	}
}
