package com.seeu.teamwall;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.seeu.common.ItemClickListener;
import com.seeu.R;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Holder for the team category.
 */
class CategoryViewHolder extends ViewHolder implements OnClickListener {

	private CardView rootLayout;
	private TextView name;

	private ItemClickListener listener;

	public CategoryViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);

		this.listener = listener;
		this.rootLayout = itemView.findViewById(R.id.rootLayoutTeamTypeItem);
		this.name = itemView.findViewById(R.id.teamTypeName);

		itemView.setOnClickListener(this);
	}

	/**
	 * Set the default background for the view.
	 */
	private void setDefaultBackground() {
		this.rootLayout.setBackgroundResource(R.drawable.not_selected_category_background);
	}

	/**
	 * Set the selected background for the view.
	 */
	private void setSelectedBackground() {
		this.rootLayout.setBackgroundResource(R.drawable.selected_category_background);
	}

	/**
	 * Set the name of the category in the UI.
	 * @param name the category's name
	 */
	private void setName(final String name) {
		this.name.setText(name);
	}

	/**
	 * Set the category's info in the view.
	 * @param category the category to display
	 * @param selected true if the category is the selected one. Otherwise, false
	 */
	public void setData(Category category, boolean selected) {
		if (selected) {
			setSelectedBackground();
		} else {
			setDefaultBackground();
		}

		setName(category.getName());
	}

	@Override
	public void onClick(View v) {
		listener.onItemClick(v, getAdapterPosition());
	}
}
