package com.seeu.utils.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by thomasfouan on 16/05/2018.
 *
 * Class that manages requests to the server and parses the response with the {@link Gson} library.
 *
 * @param <T> the type of object expected to receive in JSON after executing the request
 */
public class GsonRequest<T> extends Request<T> {
	private final Gson gson = new Gson();
	private final Class<T> clazz;
	private final Map<String, String> headers;
	private final Map<String, String> params;
	private String rawData;
	private final CustomResponseListener<T> responseListener;

	/**
	 * Make a request and return a parsed object from JSON.
	 *
	 * @param url URL of the request to make
	 * @param method request method
	 * @param clazz Relevant class object, for Gson's reflection
	 * @param params Map of request parameters
	 * @param responseListener response listener
	 */
	public GsonRequest(String url, int method, Class<T> clazz, Map<String, String> params, CustomResponseListener<T> responseListener) {
		super(method, url, responseListener);

		setRetryPolicy(new DefaultRetryPolicy(
				30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		this.clazz = clazz;
		this.params = params;
		this.responseListener = responseListener;
		this.headers = new HashMap<>();
		headers.put("Accept", "application/json");
		this.rawData = null;
	}

	/**
	 * Make a post request with raw data and return a parsed object from JSON.
	 *
	 * @param url URL of the request to make
	 * @param clazz Relevant class object, for Gson's reflection
	 * @param rawPostData raw data to send as a POST request
	 * @param responseListener response listener
	 */
	public GsonRequest(String url, Class<T> clazz, String rawPostData, CustomResponseListener<T> responseListener) {
		this(url, Method.POST, clazz, null, responseListener);

		this.rawData = rawPostData;
	}

	/**
	 * Make a secured request with a token and return a parsed object from JSON.
	 *
	 * @param url URL of the request to make
	 * @param method request method
	 * @param clazz Relevant class object, for Gson's reflection
	 * @param token the token to send along with the request
	 * @param params Map of request parameters
	 * @param responseListener response listener
	 */
	public GsonRequest(String url, int method, Class<T> clazz, String token, Map<String, String> params, CustomResponseListener<T> responseListener) {
		this(url, method, clazz, params, responseListener);

		headers.put("Authorization", "Bearer " + token);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params != null ? params : super.getParams();
	}

	@Override
	public String getBodyContentType() {
		return (null != rawData) ? "text/plain; charset=" + getParamsEncoding() : "application/json";
	}

	@Override
	public byte[] getBody() {
		return (null != rawData)
				? rawData.getBytes()
				: getRawJsonBody();
	}

	private byte[] getRawJsonBody() {
		if (null == params || params.size() == 0) {
			return null;
		}

		Set<Entry<String, String>> entrySet = params.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();

		if (entrySet.size() == 1) {
			return iterator.next().getValue().getBytes();
		}

		StringBuilder stringBuilder = new StringBuilder("{");

		for (int i = 0; iterator.hasNext(); i++) {
			if (i > 0) {
				stringBuilder.append(",");
			}

			Entry<String, String> entry = iterator.next();
			String line = "\"" + entry.getKey() + "\":" + entry.getValue();
			stringBuilder.append(line);
		}
		stringBuilder.append("}");

		return stringBuilder.toString().getBytes();
	}

	@Override
	protected void deliverResponse(T response) {
		responseListener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(
					response.data,
					HttpHeaderParser.parseCharset(response.headers));

			responseListener.onHeadersResponse(response.headers);

			return Response.success(
					gson.fromJson(json, clazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException | JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}
