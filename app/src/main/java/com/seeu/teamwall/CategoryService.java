package com.seeu.teamwall;

import android.content.Context;

import com.android.volley.Request;
import com.seeu.common.AbstractService;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

/**
 * Created by thomasfouan on 10/06/2018.
 *
 * Service that make requests related to the Category entity to the API.
 */
public class CategoryService extends AbstractService {

	public CategoryService(Context context) {
		super(context, "/api/medias/categories");
	}

	/**
	 * Get all the team categories from the database.
	 * @param listener callback listener called when the response is available from the server
	 */
	public void getAllCategories(CustomResponseListener<Category[]> listener) {
		GsonRequest<Category[]> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.GET,
				Category[].class,
				getToken(),
				null,
				listener);

		queue.add(request);
	}
}
