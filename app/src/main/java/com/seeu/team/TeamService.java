package com.seeu.team;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.seeu.common.AbstractService;
import com.seeu.member.Member;
import com.seeu.teamwall.TeamType;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.HashMap;
import java.util.Map;

public class TeamService extends AbstractService {

	public TeamService(Context context) {
		super(context, "/teams");
	}

	public void getTeams(TeamType teamType, CustomResponseListener<Team[]> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		params.put("selectedTypeId", String.valueOf(teamType.getId()));

		GsonRequest<Team[]> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				Team[].class,
				token,
				params,
				listener);

		queue.add(request);
	}

	public void getTeam(Member member, CustomResponseListener<Team> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		params.put("userId", String.valueOf(member.getId()));

		GsonRequest<Team> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				Team.class,
				token,
				params,
				listener);

		queue.add(request);
	}

	public void getMergedTeam(Team team, CustomResponseListener<Team> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		params.put("teamId", String.valueOf(team.getId()));

		GsonRequest<Team> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				Team.class,
				token,
				params,
				listener);

		queue.add(request);
	}

	public void createTeam(Team team, CustomResponseListener<Team> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		Gson gson = new Gson();
		params.put("team", gson.toJson(team));

		GsonRequest<Team> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.POST,
				Team.class,
				token,
				params,
				listener);

		queue.add(request);
	}

	public void likeTeam(Team likedTeam, CustomResponseListener<Void> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		params.put("likedTeamId", String.valueOf(likedTeam.getId()));

		GsonRequest<Void> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.POST,
				Void.class,
				token,
				params,
				listener);

		queue.add(request);
	}

	public void mergeTeam(Team teamToMerge, CustomResponseListener<Void> listener) {
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		params.put("teamToMergeId", String.valueOf(teamToMerge.getId()));

		GsonRequest<Void> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.POST,
				Void.class,
				token,
				params,
				listener);

		queue.add(request);
	}
}
