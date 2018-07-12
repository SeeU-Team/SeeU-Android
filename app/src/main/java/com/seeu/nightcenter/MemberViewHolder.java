package com.seeu.nightcenter;

import android.support.v7.widget.AppCompatImageButton;
import android.view.View;

import com.seeu.R;
import com.seeu.common.BaseMemberViewHolder;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Holder for a member item in the list of members in the night center.
 */
class MemberViewHolder extends BaseMemberViewHolder {

	private ItemClickListener messageActionListener;

	public MemberViewHolder(View itemView, ItemClickListener itemClickListener, ItemClickListener messageActionListener, boolean startedFromTeamwall) {
		super(itemView, itemClickListener);
		this.messageActionListener = messageActionListener;

		AppCompatImageButton messageAction = itemView.findViewById(R.id.messageAction);
		if (startedFromTeamwall) {
			messageAction.setVisibility(View.GONE);
		} else {
			messageAction.setOnClickListener(this::onMessageBtnClick);
		}
	}

	@Override
	public void setData(Member member) {
		super.setData(member);
	}

	/**
	 * Method called when the user clicked on the message button.
	 * Calls the listener method to notify it that the button has benn clicked the user.
	 * @param v the view clicked
	 */
	public void onMessageBtnClick(View v) {
		messageActionListener.onItemClick(v, getAdapterPosition());
	}
}
