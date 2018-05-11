package com.seeu.team.edit;

import android.app.Activity;
import android.content.Intent;
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
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.common.membersearch.MemberSearchableActivity;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.GetAndShowImageFromUriAsyncTask;

import java.io.Serializable;
import java.util.ArrayList;

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

	private MemberRecyclerAdapter memberRecyclerAdapter;

	private EditText name;
	private EditText place;
	private EditText tags;
	private EditText textDescription;

	private Team team;
	private boolean isNewTeam;
	private boolean isPictureLayoutDrawn;
	private Uri chosenPictureUri;

	public EditTeamProfileActivity() {
		super();
		team = new Team();
		isNewTeam = true;
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

		chosenPicture.getViewTreeObserver().addOnPreDrawListener(this);

		updateTeamFromCaller();
		setupMemberRecycler();
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
			team.getMembers().add(member);
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
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, team.getMembers());
		memberRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				layoutManager.smoothScrollToPosition(memberRecycler, null, memberRecyclerAdapter.getItemCount());
			}
		});

		memberRecycler.setLayoutManager(layoutManager);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	private void updateTeamFromCaller() {
		Serializable ser = getIntent().getSerializableExtra(Team.INTENT_EXTRA_KEY);

		// If a team was passed by the caller, get it for update
		if (null != ser) {
			team = (Team) ser;
			isNewTeam = false;

			if (null == team.getMembers()) {
				team.setMembers(new ArrayList<>());
			}

			updateUI();
		}
	}

	private void updateUI() {
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
		if (isNewTeam && null == chosenPictureUri) {
			Toast.makeText(this, "You must select a picture", Toast.LENGTH_SHORT).show();
			return false;
		}

		team.setName(name.getText().toString());
		team.setPlace(place.getText().toString());
		team.setTags(tags.getText().toString());
		team.setDescription(textDescription.getText().toString());
		// TODO: How to add the creator (leader) of the team ?? Via token or add him first in the list ??

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
		intent.putExtra("members", team.getMembers());

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

		if (null != chosenPictureUri) {
			// TODO: Get data of picture, save it on cloud and get the url of the picture to save it in our DB
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

			String url = Team.DEBUG_PICTURE_URL;
			team.setPictureUrl(url);
		}

		if (isNewTeam) {
			// TODO: make http request to save the newly created team
		} else {
			// TODO: make http request to save the updated team
		}

		finish();
	}
}
