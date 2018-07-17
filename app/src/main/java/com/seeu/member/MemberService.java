package com.seeu.member;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.google.gson.Gson;
import com.seeu.common.AbstractService;
import com.seeu.utils.ImageUtils;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomasfouan on 14/06/2018.
 *
 * Service that communicates with the API for Member entity.
 */
public class MemberService extends AbstractService {

	public MemberService(Context context) {
		super(context, "/api/users");
	}

	public void updateMember(Member member, Bitmap bitmapImage, CustomResponseListener<Void> listener) {
		if (null != bitmapImage) {
			new UpdateMemberWithNewPictureAsyncTask(member, listener).execute(bitmapImage);
		} else {
			updateMember(member, (String) null, listener);
		}
	}

	/**
	 * Save the updated member passed in parameter in the Database.
	 * @param member the member to save
	 * @param imageBase64 the new profile picture encoded in Base64. May be null if not updated
	 * @param listener callback listener called when the response is available from the server
	 */
	private void updateMember(Member member, String imageBase64, CustomResponseListener<Void> listener) {
		Map<String, String> params = new HashMap<>(2);
		Gson gson = new Gson();
		params.put("member", gson.toJson(member));

		if (null != imageBase64) {
			params.put("profilePicture", "\"" + imageBase64 + "\"");
		} else {
			params.put("profilePicture", null);
		}

		GsonRequest<Void> request = new GsonRequest<>(
				BASE_URL,
				Request.Method.PUT,
				Void.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	public void updateAppInstanceId(Member member, String appInstanceId, CustomResponseListener<Void> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("appInstanceId", appInstanceId);

		GsonRequest<Void> request = new GsonRequest<>(
				BASE_URL + "/" + member.getId() + "/appInstanceId",
				Request.Method.PUT,
				Void.class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	/**
	 * Get friends of a member.
	 * A friend is a member that had a previous discussion with the current member.
	 * @param currentMember the member we want his friends
	 * @param listener listener that will retrieve the data after the request completes
	 */
	public void getFriends(Member currentMember, CustomResponseListener<Member[]> listener) {
		String fullUrl = BASE_URL + "/" + currentMember.getId() + "/friends";

		// TODO: send the member in the request or the token is enough ????
		GsonRequest<Member[]> request = new GsonRequest<>(
				fullUrl,
				Request.Method.GET,
				Member[].class,
				getToken(),
				null,
				listener);

		queue.add(request);
	}

	/**
	 * Get Facebook friends of a member that uses the application.
	 *
	 * @param listener listener that will retrieve the data after the request completes
	 */
	public void getFacebookFriends(CustomResponseListener<Member[]> listener) {
		Map<String, String> params = new HashMap<>(1);
		params.put("access_token", AccessToken.getCurrentAccessToken().getToken());

		// TODO: send the member in the request or the token is enough ????
		GsonRequest<Member[]> request = new GsonRequest<>(
				getFullGETUrl(BASE_URL + "/facebookFriends", params),
				Request.Method.GET,
				Member[].class,
				getToken(),
				params,
				listener);

		queue.add(request);
	}

	private class UpdateMemberWithNewPictureAsyncTask extends AsyncTask<Bitmap, Void, String> {

		private Member member;
		private CustomResponseListener<Void> listener;

		public UpdateMemberWithNewPictureAsyncTask(Member member, CustomResponseListener<Void> listener) {
			this.member = member;
			this.listener = listener;
		}

		@Override
		protected String doInBackground(Bitmap... bitmaps) {
			Bitmap bitmap = bitmaps[0];

			return null != bitmap
					? ImageUtils.getStringImage(bitmap)
					: null;
		}

		@Override
		protected void onPostExecute(String s) {
			if (null != s) {
				updateMember(member, s, listener);
			}
		}
	}
}
