package com.seeu.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 06/05/2018.
 *
 * View Holder for a message send by other users than the current user and displayed in the recycler view used in the ChatActivity.
 */
public class OthersMessageViewHolder extends MessageViewHolder {

	private ImageView memberPicture;
	private TextView memberName;

	public OthersMessageViewHolder(View itemView) {
		super(itemView);

		memberPicture 	= itemView.findViewById(R.id.memberPicture);
		memberName 		= itemView.findViewById(R.id.memberName);
	}

	/**
	 * Set the picture of the member that sent the message.
	 * @param url the url of the member's picture
	 */
	public void setMemberPicture(String url) {
		new DownloadImageAndSetBackgroundTask(memberPicture, 32, 32, 32).execute(url);
	}

	/**
	 * Set the name of the member that sent the message.
	 * @param name the member's name
	 */
	public void setMemberName(String name) {
		memberName.setText(name);
	}
}
