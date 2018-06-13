package com.seeu.team.edit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;
import com.seeu.member.profile.MemberProfileActivity;

import java.util.List;

/**
 * Created by thomasfouan on 08/05/2018.
 */

class MemberRecyclerAdapter extends Adapter<MemberViewHolder> implements ItemClickListener {

	private LayoutInflater inflater;
	private List<Member> members;

	public MemberRecyclerAdapter(Context context, List<Member> members) {
		inflater = LayoutInflater.from(context);
		this.members = members;
	}

	private Member getItem(int position) {
		return members.get(position);
	}

	@Override
	public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.edit_teamprofile_layout_member_item, parent, false);
		return new MemberViewHolder(view, this, this::onDeleteItemClick);
	}

	@Override
	public void onBindViewHolder(MemberViewHolder holder, int position) {
		Member member = getItem(position);

		holder.setPicture(member.getProfilePhotoUrl());
	}

	@Override
	public int getItemCount() {
		return members.size();
	}

	@Override
	public void onItemClick(View view, int position) {
		Member member = getItem(position);
		Context context = view.getContext();
		Intent intent = new Intent(context, MemberProfileActivity.class);
		intent.putExtra(Member.STORAGE_KEY, member);

		context.startActivity(intent);
	}

	private void onDeleteItemClick(View v, int position) {
		Member member = getItem(position);
		members.remove(member);
		notifyDataSetChanged();
	}
}
