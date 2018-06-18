package com.seeu.member;

import android.content.Context;

import com.facebook.AccessToken;
import com.seeu.common.AbstractService;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

public class MemberService extends AbstractService {

	public MemberService(Context context) {
		super(context, "/login");
	}

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
