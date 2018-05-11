package com.seeu.team.edit;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import com.seeu.common.AbstractEditEntityActivity;
import com.seeu.common.subviews.PictureChooser;
import com.seeu.member.Member;
import com.seeu.team.Team;
import com.seeu.common.membersearch.MemberSearchableActivity;
import com.seeu.teamwall.TeamWallFragment;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.GetAndShowImageFromUriAsyncTask;

import java.io.Serializable;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.ViewTreeObserver.OnPreDrawListener;

/**
 * Created by thomasfouan on 08/05/2018.
 */

public class EditTeamProfileActivity extends AbstractEditEntityActivity<Team> {

	private static final int INTENT_ACTION_SEARCH = 2;

	private PictureChooser pictureChooser;
	private MemberRecyclerAdapter memberRecyclerAdapter;

	private EditText name;
	private EditText place;
	private EditText tags;
	private EditText textDescription;

	public EditTeamProfileActivity() {
		super();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_teamprofile_activity);

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
			Member member = (Member) data.getSerializableExtra(Member.INTENT_EXTRA_KEY);
			entity.getMembers().add(member);
			memberRecyclerAdapter.notifyItemInserted(memberRecyclerAdapter.getItemCount());
		}
	}

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
	 * Handle click event on add member's button.
	 * @param v
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
			entity.setPictureUrl(url);
		}

		if (isNewEntity) {
			// TODO: make http request to save the newly created team
		} else {
			// TODO: make http request to save the updated team
		}
	}

}
