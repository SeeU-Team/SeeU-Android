package com.seeu.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;

import java.util.List;

public abstract class BaseMemberRecyclerAdapter extends Adapter<BaseMemberViewHolder> {

	protected LayoutInflater inflater;
	protected List<Member> members;

	protected BaseMemberRecyclerAdapter(Context context, List<Member> members) {
		this.inflater = LayoutInflater.from(context);
		this.members = members;
	}

	protected Member getItem(int position) {
		return members.get(position);
	}

	@Override
	public void onBindViewHolder(BaseMemberViewHolder holder, int position) {
		Member member = getItem(position);
		holder.setData(member);
	}

	@Override
	public int getItemCount() {
		return members.size();
	}
}
