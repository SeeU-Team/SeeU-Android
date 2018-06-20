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

/**
 * Created by thomasfouan on 10/05/2018.
 *
 * View Holder for a Member matching the query made by the user.
 */
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

	/**
	 * Set the picture of the member in the view.
	 * @param url the url of the picture to show
	 */
	private void setPicture(String url) {
		new DownloadImageAndSetBackgroundTask(picture, 20, 40, 40).execute(url);
	}

	/**
	 * Set the name of the member in the view.
	 * @param name the member's name
	 */
	private void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Set member's info in the view from the member entity.
	 * @param member the member entity
	 */
	public void setData(Member member) {
		setPicture(member.getProfilePhotoUrl());
		setName(member.getName());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
