package com.seeu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class ConnectionActivity extends Activity {

	private CallbackManager callbackManager;

	private LoginButton loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_activity);

		final AccessToken accessToken = AccessToken.getCurrentAccessToken();

		if (null != accessToken) {
			// Go to the main activity and skip this activity
			Log.d("Facebook", "already logged in");
			Log.d("Facebook", accessToken.getToken());

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

			Intent intent = new Intent(ConnectionActivity.this, TabbedActivity.class);
			startActivity(intent);
			finish();
		}

		callbackManager = CallbackManager.Factory.create();
		loginButton = findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile", "user_photos");

		// If using in a fragment
		//loginButton.setFragment(this);

		// Callback registration
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code
				Log.d("Facebook", "Login success");

				Intent intent = new Intent(ConnectionActivity.this, TabbedActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onCancel() {
				// App code
			}

			@Override
			public void onError(FacebookException exception) {
				// App code
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
