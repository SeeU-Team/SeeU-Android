package com.seeu.messages;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.seeu.R;
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
		View view = inflater.inflate(R.layout.messages_layout_member_item, parent, false);
		return new MemberViewHolder(view, this);
	}

	@Override
	public void onBindViewHolder(MemberViewHolder holder, int position) {
		Member member = getItem(position);

		holder.setPicture(member.getPictureUrl());
		holder.setName(member.getName());
		holder.setMark(member.getMark());
		holder.setStatus(member.isConnected(), member.getLastConnection());
	}

	@Override
	public int getItemCount() {
		return members.size();
	}

	@Override
	public void onItemClick(View view, int position) {
		Toast.makeText(view.getContext(), "You clicked " + getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
	}
}
