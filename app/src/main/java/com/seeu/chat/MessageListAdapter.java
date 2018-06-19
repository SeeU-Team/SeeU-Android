package com.seeu.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.seeu.R;

import java.util.List;

/**
 * Created by thomasfouan on 06/05/2018.
 *
 * Adapter for the recycler view used to display messages in the {@link ChatActivity}.
 */
public class MessageListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<Message> messages;

	public MessageListAdapter(Context context, @NonNull  List<Message> messages) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.messages = messages;
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return MessageViewType.values().length;
	}

	@Override
	public int getItemViewType(int position) {
		Message message = (Message) getItem(position);

		return message.belongsToCurrentUser(context) ? MessageViewType.MY_MESSAGE.getValue() : MessageViewType.OTHERS_MESSAGE.getValue();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = (Message) getItem(position);
		MessageViewType viewType = MessageViewType.fromValue(getItemViewType(position));
		MessageViewHolder holder;

		if (MessageViewType.MY_MESSAGE.equals(viewType)) {
			if (null == convertView) {
				// Create new view
				convertView = inflater.inflate(R.layout.chat_layout_my_message_item, parent, false);
				holder = new MessageViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				// Reuse old view and only change attributes value
				holder = (MessageViewHolder) convertView.getTag();
			}
		} else if (MessageViewType.OTHERS_MESSAGE.equals(viewType)) {
			if (null == convertView) {
				// Create new view
				convertView = inflater.inflate(R.layout.chat_layout_others_message_item, parent, false);
				holder = new OthersMessageViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				// Reuse old view and only change attributes value
				holder = (OthersMessageViewHolder) convertView.getTag();
			}

			// Define specific elements for others message view
			OthersMessageViewHolder othersHolder = (OthersMessageViewHolder) holder;
			othersHolder.setMemberPicture(message.getOwner().getProfilePhotoUrl());
			othersHolder.setMemberName(message.getOwner().getName());
		} else {
			throw new IllegalStateException("Unknown type of message view type");
		}

		holder.setMessageContent(message.getContent());
		return convertView;
	}
}
