package com.seeu.member.edit;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.seeu.R;
import com.seeu.common.AbstractEditEntityActivity;
import com.seeu.common.subviews.PictureChooser;
import com.seeu.member.Member;
import com.seeu.member.MemberService;
import com.seeu.utils.ImageUtils;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.io.IOException;
import java.util.Map;

/**
 * Created by thomasfouan on 15/05/2018.
 *
 * Activity that display the edition of member's profile.
 */
public class EditMemberProfileActivity extends AbstractEditEntityActivity<Member> implements CustomResponseListener<Member> {

	private PictureChooser pictureChooser;
	private EditText catchPhrase;
	private EditText description;

	private MemberService memberService;

	public EditMemberProfileActivity() {
		super();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_memberprofile_activity);

		this.memberService = new MemberService(this);

		pictureChooser = new PictureChooser();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout_picture_chooser, pictureChooser);
		transaction.commit();

		catchPhrase = findViewById(R.id.memberCatchPhrase);
		description = findViewById(R.id.memberTextDescription);

		updateUI();
	}

	@Override
	protected Member getEntityInstance() {
		return new Member();
	}

	@Override
	protected void updateUI() {
		pictureChooser.setCurrentPictureUrl(entity.getProfilePhotoUrl());
		catchPhrase.setText(entity.getCatchPhrase());
		description.setText(entity.getDescription());
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
		entity.setCatchPhrase(catchPhrase.getText().toString());
		entity.setDescription(description.getText().toString());
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
			// Error ! We cannot create a member in the application...
			throw new IllegalStateException("We cannot create a member in the application...");
		} else {
			// Save the updated member with its new picture or not
			memberService.updateMember(entity, imageBase64, this);
		}
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		error.printStackTrace();
		String message = "An error occurred while trying to " + ((isNewEntity) ? "create" : "update") + " the member";

		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(Member response) {
		// Save the updated member in the shared preferences
		SharedPreferencesManager.putEntity(this, Member.STORAGE_KEY, response);
		// End this activity when successfully save the member
		finish();
	}
}
