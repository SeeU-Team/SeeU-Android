package com.seeu.team.edit;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.common.AbstractEditEntityActivity;
import com.seeu.common.subviews.PictureChooser;
import com.seeu.member.Member;
import com.seeu.team.Asset;
import com.seeu.team.AssetService;
import com.seeu.team.Team;
import com.seeu.common.membersearch.MemberSearchableActivity;
import com.seeu.team.TeamService;
import com.seeu.team.profile.AssetRecyclerAdapter;
import com.seeu.teamwall.Category;
import com.seeu.teamwall.CategoryRecyclerAdapter;
import com.seeu.teamwall.CategoryService;
import com.seeu.utils.ImageUtils;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

	private CategoryRecyclerAdapter categoryRecyclerAdapter;
	private List<Category> categories;
	private CategoryService categoryService;

	private AssetRecyclerAdapter assetRecyclerAdapter;
	private List<Asset> assets;
	private AssetService assetService;

	private EditText name;
	private EditText place;
	private EditText tags;
	private EditText textDescription;

	private TeamService teamService;

	public EditTeamProfileActivity() {
		super();
		categories = new ArrayList<>();
		assets = new ArrayList<>();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_teamprofile_activity);

		teamService = new TeamService(this);
		categoryService = new CategoryService(this);
		assetService = new AssetService(this);

		pictureChooser = new PictureChooser();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout_picture_chooser, pictureChooser);
		transaction.commit();

		name			= findViewById(R.id.teamName);
		place			= findViewById(R.id.teamPlace);
		tags			= findViewById(R.id.teamTags);
		textDescription	= findViewById(R.id.teamTextDescription);

		setupMemberRecycler();
		setupCategoryRecycler();
		setupAssetRecycler();

		loadCategories();
		loadAssets();
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

		// Keep reference of the dataset (arraylist here) in the adapter
		memberRecyclerAdapter = new MemberRecyclerAdapter(this, entity.getMembers());
		memberRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				memberRecycler.getLayoutManager().smoothScrollToPosition(memberRecycler, null, memberRecyclerAdapter.getItemCount());
			}
		});

		memberRecycler.setAdapter(memberRecyclerAdapter);
	}

	/**
	 * Method that set up the recycler view for the team categories.
	 */
	private void setupCategoryRecycler() {
		// Keep reference of the dataset (arraylist here) in the adapter
		categoryRecyclerAdapter = new CategoryRecyclerAdapter(this, categories, this::onCategoryClick);

		// set up the RecyclerView for the categories of team
		RecyclerView categoryRecycler = findViewById(R.id.categoryRecycler);
		categoryRecycler.setAdapter(categoryRecyclerAdapter);
	}

	private void setupAssetRecycler() {
		assetRecyclerAdapter = new AssetRecyclerAdapter(this, assets, this::onAssetClick);

		RecyclerView assetRecycler = findViewById(R.id.assetRecycler);
		assetRecycler.setAdapter(assetRecyclerAdapter);
	}

	/**
	 * Load all the categories of teams. Refresh teams when received.
	 */
	private void loadCategories() {
		categoryService.getAllCategories(new CustomResponseListener<Category[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("EditTeamProfileActivity", "Error while loading categories", error);
				Toast.makeText(EditTeamProfileActivity.this, "Error while loading categories " + error.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResponse(Category[] response) {
				Collections.addAll(categories, response);

				if (null != categoryRecyclerAdapter) {
					categoryRecyclerAdapter.setSelected(0);
					categoryRecyclerAdapter.notifyDataSetChanged();
				}

				updateSelectedCategory();
			}
		});
	}

	private void loadAssets() {
		assetService.getAllAssets(new CustomResponseListener<Asset[]>() {
			@Override
			public void onHeadersResponse(Map<String, String> headers) {
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("EditTeamProfileActivity", "Error while loading assets", error);
				Toast.makeText(EditTeamProfileActivity.this, "Error while loading assets " + error.getMessage(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onResponse(Asset[] response) {
				Collections.addAll(assets, response);

				if (null != assetRecyclerAdapter) {
					assetRecyclerAdapter.notifyDataSetChanged();
				}

				updateSelectedAssets();
			}
		});
	}

	public void onCategoryClick(View view, int position) {
		categoryRecyclerAdapter.setSelected(position);
	}

	public void onAssetClick(View view, int position) {
		assetRecyclerAdapter.select(position);
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
		pictureChooser.setCurrentPictureUrl(entity.getProfilePhotoUrl());

		name.setText(entity.getName());
		place.setText(entity.getPlace());
		tags.setText(entity.getTagsAsString());
		textDescription.setText(entity.getDescription());

		updateSelectedCategory();
		updateSelectedAssets();
	}

	private void updateSelectedCategory() {
		if (null == categoryRecyclerAdapter) {
			return;
		}

		for (Category actualCategory : entity.getCategories()) {
			for (int i = 0; i < categories.size(); i++) {
				if (actualCategory.equals(categories.get(i))) {
					// For now, we can select only one category
					categoryRecyclerAdapter.setSelected(i);
					break;
				}
			}
		}
	}

	private void updateSelectedAssets() {
		if (null == assetRecyclerAdapter) {
			return;
		}

		for (Asset asset : entity.getAssets()) {
			for (int i = 0; i < assets.size(); i++) {
				if (asset.equals(assets.get(i))) {
					assetRecyclerAdapter.select(i);
				}
			}
		}
	}

	@Override
	protected boolean isEntityValid() {
		if (isNewEntity) {
			if (null == pictureChooser.getChosenPictureUri()) {
				Toast.makeText(this, "You must select a picture", Toast.LENGTH_SHORT).show();
				return false;
			} else if (name.getText().toString().trim().isEmpty()) {
				Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		return true;
	}

	@Override
	protected void updateEntity() {
		entity.setName(name.getText().toString());
		entity.setPlace(place.getText().toString());
		entity.setTagsFromString(tags.getText().toString());
		entity.setDescription(textDescription.getText().toString());

		// Add the current user first in the list of members to make him LEADER
		if (isNewEntity) {
			Member currentUser = SharedPreferencesManager.getEntity(this, Member.STORAGE_KEY, Member.class);
			entity.getMembers().add(0, currentUser);
		}

		entity.getCategories().clear();
		entity.getCategories().add(categoryRecyclerAdapter.getSelectedItem());

		entity.getAssets().clear();
		entity.getAssets().addAll(assetRecyclerAdapter.getSelectedItems());
	}

	@Override
	protected void saveEntity() {
		Uri chosenPictureUri = pictureChooser.getChosenPictureUri();
		Bitmap bitmap = null;

		if (null != chosenPictureUri) {
			try {
				// Get data of picture
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), chosenPictureUri);
				//encoding image to string
			} catch (IOException e) {
				e.printStackTrace();
				bitmap = null;
			}
		}

		if (isNewEntity) {
			// Save the newly created team with its picture
			teamService.createTeam(entity, bitmap, this);
		} else {
			// Save the updated team with its new picture or not
			teamService.updateTeam(entity, bitmap, this);
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
		// TODO: if update, no team is returned
		if (isNewEntity) {
			SharedPreferencesManager.putEntity(this, Team.STORAGE_KEY, response);
		}
		// End this activity when successfully save the team

		Intent returnIntent = new Intent();
		returnIntent.putExtra("result", "ok");
		setResult(Activity.RESULT_OK, returnIntent);
		finish();
	}
}
