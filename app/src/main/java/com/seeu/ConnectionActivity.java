package com.seeu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.seeu.common.Constants;
import com.seeu.member.Member;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;
import com.seeu.utils.network.GsonRequest;

import java.util.Map;

public class ConnectionActivity extends Activity implements FacebookCallback<LoginResult>, CustomResponseListener<Member> {

	private CallbackManager callbackManager;

	private LoginButton loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_activity);

		final AccessToken accessToken = AccessToken.getCurrentAccessToken();

		if (null != accessToken) {
			Log.d("Facebook", "already logged in");
			goToTeamWall(accessToken);
		}

		callbackManager = CallbackManager.Factory.create();
		loginButton = findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile", "user_photos");

		// If using in a fragment
		//loginButton.setFragment(this);

		// Callback registration
		loginButton.registerCallback(callbackManager, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSuccess(LoginResult loginResult) {
		Log.d("Facebook", "Login success");
		goToTeamWall(loginResult.getAccessToken());
	}

	@Override
	public void onCancel() {
		System.out.println("Facebook login canceled");
	}

	@Override
	public void onError(FacebookException exception) {
		System.out.println("Facebook login error");
		exception.printStackTrace();
	}

	private void goToTeamWall(final AccessToken accessToken) {
		/*
			GraphRequest request = GraphRequest.newMeRequest(
					accessToken,
					(object, response) -> {
						Log.d("Facebook Graph", object.toString());
					});

			Bundle parameters = new Bundle();
			parameters.putString("fields", "id,name,link,albums,picture.type(large),cover");
			request.setParameters(parameters);
			request.executeAsync();
			*/

		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = Constants.SEEU_API_URL + "/login";

		// Request a string response from the provided URL.
		GsonRequest<Member> request = new GsonRequest<>(
				url,
				Member.class,
				accessToken.getToken(),
				this);

		// Add the request to the RequestQueue.
		queue.add(request);
	}

	@Override
	public void onResponse(Member response) {
		// TODO: get user info from the response and save it
		SharedPreferencesManager.putEntity(this, response);

		Intent intent = new Intent(ConnectionActivity.this, TabbedActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
		String token = headers.get("Authorization").split(" ")[1];

		// Save it in the shared preferences
		// TODO: encrypt the token before save it
		SharedPreferencesManager.putToken(this, token);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		error.printStackTrace();
		System.out.println(error.getMessage());
	}
}
