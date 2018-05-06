package com.seeu.chat;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.seeu.R;

/**
 * Created by thomasfouan on 06/05/2018.
 */

class MessageViewHolder extends ViewHolder {

	private TextView messageContent;

	public MessageViewHolder(View itemView) {
		super(itemView);

		messageContent 	= itemView.findViewById(R.id.messageContent);
	}

	public void setMessageContent(String message) {
		messageContent.setText(message);
	}
}
