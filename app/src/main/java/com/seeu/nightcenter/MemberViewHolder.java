package com.seeu.nightcenter;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.common.subviews.Mark;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;

/**
 * Created by thomasfouan on 30/04/2018.
 */

class MemberViewHolder extends ViewHolder implements OnClickListener {

	private ImageView picture;
	private TextView name;
	private Mark mark;

	private ItemClickListener itemClickListener;

	public MemberViewHolder(View itemView, ItemClickListener itemClickListener, ItemClickListener messageActionListener) {
		super(itemView);

		picture				= itemView.findViewById(R.id.memberPicture);
		name				= itemView.findViewById(R.id.memberName);

		TextView markView = itemView.findViewById(R.id.memberMark);
		mark = new Mark(markView);

		AppCompatImageButton messageAction = itemView.findViewById(R.id.messageAction);
		messageAction.setOnClickListener((View view) -> messageActionListener.onItemClick(view, getAdapterPosition()));

		itemView.setOnClickListener(this);
		this.itemClickListener = itemClickListener;
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

	@Override
	public void onClick(View v) {
		itemClickListener.onItemClick(v, getAdapterPosition());
	}
}
