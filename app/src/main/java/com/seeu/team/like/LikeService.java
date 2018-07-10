package com.seeu.team.like;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.seeu.common.AbstractService;
import com.seeu.team.Team;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomasfouan on 29/06/2018.
 */
public class LikeService extends AbstractService {

	public LikeService(Context context) {
		super(context, "/api/likes");
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
				getFullGETUrl(BASE_URL, params),
				Request.Method.GET,
				Team[].class,
				getToken(),
				null,
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
				getFullGETUrl(BASE_URL + "/merged", params),
				Request.Method.GET,
				Team.class,
				getToken(),
				null,
				listener);

		queue.add(request);
	}

	public void getAllTeamsAlreadyMergedByTeam(Team team, CustomResponseListener<Merge[]> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("teamId", String.valueOf(team.getId()));

		GsonRequest<Merge[]> request = new GsonRequest<>(
				getFullGETUrl(BASE_URL + "/allMerged", params),
				Request.Method.GET,
				Merge[].class,
				getToken(),
				null,
				listener);

		queue.add(request);
	}

	/**
	 * Like the team if the current member is leader, or send notification to leader if not.
	 * The user's token provided along with the request allows the API to get the team he belongs to, and his status (leader or not).
	 * @param likedTeam the team that the leader liked
	 * @param listener callback listener called when the response is available from the server
	 */
	public void likeTeam(Team myTeam, Team likedTeam, CustomResponseListener<Like> listener) {
		Gson gson = new Gson();
		Like like = Like.builder()
				.idLike(myTeam.getId())
				.idLiked(likedTeam.getId())
				.build();

		Map<String, String> params = new HashMap<>(1);
		params.put("like", gson.toJson(like));

		GsonRequest<Like> request = new GsonRequest<>(
				BASE_URL + "/like",
				Request.Method.POST,
				Like.class,
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
	public void mergeTeam(Team myTeam, Team teamToMerge, CustomResponseListener<Merge> listener) {
		Gson gson = new Gson();
		Merge merge = Merge.builder()
				.idFirst(myTeam.getId())
				.idSecond(teamToMerge.getId())
				.build();

		Map<String, String> params = new HashMap<>(1);
		params.put("merge", gson.toJson(merge));

		GsonRequest<Merge> request = new GsonRequest<>(
				BASE_URL + "/merge",
				Request.Method.POST,
				Merge.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}
}
