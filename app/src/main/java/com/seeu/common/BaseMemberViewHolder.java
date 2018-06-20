package com.seeu.common;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.subviews.Mark;
import com.seeu.member.Member;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 10/04/2018.
 *
 * Class that provides a default View Holder for the Member entity.
 */
public class BaseMemberViewHolder extends ViewHolder implements OnClickListener, ViewTreeObserver.OnPreDrawListener {

	private Member member;
	private boolean isPictureLayoutDrawn;

	private ImageView picture;
	private TextView name;
	private Mark mark;

	private ItemClickListener listener;

	public BaseMemberViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);
		member = null;
		isPictureLayoutDrawn = false;
		this.listener = listener;

		picture	= itemView.findViewById(R.id.memberPicture);
		name	= itemView.findViewById(R.id.memberName);

		TextView markView	= itemView.findViewById(R.id.memberMark);
		mark = new Mark(markView);

		picture.getViewTreeObserver().addOnPreDrawListener(this);

		itemView.setOnClickListener(this);
	}

	/**
	 * Set the member's picture in the view.
	 * @param pictureUrl the url of the member's picture
	 */
	private void setPicture(String pictureUrl) {
		if (isPictureLayoutDrawn) {
			new DownloadImageAndSetBackgroundTask(picture, 200).execute(pictureUrl);
		}
	}

	/**
	 * Set the member's name in the view.
	 * @param name the member's name
	 */
	private void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Set the mark of the member in the view.
	 * @param mark the mark of the member
	 */
	private void setMark(int mark) {
		this.mark.setMark(mark);
	}

	/**
	 * Set the view from member information.
	 * @param member the member to display
	 */
	protected void setData(Member member) {
		this.member = member;

		setPicture(member.getProfilePhotoUrl());
		setName(member.getName());
		setMark(member.getMark());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}

	@Override
	public boolean onPreDraw() {
		isPictureLayoutDrawn = true;
		picture.getViewTreeObserver().removeOnPreDrawListener(this);

		if (null != member) {
			new DownloadImageAndSetBackgroundTask(picture, 200).execute(member.getProfilePhotoUrl());
		}

		return true;
	}
}
