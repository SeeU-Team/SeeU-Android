package com.seeu.team;

import android.content.Context;

import com.android.volley.Request;
import com.seeu.common.AbstractService;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

/**
 * Created by thomasfouan on 14/07/2018.
 */
public class AssetService extends AbstractService {

	public AssetService(Context context) {
		super(context, "/api/medias/assets");
	}

	public void getAllAssets(CustomResponseListener<Asset[]> listener) {
		GsonRequest<Asset[]> gsonRequest = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				Asset[].class,
				getToken(),
				null,
				listener);

		queue.add(gsonRequest);
	}
}
