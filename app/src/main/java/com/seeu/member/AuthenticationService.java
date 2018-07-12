package com.seeu.member;

import android.content.Context;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.seeu.common.AbstractService;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomasfouan on 25/06/2018.
 *
 * Service that communicate with the API to authenticate the user.
 */
public class AuthenticationService extends AbstractService {

	public AuthenticationService(Context context) {
		super(context, "/login");
	}

	/**
	 * Authenticates the user from its Facebook token and return its info.
	 * In addition, it returns a token in the headers to provide for future calls to the SeeU API.
	 * @param accessToken the Facebook token got with Facebook Login
	 * @param listener listener that will retrieve the data after the request completes
	 */
	public void getMember(AccessToken accessToken, String appInstanceId, CustomResponseListener<Member> listener) {
		Map<String, String> params = new HashMap<>(2);
		params.put("accessToken", "\"" + accessToken.getToken() + "\"");
		params.put("appInstanceId", "\"" + appInstanceId + "\"");

		GsonRequest<Member> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.POST,
				Member.class,
				params,
				listener);

		// Add the request to the RequestQueue.
		queue.add(request);
	}
}
