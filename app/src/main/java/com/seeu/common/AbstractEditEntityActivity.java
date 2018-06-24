package com.seeu.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.Serializable;

/**
 * Created by thomasfouan on 20/05/2018.
 *
 * Abstract Activity that provides template for {@link Entity} edition.
 * It could be used for create an entity, or update an existing one.
 *
 * @param <T> the type of Entity to edit
 */
public abstract class AbstractEditEntityActivity<T extends Entity> extends Activity {

	protected T entity;
	protected boolean isNewEntity;

	protected AbstractEditEntityActivity() {
		this.entity = getEntityInstance();
		this.isNewEntity = false;
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		updateEntityFromCaller();
	}

	/**
	 * Update the entity to edit with potential one given by the caller.
	 * If there is one, it is for update. Otherwise, it is for creation.
	 */
	private void updateEntityFromCaller() {
		Serializable ser = getIntent().getSerializableExtra(T.STORAGE_KEY);

		// If an entity was passed by the caller, get it for update
		if (null != ser) {
			entity = (T) ser;
			isNewEntity = false;
		}
	}

	/**
	 * Handle click event on cancel button.
	 * @param v the view that represents the button clicked
	 */
	public void cancelForm(View v) {
		finish();
	}

	/**
	 * Handle click event on validation button.
	 * @param v the view that represents the button clicked
	 */
	public void validForm(View v) {
		if (!isEntityValid()) {
			return;
		}

		updateEntity();
		saveEntity();

		finish();
	}

	/**
	 * Return a new entity of T.
	 *
	 * @return the newly created entity
	 */
	protected abstract T getEntityInstance();

	/**
	 * Called to update the UI with data from entity.
	 */
	protected abstract void updateUI();

	/**
	 * Check if the entity is valid when the user wants to save the changes.
	 *
	 * @return true if it is valid. Otherwise, return false
	 */
	protected abstract boolean isEntityValid();

	/**
	 * Update the entity info from UI.
	 */
	protected abstract void updateEntity();

	/**
	 * Save the changes in the database.
	 */
	protected abstract void saveEntity();
}
