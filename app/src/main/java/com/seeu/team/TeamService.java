package com.seeu.team;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.seeu.common.AbstractService;
import com.seeu.member.Member;
import com.seeu.teamwall.TeamType;
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
		super(context, "/teams");
	}

	/**
	 * Get all the teams of the type passed in parameter.
	 * @param teamType the type of the teams
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getTeams(TeamType teamType, CustomResponseListener<Team[]> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("selectedTypeId", String.valueOf(teamType.getId()));

		GsonRequest<Team[]> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				Team[].class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	/**
	 * Get the team of the current user if he belongs to one. Otherwise, return NOT_FOUND (404) status.
	 * @param member the member for which we want the team
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getTeam(Member member, CustomResponseListener<Team> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("userId", String.valueOf(member.getId()));

		// TODO: send the member in the request or the token is enough ????
		GsonRequest<Team> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				Team.class,
				getToken(),
				params,
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
		params.put("profilePicture", imageBase64);

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
			params.put("profilePicture", imageBase64);
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

	/**
	 * Get all teams that the team passed in parameter has mutually liked.
	 * @param team the team for which we want the get the liked teams
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getLikedTeams(Team team, CustomResponseListener<Team[]> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("teamId", String.valueOf(team.getId()));

		GsonRequest<Team[]> request = new GsonRequest<>(
				BASE_URL + "/liked",
				Request.Method.GET,
				Team[].class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	/**
	 * Get the team that the team passed in parameter has merged with.
	 * @param team the team for which we want the get the merged team
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getMergedTeam(Team team, CustomResponseListener<Team> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("teamId", String.valueOf(team.getId()));

		GsonRequest<Team> request = new GsonRequest<>(
				BASE_URL + "/merged",
				Request.Method.GET,
				Team.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	/**
	 * Like the team if the current member is leader, or send notification to leader if not.
	 * The user's token provided along with the request allows the API to get the team he belongs to, and his status (leader or not).
	 * @param likedTeam the team that the leader liked
	 * @param listener callback listener called when the response is available from the server
	 */
	public void likeTeam(Team likedTeam, CustomResponseListener<Void> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("likedTeamId", String.valueOf(likedTeam.getId()));

		GsonRequest<Void> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.POST,
				Void.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	/**
	 * Merge the team of the user (leader) with the team passed in parameter.
	 * @param teamToMerge the team the leader wants to merge with
	 * @param listener callback listener called when the response is available from the server
	 */
	public void mergeTeam(Team teamToMerge, CustomResponseListener<Void> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("teamToMergeId", String.valueOf(teamToMerge.getId()));

		GsonRequest<Void> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.POST,
				Void.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}
}
