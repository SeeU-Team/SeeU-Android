package com.seeu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by thomasfouan on 16/03/2018.
 */

public class TypeTeamRecyclerAdapter extends RecyclerView.Adapter<TypeTeamViewHolder> {

	private LayoutInflater inflater;
	private List<Integer> colors;
	private List<String> names;
	private ClickListener listener;

	public TypeTeamRecyclerAdapter(Context context, List<Integer> colors, List<String> names, ClickListener listener) {
		this.inflater = LayoutInflater.from(context);
		this.colors = colors;
		this.names = names;
		this.listener = listener;
	}

	@Override
	public TypeTeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.layout_type_team_item, parent, false);
		return new TypeTeamViewHolder(view, listener);
	}

	@Override
	public void onBindViewHolder(TypeTeamViewHolder holder, int position) {
		int color = colors.get(position);
		String name = names.get(position);

		holder.rootLayout.setBackgroundResource(R.drawable.drawable_radius);
		holder.name.setText(name);
		holder.setPosition(position);
	}

	@Override
	public int getItemCount() {
		return names.size();
	}

	public String getItem(int position) {
		return names.get(position);
	}
}
