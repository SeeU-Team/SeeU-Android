package com.seeu.teamwall;

import android.content.Context;

import com.android.volley.Request;
import com.seeu.common.AbstractService;
import com.seeu.common.Constants;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

/**
 * Created by thomasfouan on 10/06/2018.
 *
 * Service that make requests related to the TeamType entity to the API.
 */
public class TeamTypeService extends AbstractService {

	protected TeamTypeService(Context context) {
		super(context, "/teamTypes");
	}

	/**
	 * Get all the team's types from the database.
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getAllTeamTypes(CustomResponseListener<TeamType[]> listener) {
		String token = SharedPreferencesManager.getToken(context);

		GsonRequest<TeamType[]> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				TeamType[].class,
				token,
				null,
				listener);

		queue.add(request);
	}
}
