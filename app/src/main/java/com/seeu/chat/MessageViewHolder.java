package com.seeu.chat;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.TextView;

import com.seeu.R;

/**
 * Created by thomasfouan on 06/05/2018.
 *
 * View Holder for a message send by the current user and displayed in the recycler view used in the ChatActivity.
 */
class MessageViewHolder extends ViewHolder {

	private TextView messageContent;

	public MessageViewHolder(View itemView) {
		super(itemView);

		messageContent 	= itemView.findViewById(R.id.messageContent);
	}

	/**
	 * Set the textview to the content of the message.
	 * @param message the message to display
	 */
	public void setMessageContent(String message) {
		messageContent.setText(message);
	}
}
