package com.seeu.common.membersearch;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

class MemberViewHolder extends ViewHolder implements OnClickListener {

	private ImageView picture;
	private TextView name;

	private ItemClickListener listener;

	public MemberViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);

		picture	= itemView.findViewById(R.id.memberPicture);
		name	= itemView.findViewById(R.id.memberName);

		this.listener = listener;
		itemView.setOnClickListener(this);
	}

	private void setPicture(String url) {
		new DownloadImageAndSetBackgroundTask(picture, 20, 40, 40).execute(url);
	}

	private void setName(String name) {
		this.name.setText(name);
	}

	public void setData(Member member) {
		setPicture(member.getPictureUrl());
		setName(member.getName());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
