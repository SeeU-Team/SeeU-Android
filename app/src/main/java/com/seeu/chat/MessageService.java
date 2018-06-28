package com.seeu.chat;

import android.content.Context;

import com.android.volley.Request;
import com.seeu.common.AbstractService;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomasfouan on 19/06/2018.
 *
 * Service that make requests related to the messages to the API.
 */
public class MessageService extends AbstractService {

	public MessageService(Context context) {
		super(context, "/api/messages");
	}

	/**
	 * Get the messages between the current user and another user.
	 * @param currentUser the current user
	 * @param otherUser the other user
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getMessages(Member currentUser, Member otherUser, CustomResponseListener<Message[]> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(2);
		params.put("firstUserId", String.valueOf(currentUser.getId()));
		params.put("secondUserId", String.valueOf(otherUser.getId()));

		GsonRequest<Message[]> request = new GsonRequest<>(
				getFullGETUrl(BASE_URL, params),
				Request.Method.GET,
				Message[].class,
				token,
				null,
				listener);

		// Add the request to the RequestQueue.
		queue.add(request);
	}

	/**
	 * Get the messages of a team.
	 * @param team the team
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getMessages(Team team, CustomResponseListener<Message[]> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		params.put("teamId", String.valueOf(team.getId()));

		GsonRequest<Message[]> request = new GsonRequest<>(
				getFullGETUrl(BASE_URL, params),
				Request.Method.GET,
				Message[].class,
				token,
				null,
				listener);

		// Add the request to the RequestQueue.
		queue.add(request);
	}
}
