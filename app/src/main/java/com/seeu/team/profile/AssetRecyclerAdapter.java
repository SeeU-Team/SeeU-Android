package com.seeu.team.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.team.Asset;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by thomasfouan on 07/05/2018.
 *
 * Adapter for the list of team's assets.
 */
public class AssetRecyclerAdapter extends Adapter<AssetViewHolder> {

	private LayoutInflater inflater;
	private List<Asset> assets;
	private List<Asset> selectedAssets;
	private ItemClickListener listener;

	public AssetRecyclerAdapter(Context context, List<Asset> assets) {
		this.inflater = LayoutInflater.from(context);
		this.assets = assets;
		selectedAssets = new ArrayList<>();
		listener = null;
	}

	public AssetRecyclerAdapter(Context context, List<Asset> assets, ItemClickListener listener) {
		this(context, assets);
		this.listener = listener;
	}

	/**
	 * Return the team description at the given position.
	 * @param position the position of the team description in the list
	 * @return the team description
	 */
	private Asset getItem(int position) {
		return assets.get(position);
	}

	@Override
	public AssetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.teamprofile_layout_asset_item, parent, false);
		return new AssetViewHolder(view, listener);
	}

	@Override
	public void onBindViewHolder(AssetViewHolder holder, int position) {
		Asset asset = getItem(position);
		holder.setData(asset, selectedAssets.contains(asset));
	}

	@Override
	public int getItemCount() {
		return assets.size();
	}

	public void select(int position) {
		Asset asset = getItem(position);
		if (selectedAssets.contains(asset)) {
			selectedAssets.remove(asset);
		} else {
			selectedAssets.add(asset);
		}

		notifyItemChanged(position);
	}

	public List<Asset> getSelectedItems() {
		return selectedAssets;
	}
}
