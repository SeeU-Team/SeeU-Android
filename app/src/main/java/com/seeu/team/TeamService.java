package com.seeu.team;

import android.content.Context;

import com.android.volley.Request;
import com.seeu.common.AbstractService;
import com.seeu.common.Constants;
import com.seeu.teamwall.TeamType;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.HashMap;
import java.util.Map;

public class TeamService extends AbstractService {

	public TeamService(Context context) {
		super(context);
	}

	public void getTeams(TeamType teamType, CustomResponseListener<Team[]> listener) {
		String url = Constants.SEEU_API_URL + "/teams";
		String token = SharedPreferencesManager.getToken(context);
		Map<String, String> params = new HashMap<>(1);
		params.put("selectedTypeId", String.valueOf(teamType.getId()));

		GsonRequest<Team[]> request = new GsonRequest<>(
				url,
				Request.Method.GET,
				Team[].class,
				token,
				params,
				listener);

		queue.add(request);
	}
}
