package com.seeu.nightcenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.BaseMemberRecyclerAdapter;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.member.profile.MemberProfileActivity;

import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class MemberRecyclerAdapter extends BaseMemberRecyclerAdapter implements ItemClickListener {

	public MemberRecyclerAdapter(Context context, List<Member> members) {
		super(context, members);
	}

	@Override
	public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.nightcenter_layout_member_item, parent, false);
		return new MemberViewHolder(view, this, this::startMessageActivity);
	}

	@Override
	public void onItemClick(View view, int position) {
		Member member = getItem(position);
		Context context = view.getContext();
		Intent intent = new Intent(context, MemberProfileActivity.class);
		intent.putExtra(Member.INTENT_EXTRA_KEY, member);

		context.startActivity(intent);
	}

	private void startMessageActivity(View v, int position) {
		Member member = getItem(position);
		Context context = v.getContext();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(Member.INTENT_EXTRA_KEY, member);

		context.startActivity(intent);
	}
}
