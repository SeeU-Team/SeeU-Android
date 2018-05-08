package com.seeu.teamprofile.edit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.seeu.R;
import com.seeu.common.Member;
import com.seeu.common.Team;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.GetAndShowImageFromUriAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.ViewTreeObserver.OnPreDrawListener;

/**
 * Created by thomasfouan on 08/05/2018.
 */

public class EditTeamProfileActivity extends Activity implements OnPreDrawListener {

	private static final int INTENT_PICK_IMAGE = 1;

	private ConstraintLayout pictureChooser;
	private ImageView pictureChosen;

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
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_teamprofile_activity);

		pictureChooser	= findViewById(R.id.teamPictureChooser);
		pictureChosen	= findViewById(R.id.teamPictureChosen);
		name			= findViewById(R.id.teamName);
		place			= findViewById(R.id.teamPlace);
		tags			= findViewById(R.id.teamTags);
		textDescription	= findViewById(R.id.teamTextDescription);

		setupMemberRecycler();

		pictureChooser.setOnClickListener(v -> {
			startAndroidPictureChooser();
		});

		pictureChosen.getViewTreeObserver().addOnPreDrawListener(this);

		getInfoFromCaller();
	}

	private void setupMemberRecycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, members);

		RecyclerView memberRecycler = findViewById(R.id.memberRecycler);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	private void getInfoFromCaller() {
		Bundle bundle = getIntent().getExtras();

		if (null != bundle && bundle.containsKey("TeamId")) {
			long teamId = bundle.getLong("TeamId");

			//loadTeam(teamId);
		}
	}

	private void startAndroidPictureChooser() {
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");

		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");

		Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

		startActivityForResult(chooserIntent, INTENT_PICK_IMAGE);
	}

	private void loadTeam(long id) {
		// TODO: Make http request to load data
		team = Team.getDebugTeam((int) id);

		pictureChooser.setVisibility(GONE);
		pictureChosen.setVisibility(View.VISIBLE);

		if (isPictureLayoutDrawn) {
			new DownloadImageAndSetBackgroundTask(pictureChosen, 10);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (INTENT_PICK_IMAGE == requestCode && RESULT_OK == resultCode) {
			if (null != data && null != data.getData()) {
				Uri uri = data.getData();

				new GetAndShowImageFromUriAsyncTask(getContentResolver(), pictureChosen, pictureChooser).execute(uri);
			}
		}
	}

	@Override
	public boolean onPreDraw() {
		isPictureLayoutDrawn = true;
		pictureChosen.getViewTreeObserver().removeOnPreDrawListener(this);

		if (null != team) {
			new DownloadImageAndSetBackgroundTask(pictureChosen, 10).execute(team.getPictureUrl());
		}

		return true;
	}

	public void onAddMemberBtnClick(View v) {

	}
}
