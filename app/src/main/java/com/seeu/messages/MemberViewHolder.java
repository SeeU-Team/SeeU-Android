package com.seeu.messages;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.common.Member;
import com.seeu.common.subviews.Mark;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by thomasfouan on 30/04/2018.
 */

class MemberViewHolder extends ViewHolder implements OnClickListener {

	private ImageView picture;
	private TextView name;
	private Mark mark;
	private View connectedIndicator;
	private TextView lastConnection;

	private ItemClickListener listener;

	public MemberViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);

		picture				= itemView.findViewById(R.id.memberPicture);
		name				= itemView.findViewById(R.id.memberName);
		connectedIndicator	= itemView.findViewById(R.id.connectedMemberIndicator);
		lastConnection		= itemView.findViewById(R.id.memberLastConnection);

		TextView markView = itemView.findViewById(R.id.memberMark);
		mark = new Mark(markView);

		itemView.setOnClickListener(this);
		this.listener = listener;
	}

	public void setPicture(String pictureUrl) {
		new DownloadImageAndSetBackgroundTask(picture, 50, 100, 100).execute(pictureUrl);
	}

	public void setName(String name) {
		this.name.setText(name);
	}

	public void setMark(int mark) {
		this.mark.setMark(mark);
	}

	public void setStatus(boolean isConnected, Date lastConnectionDate) {
		if (isConnected) {
			connectedIndicator.setVisibility(View.VISIBLE);
			lastConnection.setVisibility(View.INVISIBLE);
		} else {
			connectedIndicator.setVisibility(View.INVISIBLE);
			lastConnection.setVisibility(View.VISIBLE);

			setLastConnection(lastConnectionDate);
		}
	}

	private void setLastConnection(Date lastConnectionDate) {
		long diffInDays;

		if (null != lastConnectionDate) {
			Date now = new Date();
			long diffInMillis = now.getTime() - lastConnectionDate.getTime();
			diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
		} else {
			diffInDays = -1;
		}

		String text = lastConnection.getResources().getString(R.string.member_last_connection, diffInDays);
		lastConnection.setText(text);
	}

	public void setData(Member member) {
		setPicture(member.getPictureUrl());
		setName(member.getName());
		setMark(member.getMark());
		setStatus(member.isConnected(), member.getLastConnection());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
