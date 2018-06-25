package com.seeu.team.edit;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.common.AbstractEditEntityActivity;
import com.seeu.common.subviews.PictureChooser;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.common.membersearch.MemberSearchableActivity;
import com.seeu.team.TeamService;
import com.seeu.utils.ImageUtils;
import com.seeu.utils.network.CustomResponseListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by thomasfouan on 08/05/2018.
 *
 * Activity that display the edition of team's profile.
 */
public class EditTeamProfileActivity extends AbstractEditEntityActivity<Team> implements CustomResponseListener<Team> {

	private static final int INTENT_ACTION_SEARCH = 2;

	private PictureChooser pictureChooser;
	private MemberRecyclerAdapter memberRecyclerAdapter;

	private EditText name;
	private EditText place;
	private EditText tags;
	private EditText textDescription;

	private TeamService teamService;

	public EditTeamProfileActivity() {
		super();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_teamprofile_activity);

		this.teamService = new TeamService(this);

		pictureChooser = new PictureChooser();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout_picture_chooser, pictureChooser);
		transaction.commit();

		name			= findViewById(R.id.teamName);
		place			= findViewById(R.id.teamPlace);
		tags			= findViewById(R.id.teamTags);
		textDescription	= findViewById(R.id.teamTextDescription);

		setupMemberRecycler();
		updateUI();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (INTENT_ACTION_SEARCH == requestCode && RESULT_OK == resultCode) {
			Member member = (Member) data.getSerializableExtra(Member.STORAGE_KEY);
			entity.getMembers().add(member);
			memberRecyclerAdapter.notifyItemInserted(memberRecyclerAdapter.getItemCount());
		}
	}

	/**
	 * Setup the recycler view to display the member list.
	 */
	private void setupMemberRecycler() {
		RecyclerView memberRecycler = findViewById(R.id.memberRecycler);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, entity.getMembers());
		memberRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				layoutManager.smoothScrollToPosition(memberRecycler, null, memberRecyclerAdapter.getItemCount());
			}
		});

		memberRecycler.setLayoutManager(layoutManager);
		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	/**
	 * Starts the member search activity when the user clicks on add member's button.
	 * @param v the button clicked
	 */
	public void startMemberSearchActivity(View v) {
		Intent intent = new Intent(this, MemberSearchableActivity.class);
		intent.putExtra("members", entity.getMembers());

		startActivityForResult(intent, INTENT_ACTION_SEARCH);
	}

	@Override
	protected Team getEntityInstance() {
		return new Team();
	}

	@Override
	protected void updateUI() {
//		if (null == team.getMembers()) {
//			team.setMembers(new ArrayList<>());
//		}
		pictureChooser.setCurrentPictureUrl(entity.getPictureUrl());

		name.setText(entity.getName());
		place.setText(entity.getPlace());
		tags.setText(entity.getTags());
		textDescription.setText(entity.getDescription());
	}

	@Override
	protected boolean isEntityValid() {
		if (isNewEntity
				&& null == pictureChooser.getChosenPictureUri()) {
			Toast.makeText(this, "You must select a picture", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	protected void updateEntity() {
		entity.setName(name.getText().toString());
		entity.setPlace(place.getText().toString());
		entity.setTags(tags.getText().toString());
		entity.setDescription(textDescription.getText().toString());
		// TODO: How to add the creator (leader) of the team ?? Via token or add him first in the list ??
	}

	@Override
	protected void saveEntity() {
		Uri chosenPictureUri = pictureChooser.getChosenPictureUri();
		String imageBase64 = null;

		if (null != chosenPictureUri) {
			try {
				// Get data of picture
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), chosenPictureUri);
				//encoding image to string
				imageBase64 = ImageUtils.getStringImage(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
				imageBase64 = null;
			}
		}

		if (isNewEntity) {
			// Save the newly created team with its picture
			teamService.createTeam(entity, imageBase64, this);
		} else {
			// Save the updated team with its new picture or not
			teamService.updateTeam(entity, imageBase64, this);
		}
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		error.printStackTrace();
		String message = "An error occurred while trying to " + ((isNewEntity) ? "create" : "update") + " the team";

		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(Team response) {
		// End this activity when successfully save the team
		finish();
	}
}
