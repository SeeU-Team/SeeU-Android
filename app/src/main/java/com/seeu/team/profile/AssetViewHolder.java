package com.seeu.team.profile;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.seeu.R;
import com.seeu.common.ItemClickListener;
import com.seeu.team.Asset;
import com.seeu.utils.DownloadImageAndSetBackgroundTask;
import com.seeu.utils.ImageUtils;

/**
 * Created by thomasfouan on 07/05/2018.
 *
 * Holder for a team description.
 */
public class AssetViewHolder extends ViewHolder implements OnClickListener {

	private ConstraintLayout rootLayout;
	private ImageView icon;
	private DownloadImageAndSetBackgroundTask asyncTask;
	private TextView name;
	private ItemClickListener listener;

	public AssetViewHolder(View itemView, ItemClickListener listener) {
		super(itemView);

		rootLayout = itemView.findViewById(R.id.assetRootLayout);
		icon	= itemView.findViewById(R.id.assetIcon);
		name	= itemView.findViewById(R.id.assetName);

		rootLayout.setOnClickListener(this);

		this.listener = listener;
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
	 * Set the description's icon in the view.
	 * @param iconUrl the url of the icon to display.
	 */
	private void setIcon(String iconUrl) {
		ImageUtils.runJustBeforeBeingDrawn(icon, () -> {
			asyncTask = new DownloadImageAndSetBackgroundTask(icon, 0);
			asyncTask.execute(iconUrl);
		});
	}

	/**
	 * Set the description's name in the view.
	 * @param name the description's name
	 */
	private void setName(String name) {
		this.name.setText(name);
	}

	/**
	 * Set the team description's info in the view.
	 * @param asset the team description to display
	 */
	public void setData(Asset asset, boolean selected) {
		if (null != asyncTask) {
			asyncTask.cancelDownload();
		}

		if (selected) {
			setSelectedBackground();
		} else {
			setDefaultBackground();
		}

		setIcon(asset.getImageLight());
		setName(asset.getName());
	}

	@Override
	public void onClick(View v) {
		if (null != listener) {
			listener.onItemClick(v, getAdapterPosition());
		}
	}
}
