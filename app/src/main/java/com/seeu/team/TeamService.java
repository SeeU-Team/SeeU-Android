package com.seeu.team;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.seeu.common.AbstractService;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.teamwall.Category;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomasfouan on 10/06/2018.
 *
 * Service that make requests related to the Team entity to the API.
 */
public class TeamService extends AbstractService {

	public TeamService(Context context) {
		super(context, "/api/teams");
	}

	/**
	 * Get all the teams of the category passed in parameter.
	 * @param category the category of the teams to load
	 * @param team the team of the current user
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getTeams(Category category, Team team, CustomResponseListener<Team[]> listener) {
		Map<String, String> params = new HashMap<>(2);
		params.put("categoryId", String.valueOf(category.getId()));
		params.put("teamId", String.valueOf(team.getId()));

		GsonRequest<Team[]> request = new GsonRequest<>(
				getFullGETUrl(BASE_URL, params),
				Request.Method.GET,
				Team[].class,
				getToken(),
				null,
				listener);

		queue.add(request);
	}

	/**
	 * Get the team of the current user if he belongs to one. Otherwise, return NOT_FOUND (404) status.
	 * @param member the member for which we want the team
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getTeam(Member member, CustomResponseListener<MemberHasTeam> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("memberId", String.valueOf(member.getId()));

		// TODO: send the member in the request or the token is enough ????
		GsonRequest<MemberHasTeam> request = new GsonRequest<>(
				getFullGETUrl(BASE_URL, params),
				Request.Method.GET,
				MemberHasTeam.class,
				getToken(),
				null,
				listener);

		queue.add(request);
	}

	/**
	 * Save the new team passed in parameter in the Database.
	 * @param team the team to save
	 * @param imageBase64 the profile picture encoded in Base64
	 * @param listener callback listener called when the response is available from the server
	 */
	public void createTeam(Team team, String imageBase64, CustomResponseListener<Team> listener) {
		if (null == imageBase64) {
			throw new IllegalArgumentException("The image must not be null");
		}

		Map<String, String> params = new HashMap<>(2);
		Gson gson = new Gson();
		params.put("team", gson.toJson(team));
		params.put("profilePicture", "\"" + imageBase64 + "\"");

		GsonRequest<Team> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.POST,
				Team.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	/**
	 * Save the updated team passed in parameter in the Database.
	 * @param team the team to save
	 * @param imageBase64 the new profile picture encoded in Base64. May be null if not updated
	 * @param listener callback listener called when the response is available from the server
	 */
	public void updateTeam(Team team, String imageBase64, CustomResponseListener<Team> listener) {
		Map<String, String> params = new HashMap<>(2);
		Gson gson = new Gson();
		params.put("team", gson.toJson(team));

		if (null != imageBase64) {
			params.put("profilePicture", "\"" + imageBase64 + "\"");
		} else {
			params.put("profilePicture", null);
		}

		GsonRequest<Team> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.PUT,
				Team.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}
}
