package com.seeu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class ConnectionActivity extends AppCompatActivity {

	private CallbackManager callbackManager;

	private LoginButton loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);

		boolean loggedIn = AccessToken.getCurrentAccessToken() == null;

		if (loggedIn) {
			// Go to the main activity and skip this activity
			Log.d("Facebook", "already logged in");
		}

		callbackManager = CallbackManager.Factory.create();
		loginButton = findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile");

		// If using in a fragment
		//loginButton.setFragment(this);

		// Callback registration
		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code
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
