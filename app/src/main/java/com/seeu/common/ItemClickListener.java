package com.seeu.common;

import android.view.View;

/**
 * Created by thomasfouan on 16/03/2018.
 *
 * Interface used by classes (Activity/Fragment or Adapter) to get notified when the user clicks on an item in a recycler view.
 */
public interface ItemClickListener {

	/**
	 * Method called when the user clicks on an item in the recycler view.
	 * @param view the view that has been clicked
	 * @param position the position of the item in the list
	 */
	void onItemClick(View view, int position);
}
