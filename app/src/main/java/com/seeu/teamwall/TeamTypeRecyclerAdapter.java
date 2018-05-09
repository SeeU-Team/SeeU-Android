package com.seeu.teamwall;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.common.ItemClickListener;
import com.seeu.R;

import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class TeamTypeRecyclerAdapter extends Adapter<TeamTypeViewHolder> {

	private LayoutInflater inflater;
	private List<TeamType> types;
	private ItemClickListener listener;
	private int selected;

	public TeamTypeRecyclerAdapter(Context context, @NonNull List<TeamType> types, ItemClickListener listener) {
		this.inflater = LayoutInflater.from(context);
		this.types = types;
		this.listener = listener;
		this.selected = (0 < types.size()) ? 0 : -1;
	}

	@Override
	public TeamTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.teamwall_layout_team_type_item, parent, false);
		return new TeamTypeViewHolder(view, listener);
	}

	@Override
	public void onBindViewHolder(TeamTypeViewHolder holder, int position) {
		TeamType teamType = getItem(position);
		holder.setData(teamType, selected == position);
	}

	@Override
	public int getItemCount() {
		return types.size();
	}

	public TeamType getItem(int position) {
		return types.get(position);
	}

	public void setSelected(int newSelected) {
		notifyItemChanged(selected);
		notifyItemChanged(newSelected);
		this.selected = newSelected;
	}
}
