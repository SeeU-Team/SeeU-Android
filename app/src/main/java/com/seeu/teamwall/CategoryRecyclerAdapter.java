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
 *
 * Adapter for the list of the team category.
 */
public class CategoryRecyclerAdapter extends Adapter<CategoryViewHolder> {

	private LayoutInflater inflater;
	private List<Category> categories;
	private ItemClickListener listener;
	private int selected;

	public CategoryRecyclerAdapter(Context context, @NonNull List<Category> categories, ItemClickListener listener) {
		this.inflater = LayoutInflater.from(context);
		this.categories = categories;
		this.listener = listener;
		this.selected = (0 < categories.size()) ? 0 : -1;
	}

	@Override
	public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.teamwall_layout_category_item, parent, false);
		return new CategoryViewHolder(view, listener);
	}

	@Override
	public void onBindViewHolder(CategoryViewHolder holder, int position) {
		Category category = getItem(position);
		holder.setData(category, selected == position);
	}

	@Override
	public int getItemCount() {
		return categories.size();
	}

	/**
	 * Return the category at the given position.
	 * @param position the position of the category in the list.
	 * @return the category
	 */
	public Category getItem(int position) {
		return categories.get(position);
	}

	public Category getSelectedItem() {
		return getItem(selected);
	}

	/**
	 * Set the selected category item of the list.
	 * The selected item is highlighted.
	 * @param newSelected the position of the selected category
	 */
	public void setSelected(int newSelected) {
		notifyItemChanged(selected);
		notifyItemChanged(newSelected);
		this.selected = newSelected;
	}
}
