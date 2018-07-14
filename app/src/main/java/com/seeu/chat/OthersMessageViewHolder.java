package com.seeu.chat;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;

/**
 * Created by thomasfouan on 06/05/2018.
 *
 * View Holder for a message send by other users than the current user and displayed in the recycler view used in the ChatActivity.
 */
public class OthersMessageViewHolder extends MessageViewHolder {

	private ImageView memberPicture;
	private TextView memberName;
	private DownloadImageAndSetBackgroundTask asyncTask;

	public OthersMessageViewHolder(View itemView) {
		super(itemView);

		memberPicture 	= itemView.findViewById(R.id.memberPicture);
		memberName 		= itemView.findViewById(R.id.memberName);
	}

	/**
	 * Set the picture of the member that sent the message.
	 * @param url the url of the member's picture
	 */
	private void setMemberPicture(String url) {
		ImageUtils.runJustBeforeBeingDrawn(memberPicture, () -> {
			asyncTask = new DownloadImageAndSetBackgroundTask(memberPicture, 32);
			asyncTask.execute(url);
		});
	}

	/**
	 * Set the name of the member that sent the message.
	 * @param name the member's name
	 */
	private void setMemberName(String name) {
		memberName.setText(name);
	}

	public void setData(Message message) {
		if (null != asyncTask) {
			asyncTask.cancelDownload();
		}

		setMemberName(message.getOwner().getName());
		setMemberPicture(message.getOwner().getProfilePhotoUrl());
	}
}
