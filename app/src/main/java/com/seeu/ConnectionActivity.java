package com.seeu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.seeu.member.AuthenticationService;
import com.seeu.member.Member;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.util.Map;

/**
 * Created by thomasfouan on 16/02/2018.
 *
 * The starting activity of the application. Display the Facebook Login required to access to the application.
 */
public class ConnectionActivity extends Activity implements FacebookCallback<LoginResult>, CustomResponseListener<Member> {

	private CallbackManager callbackManager;
	private LoginButton loginButton;
	private TextView errorView;

	private AuthenticationService authenticationService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection_activity);

		this.authenticationService = new AuthenticationService(this);

		final AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (null != accessToken) {
			Log.d("Facebook", "already logged in");
			loadMemberInfo(accessToken);
		}

		callbackManager = CallbackManager.Factory.create();
		loginButton = findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile", "user_photos", "user_friends");
		// Callback registration
		loginButton.registerCallback(callbackManager, this);

		errorView = findViewById(R.id.error_login);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		callbackManager.onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSuccess(LoginResult loginResult) {
		Log.d("Facebook", "Login success");
		loadMemberInfo(loginResult.getAccessToken());
	}

	@Override
	public void onCancel() {
		System.out.println("Facebook login canceled");
	}

	@Override
	public void onError(FacebookException exception) {
		System.out.println("Facebook login error");
		exception.printStackTrace();
		errorView.setText(exception.toString());
	}

	/**
	 * Load information about the connected user with the access token provided by Facebook.
	 * Start the main activity when the request is done.
	 * @param accessToken the access token provided by Facebook
	 */
	private void loadMemberInfo(final AccessToken accessToken) {

//		GraphRequest request = GraphRequest.newMeRequest(
//				accessToken,
//				(object, response) -> {
//					Log.d("Facebook Graph", object.toString());
//				});
//
//		Bundle parameters = new Bundle();
//		parameters.putString("fields", "id,name,link,picture.type(large),cover,friends");
//		request.setParameters(parameters);
//		request.executeAsync();

		SharedPreferencesManager.putFacebookToken(this, accessToken.getToken());
		authenticationService.getMember(accessToken, this);
	}

	@Override
	public void onResponse(Member response) {
		SharedPreferencesManager.putEntity(this, Member.STORAGE_KEY, response);

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
		errorView.setText(error.getLocalizedMessage());
	}
}
