package com.seeu.nightcenter;

import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import com.seeu.R;
import com.seeu.common.BaseMemberViewHolder;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;

/**
 * Created by thomasfouan on 30/04/2018.
 */

class MemberViewHolder extends BaseMemberViewHolder {

	private ItemClickListener messageActionListener;

	public MemberViewHolder(View itemView, ItemClickListener itemClickListener, ItemClickListener messageActionListener) {
		super(itemView, itemClickListener);
		this.messageActionListener = messageActionListener;

		AppCompatImageButton messageAction = itemView.findViewById(R.id.messageAction);
		messageAction.setOnClickListener(this::onMessageBtnClick);
	}

	public void setData(Member member) {
		super.setData(member);
	}

	public void onMessageBtnClick(View v) {
		messageActionListener.onItemClick(v, getAdapterPosition());
	}
}
