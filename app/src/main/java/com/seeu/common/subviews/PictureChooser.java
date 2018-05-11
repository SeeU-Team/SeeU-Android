package com.seeu.common.subviews;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seeu.R;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.GetAndShowImageFromUriAsyncTask;

import static android.app.Activity.RESULT_OK;
import static android.view.View.*;
import static android.view.View.GONE;

public class PictureChooser extends Fragment implements OnClickListener {

	private static final int INTENT_PICK_IMAGE = 1;

	private ConstraintLayout pictureChooser;
	private boolean isChooserLayoutDrawn;

	private ImageView chosenPicture;
	private boolean isPictureLayoutDrawn;
	private String currentPictureUrl;
	private Uri chosenPictureUri;

	public PictureChooser() {
		isChooserLayoutDrawn = false;
		isPictureLayoutDrawn = false;
		currentPictureUrl = null;
		chosenPictureUri = null;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.picture_chooser, container, false);

		pictureChooser = view.findViewById(R.id.pictureChooser);
		chosenPicture = view.findViewById(R.id.pictureChosen);

		pictureChooser.getViewTreeObserver().addOnPreDrawListener(this::onPreDrawChooser);
		chosenPicture.getViewTreeObserver().addOnPreDrawListener(this::onPreDrawPicture);

		CardView rootElement = view.findViewById(R.id.pictureChooserRootElement);
		rootElement.setOnClickListener(this);

		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (INTENT_PICK_IMAGE == requestCode && RESULT_OK == resultCode) {
			if (null != data && null != data.getData()) {
				chosenPictureUri = data.getData();
				new GetAndShowImageFromUriAsyncTask(getActivity().getContentResolver(), chosenPicture).execute(chosenPictureUri);

				chosenPicture.setVisibility(VISIBLE);
				pictureChooser.setVisibility(GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
		getIntent.setType("image/*");

		Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		pickIntent.setType("image/*");

		Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

		startActivityForResult(chooserIntent, INTENT_PICK_IMAGE);
	}

	public boolean onPreDrawChooser() {
		if (!isChooserLayoutDrawn) {
			isChooserLayoutDrawn = true;
			pictureChooser.getViewTreeObserver().removeOnPreDrawListener(this::onPreDrawChooser);

			updateUI();
		}
		return true;
	}

	public boolean onPreDrawPicture() {
		if (!isPictureLayoutDrawn) {
			isPictureLayoutDrawn = true;
			chosenPicture.getViewTreeObserver().removeOnPreDrawListener(this::onPreDrawPicture);

			updateUI();
		}
		return true;
	}

	private void updateUI() {
		if (isChooserLayoutDrawn) {
			if (null != currentPictureUrl) {
				pictureChooser.setVisibility(GONE);
			} else {
				pictureChooser.setVisibility(VISIBLE);
			}
		}

		if (isPictureLayoutDrawn) {
			if (null != currentPictureUrl) {
				chosenPicture.setVisibility(VISIBLE);
				new DownloadImageAndSetBackgroundTask(chosenPicture, 10).execute(currentPictureUrl);
			} else {
				chosenPicture.setVisibility(GONE);
			}
		}
	}

	public void setCurrentPictureUrl(String url) {
		currentPictureUrl = url;
		updateUI();
	}

	public Uri getChosenPictureUri() {
		return chosenPictureUri;
	}
}
