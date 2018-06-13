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

			String url = Member.DEBUG_PICTURE_URL;
			entity.setProfilePhotoUrl(url);
		}

		if (isNewEntity) {
			// TODO: make http request to save the newly created team
		} else {
			// TODO: make http request to save the updated team
		}
	}
}
