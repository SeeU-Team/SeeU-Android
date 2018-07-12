package com.seeu.nightcenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.BaseMemberRecyclerAdapter;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.member.profile.MemberProfileActivity;
import com.seeu.teamwall.TeamWallFragment;

import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 *
 * Adapter for recycler view that display list of members in the night center.
 */
public class MemberRecyclerAdapter extends BaseMemberRecyclerAdapter implements ItemClickListener {

	private boolean startedFromTeamwall;

	public MemberRecyclerAdapter(Context context, List<Member> members, boolean startedFromTeamwall) {
		super(context, members);
		this.startedFromTeamwall = startedFromTeamwall;
	}

	@Override
	public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.nightcenter_layout_member_item, parent, false);
		return new MemberViewHolder(view, this, this::startMessageActivity, startedFromTeamwall);
	}

	@Override
	public void onItemClick(View view, int position) {
		Member member = getItem(position);
		Context context = view.getContext();
		Intent intent = new Intent(context, MemberProfileActivity.class);
		intent.putExtra(Member.STORAGE_KEY, member);
		intent.putExtra(TeamWallFragment.TEAMWALL_STARTED_NAME, startedFromTeamwall);

		context.startActivity(intent);
	}

	/**
	 * Method called when the user clicks on the message button on a member item.
	 * @param v the view clicked by the user
	 * @param position the position of the view where the user clicked
	 */
	private void startMessageActivity(View v, int position) {
		Member member = getItem(position);
		Context context = v.getContext();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(Member.STORAGE_KEY, member);

		context.startActivity(intent);
	}
}
