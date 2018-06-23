package com.seeu.member;

import android.content.Context;

import com.facebook.AccessToken;
import com.seeu.common.AbstractService;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

/**
 * Created by thomasfouan on 14/06/2018.
 *
 * Service that communicate with the API for Member entity.
 */
public class MemberService extends AbstractService {

	public MemberService(Context context) {
		// TODO: to change with /members. Create Authentication Service ?
		super(context, "/login");
	}

	/**
	 * Authenticate the user from its Facebook token and return its info.
	 * In addition, it returns a token in the headers to provide for future calls to the SeeU API.
	 * @param accessToken the Facebook API get with Facebook Login
	 * @param listener listener that will retrieve the data after the request completes
	 */
	public void getMember(AccessToken accessToken, CustomResponseListener<Member> listener) {
		GsonRequest<Member> request = new GsonRequest<>(
				BASE_URL,
				Member.class,
				accessToken.getToken(),
				listener);

		// Add the request to the RequestQueue.
		queue.add(request);
	}
}
