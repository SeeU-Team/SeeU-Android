package com.seeu.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;

import com.seeu.member.Member;

import java.util.List;

/**
 * Created by thomasfouan on 20/05/2018.
 *
 * Abstract recycler adapter for the Member entity.
 */
public abstract class BaseMemberRecyclerAdapter extends Adapter<BaseMemberViewHolder> {

	protected LayoutInflater inflater;
	protected List<Member> members;

	protected BaseMemberRecyclerAdapter(Context context, List<Member> members) {
		this.inflater = LayoutInflater.from(context);
		this.members = members;
	}

	/**
	 * Get the member in the list of members at the given position.
	 * @param position the position of the member in the list
	 * @return the member
	 */
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
