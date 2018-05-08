package com.seeu.teamprofile.edit;

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
 * Created by thomasfouan on 08/05/2018.
 */

class MemberRecyclerAdapter extends Adapter<MemberViewHolder> implements ItemClickListener {

	private LayoutInflater inflater;
	private List<Member> members;

	public MemberRecyclerAdapter(Context context, List<Member> members) {
		inflater = LayoutInflater.from(context);
		this.members = members;
	}

	public Member getItem(int position) {
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

		holder.setPicture(member.getPictureUrl());
	}

	@Override
	public int getItemCount() {
		return members.size();
	}

	@Override
	public void onItemClick(View view, int position) {
		Context context = view.getContext();
		Member member = getItem(position);
		Toast.makeText(context, "You clicked " + member + " on item position " + position, Toast.LENGTH_SHORT).show();

		// TODO: start member profile activity
//		Intent intent = new Intent(context, ...);
//		context.startActivity(intent);
	}

	public void onDeleteItemClick(View v, int position) {
		members.remove(getItem(position));
		notifyDataSetChanged();
	}
}
