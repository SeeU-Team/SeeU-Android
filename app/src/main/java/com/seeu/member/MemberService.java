package com.seeu.member;

import android.content.Context;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.seeu.common.AbstractService;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomasfouan on 14/06/2018.
 *
 * Service that communicates with the API for Member entity.
 */
public class MemberService extends AbstractService {

	public MemberService(Context context) {
		super(context, "/api/users");
	}

	/**
	 * Save the updated member passed in parameter in the Database.
	 * @param member the member to save
	 * @param imageBase64 the new profile picture encoded in Base64. May be null if not updated
	 * @param listener callback listener called when the response is available from the server
	 */
	public void updateMember(Member member, String imageBase64, CustomResponseListener<Member> listener) {
		Map<String, String> params = new HashMap<>(2);
		Gson gson = new Gson();
		params.put("member", gson.toJson(member));

		if (null != imageBase64) {
			params.put("profilePicture", imageBase64);
		}

		GsonRequest<Member> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.PUT,
				Member.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	/**
	 * Get friends of a member.
	 * A friend is a member that had a previous discussion with the current member.
	 * @param currentMember the member we want his friends
	 * @param listener listener that will retrieve the data after the request completes
	 */
	public void getFriends(Member currentMember, CustomResponseListener<Member[]> listener) {
		String fullUrl = BASE_URL + "/" + currentMember.getId() + "/friends";

		// TODO: send the member in the request or the token is enough ????
		GsonRequest<Member[]> request = new GsonRequest<>(
				fullUrl,
				Request.Method.GET,
				Member[].class,
				getToken(),
				null,
				listener);

		queue.add(request);
	}

	/**
	 * Get Facebook friends of a member that uses the application.
	 *
	 * @param accessToken the facebook user's we want his Facebook friends
	 * @param listener listener that will retrieve the data after the request completes
	 */
	public void getFacebookFriends(String accessToken, CustomResponseListener<Member[]> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("access_token", accessToken);

		// TODO: send the member in the request or the token is enough ????
		GsonRequest<Member[]> request = new GsonRequest<>(
				getFullGETUrl(BASE_URL + "/facebookFriends", params),
				Request.Method.GET,
				Member[].class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}
}
