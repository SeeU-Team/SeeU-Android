package com.seeu.teamwall;

import android.content.Context;
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

public class TypeTeamRecyclerAdapter extends Adapter<TypeTeamViewHolder> {

	private LayoutInflater inflater;
	private List<String> names;
	private ItemClickListener listener;
	private int selected;

	public TypeTeamRecyclerAdapter(Context context, List<String> names, ItemClickListener listener) {
		this.inflater = LayoutInflater.from(context);
		this.names = names;
		this.listener = listener;
		this.selected = (0 < names.size()) ? 0 : -1;
	}

	@Override
	public TypeTeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.teamwall_layout_type_team_item, parent, false);
		return new TypeTeamViewHolder(view, listener);
	}

	@Override
	public void onBindViewHolder(TypeTeamViewHolder holder, int position) {
		if (selected == position) {
			holder.setSelectedBackground();
		} else {
			holder.setDefaultBackground();
		}

		holder.setName(names.get(position));
	}

	@Override
	public int getItemCount() {
		return names.size();
	}

	public String getItem(int position) {
		return names.get(position);
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int newSelected) {
		notifyItemChanged(selected);
		notifyItemChanged(newSelected);
		this.selected = newSelected;
	}
}
