package com.seeu.common.membersearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.member.Member;

import java.util.List;

/**
 * Created by thomasfouan on 10/05/2018.
 *
 * Adapter for the recycler view used to display members in {@link MemberSearchableActivity}
 */
public class MemberRecyclerAdapter extends Adapter<MemberViewHolder> {

	private LayoutInflater inflater;
	private List<Member> members;
	private ItemClickListener listener;

	public MemberRecyclerAdapter(Context context, List<Member> members, ItemClickListener listener) {
		this.inflater = LayoutInflater.from(context);
		this.members = members;
		this.listener = listener;
	}

	/**
	 * Get the member in the list at the given position.
	 * @param position the position
	 * @return the member
	 */
	public Member getItem(int position) {
		return members.get(position);
	}

	@Override
	public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.member_searchable_layout_member_item, parent, false);
		return new MemberViewHolder(view, listener);
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
}
