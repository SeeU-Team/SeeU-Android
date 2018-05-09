package com.seeu.teamprofile.edit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.common.Member;
import com.seeu.common.Team;
import com.seeu.common.membersearch.MemberSearchableActivity;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.GetAndShowImageFromUriAsyncTask;
import com.seeu.utils.ImageUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.ViewTreeObserver.OnPreDrawListener;

/**
 * Created by thomasfouan on 08/05/2018.
 */

public class EditTeamProfileActivity extends Activity implements OnPreDrawListener {

	private static final int INTENT_PICK_IMAGE = 1;
	private static final int INTENT_ACTION_SEARCH = 2;

	private ConstraintLayout pictureChooser;
	private ImageView chosenPicture;
	private Uri chosenPictureUri;

	private MemberRecyclerAdapter memberRecyclerAdapter;
	private List<Member> members;

	private EditText name;
	private EditText place;
	private EditText tags;
	private EditText textDescription;

	private Team team;
	private boolean isPictureLayoutDrawn;

	public EditTeamProfileActivity() {
		super();
		members = new ArrayList<>();
		team = null;
		isPictureLayoutDrawn = false;
		chosenPictureUri = null;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_teamprofile_activity);

		pictureChooser	= findViewById(R.id.teamPictureChooser);
		chosenPicture = findViewById(R.id.teamPictureChosen);
		name			= findViewById(R.id.teamName);
		place			= findViewById(R.id.teamPlace);
		tags			= findViewById(R.id.teamTags);
		textDescription	= findViewById(R.id.teamTextDescription);

		setupMemberRecycler();

		chosenPicture.getViewTreeObserver().addOnPreDrawListener(this);

		getInfoFromCaller();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (INTENT_PICK_IMAGE == requestCode && RESULT_OK == resultCode) {
			if (null != data && null != data.getData()) {
				chosenPictureUri = data.getData();

				new GetAndShowImageFromUriAsyncTask(getContentResolver(), chosenPicture, pictureChooser).execute(chosenPictureUri);
			}
		} else if (INTENT_ACTION_SEARCH == requestCode && RESULT_OK == resultCode) {
			Member member = (Member) data.getSerializableExtra("member");
			members.add(member);
			memberRecyclerAdapter.notifyItemInserted(memberRecyclerAdapter.getItemCount());
		}
	}

	@Override
	public boolean onPreDraw() {
		isPictureLayoutDrawn = true;
		chosenPicture.getViewTreeObserver().removeOnPreDrawListener(this);

		if (null != team) {
			new DownloadImageAndSetBackgroundTask(chosenPicture, 10).execute(team.getPictureUrl());
		}

		return true;
	}

	private void setupMemberRecycler() {
		RecyclerView memberRecycler = findViewById(R.id.memberRecycler);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, members);
		memberRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				layoutManager.smoothScrollToPosition(memberRecycler, null, memberRecyclerAdapter.getItemCount());
			}
		});

		memberRecycler.setLayoutManager(layoutManager);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	private void getInfoFromCaller() {
		Bundle bundle = getIntent().getExtras();

		if (null != bundle && bundle.containsKey("TeamId")) {
			long teamId = bundle.getLong("TeamId");

			loadTeam(teamId);
		}
	}

	private void loadTeam(long id) {
		// TODO: Make http request to load data
		team = Team.getDebugTeam((int) id);

		members.addAll(team.getMembers());
		team.setMembers(members);

		pictureChooser.setVisibility(GONE);
		chosenPicture.setVisibility(View.VISIBLE);

		if (isPictureLayoutDrawn) {
			new DownloadImageAndSetBackgroundTask(chosenPicture, 10);
		}

		name.setText(team.getName());
		place.setText(team.getPlace());
		tags.setText(team.getTags());
		textDescription.setText(team.getDescription());
	}

	private boolean isTeamValid() {
		if (null == team) {
			if (null == chosenPictureUri) {
				Toast.makeText(this, "You must select a picture", Toast.LENGTH_SHORT).show();
				return false;
			}

			team = new Team();
		}

		team.setName(name.getText().toString());
		team.setPlace(place.getText().toString());
		team.setTags(tags.getText().toString());
		team.setDescription(textDescription.getText().toString());
		// TODO: How to add the creator (leader) of the team ?? Via token or add him first in the list ??
		team.setMembers(members);

		return true;
	}

	/**
	 * Handle click event on picture chooser.
	 * @param v
	 */
	public void startAndroidPictureChooser(View v) {
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");

		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");

		Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

		startActivityForResult(chooserIntent, INTENT_PICK_IMAGE);
	}

	/**
	 * Handle click event on add member's button.
	 * @param v
	 */
	public void startMemberSearchActivity(View v) {
		Intent intent = new Intent(this, MemberSearchableActivity.class);

		long[] ids = new long[members.size()];
		for (int i = 0; i < members.size(); i++) {
			ids[i] = members.get(i).getId();
		}
		intent.putExtra("membersId", ids);

		startActivityForResult(intent, INTENT_ACTION_SEARCH);
	}

	/**
	 * Handle click event on cancel button.
	 * @param v
	 */
	public void cancelForm(View v) {
		finish();
	}

	/**
	 * Handle click event on validation button.
	 * @param v
	 */
	public void validForm(View v) {
		if (!isTeamValid()) {
			return;
		}

		String url;

		if (null != chosenPictureUri) {
			// TODO: Get data of picture, save it on cloud and get the url of the picture to save it in our DB
			url = Team.DEBUG_PICTURE_URL;

//			HttpURLConnection urlConnection = null;
//			try {
//				InputStream imageStream = getContentResolver().openInputStream(chosenPictureUri);
//
//				URL httpUrl = new URL("http://toto.tata.fr");
//				urlConnection = (HttpURLConnection) httpUrl.openConnection();
//				urlConnection.setDoInput(true);
//				urlConnection.setDoOutput(true);
//				urlConnection.setChunkedStreamingMode(0);
//				urlConnection.setRequestMethod("POST");
//				urlConnection.setRequestProperty("Content-Type", "multipart/form-data");
//
//				OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//
//				byte[] buffer = new byte[100000];
//				while ((imageStream.read(buffer)) != -1) {
//					out.write(buffer);
//				}
//
//				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//				url = in.toString();
//
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (null != urlConnection) {
//					urlConnection.disconnect();
//				}
//			}
		} else {
			url = team.getPictureUrl();
		}

		team.setPictureUrl(url);

		// TODO: make http request to save the newly created team
		finish();
	}
}
