package com.seeu.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.io.Serializable;

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

	private void updateEntityFromCaller() {
		Serializable ser = getIntent().getSerializableExtra(T.INTENT_EXTRA_KEY);

		// If an entity was passed by the caller, get it for update
		if (null != ser) {
			entity = (T) ser;
			isNewEntity = false;
		}
	}

	/**
	 * Handle click event on cancel button.
	 * @param v
	 */
	public void cancelForm(View v) {
		finish();
	}

	/**
	 * Handle click event on validation button.
	 * @param v
	 */
	public void validForm(View v) {
		if (!isEntityValid()) {
			return;
		}

		updateEntity();
		saveEntity();

		finish();
	}

	protected abstract T getEntityInstance();

	protected abstract void updateUI();

	protected abstract boolean isEntityValid();

	protected abstract void updateEntity();

	protected abstract void saveEntity();
}
