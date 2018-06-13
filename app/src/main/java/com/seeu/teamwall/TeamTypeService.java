package com.seeu.teamwall;

import android.content.Context;

import com.android.volley.Request;
import com.seeu.common.AbstractService;
import com.seeu.common.Constants;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

public class TeamTypeService extends AbstractService {

	protected TeamTypeService(Context context) {
		super(context);
	}

	public void getAllTeamTypes(CustomResponseListener<TeamType[]> listener) {
		String url = Constants.SEEU_API_URL + "/teamTypes";
		String token = SharedPreferencesManager.getToken(context);

		GsonRequest<TeamType[]> request = new GsonRequest<>(
				url,
				Request.Method.GET,
				TeamType[].class,
				token,
				null,
				listener);

		queue.add(request);
	}
}
