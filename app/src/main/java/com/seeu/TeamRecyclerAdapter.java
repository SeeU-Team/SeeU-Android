package com.seeu;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by thomasfouan on 19/03/2018.
 */

public class TeamRecyclerAdapter extends Adapter<TeamViewHolder> {

	private LayoutInflater inflater;
	private List<String> names;
	private ClickListener listener;

	public TeamRecyclerAdapter(Context context, List<String> names, ClickListener listener) {
		this.inflater = LayoutInflater.from(context);
		this.names = names;
		this.listener = listener;
	}

	@Override
	public TeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.layout_team_item, parent, false);
		return new TeamViewHolder(view, listener);
	}

	@Override
	public void onBindViewHolder(TeamViewHolder holder, int position) {
		String[] teamMemberPictures = new String[position];
		for (int i = 0; i < position; i++) {
			teamMemberPictures[i] = "https://scontent.xx.fbcdn.net/v/t1.0-1/p200x200/22365631_1924906760859051_4812781837110089872_n.jpg?oh=a27a7d0053306572e7e485b647db99d4&oe=5B3A50DA";
		}
		float maleProportion = (position % 10) / (float) 10.0;

		holder.setName(names.get(position));
		holder.setTags("#uno#dos#tres");
		holder.setPicture("https://scontent.xx.fbcdn.net/v/t1.0-9/s720x720/28796203_2100578413291884_3128132353430932217_n.jpg?oh=cf890e20f692253214d595fd266fd73b&oe=5B3BCC27");
		holder.setMemberPictures(teamMemberPictures);
		holder.setGenderIndex(maleProportion);
		holder.setDescriptionPictures();
	}

	@Override
	public int getItemCount() {
		return names.size();
	}

	public String getItem(int position) {
		return names.get(position);
	}
}
