package com.seeu.nightcenter;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.common.BaseMemberViewHolder;
import com.seeu.common.ItemClickListener;
import com.seeu.common.Member;
import com.seeu.common.subviews.Mark;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

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
