package com.seeu.team.edit;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.SharedPreferencesManager;

import java.lang.ref.WeakReference;

/**
 * Created by thomasfouan on 08/05/2018.
 *
 * Holder for a member item in the list.
 */
class MemberViewHolder extends ViewHolder {

	private ImageView picture;
	private FloatingActionButton deleteActionBtn;

	private ItemClickListener itemClickListener;
	private ItemClickListener deleteItemListener;

	public MemberViewHolder(View itemView, ItemClickListener itemClickListener, ItemClickListener deleteItemListener) {
		super(itemView);

		picture = itemView.findViewById(R.id.memberPicture);

		deleteActionBtn = itemView.findViewById(R.id.deleteMemberBtn);
		deleteActionBtn.setOnClickListener(this::onDeleteItemClick);

		itemView.setOnClickListener(this::onItemClick);

		this.itemClickListener = itemClickListener;
		this.deleteItemListener = deleteItemListener;
	}

	/**
	 * Set the picture of the member in the UI.
	 * @param url the member picture's url
	 */
	private void setPicture(String url) {
		new DownloadImageAndSetBackgroundTask(picture, 30, 80, 80).execute(url);
	}

	public void setData(Member member, boolean isCurrentUser) {
		setPicture(member.getProfilePhotoUrl());

		// Show the delete button for all users except the current user (the leader)
		if (!isCurrentUser) {
			deleteActionBtn.setVisibility(View.VISIBLE);
		}
	}

	public void onItemClick(View v) {
		itemClickListener.onItemClick(v, getAdapterPosition());
	}

	public void onDeleteItemClick(View v) {
		deleteItemListener.onItemClick(v, getAdapterPosition());
	}
}
