package com.seeu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;

public class TabbedActivity extends Activity {

	private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = (item) -> {
		Fragment selectedFragment = null;
		switch (item.getItemId()) {
			case R.id.navigation_teamwall:
				selectedFragment = new TeamWallFragment();
				break;
			case R.id.navigation_messages:
				selectedFragment = new MessagesFragment();
				break;
			case R.id.navigation_nightcenter:
				selectedFragment = new NightCenterFragment();
		}

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout, selectedFragment);
		transaction.commit();

		return true;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabbed);

		BottomNavigationView navigation = findViewById(R.id.navigation);
		navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		//Manually displaying the first fragment - one time only
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.frame_layout, new TeamWallFragment());
		transaction.commit();
	}

}
