package com.seeu.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 06/05/2018.
 */

public class OthersMessageViewHolder extends MessageViewHolder {

	private ImageView memberPicture;
	private TextView memberName;

	public OthersMessageViewHolder(View itemView) {
		super(itemView);

		memberPicture 	= itemView.findViewById(R.id.memberPicture);
		memberName 		= itemView.findViewById(R.id.memberName);
	}

	public void setMemberPicture(String url) {
		new DownloadImageAndSetBackgroundTask(memberPicture, 32, 32, 32).execute(url);
	}

	public void setMemberName(String name) {
		memberName.setText(name);
	}
}
