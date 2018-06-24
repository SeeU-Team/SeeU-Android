package com.seeu.team.edit;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 08/05/2018.
 *
 * Holder for a member item in the list.
 */
class MemberViewHolder extends ViewHolder implements OnClickListener {

	private ImageView picture;

	private ItemClickListener itemClickListener;

	public MemberViewHolder(View itemView, ItemClickListener itemClickListener, ItemClickListener deleteItemListener) {
		super(itemView);

		picture = itemView.findViewById(R.id.memberPicture);

		FloatingActionButton deleteActionBtn = itemView.findViewById(R.id.deleteMemberBtn);
		deleteActionBtn.setOnClickListener(v -> {
			deleteItemListener.onItemClick(v, getAdapterPosition());
		});

		itemView.setOnClickListener(this);
		this.itemClickListener = itemClickListener;
	}

	/**
	 * Set the picture of the member in the UI.
	 * @param url the member picture's url
	 */
	public void setPicture(String url) {
		new DownloadImageAndSetBackgroundTask(picture, 30, 80, 80).execute(url);
	}

	@Override
	public void onClick(View v) {
		itemClickListener.onItemClick(v, getAdapterPosition());
	}
}
