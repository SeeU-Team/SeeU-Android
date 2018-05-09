package com.seeu.nightcenter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.common.ItemClickListener;
import com.seeu.common.Member;

import java.util.List;

/**
 * Created by thomasfouan on 30/04/2018.
 */

public class MemberRecyclerAdapter extends Adapter<MemberViewHolder> implements ItemClickListener {

	private LayoutInflater inflater;
	private List<Member> members;

	public MemberRecyclerAdapter(Context context, List<Member> members) {
		this.inflater = LayoutInflater.from(context);
		this.members = members;
	}

	public Member getItem(int position) {
		return members.get(position);
	}

	@Override
	public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.nightcenter_layout_member_item, parent, false);

		return new MemberViewHolder(view, this, this::startMessageActivity);
	}

	@Override
	public void onBindViewHolder(MemberViewHolder holder, int position) {
		Member member = getItem(position);
		holder.setData(member);
	}

	@Override
	public int getItemCount() {
		return members.size();
	}

	@Override
	public void onItemClick(View view, int position) {
		// TODO: start member profile activity
		Toast.makeText(view.getContext(), "You clicked " + getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
	}

	private void startMessageActivity(View v, int position) {
		Member member = getItem(position);
		Context context = v.getContext();
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra(Member.INTENT_EXTRA_KEY, member);

		context.startActivity(intent);
	}
}
