package com.seeu.messages;

import android.view.View;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.BaseMemberViewHolder;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class MemberViewHolder extends BaseMemberViewHolder {

	private View connectedIndicator;
	private TextView lastConnection;

	public MemberViewHolder(View itemView, ItemClickListener listener) {
		super(itemView, listener);

		connectedIndicator	= itemView.findViewById(R.id.connectedMemberIndicator);
		lastConnection		= itemView.findViewById(R.id.memberLastConnection);
	}

	private void setStatus(boolean isConnected, Date lastConnectionDate) {
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
		super.setData(member);
		setStatus(member.isConnected(), member.getLastConnection());
	}
}
