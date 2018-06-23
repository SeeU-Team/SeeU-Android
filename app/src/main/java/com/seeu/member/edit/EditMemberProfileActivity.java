package com.seeu.member.edit;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.common.AbstractEditEntityActivity;
import com.seeu.common.subviews.PictureChooser;
import com.seeu.member.Member;

/**
 * Created by thomasfouan on 15/05/2018.
 *
 * Abstract activity that provide template for edit a profile member.
 */
public class EditMemberProfileActivity extends AbstractEditEntityActivity<Member> {

	private PictureChooser pictureChooser;
	private EditText catchPhrase;
	private EditText description;

	public EditMemberProfileActivity() {
		super();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_memberprofile_activity);

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

		if (null != chosenPictureUri) {
			// TODO: Get data of picture
			// InputStream imageStream = getContentResolver().openInputStream(chosenPictureUri);

//			String url = Member.DEBUG_PICTURE_URL;
//			entity.setProfilePhotoUrl(url);
		}

		if (isNewEntity) {
			// TODO: make POST http request to save the newly created member. Send the picture along with member's info
		} else {
			// TODO: make PUT http request to save the updated member.
			// TODO: if the picture has changed, send the image data with the member's info
		}
	}
}
